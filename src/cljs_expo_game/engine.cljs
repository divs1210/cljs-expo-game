(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [cljs-expo-game.components :as com]))

(def ReactNative
  (js/require "react-native"))

(def status-bar
  (r/adapt-react-class (.-StatusBar ReactNative)))

(def ReactNativeGameEngine
  (js/require "react-native-game-engine"))

(def game-engine
  (r/adapt-react-class (.-GameEngine ReactNativeGameEngine)))

(def RADIUS 20)

(defn finger
  [{:keys [id position screen]}]
  (let [[x y] (js->clj position)]
    [com/view {:style {:border-color "#CCC"
                       :border-width 4
                       :border-radius (* RADIUS 2)
                       :width (* RADIUS 2)
                       :height (* RADIUS 2)
                       :background-color "pink"
                       :position "absolute"
                       :left x
                       :top y}}
     [com/text id]]))

(defn move-finger
  [entities events]
  (doseq [t (.-touches events)
          :when (= "move" (.-type t))
          :let [finger (aget entities (.-id t))
                [x y] (js->clj (.-position finger))]
          :when (and finger (.-position finger))
          :let [new-pos #js [(-> t .-delta .-pageX (+ x))
                             (-> t .-delta .-pageY (+ y))]]]
    (set! (.-position finger)
          new-pos))
  entities)

(defn game []
  [game-engine {:style {:flex 1
                        :background-color "#FFF"}
                :systems #js [move-finger]
                :entities #js {"1" #js {:id "1"
                                        :position #js [40 200]
                                        :renderer (r/reactify-component finger)}
                               "2" #js {:id "2"
                                        :position #js [100 400]
                                        :renderer (r/reactify-component finger)}}}
   [status-bar {:hidden true}]])
