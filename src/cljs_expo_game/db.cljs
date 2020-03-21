(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]))

;; spec of app-db
(s/def ::app-db map?)

;; initial state of app-db
(def app-db
  {:world [{:pos [4 0]
            :tile tiles/beach-tl-grass}
           {:pos [4 1]
            :tile tiles/beach-tm-grass}
           {:pos [4 2]
            :tile tiles/beach-tm-grass}
           {:pos [4 3]
            :tile tiles/beach-tm-grass}

           {:pos [5 0]
            :tile tiles/beach-lm-grass}
           {:pos [5 1]
            :tile tiles/grass}
           {:pos [5 2]
            :tile tiles/grass}
           {:pos [5 3]
            :tile tiles/grass}

           {:pos [6 0]
            :tile tiles/beach-lm-grass}
           {:pos [6 1]
            :tile tiles/grass}
           {:pos [6 2]
            :tile tiles/grass}
           {:pos [6 3]
            :tile tiles/grass}]
   :sprites {:rama {:idle {:up tiles/rama-idle-up
                           :down tiles/rama-idle-down
                           :left tiles/rama-idle-left
                           :right tiles/rama-idle-right}
                    :walk {}}}
   :characters {0 {:id 0
                   :type :rama
                   :pos [5 2]
                   :state :idle
                   :dir :up}}
   :fingers {}})
