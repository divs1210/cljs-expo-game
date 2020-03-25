(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]
            [cljs-expo-game.constants :as k]))

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
           {:pos [4 4]
            :tile tiles/beach-tr-grass}

           {:pos [5 0]
            :tile tiles/beach-lm-grass}
           {:pos [5 1]
            :tile tiles/grass}
           {:pos [5 2]
            :tile tiles/grass}
           {:pos [5 3]
            :tile tiles/grass}
           {:pos [5 4]
            :tile tiles/beach-trd-grass}
           {:pos [5 5]
            :tile tiles/beach-tr-grass}

           {:pos [6 0]
            :tile tiles/beach-lm-grass}
           {:pos [6 1]
            :tile tiles/grass}
           {:pos [6 2]
            :tile tiles/grass}
           {:pos [6 3]
            :tile tiles/grass}
           {:pos [6 4]
            :tile tiles/grass}
           {:pos [6 5]
            :tile tiles/beach-rm-grass}

           {:pos [7 0]
            :tile tiles/beach-tld-grass}
           {:pos [7 1]
            :tile tiles/grass}
           {:pos [7 2]
            :tile tiles/grass}
           {:pos [7 3]
            :tile tiles/grass}
           {:pos [7 4]
            :tile tiles/grass}
           {:pos [7 5]
            :tile tiles/beach-trd-grass}

           {:pos [8 0]
            :tile tiles/grass}
           {:pos [8 1]
            :tile tiles/grass}
           {:pos [8 2]
            :tile tiles/grass}
           {:pos [8 3]
            :tile tiles/grass}
           {:pos [8 4]
            :tile tiles/grass}
           {:pos [8 5]
            :tile tiles/grass}

           {:pos [9 0]
            :tile tiles/grass}
           {:pos [9 1]
            :tile tiles/grass}
           {:pos [9 2]
            :tile tiles/grass}
           {:pos [9 3]
            :tile tiles/grass}
           {:pos [9 4]
            :tile tiles/grass}
           {:pos [9 5]
            :tile tiles/grass}

           {:pos [10 0]
            :tile tiles/grass}
           {:pos [10 1]
            :tile tiles/grass}
           {:pos [10 2]
            :tile tiles/grass}
           {:pos [10 3]
            :tile tiles/grass}
           {:pos [10 4]
            :tile tiles/grass}
           {:pos [10 5]
            :tile tiles/grass}]
   :sprites {:rama {:idle {:up {:frames tiles/rama-idle-up}
                           :down {:frames tiles/rama-idle-down}
                           :left {:frames tiles/rama-idle-left}
                           :right {:frames tiles/rama-idle-right}}
                    :walk {:up {:frames tiles/rama-walk-up}
                           :down {:frames tiles/rama-walk-down}
                           :left {:frames tiles/rama-walk-left}
                           :right {:frames tiles/rama-walk-right}}
                    :shoot {:up {:frames tiles/rama-shoot-up}
                            :down {:frames tiles/rama-shoot-down}
                            :left {:frames tiles/rama-shoot-left}
                            :right {:frames tiles/rama-shoot-right}}}
             :vishwamitra {:idle {:down {:frames tiles/vishwamitra-idle}}}
             :bow-pickup {:idle {:down {:frames tiles/bow-pickup}}}
             :dpad {:idle tiles/dpad
                    :up tiles/dpad-up
                    :down tiles/dpad-down
                    :left tiles/dpad-left
                    :right tiles/dpad-right}
             :shoot-btn {:idle :gray
                         :press :white}}
   :objects {0 {:id 0
                :type :rama
                :pos [(* 4 k/TILE-WIDTH) (* 7 k/TILE-HEIGHT)]
                :state :idle
                :dir :up
                :inventory {}
                :collidables #{:bow-pickup}
                :curr-frame 0}
             1 {:id 1
                :type :vishwamitra
                :pos [(* 1 k/TILE-WIDTH) (* 4 k/TILE-HEIGHT)]
                :state :idle
                :dir :down
                :curr-frame 0}
             2 {:id 2
                :type :bow-pickup
                :pos [(* 1.25 k/TILE-WIDTH) (* 5.5 k/TILE-HEIGHT)]
                :width (/ k/TILE-WIDTH 2)
                :height (/ k/TILE-HEIGHT 2)
                :state :idle
                :dir :down
                :curr-frame 0}}
   :fingers {}
   :controls {:dpad {:state :idle}
              :shoot-btn {:state :idle}}})
