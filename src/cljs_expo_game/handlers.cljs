(ns cljs-expo-game.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
    [clojure.spec.alpha :as s]
    [cljs-expo-game.db :as db :refer [app-db]]
    [cljs-expo-game.util :as u]
    [cljs-expo-game.constants :as k]))

;; -- Interceptors ----------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/develop/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)]
                   (check-and-throw ::db/app-db db)
                   context)))
    ->interceptor))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  [validate-spec]
  (fn [_ _]
    app-db))

(reg-event-db
 :add-finger
 (fn [db [_ {:keys [id pos]}]]
   (assoc-in db [:fingers id :path] [pos])))

(reg-event-db
 :move-finger
 (fn [db [_ {:keys [id pos]}]]
   (update-in db [:fingers id :path] conj pos)))

(reg-event-db
 :remove-finger
 (fn [db [_ {:keys [id]}]]
   (u/dissoc-in db [:fingers id])))

(defn get-touches [db]
  (->> db :fingers vals (map :path) (map last)))

(defn handle-dpad [db]
  (let [touches (get-touches db)
        [x y] k/DPAD-POS
        third-height (/ k/DPAD-HEIGHT 3)
        half-width (/ k/DPAD-WIDTH 2)
        up-box [x (+ y k/CONTROLS-Y) k/DPAD-WIDTH third-height]
        down-box [x (+ y k/CONTROLS-Y (* 2 third-height)) k/DPAD-WIDTH third-height]
        left-box [x (+ y k/CONTROLS-Y) half-width k/DPAD-HEIGHT]
        right-box [(+ x half-width) (+ y k/CONTROLS-Y) half-width k/DPAD-HEIGHT]]
    (cond
      (some #(u/box-contains? up-box %) touches)
      (assoc-in db [:controls :dpad :state] :up)

      (some #(u/box-contains? down-box %) touches)
      (assoc-in db [:controls :dpad :state] :down)

      (some #(u/box-contains? left-box %) touches)
      (assoc-in db [:controls :dpad :state] :left)

      (some #(u/box-contains? right-box %) touches)
      (assoc-in db [:controls :dpad :state] :right)

      :else
      (assoc-in db [:controls :dpad :state] :idle))))

(defn handle-shoot [db]
  (if (get-in db [:objects 0 :inventory :bow])
    (let [touches (get-touches db)
          [x y] k/SHOOT-BTN-POS
          btn-area [x (+ y k/CONTROLS-Y) k/SHOOT-BTN-WIDTH k/SHOOT-BTN-HEIGHT]]
      (if (some #(u/box-contains? btn-area %) touches)
        (assoc-in db [:controls :shoot-btn :state] :press)
        (assoc-in db [:controls :shoot-btn :state] :idle)))
    db))

(defn set-rama-state [db]
  (let [shoot-btn-state (-> db :controls :shoot-btn :state)
        dpad-state (-> db :controls :dpad :state)]
    (cond
      (= :press shoot-btn-state)
      (assoc-in db [:objects 0 :state] :shoot)

      (not= :idle dpad-state)
      (-> db
          (assoc-in [:objects 0 :state] :walk)
          (assoc-in [:objects 0 :dir] dpad-state))

      (= :idle dpad-state)
      (assoc-in db [:objects 0 :state] :idle))))

(defn handle-movements [db]
  (let [{:keys [state dir]} (get-in db [:objects 0])
        [dx dy] k/WALK-VEL]
    (if (= :walk state)
      (update-in db [:objects 0 :pos]
                 (fn [[x y]]
                   (case dir
                     :left [(- x dx) y]
                     :right [(+ x dx) y]
                     :up [x (- y dy)]
                     :down [x (+ y dy)])))
      db)))

(reg-event-db
 :clean-collisions
 (fn [db [_ obj1 obj2]]
   (assoc db :collisions {})))

(reg-event-db
 :register-collision
 (fn [db [_ o1 o2]]
   (update-in db [:collisions (:id o1)] conj (:id o2))))

(defn register-collisions! [db]
  (u/evt> [:clean-collisions])
  (doseq [obj1 (-> db :objects vals)
          col (:collidables obj1)
          obj2 (filter #(= col (:type %))
                       (-> db :objects
                           (dissoc (:id obj1))
                           vals))
          :let [[x1 y1] (:pos obj1)
                [x2 y2] (:pos obj2)
                w1 (or (:width obj1) k/TILE-WIDTH)
                w2 (or (:width obj2) k/TILE-WIDTH)
                h1 (or (:height obj1) k/TILE-HEIGHT)
                h2 (or (:height obj2) k/TILE-HEIGHT)
                obj1-box [x1 y1 w1 h1]
                obj2-box [x2 y2 w2 h2]]
          :when (u/colliding? obj1-box obj2-box)]
    (u/evt> [:register-collision obj1 obj2]))
  db)

(defn handle-collisions
  [db]
  (let [rama-collisions (get-in db [:collisions 0])]
    (reduce (fn [db id]
              (let [rama (get-in db [:objects 0])
                    obj (get-in db [:objects id])]
                (case (:type obj)
                  :bow-pickup
                  (-> db
                      (assoc-in [:objects 0 :inventory :bow] {})
                      (u/dissoc-in [:objects id]))

                  ;; default
                  db)))
            db
            rama-collisions)))

(defn handle-interactions [db]
  (-> db
      handle-dpad
      handle-shoot
      set-rama-state
      handle-movements
      register-collisions!
      handle-collisions))

(defn next-frame [db]
  (let [{:keys [objects sprites]} db]
    (assoc db :objects
           (into {}
                 (for [[id c] objects
                       :let [{:keys [type state dir pos curr-frame]} c
                             frames (get-in sprites [type state dir :frames])]]
                   [id (update c :curr-frame #(mod (inc %) (count frames)))])))))

(reg-event-db
 :tick
 (fn [db _]
   (-> db
       handle-interactions
       next-frame)))
