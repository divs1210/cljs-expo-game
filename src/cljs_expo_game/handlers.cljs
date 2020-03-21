(ns cljs-expo-game.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
    [clojure.spec.alpha :as s]
    [cljs-expo-game.db :as db :refer [app-db]]
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
 :add-finger
 (fn [db [_ id position]]
   (assoc-in db [:fingers id] position)))

(reg-event-db
 :move-finger
 (fn [db [_ id position]]
   (assoc-in db [:fingers id] position)))

(reg-event-db
 :remove-finger
 (fn [db [_ id]]
   (u/dissoc-in db [:fingers] id)))

(reg-event-db
 :set-dir
 (fn [db [_ id dir]]
   (assoc-in db [:characters id :dir] dir)))
