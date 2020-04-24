(ns cljs-expo-game.db
  (:require [clojure.spec.alpha :as s]
            [cljs-expo-game.tiles :as tiles]
            [cljs-expo-game.objects :as o]
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
             :lakshmana {:idle {:up {:frames tiles/lxmn-idle-up}
                                :down {:frames tiles/lxmn-idle-down}
                                :left {:frames tiles/lxmn-idle-left}
                                :right {:frames tiles/lxmn-idle-right}}
                         :walk {:up {:frames tiles/lxmn-walk-up}
                                :down {:frames tiles/lxmn-walk-down}
                                :left {:frames tiles/lxmn-walk-left}
                                :right {:frames tiles/lxmn-walk-right}}}
             :tadaka {:idle {:up {:frames tiles/tadaka-idle-up}
                             :down {:frames tiles/tadaka-idle-down}
                             :left {:frames tiles/tadaka-idle-left}
                             :right {:frames tiles/tadaka-idle-right}}
                      :walk {:up {:frames tiles/tadaka-walk-up}
                             :down {:frames tiles/tadaka-walk-down}
                             :left {:frames tiles/tadaka-walk-left}
                             :right {:frames tiles/tadaka-walk-right}}}
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
   :objects {:rama (-> o/rama
                       (assoc :pos [60 390]
                              :dir :right))
             #_:lakshmana #_(assoc o/lakshmana
                               :pos [45 405]
                               :dir :right)
             :tadaka (assoc o/tadaka
                            :pos [(-> k/RES first (- k/TILE-WIDTH)) 350]
                            :dir :left
                            :state :idle)}
   :text nil
   :fingers {}
   :controls {:dpad {:state :idle
                     :dir :down}
              :shoot-btn {:state :idle
                          :enabled? true}}
   :world (for [row (range 16)
                col (range 7)]
            {:pos [row col]
             :tile tiles/grass})})
