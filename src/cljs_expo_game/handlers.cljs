(ns cljs-expo-game.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
    [clojure.spec.alpha :as s]
    [cljs-expo-game.constants :as k]
    [cljs-expo-game.db :as db :refer [app-db]]
    [cljs-expo-game.engine :as e]
    [cljs-expo-game.util :as u]))

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
  :no-op
  (fn [db _]
    db))

(reg-event-db
  :set-in
  (fn [db [_ path val]]
    (assoc-in db path val)))

(reg-event-db
 :tick
 (fn [db _]
   (-> db
       e/update-state
       e/handle-interactions
       e/next-frame)))

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

(reg-event-db
 :clean-collisions
 (fn [db _]
   (assoc db :collisions {})))

(reg-event-db
 :register-collision
 (fn [db [_ o1 o2 dir]]
   (assoc-in db [:collisions (:id o1) (:id o2)] dir)))

(reg-event-db
 :set-text
 (fn [db [_ text]]
   (assoc db :text text)))

(reg-event-db
 :clear-text
 (fn [db [_ id]]
   (if (= id (-> db :text :id))
     (assoc db :text nil)
     db)))

(reg-event-db
 :add-to-inventory
 (fn [db [_ key val]]
   (assoc-in db [:objects 0 :inventory key] val)))

(reg-event-db
 :add-object
 (fn [db [_ {:keys [id type] :as obj}]]
   (let [id (or id (gensym type))
         obj (assoc obj :id id)]
     (assoc-in db [:objects id] obj))))

(reg-event-db
 :remove-object
 (fn [db [_ id]]
   (u/dissoc-in db [:objects id])))

(reg-event-db
 :after-ms
 (fn [db [_ ms event]]
   (js/setTimeout #(u/evt> event) ms)
   db))

(reg-event-db
 :uncollide
 (fn [db [_ this obj dir]]
   (let [[cx1 cy1 cw1 ch1] (u/obj->center-box this)
         [x2 y2 w2 h2 :as b] (u/obj->box obj)
         [cx2 cy2 cw2 ch2] (u/center-box b)
         [ncx2 ncy2] (case dir
                       :top [cx2 (- cy1 ch2 2)]
                       :bottom [cx2 (+ cy1 ch1 2)]
                       :left [(- cx1 cw2 2) cy2]
                       :right [(+ cx1 cw1 2) cy2])
         dx (- ncx2 cx2)
         dy (- ncy2 cy2)
         new-pos [(+ x2 dx) (+ y2 dy)]]
     (assoc-in db [:objects (:id obj) :pos] new-pos))))

(reg-event-db
 :walk
 (fn [db [_ obj]]
   (let [{:keys [id state dir pos]} obj
         [x y] pos
         [dx dy] k/WALK-VEL
         new-pos (case dir
                   :left [(- x dx) y]
                   :right [(+ x dx) y]
                   :up [x (- y dy)]
                   :down [x (+ y dy)]
                   ;; else
                   [x y])]
     (-> db
         (assoc-in [:objects id :pos] new-pos)
         (assoc-in [:objects id :state] :walk)))))
