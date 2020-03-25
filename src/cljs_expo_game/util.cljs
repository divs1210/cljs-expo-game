(ns cljs-expo-game.util
  (:require [re-frame.core :as rf]
            [chivorcam.core :refer [defmacro defmacfn]]))

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

(defn box-contains?
  [[x y width height] [x1 y1]]
  (and (<= x x1 (+ x width))
       (<= y y1 (+ y height))))

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
