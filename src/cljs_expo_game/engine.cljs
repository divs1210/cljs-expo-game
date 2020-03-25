(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>]]
            [cljs-expo-game.tiles :as tiles]
            [cljs.pprint :refer [pprint]]
            [cljs-expo-game.constants :as k]))

(defn controls []
  (let [dpad-state @(<sub [:controls :dpad :state])
        dpad-tile @(<sub [:sprites :dpad dpad-state])
        shoot-btn-state @(<sub [:controls :shoot-btn :state])
        shoot-btn-color @(<sub [:sprites :shoot-btn shoot-btn-state])]
    [com/view
     {:style {:position :absolute
              :left 0
              :top k/CONTROLS-Y
              :width (k/RES 0)
              :height (k/RES 1)
              :background-color :gray}}

     [com/image
      {:source dpad-tile
       :style {:position :absolute
               :resize-mode :stretch
               :left (k/DPAD-POS 0)
               :top (k/DPAD-POS 1)
               :width k/DPAD-WIDTH
               :height k/DPAD-HEIGHT}}]

     (when @(<sub [:objects 0 :inventory :bow])
       [com/view
        {:style {:border-color :black
                 :border-width 6
                 :border-radius (* k/FRADIUS 2)
                 :width k/SHOOT-BTN-WIDTH
                 :height k/SHOOT-BTN-HEIGHT
                 :background-color shoot-btn-color
                 :position :absolute
                 :left (k/SHOOT-BTN-POS 0)
                 :top (k/SHOOT-BTN-POS 1)}}])]))

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
         objects @(<sub [:objects])]
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

      ;; render objects
      (for [[id object] (sort-by (fn [[id ch]]
                                      (-> ch :pos last))
                                    objects)
            :let [type (:type object)
                  {:keys [pos width height state dir curr-frame]} object
                  [x y] pos
                  frames (get-in sprites [type state dir :frames])
                  tile (nth frames curr-frame)]]
        ^{:key id}
        [com/image
         {:source tile
          :style {:position :absolute
                  :resize-mode :stretch
                  :width (or width k/TILE-WIDTH)
                  :height (or height k/TILE-HEIGHT)
                  :left x
                  :top y}}])

      ;; render control panel
      [controls]])])
