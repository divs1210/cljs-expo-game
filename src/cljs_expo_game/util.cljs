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
