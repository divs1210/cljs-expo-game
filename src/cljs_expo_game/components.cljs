(ns cljs-expo-game.components
  (:require [reagent.core :as r]))

(def ReactNative (js/require "react-native"))

(def status-bar (r/adapt-react-class (.-StatusBar ReactNative)))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def image-background (r/adapt-react-class (.-ImageBackground ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))
(def ionicons (.-Ionicons AtExpo))
(def ion-icon (r/adapt-react-class ionicons))

(def Dimensions
  (.-Dimensions ReactNative))

(defn alert [text]
  (.alert (.-Alert ReactNative) text))
