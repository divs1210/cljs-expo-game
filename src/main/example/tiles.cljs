(ns example.tiles)

;; Terrain
;; =======
(defonce water
  (js/require "../assets/images/terrain/water.jpeg"))

(defonce grass
  (js/require "../assets/images/terrain/grass.png"))

(defonce sand
  (js/require "../assets/images/terrain/sand.png"))

;; bottom left
(defonce beach-bl-grass
  (js/require "../assets/images/terrain/beach_bl_grass.png"))

;; left middle
(defonce beach-lm-grass
  (js/require "../assets/images/terrain/beach_lm_grass.png"))

;; bottom middle
(defonce beach-bm-grass
  (js/require "../assets/images/terrain/beach_bm_grass.png"))

;; right middle
(defonce beach-rm-grass
  (js/require "../assets/images/terrain/beach_rm_grass.png"))

;; bottom right
(defonce beach-br-grass
  (js/require "../assets/images/terrain/beach_br_grass.png"))

;; top left
(defonce beach-tl-grass
  (js/require "../assets/images/terrain/beach_tl_grass.png"))

;; top medium
(defonce beach-tm-grass
  (js/require "../assets/images/terrain/beach_tm_grass.png"))

;; top right
(defonce beach-tr-grass
  (js/require "../assets/images/terrain/beach_tr_grass.png"))

;; top left diagonal
(defonce beach-tld-grass
  (js/require "../assets/images/terrain/beach_tld_grass.png"))

;; top right diagonal
(defonce beach-trd-grass
  (js/require "../assets/images/terrain/beach_trd_grass.png"))


;; Rama
;; ====
(defonce rama-idle-up
  [(js/require "../assets/images/rama/walk_up_0.png")])

(defonce rama-idle-down
  [(js/require "../assets/images/rama/walk_down_0.png")])

(defonce rama-idle-left
  [(js/require "../assets/images/rama/walk_left_0.png")])

(defonce rama-idle-right
  [(js/require "../assets/images/rama/walk_right_0.png")])

(defonce rama-walk-up
  [(js/require "../assets/images/rama/walk_up_0.png")
   (js/require "../assets/images/rama/walk_up_1.png")
   (js/require "../assets/images/rama/walk_up_2.png")
   (js/require "../assets/images/rama/walk_up_3.png")
   (js/require "../assets/images/rama/walk_up_4.png")
   (js/require "../assets/images/rama/walk_up_5.png")
   (js/require "../assets/images/rama/walk_up_6.png")
   (js/require "../assets/images/rama/walk_up_7.png")
   (js/require "../assets/images/rama/walk_up_8.png")])

(defonce rama-walk-down
  [(js/require "../assets/images/rama/walk_down_0.png")
   (js/require "../assets/images/rama/walk_down_1.png")
   (js/require "../assets/images/rama/walk_down_2.png")
   (js/require "../assets/images/rama/walk_down_3.png")
   (js/require "../assets/images/rama/walk_down_4.png")
   (js/require "../assets/images/rama/walk_down_5.png")
   (js/require "../assets/images/rama/walk_down_6.png")
   (js/require "../assets/images/rama/walk_down_7.png")
   (js/require "../assets/images/rama/walk_down_8.png")])

(defonce rama-walk-left
  [(js/require "../assets/images/rama/walk_left_0.png")
   (js/require "../assets/images/rama/walk_left_1.png")
   (js/require "../assets/images/rama/walk_left_2.png")
   (js/require "../assets/images/rama/walk_left_3.png")
   (js/require "../assets/images/rama/walk_left_4.png")
   (js/require "../assets/images/rama/walk_left_5.png")
   (js/require "../assets/images/rama/walk_left_6.png")
   (js/require "../assets/images/rama/walk_left_7.png")
   (js/require "../assets/images/rama/walk_left_8.png")])

(defonce rama-walk-right
  [(js/require "../assets/images/rama/walk_right_0.png")
   (js/require "../assets/images/rama/walk_right_1.png")
   (js/require "../assets/images/rama/walk_right_2.png")
   (js/require "../assets/images/rama/walk_right_3.png")
   (js/require "../assets/images/rama/walk_right_4.png")
   (js/require "../assets/images/rama/walk_right_5.png")
   (js/require "../assets/images/rama/walk_right_6.png")
   (js/require "../assets/images/rama/walk_right_7.png")
   (js/require "../assets/images/rama/walk_right_8.png")])

