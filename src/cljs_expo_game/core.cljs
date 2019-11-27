(ns cljs-expo-game.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core
             :refer [subscribe dispatch dispatch-sync]]
            [oops.core :refer [ocall]]
            [cljs-expo-game.components
             :refer [view image text ion-icon touchable-highlight alert]]
            [cljs-expo-game.handlers]
            [cljs-expo-game.subs]))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column"
                     :margin 40
                     :align-items "center"}}
       [image {:source (js/require "./assets/images/cljs.png")
               :style {:width 200
                       :height 200}}]
       [text {:style {:font-size 30
                      :font-weight "100"
                      :margin-bottom 20
                      :text-align "center"}}
        @greeting]
       [ion-icon {:name "ios-arrow-down"
                  :size 60
                  :color "green"}]
       [touchable-highlight {:style {:background-color "#999"
                                     :padding 10
                                     :border-radius 5}
                             :on-press #(alert "HELLO, BRO!")}
        [text {:style {:color "white"
                       :text-align "center"
                       :font-weight "bold"}}
         "press me"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (ocall (js/require "expo")
         "registerRootComponent"
         (r/reactify-component app-root)))
