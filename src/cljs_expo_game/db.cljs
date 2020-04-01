(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]
            [cljs-expo-game.objects :as o]))

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
             :lakshmana {:idle {:up {:frames tiles/lxmn-idle-up}
                                :down {:frames tiles/lxmn-idle-down}
                                :left {:frames tiles/lxmn-idle-left}
                                :right {:frames tiles/lxmn-idle-right}}
                         :walk {:up {:frames tiles/lxmn-walk-up}
                                :down {:frames tiles/lxmn-walk-down}
                                :left {:frames tiles/lxmn-walk-left}
                                :right {:frames tiles/lxmn-walk-right}}}
             :hut {:idle {:down {:frames tiles/hut}}}
             :bonfire {:idle {:down {:frames tiles/bonfire}}}
             :bow-pickup {:idle {:down {:frames tiles/bow-pickup}}}
             :scarecrow {:idle {:down {:frames tiles/scarecrow}}}
             :collision-area {:idle {:down {:frames tiles/collision-area}}}
             :arrow {:idle {:up {:frames tiles/arrow-up}
                            :down {:frames tiles/arrow-down}
                            :left {:frames tiles/arrow-left}
                            :right {:frames tiles/arrow-right}}}
             :deer {:idle {:up {:frames tiles/deer-idle-up}
                           :down {:frames tiles/deer-idle-down}}
                    :run {:up {:frames tiles/deer-run-up}
                          :down {:frames tiles/deer-run-down}
                          :left {:frames tiles/deer-run-left}
                          :right {:frames tiles/deer-run-right}}}
             :dpad {:idle {:down {:frames tiles/dpad-idle}}
                    :press {:up {:frames tiles/dpad-up}
                            :down {:frames tiles/dpad-down}
                            :left {:frames tiles/dpad-left}
                            :right {:frames tiles/dpad-right}}}
             :shoot-btn {:idle :gold
                         :press :orange}}
   :text {:id 0
          :speaker "Rishi Vishwamitra"
          :speech "Prince Rama,\nyou must learn to fight for righteousness and to protect dharma!\nTake this bow!"}
   :objects {:rama o/rama
             :lakshmana o/lakshmana
             :vishwamitra o/vishwamitra
             :bow-pickup1 (assoc o/bow-pickup :id :bow-pickup1)
             :bonfire1 (assoc o/bonfire :id :bonfire1)
             :hut1 (assoc o/hut :id :hut1)
             :scarecrow1 (assoc o/scarecrow :id :scarecrow1)}
   :fingers {}
   :controls {:dpad {:state :idle
                     :dir :down}
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
