(ns cljs-expo-game.render
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>] :as u]
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
              :height k/CONTROLS-HEIGHT}}
     ;; walk
     [com/image
      {:source dpad-tile
       :style {:position :absolute
               :resize-mode :stretch
               :left (k/DPAD-POS 0)
               :top (k/DPAD-POS 1)
               :width k/DPAD-WIDTH
               :height k/DPAD-HEIGHT}}]
     ;; shoot
     (when @(<sub [:objects 0 :inventory :bow])
       [com/view
        {:style {:justify-content :center
                 :align-items :center
                 :border-radius (* k/FRADIUS 2)
                 :border-width 6
                 :border-color :black
                 :width k/SHOOT-BTN-WIDTH
                 :height k/SHOOT-BTN-HEIGHT
                 :background-color shoot-btn-color
                 :position :absolute
                 :left (k/SHOOT-BTN-POS 0)
                 :top (k/SHOOT-BTN-POS 1)}}])
     ;; reset
     [com/view
      {:style {:justify-content :center
               :border-radius (/ k/FRADIUS 2)
               :border-width 6
               :border-color :black
               :align-items :center
               :width k/TILE-WIDTH
               :height k/TILE-HEIGHT
               :background-color :red
               :position :absolute
               :left (* 5 k/TILE-WIDTH)
               :bottom 0}
       :on-touch-start #(evt> [:initialize-db])}
      [com/text
       "RESET"]]]))

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
      (for [[id object] (sort-by (fn [[id obj]]
                                   (let [[x y w h] (u/obj->box obj)]
                                     (+ y h)))
                                 objects)
            :let [{:keys [type pos width height rot
                          state dir curr-frame]
                   :or {rot 0
                        width k/TILE-WIDTH
                        height k/TILE-HEIGHT}} object

                  [x y] pos
                  frames (get-in sprites [type state dir :frames])
                  tile (nth frames curr-frame)]]
        ^{:key id}
        [com/image
         {:source tile
          :style {:position :absolute
                  :resize-mode :stretch
                  :width width
                  :height height
                  :left x
                  :top y
                  :transform [{:rotate (str rot "deg")}]}}])

      ;; render collision boxes
      (for [[id object] objects
            :let [[x y w h] (u/obj->center-box object)]]
        ^{:key id}
        [com/view
         {:style {:position :absolute
                  :width w
                  :height h
                  :left x
                  :top y
                  :background-color "rgba(200, 0, 0, 0.3)"}}])

      ;; render speech
      (when-let [{:keys [speaker speech]} @(<sub [:text])]
        [com/view
         {:style {:justify-content :center
                  :border-radius (/ k/FRADIUS 4)
                  :border-width 6
                  :padding 4
                  :border-color :black
                  :align-items :center
                  :width (* 4 k/TILE-WIDTH)
                  :background-color :white
                  :position :absolute
                  :left k/TILE-WIDTH
                  :top k/TILE-HEIGHT}
          :on-touch-start #(evt> [:initialize-db])}
         [com/text
          {:style {:font-weight :bold
                   :padding 2}}
          speaker]
         [com/text
          speech]])

      ;; render control panel
      [controls]

      ;; render info
      [com/text
       {:style {:position :absolute
                :bottom 10
                :left 10
                :background-color :black
                :color :white
                :padding 3}}
       (let [rama (objects 0)
             [x y] (:pos rama)
             [row col] (u/obj->grid rama)]
         (str "Pos: " (int x) "," (int y)
              " Grid: " (int row) "," (int col)
              " Frame: " (:curr-frame rama)
              " Objects: " (count objects)))]])])
