(ns cljs-expo-game.render
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-expo-game.components :as com]
            [cljs-expo-game.util :refer [<sub evt>] :as u]
            [cljs-expo-game.tiles :as tiles]
            [cljs.pprint :refer [pprint]]
            [cljs-expo-game.constants :as k]))

(defn controls []
  (let [sprites @(<sub [:sprites])
        dpad @(<sub [:controls :dpad])
        arrows @(<sub [:inventory :arrows])
        shoot-btn-state @(<sub [:controls :shoot-btn :state])
        shoot-btn-color @(<sub [:sprites :shoot-btn shoot-btn-state])
        mantra-btn-state @(<sub [:controls :mantra-btn :state])
        mantra-btn-color @(<sub [:sprites :mantra-btn mantra-btn-state])]
    [com/view
     {:style {:position :absolute
              :left 0
              :top k/CONTROLS-Y
              :width (k/RES 0)
              :height k/CONTROLS-HEIGHT}}
     ;; walk
     [com/sprite
      {:tile-index (sprites :dpad)
       :curr-state (:state dpad)
       :curr-dir (:dir dpad)
       :curr-frame 0
       :style {:top (k/DPAD-POS 1)
               :left (k/DPAD-POS 0)
               :width k/DPAD-WIDTH
               :height k/DPAD-HEIGHT}}]

     ;; shoot
     (when (pos? arrows)
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
                 :top (k/SHOOT-BTN-POS 1)}}
        [com/text arrows]])

     ;; mantra
     [com/view
      {:style {:justify-content :center
               :align-items :center
               :border-radius (* k/FRADIUS 2)
               :border-width 6
               :border-color :black
               :width k/SHOOT-BTN-WIDTH
               :height k/SHOOT-BTN-HEIGHT
               :background-color mantra-btn-color
               :position :absolute
               :left (k/MANTRA-BTN-POS 0)
               :top (k/MANTRA-BTN-POS 1)}}
      [com/text "ॐ"]]

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
            :let [{:keys [type rot state dir curr-frame]} object
                  [x y width height] (u/obj->box object)]]
        ^{:key id}
        [com/sprite
         {:tile-index (sprites type)
          :curr-state state
          :curr-dir dir
          :curr-frame curr-frame
          :style {:top y
                  :left x
                  :width width
                  :height height
                  :rot rot}}])

      ;; render character healths
      (for [[id object] (->> objects
                             (filter (fn [[_ obj]]
                                       (some? (:hp obj))))
                             (sort-by (fn [[_ obj]]
                                        (let [[_ y _ h] (u/obj->box obj)]
                                          (+ y h)))))
            :let [{:keys [hp life]} object
                  [x y width _] (u/obj->box object)
                  bar-x (+ x (* 0.2 width))
                  bar-y (- y 2)
                  bar-width (* 0.6 width)]]
        ^{:key id}
        [com/view
         {:style {:top bar-y
                  :left bar-x
                  :width bar-width
                  :height 5
                  :border-width 1
                  :border-color :black}}
         [com/view
          {:style {:top 0
                   :left 0
                   :width (if (pos? life)
                            (* (- bar-width 2)
                               (/ life hp))
                            0)
                   :height 3
                   :background-color :red}}]])

      ;; render glow
      (for [[id object] (->> objects
                             (filter (fn [[_ obj]]
                                       (:glow? obj)))
                             (sort-by (fn [[_ obj]]
                                        (let [[_ y _ h] (u/obj->box obj)]
                                          (+ y h)))))
            :let [[x y w h] (u/obj->box object)
                  glow-rate (:glow-rate object)
                  width (* w glow-rate 2)
                  height (* h glow-rate 2)
                  x' (+ x
                        (/ w 2)
                        (- (/ width 2)))
                  y' (+ y
                        (/ h 2)
                        (- (/ height 2)))]]
        ^{:key id}
        [com/view
         {:style {:justify-content :center
                  :align-items :center
                  :border-radius (* k/FRADIUS 2)
                  :border-width 2
                  :border-color :orange
                  :width width
                  :height height
                  :background-color :gold
                  :position :absolute
                  :opacity glow-rate
                  :left x'
                  :top y'}}])

      ;; render collision boxes
      #_(for [[id object] objects
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
          {:style {:text-align :center}}
          speech]])

      ;; render control panel
      [controls]])])
