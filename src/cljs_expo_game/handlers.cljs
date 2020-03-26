(ns cljs-expo-game.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
    [clojure.spec.alpha :as s]
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
 :tick
 (fn [db _]
   (-> db
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
 (fn [db [_ obj1 obj2]]
   (assoc db :collisions {})))

(reg-event-db
 :register-collision
 (fn [db [_ o1 o2]]
   (update-in db [:collisions (:id o1)] conj (:id o2))))

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
 :remove-object
 (fn [db [_ id]]
   (u/dissoc-in db [:objects id])))

(reg-event-db
 :after-ms
 (fn [db [_ ms event]]
   (js/setTimeout #(u/evt> event) ms)
   db))