(defonce rama-shoot-up
  [(js/require "../assets/images/rama/shoot_up_0.png")
   (js/require "../assets/images/rama/shoot_up_1.png")
   (js/require "../assets/images/rama/shoot_up_2.png")
   (js/require "../assets/images/rama/shoot_up_3.png")
   (js/require "../assets/images/rama/shoot_up_4.png")
   (js/require "../assets/images/rama/shoot_up_5.png")
   (js/require "../assets/images/rama/shoot_up_6.png")
   (js/require "../assets/images/rama/shoot_up_7.png")
   (js/require "../assets/images/rama/shoot_up_8.png")
   (js/require "../assets/images/rama/shoot_up_9.png")
   (js/require "../assets/images/rama/shoot_up_10.png")
   (js/require "../assets/images/rama/shoot_up_11.png")
   (js/require "../assets/images/rama/shoot_up_12.png")])

(defonce rama-shoot-down
  [(js/require "../assets/images/rama/shoot_down_0.png")
   (js/require "../assets/images/rama/shoot_down_1.png")
   (js/require "../assets/images/rama/shoot_down_2.png")
   (js/require "../assets/images/rama/shoot_down_3.png")
   (js/require "../assets/images/rama/shoot_down_4.png")
   (js/require "../assets/images/rama/shoot_down_5.png")
   (js/require "../assets/images/rama/shoot_down_6.png")
   (js/require "../assets/images/rama/shoot_down_7.png")
   (js/require "../assets/images/rama/shoot_down_8.png")
   (js/require "../assets/images/rama/shoot_down_9.png")
   (js/require "../assets/images/rama/shoot_down_10.png")
   (js/require "../assets/images/rama/shoot_down_11.png")
   (js/require "../assets/images/rama/shoot_down_12.png")])

(defonce rama-shoot-left
  [(js/require "../assets/images/rama/shoot_left_0.png")
   (js/require "../assets/images/rama/shoot_left_1.png")
   (js/require "../assets/images/rama/shoot_left_2.png")
   (js/require "../assets/images/rama/shoot_left_3.png")
   (js/require "../assets/images/rama/shoot_left_4.png")
   (js/require "../assets/images/rama/shoot_left_5.png")
   (js/require "../assets/images/rama/shoot_left_6.png")
   (js/require "../assets/images/rama/shoot_left_7.png")
   (js/require "../assets/images/rama/shoot_left_8.png")
   (js/require "../assets/images/rama/shoot_left_9.png")
   (js/require "../assets/images/rama/shoot_left_10.png")
   (js/require "../assets/images/rama/shoot_left_11.png")
   (js/require "../assets/images/rama/shoot_left_12.png")])

(defonce rama-shoot-right
  [(js/require "../assets/images/rama/shoot_right_0.png")
   (js/require "../assets/images/rama/shoot_right_1.png")
   (js/require "../assets/images/rama/shoot_right_2.png")
   (js/require "../assets/images/rama/shoot_right_3.png")
   (js/require "../assets/images/rama/shoot_right_4.png")
   (js/require "../assets/images/rama/shoot_right_5.png")
   (js/require "../assets/images/rama/shoot_right_6.png")
   (js/require "../assets/images/rama/shoot_right_7.png")
   (js/require "../assets/images/rama/shoot_right_8.png")
   (js/require "../assets/images/rama/shoot_right_9.png")
   (js/require "../assets/images/rama/shoot_right_10.png")
   (js/require "../assets/images/rama/shoot_right_11.png")
   (js/require "../assets/images/rama/shoot_right_12.png")])


;; Lakshmana
;; =========
(defonce lxmn-idle-up
  [(js/require "../assets/images/lakshmana/walk_up_0.png")])

(defonce lxmn-idle-down
  [(js/require "../assets/images/lakshmana/walk_down_0.png")])

(defonce lxmn-idle-left
  [(js/require "../assets/images/lakshmana/walk_left_0.png")])

(defonce lxmn-idle-right
  [(js/require "../assets/images/lakshmana/walk_right_0.png")])

