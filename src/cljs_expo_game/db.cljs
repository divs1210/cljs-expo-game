(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]
            [cljs-expo-game.constants :as k]))

;; spec of app-db
(s/def ::app-db map?)

;; initial state of app-db
(def app-db
  {:sprites {:rama {:idle {:up {:frames tiles/rama-idle-up}
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
             :hut {:idle {:down {:frames tiles/hut}}}
             :bonfire {:idle {:down {:frames tiles/bonfire}}}
             :bow-pickup {:idle {:down {:frames tiles/bow-pickup}}}
             :arrow {:idle {:up {:frames tiles/arrow-up}
                            :down {:frames tiles/arrow-down}
                            :left {:frames tiles/arrow-left}
                            :right {:frames tiles/arrow-right}}}
             :dpad {:idle tiles/dpad
                    :up tiles/dpad-up
                    :down tiles/dpad-down
                    :left tiles/dpad-left
                    :right tiles/dpad-right}
             :shoot-btn {:idle :gold
                         :press :orange}}
   :text {:id 0
          :speaker "Rishi Vishwamitra"
          :speech "Prince Rama, you must learn to fight for righteousness and to protect dharma! Take this bow!"}
   :objects {0 {:id 0
                :type :rama
                :pos [(* 4 k/TILE-WIDTH) (* 7 k/TILE-HEIGHT)]
                :state :idle
                :dir :up
                :inventory {}
                :curr-frame 0}
             1 {:id 1
                :type :vishwamitra
                :pos [(* 2.4 k/TILE-WIDTH) (* 4.5 k/TILE-HEIGHT)]
                :width (* 0.6 k/TILE-WIDTH)
                :height (* 1.1 k/TILE-HEIGHT)
                :state :idle
                :dir :down
                :curr-frame 0
                :on-collide {:rama
                             (fn [this rama dir]
                               (let [id (gensym)]
                                 [[:uncollide this rama dir]
                                  [:set-text {:id id
                                              :speaker "Rishi Vishwamitra"
                                              :speech "You are invading my personal space, Prince Rama!"}]
                                  [:after-ms 2000 [:clear-text id]]]))

                             :arrow
                             (fn [this arrow _]
                               (let [id (gensym)]
                                 [[:remove-object (:id arrow)]
                                  [:set-text {:id id
                                              :speaker "Rishi Vishwamitra"
                                              :speech "Careful, son!"}]
                                  [:after-ms 2000 [:clear-text id]]]))}}
             2 {:id 2
                :type :bow-pickup
                :pos [(* 2.3 k/TILE-WIDTH) (* 6 k/TILE-HEIGHT)]
                :rot -90
                :width (/ k/TILE-WIDTH 2)
                :height (/ k/TILE-HEIGHT 2)
                :state :idle
                :dir :down
                :curr-frame 0
                :on-collide {:rama
                             (fn [this rama _]
                               [[:remove-object (:id this)]
                                [:clear-text 0]
                                [:add-to-inventory :bow {}]])}}
             3 {:id 3
                :type :bonfire
                :pos [(* 3.5 k/TILE-WIDTH) (* 5 k/TILE-HEIGHT)]
                :width (/ k/TILE-WIDTH 2)
                :height (/ k/TILE-HEIGHT 2)
                :state :idle
                :dir :down
                :curr-frame 0
                :on-collide {:rama
                             (fn [this rama dir]
                               [[:uncollide this rama dir]])}}
             4 {:id 4
                :type :hut
                :pos [(* 0.8 k/TILE-WIDTH) (* 4.7 k/TILE-HEIGHT)]
                :width (* k/TILE-WIDTH 1.2)
                :height (* k/TILE-HEIGHT 1.3)
                :state :idle
                :dir :down
                :curr-frame 0
                :on-collide {:rama
                             (fn [this rama dir]
                               [[:uncollide this rama dir]])

                             :arrow
                             (fn [this arrow _]
                               [[:remove-object (:id arrow)]])}}}
   :fingers {}
   :controls {:dpad {:state :idle}
              :shoot-btn {:state :idle}}
   :world [{:pos [4 0]
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
           {:pos [7 6]
            :tile tiles/beach-tm-grass}

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
           {:pos [8 6]
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
           {:pos [9 6]
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
            :tile tiles/grass}
           {:pos [10 6]
            :tile tiles/grass}

           {:pos [11 0]
            :tile tiles/grass}
           {:pos [11 1]
            :tile tiles/grass}
           {:pos [11 2]
            :tile tiles/grass}
           {:pos [11 3]
            :tile tiles/grass}
           {:pos [11 4]
            :tile tiles/grass}
           {:pos [11 5]
            :tile tiles/grass}
           {:pos [11 6]
            :tile tiles/grass}

           {:pos [12 0]
            :tile tiles/grass}
           {:pos [12 1]
            :tile tiles/grass}
           {:pos [12 2]
            :tile tiles/grass}
           {:pos [12 3]
            :tile tiles/grass}
           {:pos [12 4]
            :tile tiles/grass}
           {:pos [12 5]
            :tile tiles/grass}
           {:pos [12 6]
            :tile tiles/grass}

           {:pos [13 0]
            :tile tiles/grass}
           {:pos [13 1]
            :tile tiles/grass}
           {:pos [13 2]
            :tile tiles/grass}
           {:pos [13 3]
            :tile tiles/grass}
           {:pos [13 4]
            :tile tiles/grass}
           {:pos [13 5]
            :tile tiles/grass}
           {:pos [13 6]
            :tile tiles/grass}

           {:pos [14 0]
            :tile tiles/grass}
           {:pos [14 1]
            :tile tiles/grass}
           {:pos [14 2]
            :tile tiles/grass}
           {:pos [14 3]
            :tile tiles/grass}
           {:pos [14 4]
            :tile tiles/grass}
           {:pos [14 5]
            :tile tiles/grass}
           {:pos [14 6]
            :tile tiles/grass}

           {:pos [15 0]
            :tile tiles/grass}
           {:pos [15 1]
            :tile tiles/grass}
           {:pos [15 2]
            :tile tiles/grass}
           {:pos [15 3]
            :tile tiles/grass}
           {:pos [15 4]
            :tile tiles/grass}
           {:pos [15 5]
            :tile tiles/grass}
           {:pos [15 6]
            :tile tiles/grass}]})
