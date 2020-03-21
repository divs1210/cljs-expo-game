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


;; Rama
;; ====
(def rama-idle-up
  (js/require (str "./assets/images/rama/walk_up_0.png")))

(def rama-idle-down
  (js/require (str "./assets/images/rama/walk_down_0.png")))

(def rama-walk-up
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_up_" i ".png"))))

(def rama-walk-down
  (for [i (range 9)]
    (js/require (str "./assets/images/rama/walk_down_" i ".png"))))
