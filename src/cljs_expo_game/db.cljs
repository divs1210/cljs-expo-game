(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::app-db map?)

;; initial state of app-db
(def app-db
  {})
