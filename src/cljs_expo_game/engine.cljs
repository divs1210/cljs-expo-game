(ns cljs-expo-game.engine
  (:require [reagent.core :as r]
            [cljs-expo-game.components :as com]
            [cljs.pprint :refer [pprint]]))

(def ReactNativeGameEngine
  (js/require "react-native-game-engine"))

(def game-engine
  (r/adapt-react-class (.-GameEngine ReactNativeGameEngine)))

(def ^:const RADIUS 50)

(defn render-finger
  [{:keys [id position screen]}]
  (let [[x y] (js->clj position)]
    [com/view {:style {:border-color "#CCC"
                       :border-width 4
                       :border-radius (* RADIUS 2)
                       :width (* RADIUS 2)
                       :height (* RADIUS 2)
                       :background-color "pink"
                       :position "absolute"
                       :left (- x RADIUS)
                       :top (- y RADIUS)}}]))

(defn new-finger
  [& {:keys [id position]}]
  #js {:id id
       :position (clj->js position)
       :renderer (r/reactify-component render-finger)})

(defn add-finger
  [entities events]
  (doseq [t (.-touches events)
          :when (= "start" (.-type t))
          :let [id (.-id t)
                finger (aget entities id)
                new-pos [(-> t .-event .-pageX)
                         (-> t .-event .-pageY)]]]
    (if (nil? finger)
      ;; create new finger
      (aset entities id
            (new-finger :id id
                        :position new-pos))
      ;; snap to location
      (set! (.-position finger)
            (clj->js new-pos))))
  entities)

(defn move-finger
  [entities events]
  (doseq [t (.-touches events)
          :when (= "move" (.-type t))
          :let [finger (aget entities (.-id t))]
          :when (and finger (.-position finger))
          :let [[x y] (js->clj (.-position finger))
                new-pos #js [(-> t .-delta .-pageX (+ x))
                             (-> t .-delta .-pageY (+ y))]]]
    (set! (.-position finger)
          new-pos))
  entities)

(defn remove-finger
  [entities events]
  (doseq [t (.-touches events)
          :when (= "end" (.-type t))
          :let [id (.-id t)
                finger (aget entities id)]
          :when finger]
    (js-delete entities id))
  entities)

(defn render-edn-box
  [{:keys [id position data screen]}]
  (let [[x y] (js->clj position)
        data-str (with-out-str (pprint data))]
    [com/view
     {:style {:background-color "white"
              :position "absolute"
              :left x
              :top y}}
     [com/text
      {:style {:background-color "yellow"
               :padding 10
               :border-radius 5}}
      data-str]]))

(defn new-edn-box
  [& {:keys [id position data]}]
  #js {:id id
       :data data
       :position (clj->js position)
       :renderer (r/reactify-component render-edn-box)})

(defn game []
  [game-engine {:style {:flex 1
                        :background-color "#FFF"}
                :systems #js [add-finger
                              move-finger
                              remove-finger]
                :entities #js {:code (new-edn-box
                                      :id "code"
                                      :data [1 2 3]
                                      :position [100 100])}}
   [com/status-bar {:hidden true}]])
