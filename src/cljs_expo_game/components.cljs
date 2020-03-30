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
  [{:keys [frames curr-frame style]}]
  (let [curr-frame (if (< curr-frame (count frames))
                     curr-frame
                     0)
        {:keys [top left width height rot]
         :or {rot 0}}
        style]
    [view
     {:style {:position :absolute
              :top top
              :left left
              :width width
              :height height}}
     (if (= "android" k/OS)
       (for [[i f] (map-indexed vector frames)
             :let [show? (= curr-frame i)]]
         ^{:key i}
         [image
          {:source f
           :style {:position :absolute
                   :top 0
                   :left 0
                   :width width
                   :height height
                   :opacity (if show? 1 0)
                   :resize-mode :stretch
                   :transform [{:rotate (str rot "deg")}]}}])
       [image
        {:source (nth frames curr-frame)
         :style {:position :absolute
                 :top 0
                 :left 0
                 :width width
                 :height height
                 :resize-mode :stretch
                 :transform [{:rotate (str rot "deg")}]}}])]))