(defonce lxmn-walk-up
  [(js/require "../assets/images/lakshmana/walk_up_0.png")
   (js/require "../assets/images/lakshmana/walk_up_1.png")
   (js/require "../assets/images/lakshmana/walk_up_2.png")
   (js/require "../assets/images/lakshmana/walk_up_3.png")
   (js/require "../assets/images/lakshmana/walk_up_4.png")
   (js/require "../assets/images/lakshmana/walk_up_5.png")
   (js/require "../assets/images/lakshmana/walk_up_6.png")
   (js/require "../assets/images/lakshmana/walk_up_7.png")
   (js/require "../assets/images/lakshmana/walk_up_8.png")])

(defonce lxmn-walk-down
  [(js/require "../assets/images/lakshmana/walk_down_0.png")
   (js/require "../assets/images/lakshmana/walk_down_1.png")
   (js/require "../assets/images/lakshmana/walk_down_2.png")
   (js/require "../assets/images/lakshmana/walk_down_3.png")
   (js/require "../assets/images/lakshmana/walk_down_4.png")
   (js/require "../assets/images/lakshmana/walk_down_5.png")
   (js/require "../assets/images/lakshmana/walk_down_6.png")
   (js/require "../assets/images/lakshmana/walk_down_7.png")
   (js/require "../assets/images/lakshmana/walk_down_8.png")])

(defonce lxmn-walk-left
  [(js/require "../assets/images/lakshmana/walk_left_0.png")
   (js/require "../assets/images/lakshmana/walk_left_1.png")
   (js/require "../assets/images/lakshmana/walk_left_2.png")
   (js/require "../assets/images/lakshmana/walk_left_3.png")
   (js/require "../assets/images/lakshmana/walk_left_4.png")
   (js/require "../assets/images/lakshmana/walk_left_5.png")
   (js/require "../assets/images/lakshmana/walk_left_6.png")
   (js/require "../assets/images/lakshmana/walk_left_7.png")
   (js/require "../assets/images/lakshmana/walk_left_8.png")])

(defonce lxmn-walk-right
  [(js/require "../assets/images/lakshmana/walk_right_0.png")
   (js/require "../assets/images/lakshmana/walk_right_1.png")
   (js/require "../assets/images/lakshmana/walk_right_2.png")
   (js/require "../assets/images/lakshmana/walk_right_3.png")
   (js/require "../assets/images/lakshmana/walk_right_4.png")
   (js/require "../assets/images/lakshmana/walk_right_5.png")
   (js/require "../assets/images/lakshmana/walk_right_6.png")
   (js/require "../assets/images/lakshmana/walk_right_7.png")
   (js/require "../assets/images/lakshmana/walk_right_8.png")])


;; Vishwamitra
;; ===========
(defonce vishwamitra-idle
  [(js/require "../assets/images/vishwamitra/idle_1.png")]
  #_(for [i (range 9)]
      (js/require (str "../assets/images/vishwamitra/idle_" i ".png"))))


;; Tadaka
;; ======
(defonce tadaka-idle-up
  [(js/require "../assets/images/tadaka/walk_up_0.png")])

(defonce tadaka-idle-down
  [(js/require "../assets/images/tadaka/walk_down_0.png")])

(defonce tadaka-idle-left
  [(js/require "../assets/images/tadaka/walk_left_0.png")])

(defonce tadaka-idle-right
  [(js/require "../assets/images/tadaka/walk_right_0.png")])

