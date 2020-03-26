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

(def ^:const CONTROLS-HEIGHT
  (- (RES 1) CONTROLS-Y))

(def ^:const FRADIUS 50)

(def ^:const FPS
  (/ 1000 30)) ;; 30 FPS

(def ^:const DPAD-POS
  [0 (* 0.5 TILE-HEIGHT)])

(def ^:const DPAD-WIDTH
  (* (RES 0) 0.4))

(def ^:const DPAD-HEIGHT
  DPAD-WIDTH)

(def ^:const SHOOT-BTN-POS
  [(* (RES 0) 0.5)
   (* (RES 1) 0.09)])

(def ^:const SHOOT-BTN-WIDTH
  (* FRADIUS 1.2))

(def ^:const SHOOT-BTN-HEIGHT
  (* FRADIUS 1.2))

(def ^:const WALK-VEL
  [(/ TILE-WIDTH 14) (/ TILE-HEIGHT 14)])

(def ^:const ARROW-VEL
  [(/ TILE-WIDTH 3) (/ TILE-HEIGHT 3)])
