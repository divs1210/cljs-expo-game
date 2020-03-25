(ns cljs-expo-game.tiles)

;; Terrain
;; =======
(def water
  (js/require "./assets/images/terrain/water.jpeg"))

(def grass
  (js/require "./assets/images/terrain/grass.png"))

(def sand
  (js/require "./assets/images/terrain/sand.png"))

;; bottom left
(def beach-bl-grass
  (js/require "./assets/images/terrain/beach_bl_grass.png"))

;; left middle
(def beach-lm-grass
  (js/require "./assets/images/terrain/beach_lm_grass.png"))

;; bottom middle
(def beach-bm-grass
  (js/require "./assets/images/terrain/beach_bm_grass.png"))

;; right middle
(def beach-rm-grass
  (js/require "./assets/images/terrain/beach_rm_grass.png"))

;; bottom right
(def beach-br-grass
  (js/require "./assets/images/terrain/beach_br_grass.png"))

;; top left
(def beach-tl-grass
  (js/require "./assets/images/terrain/beach_tl_grass.png"))

;; top medium
(def beach-tm-grass
  (js/require "./assets/images/terrain/beach_tm_grass.png"))

;; top right
(def beach-tr-grass
  (js/require "./assets/images/terrain/beach_tr_grass.png"))

;; top left diagonal
(def beach-tld-grass
  (js/require "./assets/images/terrain/beach_tld_grass.png"))

;; top right diagonal
(def beach-trd-grass
  (js/require "./assets/images/terrain/beach_trd_grass.png"))


;; Rama
;; ====
(def rama-idle-up
  [(js/require (str "./assets/images/rama/walk_up_0.png"))])

(def rama-idle-down
  [(js/require (str "./assets/images/rama/walk_down_0.png"))])

(def rama-idle-left
  [(js/require (str "./assets/images/rama/walk_left_0.png"))])

(def rama-idle-right
  [(js/require (str "./assets/images/rama/walk_right_0.png"))])

(def rama-walk-up
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_up_" i ".png"))))

(def rama-walk-down
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_down_" i ".png"))))

(def rama-walk-left
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_left_" i ".png"))))

(def rama-walk-right
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_right_" i ".png"))))

(def rama-shoot-up
  (for [i (range 13)]
    (js/require (str "./assets/images/rama/shoot_up_" i ".png"))))

(def rama-shoot-down
  (for [i (range 13)]
    (js/require (str "./assets/images/rama/shoot_down_" i ".png"))))

(def rama-shoot-left
  (for [i (range 13)]
    (js/require (str "./assets/images/rama/shoot_left_" i ".png"))))

(def rama-shoot-right
  (for [i (range 13)]
    (js/require (str "./assets/images/rama/shoot_right_" i ".png"))))


;; Vishwamitra
;; ===========
(def vishwamitra-idle
  [(js/require (str "./assets/images/vishwamitra/idle_1.png"))]
  #_(for [i (range 1 9)]
    (js/require (str "./assets/images/vishwamitra/idle_" i ".png"))))


;; Objects
;; =======
(def bow-pickup
  [(js/require (str "./assets/images/objects/bow_pickup.png"))])

(def bonfire
  (for [i (range 5)]
    (js/require (str "./assets/images/bonfire/" i ".png"))))


;; Controls
;; ========
(def dpad
  (js/require "./assets/images/controls/dpad.png"))

(def dpad-up
  (js/require "./assets/images/controls/dpad_up.png"))

(def dpad-down
  (js/require "./assets/images/controls/dpad_down.png"))

(def dpad-left
  (js/require "./assets/images/controls/dpad_left.png"))

(def dpad-right
  (js/require "./assets/images/controls/dpad_right.png"))
