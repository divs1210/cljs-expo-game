(ns cljs-expo-game.components
  (:require [reagent.core :as r]))

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
