(ns cljs-expo-game.util
  (:require [re-frame.core :as rf]
            [chivorcam.core :refer [defmacro defmacfn]]
            [cljs-expo-game.constants :as k]))

(defn <sub
  [path]
  (rf/subscribe [:get-in path]))

(defn evt>
  [evt]
  (rf/dispatch evt))

(defn dissoc-in
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (assoc m k newmap))
      m)
    (dissoc m k)))

(defn pos->grid
  [[x y]]
  [(int (/ y k/TILE-HEIGHT))
   (int (/ x k/TILE-WIDTH))])

(defn box-contains?
  [[x y width height] [x1 y1]]
  (and (<= x x1 (+ x width))
       (<= y y1 (+ y height))))

(defn obj->box
  [obj]
  (let [[x y] (:pos obj)
        w (or (:width obj) k/TILE-WIDTH)
        h (or (:height obj) k/TILE-HEIGHT)]
    [x y w h]))

(defn center-box
  [[x y w h]]
  (let [third-width (/ w 3)
        third-height (/ h 3)]
    [(+ x third-width) (+ y third-height) third-width third-height]))

(defn obj->center-box
  [obj]
  (-> obj obj->box center-box))

(defn obj->grid
  [obj]
  (let [[x y w h] (obj->box obj)
        bottom-middle [(+ x (/ w 2))
                       (+ y h)]]
    (pos->grid bottom-middle)))

(defn colliding?
  [[x1 y1 w1 h1 :as b1] [x2 y2 w2 h2 :as b2]]
  (or (box-contains? b1 [x2 y2])
      (box-contains? b1 [(+ x2 w2) y2])
      (box-contains? b1 [x2 (+ y2 w2)])
      (box-contains? b1 [(+ x2 w2) (+ y2 w2)])

      (box-contains? b2 [x1 y1])
      (box-contains? b2 [(+ x1 w1) y1])
      (box-contains? b2 [x1 (+ y1 w1)])
      (box-contains? b2 [(+ x1 w1) (+ y1 w1)])))

(defn thirds
  [[x y w h]]
  (let [third-width (js/Math.ceil (/ w 3))
        third-height (js/Math.ceil (/ h 3))]
    {:top [x y w third-height]
     :bottom [x (+ y (* 2 third-height)) w third-height]
     :left [x y third-width h]
     :right [(+ x (* 2 third-width)) y third-width h]}))

(defn collision-dir
  [[x1 y1 w1 h1 :as b1] [x2 y2 w2 y2 :as b2]]
  (let [b1-thirds (thirds b1)
        b2-thirds (thirds b2)]
    (cond
      (colliding? (:top b1-thirds) (:bottom b2-thirds))
      :top

      (colliding? (:bottom b1-thirds) (:top b2-thirds))
      :bottom

      (colliding? (:left b1-thirds) (:right b2-thirds))
      :left

      (colliding? (:right b1-thirds) (:left b2-thirds))
      :right

      :else :center)))
