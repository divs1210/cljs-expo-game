(ns cljs-expo-game.engine
  (:require [cljs-expo-game.util :as u]
            [cljs-expo-game.constants :as k]
            [cljs-expo-game.objects :as o]))

(defn get-touches [db]
  (->> db :fingers vals (map :path) (map last)))

(defn handle-dpad [db]
  (let [touches (get-touches db)
        [x y] k/DPAD-POS
        third-height (/ k/DPAD-HEIGHT 3)
        half-width (/ k/DPAD-WIDTH 2)
        up-box [x (+ y k/CONTROLS-Y) k/DPAD-WIDTH third-height]
        down-box [x (+ y k/CONTROLS-Y (* 2 third-height)) k/DPAD-WIDTH third-height]
        left-box [x (+ y k/CONTROLS-Y) half-width k/DPAD-HEIGHT]
        right-box [(+ x half-width) (+ y k/CONTROLS-Y) half-width k/DPAD-HEIGHT]]
    (cond
      (some #(u/box-contains? up-box %) touches)
      (assoc-in db [:controls :dpad] {:state :press
                                      :dir :up})

      (some #(u/box-contains? down-box %) touches)
      (assoc-in db [:controls :dpad] {:state :press
                                      :dir :down})

      (some #(u/box-contains? left-box %) touches)
      (assoc-in db [:controls :dpad] {:state :press
                                      :dir :left})

      (some #(u/box-contains? right-box %) touches)
      (assoc-in db [:controls :dpad] {:state :press
                                      :dir :right})

      :else
      (assoc-in db [:controls :dpad] {:state :idle
                                      :dir :down}))))

(defn handle-shoot [db]
  (if (get-in db [:controls :shoot-btn :enabled?])
    (let [touches (get-touches db)
          [x y] k/SHOOT-BTN-POS
          btn-area [x (+ y k/CONTROLS-Y) k/SHOOT-BTN-WIDTH k/SHOOT-BTN-HEIGHT]]
      (if (some #(u/box-contains? btn-area %) touches)
        (assoc-in db [:controls :shoot-btn :state] :press)
        (assoc-in db [:controls :shoot-btn :state] :idle)))
    db))

(defn register-collisions! [db]
  (u/evt> [:clean-collisions])
  (doseq [obj1 (-> db :objects vals)
          col (-> obj1 :on-collide keys)
          obj2 (filter #(= col (:type %))
                       (-> db
                           :objects
                           (dissoc (:id obj1))
                           vals))
          :let [obj1-center-box (u/obj->center-box obj1)
                obj2-center-box (u/obj->center-box obj2)]
          :when (u/colliding? obj1-center-box obj2-center-box)
          :let [coll-dir (u/collision-dir (u/obj->box obj1)
                                          obj2-center-box)
                dir (if (= :center coll-dir)
                      (case (:dir obj2)
                        :up :bottom
                        :down :top
                        :left :right
                        :right :left)
                      coll-dir)]]
    (u/evt> [:register-collision obj1 obj2 dir]))
  db)

(defn handle-collisions!
  [db]
  (doseq [[this-id objs] (:collisions db)
          [obj-id coll-dir] objs
          :let [this (get-in db [:objects this-id])
                obj (get-in db [:objects obj-id])]
          :when (and this obj)
          :let [methods (:on-collide this)
                method (methods (:type obj))]
          event (method db this obj coll-dir)]
    (u/evt> event))
  db)

(defn handle-interactions [db]
  (-> db
      handle-dpad
      handle-shoot
      register-collisions!
      handle-collisions!))

(defn update-state [db]
  (doseq [[id obj] (:objects db)
          :let [{:keys [on-update]} obj
                events (when on-update
                         (on-update db obj))]
          event events]
    (u/evt> event))
  db)

(defn next-frame [db]
  (let [{:keys [objects sprites]} db]
    (assoc db :objects
           (into {}
                 (for [[id c] objects
                       :let [{:keys [type state dir pos curr-frame]} c
                             frames (get-in sprites [type state dir :frames])]]
                   [id (update c :curr-frame #(mod (inc %) (count frames)))])))))
