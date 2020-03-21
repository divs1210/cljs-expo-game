(ns cljs-expo-game.constants)

(def ReactNative (js/require "react-native"))
(def Dimensions (.-Dimensions ReactNative))

(def ^:const RES
  [(-> Dimensions (.get "window") .-width)
   (-> Dimensions (.get "window") .-height)])

(def ^:const GRID-SIZE
  [15 6])

(def ^:const TILE-WIDTH
  (int (/ (RES 0) (GRID-SIZE 1))))

(def ^:const TILE-HEIGHT
  (int (/ (RES 1) (GRID-SIZE 0))))

(def ^:const CONTROLS-Y
  (* TILE-HEIGHT 11))

(def ^:const FRADIUS 50)

(def ^:const FPS
  (/ 1000 30)) ;; 30 FPS

(def ^:const DPAD-POS
  [0 (* 0.75 TILE-HEIGHT)])

(def ^:const DPAD-WIDTH
  (* 2.2 TILE-WIDTH))

(def ^:const DPAD-HEIGHT
  (* 2.5 TILE-HEIGHT))
