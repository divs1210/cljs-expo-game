(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]))

;; spec of app-db
(s/def ::app-db map?)

;; initial state of app-db
(def app-db
  {:world [{:pos [0 0]
            :tile tiles/beach-lm-grass}
           {:pos [0 1]
            :tile tiles/grass}
           {:pos [0 2]
            :tile tiles/grass}
           {:pos [0 3]
            :tile tiles/grass}

           {:pos [1 0]
            :tile tiles/beach-bl-grass}
           {:pos [1 1]
            :tile tiles/beach-bm-grass}
           {:pos [1 2]
            :tile tiles/beach-bm-grass}
           {:pos [1 3]
            :tile tiles/beach-bm-grass}]
   :fingers {}})