(defonce tadaka-walk-up
  [(js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_1.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_2.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_3.png")
   (js/require "../assets/images/tadaka/walk_up_0.png")
   (js/require "../assets/images/tadaka/walk_up_0.png")
   (js/require "../assets/images/tadaka/walk_up_0.png")
   (js/require "../assets/images/tadaka/walk_up_0.png")
   (js/require "../assets/images/tadaka/walk_up_0.png")])

(defonce tadaka-walk-down
  [(js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_1.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_2.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_3.png")
   (js/require "../assets/images/tadaka/walk_down_0.png")
   (js/require "../assets/images/tadaka/walk_down_0.png")
   (js/require "../assets/images/tadaka/walk_down_0.png")
   (js/require "../assets/images/tadaka/walk_down_0.png")
   (js/require "../assets/images/tadaka/walk_down_0.png")])

(defonce tadaka-walk-left
  [(js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_1.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_2.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_3.png")
   (js/require "../assets/images/tadaka/walk_left_0.png")
   (js/require "../assets/images/tadaka/walk_left_0.png")
   (js/require "../assets/images/tadaka/walk_left_0.png")
   (js/require "../assets/images/tadaka/walk_left_0.png")
   (js/require "../assets/images/tadaka/walk_left_0.png")])

(defonce tadaka-walk-right
  [(js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_1.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_2.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_3.png")
   (js/require "../assets/images/tadaka/walk_right_0.png")
   (js/require "../assets/images/tadaka/walk_right_0.png")
   (js/require "../assets/images/tadaka/walk_right_0.png")
   (js/require "../assets/images/tadaka/walk_right_0.png")
   (js/require "../assets/images/tadaka/walk_right_0.png")])


;; Arrow
;; =====
(defonce arrow-up
  [(js/require "../assets/images/arrow/arrow_up.png")])

(defonce arrow-down
  [(js/require "../assets/images/arrow/arrow_down.png")])

(defonce arrow-left
  [(js/require "../assets/images/arrow/arrow_left.png")])

(defonce arrow-right
  [(js/require "../assets/images/arrow/arrow_right.png")])


;; Deer
;; ====
(defonce deer-idle-up
  [(js/require "../assets/images/deer/run_up_2.png")])

(defonce deer-idle-down
  [(js/require "../assets/images/deer/run_down_2.png")])

(defonce deer-run-up
  [(js/require "../assets/images/deer/run_up_0.png")
   (js/require "../assets/images/deer/run_up_1.png")
   (js/require "../assets/images/deer/run_up_2.png")
   (js/require "../assets/images/deer/run_up_3.png")
   (js/require "../assets/images/deer/run_up_4.png")
   (js/require "../assets/images/deer/run_up_5.png")])

(defonce deer-run-down
  [(js/require "../assets/images/deer/run_down_0.png")
   (js/require "../assets/images/deer/run_down_1.png")
   (js/require "../assets/images/deer/run_down_2.png")
   (js/require "../assets/images/deer/run_down_3.png")
   (js/require "../assets/images/deer/run_down_4.png")
   (js/require "../assets/images/deer/run_down_5.png")])

(defonce deer-run-left
  [(js/require "../assets/images/deer/run_left_0.png")
   (js/require "../assets/images/deer/run_left_1.png")
   (js/require "../assets/images/deer/run_left_2.png")
   (js/require "../assets/images/deer/run_left_3.png")
   (js/require "../assets/images/deer/run_left_4.png")
   (js/require "../assets/images/deer/run_left_5.png")])

(defonce deer-run-right
  [(js/require "../assets/images/deer/run_right_0.png")
   (js/require "../assets/images/deer/run_right_1.png")
   (js/require "../assets/images/deer/run_right_2.png")
   (js/require "../assets/images/deer/run_right_3.png")
   (js/require "../assets/images/deer/run_right_4.png")
   (js/require "../assets/images/deer/run_right_5.png")])


;; Objects
;; =======
(defonce bonfire
  [(js/require "../assets/images/bonfire/0.png")
   (js/require "../assets/images/bonfire/1.png")
   (js/require "../assets/images/bonfire/2.png")
   (js/require "../assets/images/bonfire/3.png")
   (js/require "../assets/images/bonfire/4.png")])

(defonce bow-pickup
  [(js/require "../assets/images/objects/bow_pickup.png")])

(defonce hut
  [(js/require "../assets/images/objects/hut.png")])

(defonce scarecrow
  [(js/require "../assets/images/objects/ScareCrow.png")])

(defonce collision-area
  [(js/require "../assets/images/objects/collision_area.png")])


;; Controls
;; ========
(defonce dpad-idle
  [(js/require "../assets/images/controls/dpad.png")])

(defonce dpad-up
  [(js/require "../assets/images/controls/dpad_up.png")])

(defonce dpad-down
  [(js/require "../assets/images/controls/dpad_down.png")])

(defonce dpad-left
  [(js/require "../assets/images/controls/dpad_left.png")])

(defonce dpad-right
  [(js/require "../assets/images/controls/dpad_right.png")])
