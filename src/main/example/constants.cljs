(ns example.constants)

(defonce ReactNative (js/require "react-native"))
(defonce Platform (.-Platform ReactNative))
(defonce Dimensions (.-Dimensions ReactNative))

(defonce ^:const OS
  (.-OS Platform))

(defonce ^:const RES
  [(-> Dimensions (.get "window") .-width)
   (-> Dimensions (.get "window") .-height)])

(defonce ^:const GRID-SIZE
  [15 6])

(defonce ^:const TILE-WIDTH
  (int (/ (RES 0) (GRID-SIZE 1))))

(defonce ^:const TILE-HEIGHT
  (int (/ (RES 1) (GRID-SIZE 0))))

(defonce ^:const CONTROLS-Y
  (* TILE-HEIGHT 11))

(defonce ^:const CONTROLS-HEIGHT
  (- (RES 1) CONTROLS-Y))

(defonce ^:const FRADIUS 50)

(defonce ^:const FPS
  (/ 1000 30)) ;; 30 FPS

(defonce ^:const TICK-MS
  (/ 1000 FPS))

(defonce ^:const DPAD-POS
  [(* 0.2 TILE-WIDTH) 0])

(defonce ^:const DPAD-WIDTH
  (* (RES 0) 0.4))

(defonce ^:const DPAD-HEIGHT
  DPAD-WIDTH)

(defonce ^:const SHOOT-BTN-POS
  [(* (RES 0) 0.5)
   (* (RES 1) 0.09)])

(defonce ^:const MANTRA-BTN-POS
  [(* 1.3 (SHOOT-BTN-POS 0))
   (* 0.4 (SHOOT-BTN-POS 1))])

(defonce ^:const SHOOT-BTN-WIDTH
  (* FRADIUS 1.2))

(defonce ^:const SHOOT-BTN-HEIGHT
  (* FRADIUS 1.2))

(defonce ^:const WALK-VEL
  [(/ TILE-WIDTH 14) (/ TILE-HEIGHT 14)])

(defonce ^:const ARROW-VEL
  [(/ TILE-WIDTH 3) (/ TILE-HEIGHT 3)])
