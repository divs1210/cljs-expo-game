(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>]]
            [cljs-expo-game.tiles :as tiles]
            [cljs.pprint :refer [pprint]]
            [cljs-expo-game.constants :as k]))

(defn finger
  [[x y]]
  [com/view {:style {:border-color "#CCC"
                     :border-width 4
                     :border-radius (* k/FRADIUS 2)
                     :width (* k/FRADIUS 2)
                     :height (* k/FRADIUS 2)
                     :background-color "pink"
                     :position "absolute"
                     :left (- x k/FRADIUS)
                     :top (- y k/FRADIUS)}}])

(defn controls []
  (let [state @(<sub [:dpad :state])
        tile @(<sub [:sprites :dpad state])]
    [com/view
     {:style {:position :absolute
              :left 0
              :top k/CONTROLS-Y
              :width (k/RES 0)
              :height (k/RES 1)
              :background-color :white}}

     [com/image
      {:source tile
       :style {:position :absolute
               :resize-mode :stretch
               :left (k/DPAD-POS 0)
               :top (k/DPAD-POS 1)
               :width k/DPAD-WIDTH
               :height k/DPAD-HEIGHT}}]]))

(def ticker
  (js/setInterval #(evt> [:tick])
                  k/FPS))

(defn game []
  [com/view
   {:style {:flex 1
            :clickable true}

    :on-touch-start
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:add-finger {:id (.-identifier e)
                            :pos [(.-pageX e)
                                  (.-pageY e)]}])))

    :on-touch-move
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:move-finger {:id (.-identifier e)
                             :pos [(.-pageX e)
                                   (.-pageY e)]}])))

    :on-touch-end
    (fn [e]
      (let [e (.-nativeEvent e)]
        (evt> [:remove-finger {:id (.-identifier e)}])))}

   [com/status-bar
    {:hidden true}]

   (let [world @(<sub [:world])
         sprites @(<sub [:sprites])
         characters @(<sub [:characters])
         fingers @(<sub [:fingers])]
     [com/image-background
      {:source tiles/water
       :style {:width "100%"
               :height "100%"}}

      ;; render terrain
      (for [[id terrain] (map-indexed vector world)
            :let [[row col] (:pos terrain)
                  tile (:tile terrain)]]
        ^{:key id}
        [com/image
         {:source tile
          :style {:position :absolute
                  :resize-mode :stretch
                  :width k/TILE-WIDTH
                  :height k/TILE-HEIGHT
                  :left (* k/TILE-WIDTH col)
                  :top (* k/TILE-HEIGHT row)}}])

      ;; render characters
      (for [[id character] characters
            :let [{:keys [type pos state dir curr-frame]} character
                  [x y] pos
                  frames (get-in sprites [type state dir :frames])
                  tile (nth frames curr-frame)]]
        ^{:key id}
        [com/image
         {:source tile
          :style {:position :absolute
                  :resize-mode :stretch
                  :width k/TILE-WIDTH
                  :height k/TILE-HEIGHT
                  :left x
                  :top y}}])

      ;; render fingers
      (for [[id f] fingers
            :let [pos (-> f :path last)]]
        ^{:key id}
        [finger pos])

      ;; render control panel
      [controls]])])
