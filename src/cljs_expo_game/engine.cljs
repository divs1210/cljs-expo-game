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

(def ^:const TILE-WIDTH
  (int (/ (RES 0) 6)))

(def ^:const TILE-HEIGHT
  (int (/ (RES 1) 15)))

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

   (let [world @(<sub [:world])
         sprites @(<sub [:sprites])
         characters @(<sub [:characters])]
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
                  :width TILE-WIDTH
                  :height TILE-HEIGHT
                  :left (* TILE-WIDTH col)
                  :top (* TILE-HEIGHT row)}}])

      ;; render characters
      (for [[id character] characters
            :let [{:keys [type pos state dir]} character
                  [row col] (:pos character)
                  tile (-> sprites type state dir)]]
        ^{:key id}
        [com/image
         {:source tile
          :style {:position :absolute
                  :resize-mode :stretch
                  :width TILE-WIDTH
                  :height TILE-HEIGHT
                  :left (* TILE-WIDTH col)
                  :top (* TILE-HEIGHT row)}}])

      ;; render fingers
      (for [[id pos] @(<sub [:fingers])
            :let [[x y] pos
                  rama (characters 0)
                  [row col] (:pos rama :pos)]]
        (do
          ;; control player dir
          (if (> y (* TILE-HEIGHT row))
            (evt> [:set-dir 0 :down])
            (evt> [:set-dir 0 :up]))
          (when (< (- y TILE-HEIGHT)
                   (* TILE-HEIGHT row)
                   (+ y TILE-HEIGHT))
            (if (> x (* TILE-WIDTH col))
              (evt> [:set-dir 0 :right])
              (evt> [:set-dir 0 :left])))
          ^{:key id}
          [finger pos]))])])
