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
  (let [touches (get-touches db)
        [x y] k/SHOOT-BTN-POS
        btn-area [x (+ y k/CONTROLS-Y) k/SHOOT-BTN-WIDTH k/SHOOT-BTN-HEIGHT]]
    (if (some #(u/box-contains? btn-area %) touches)
      (assoc-in db [:controls :shoot-btn :state] :press)
      (assoc-in db [:controls :shoot-btn :state] :idle))))

(defn set-rama-state [db]
  (let [dpad-state (-> db :controls :dpad :state)
        shoot-btn-state (-> db :controls :shoot-btn :state)]
    (cond
      (= :press shoot-btn-state)
      (assoc-in db [:characters 0 :state] :shoot)

      (= :idle dpad-state)
      (assoc-in db [:characters 0 :state] :idle)

      :else-dpad-pressed
      (-> db
          (assoc-in [:characters 0 :state] :walk)
          (assoc-in [:characters 0 :dir] dpad-state)))))

(defn handle-movements [db]
  (let [{:keys [state dir]} (get-in db [:characters 0])]
    (if (= :walk state)
      (update-in db [:characters 0 :pos]
                 (fn [[x y]]
                   (case dir
                     :left [(- x 5) y]
                     :right [(+ x 5) y]
                     :up [x (- y 5)]
                     :down [x (+ y 5)])))
      db)))

(defn handle-interactions [db]
  (-> db
      handle-dpad
      handle-shoot
      set-rama-state
      handle-movements))

(defn next-frame [db]
  (let [{:keys [characters sprites]} db]
    (assoc db :characters
           (into {}
                 (for [[id c] characters
                       :let [{:keys [type state dir pos curr-frame]} c
                             frames (get-in sprites [type state dir :frames])]]
                   [id (update c :curr-frame #(mod (inc %) (count frames)))])))))

(reg-event-db
 :tick
 (fn [db _]
   (-> db
       handle-interactions
       next-frame)))
