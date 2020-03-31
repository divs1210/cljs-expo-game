(ns cljs-expo-game.components
  (:require [reagent.core :as r]
            [cljs-expo-game.constants :as k]))

(def ReactNative (js/require "react-native"))
(def RNDisplay (js/require "react-native-display"))

(def status-bar (r/adapt-react-class (.-StatusBar ReactNative)))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def image-background (r/adapt-react-class (.-ImageBackground ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def display (r/adapt-react-class (.-default RNDisplay)))

(def expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))
(def ionicons (.-Ionicons AtExpo))
(def ion-icon (r/adapt-react-class ionicons))

(defn alert [text]
  (.alert (.-Alert ReactNative) text))

(defn sprite
  [{:keys [tile-index curr-state curr-dir curr-frame style]}]
  (let [curr-tiles (get-in tile-index [curr-state curr-dir :frames])

        curr-frame (if (< curr-frame (count curr-tiles))
                     curr-frame
                     0)

        {:keys [top left width height rot]} style

        rot (or rot 0)]
    (if (= "android" k/OS)
      [view
       {:style {:position :absolute
                :top top
                :left left
                :width width
                :height height}}
       (for [[state dirs-map] tile-index
             [dir tiles-map] dirs-map
             :let [first-tile (-> tiles-map :frames first)
                   show? (and (zero? curr-frame)
                              (= curr-state state)
                              (= curr-dir dir))]]
         ^{:key (hash [state dir])}
         [image
          {:source first-tile
           :style {:position :absolute
                   :top 0
                   :left 0
                   :width width
                   :height height
                   :opacity (if show? 1 0.05)
                   :resize-mode :stretch
                   :transform [{:rotate (str rot "deg")}]}}])
       (for [[i tile] (map-indexed vector curr-tiles)
             :let [show? (= curr-frame i)]]
         ^{:key i}
         [image
          {:source tile
           :style {:position :absolute
                   :top 0
                   :left 0
                   :width width
                   :height height
                   :opacity (if show? 1 0)
                   :resize-mode :stretch
                   :transform [{:rotate (str rot "deg")}]}}])]
      ;; iOS
      [image
       {:source (nth curr-tiles curr-frame)
        :style {:position :absolute
                :top top
                :left left
                :width width
                :height height
                :resize-mode :stretch
                :transform [{:rotate (str rot "deg")}]}}])))
