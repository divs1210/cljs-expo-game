(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>]]
            [cljs.pprint :refer [pprint]]))

(def ^:const RADIUS 50)

(defn finger
  [[x y]]
  [com/view {:style {:border-color "#CCC"
                     :border-width 4
                     :border-radius (* RADIUS 2)
                     :width (* RADIUS 2)
                     :height (* RADIUS 2)
                     :background-color "pink"
                     :position "absolute"
                     :left (- x RADIUS)
                     :top (- y RADIUS)}}])

(defn game []
  [com/view
   {:style {:flex 1
            :background-color "#FFF"
            :clickable true}

    :on-touch-start
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:add-finger (.-identifier e) [(.-pageX e) (.-pageY e)]])))

    :on-touch-move
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:add-finger (.-identifier e) [(.-pageX e) (.-pageY e)]])))

    :on-touch-end
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:remove-finger (.-identifier e)])))}

   [com/status-bar
    {:hidden true}]

   (for [[id pos] @(<sub [:fingers])]
     ^{:key id}
     [finger pos])])
