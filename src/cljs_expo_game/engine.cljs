(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>]]
            [cljs-expo-game.tiles :as tiles]
            [cljs.pprint :refer [pprint]]))

(def ^:const FPS 20)

(def ^:const RES
  [(-> com/Dimensions (.get "window") .-width)
   (-> com/Dimensions (.get "window") .-height)])

(def ^:const TILE-SIZE 128)

(def ^:const FRADIUS 50)

(defn finger
  [[x y]]
  [com/view {:style {:border-color "#CCC"
                     :border-width 4
                     :border-radius (* FRADIUS 2)
                     :width (* FRADIUS 2)
                     :height (* FRADIUS 2)
                     :background-color "pink"
                     :position "absolute"
                     :left (- x FRADIUS)
                     :top (- y FRADIUS)}}])

(defn game []
  [com/view
   {:style {:flex 1
            :clickable true}

    :on-touch-start
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:add-finger (.-identifier e) [(.-pageX e) (.-pageY e)]])))

    :on-touch-move
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:move-finger (.-identifier e) [(.-pageX e) (.-pageY e)]])))

    :on-touch-end
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:remove-finger (.-identifier e)])))}

   [com/status-bar
    {:hidden true}]

   [com/image-background
    {:source tiles/water
     :style {:width "100%"
             :height "100%"}}

    (for [[id terrain] (map-indexed vector @(<sub [:world]))
          :let [[row col] (:pos terrain)
                tile (:tile terrain)]]
      ^{:key id}
      [com/image
       {:source tile
        :style {:position :absolute
                :left (* TILE-SIZE col)
                :top (* TILE-SIZE row)}}])
    
    (for [[id pos] @(<sub [:fingers])]
      ^{:key id}
      [finger pos])]])
