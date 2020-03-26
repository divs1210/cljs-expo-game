(ns cljs-expo-game.engine
  (:require [cljs-expo-game.util :as u]
            [cljs-expo-game.constants :as k]))

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
      (assoc-in db [:controls :dpad :state] :up)

      (some #(u/box-contains? down-box %) touches)
      (assoc-in db [:controls :dpad :state] :down)

      (some #(u/box-contains? left-box %) touches)
      (assoc-in db [:controls :dpad :state] :left)

      (some #(u/box-contains? right-box %) touches)
      (assoc-in db [:controls :dpad :state] :right)

      :else
      (assoc-in db [:controls :dpad :state] :idle))))

(defn handle-shoot [db]
  (if (get-in db [:objects 0 :inventory :bow])
    (let [touches (get-touches db)
          [x y] k/SHOOT-BTN-POS
          btn-area [x (+ y k/CONTROLS-Y) k/SHOOT-BTN-WIDTH k/SHOOT-BTN-HEIGHT]]
      (if (some #(u/box-contains? btn-area %) touches)
        (assoc-in db [:controls :shoot-btn :state] :press)
        (assoc-in db [:controls :shoot-btn :state] :idle)))
    db))

(defn set-rama-state [db]
  (let [shoot-btn-state (-> db :controls :shoot-btn :state)
        dpad-state (-> db :controls :dpad :state)]
    (cond
      (= :press shoot-btn-state)
      (-> db
          (assoc-in [:objects 0 :state] :shoot)
          (update :objects (fn [os]
                             (let [new-id (->> os keys (apply max) inc)
                                   {:keys [pos dir curr-frame] :as rama} (get-in db [:objects 0])
                                   shoot-frames-count (-> db :sprites :rama :shoot :up :frames count)
                                   [x y w h] (u/obj->box rama)
                                   aw (case dir
                                        :up (/ h 5)
                                        :down (/ h 5)
                                        :left (/ w 2)
                                        :right (/ w 2))
                                   ah (case dir
                                        :up (/ w 2)
                                        :down (/ w 2)
                                        :left (/ h 5)
                                        :right (/ h 5))
                                   [ax ay] (case dir
                                             :up [(- (+ x (/ w 2)) (/ aw 2)) (- (+ y (/ h 2)) (/ ah 2))]
                                             :down [(- (+ x (/ w 2)) (/ aw 2)) (+ y (/ h 2))]
                                             :left [(- (+ x (/ w 2)) aw) (+ y (/ h 3))]
                                             :right [(+ x (/ w 2)) (+ y (/ h 3))])]
                               (if (= (- shoot-frames-count 2)
                                      curr-frame)
                                 (assoc os new-id {:id new-id
                                                   :type :arrow
                                                   :pos [ax ay]
                                                   :width aw
                                                   :height ah
                                                   :state :idle
                                                   :dir dir
                                                   :life 50
                                                   :curr-frame 0
                                                   :on-collide (fn [this obj]
                                                                 (let [s (gensym)]
                                                                   (case (:type obj)
                                                                     :vishwamitra
                                                                     [[:set-text {:speaker "Rishi Vishwamitra"
                                                                                  :speech "Careful, son!"
                                                                                  :id s}]
                                                                      [:remove-object (:id this)]
                                                                      [:after-ms 2000 [:clear-text s]]]
                                                                     ;; default
                                                                     [])))})
                                 os)))))

      (not= :idle dpad-state)
      (-> db
          (assoc-in [:objects 0 :state] :walk)
          (assoc-in [:objects 0 :dir] dpad-state))

      (= :idle dpad-state)
      (assoc-in db [:objects 0 :state] :idle))))

(defn move-rama [db]
  (let [rama (get-in db [:objects 0])
        {:keys [state dir pos]} rama
        [x y] pos
        [dx dy] k/WALK-VEL
        new-pos (case dir
                  :left [(- x dx) y]
                  :right [(+ x dx) y]
                  :up [x (- y dy)]
                  :down [x (+ y dy)]
                  ;; else
                  [x y])
        new-rama (assoc rama :pos new-pos)]
    (if (= :walk state)
      (if (some #(= (u/obj->grid new-rama)
                    (:pos %))
                (:world db))
        (assoc-in db [:objects 0 :pos] new-pos)
        (assoc-in db [:objects 0 :state] :idle))
      db)))

(defn move-arrows [db]
  (update db :objects
          (fn [objects]
            (into {}
                  (for [[id o] objects]
                    (if (= :arrow (:type o))
                      (let [{:keys [dir pos life]} o
                            [x y] pos
                            [dx dy] k/ARROW-VEL
                            new-pos (case dir
                                      :left [(- x dx) y]
                                      :right [(+ x dx) y]
                                      :up [x (- y dy)]
                                      :down [x (+ y dy)]
                                      ;; else
                                      [x y])]
                        [id (assoc o
                                   :pos new-pos
                                   :life (dec life))])
                      [id o]))))))

(defn remove-dead-arrows [db]
  (reduce (fn [db [id o]]
            (if (and (= :arrow (:type o))
                     (zero? (:life o)))
              (u/dissoc-in db [:objects id])
              db))
          db
          (:objects db)))

(defn handle-movements [db]
  (-> db
      move-rama
      remove-dead-arrows
      move-arrows))

(defn register-collisions! [db]
  (u/evt> [:clean-collisions])
  (doseq [obj1 (-> db :objects vals)
          col (:collidables obj1)
          obj2 (filter #(= col (:type %))
                       (-> db :objects
                           (dissoc (:id obj1))
                           vals))
          :let [obj1-box (u/obj->box obj1)
                obj2-box (u/obj->box obj2)]
          :when (u/colliding? obj1-box obj2-box)]
    (u/evt> [:register-collision obj1 obj2]))
  db)

(defn handle-collisions!
  [db]
  (doseq [[this-id obj-ids] (:collisions db)
          obj-id obj-ids
          :let [this (get-in db [:objects this-id])
                obj (get-in db [:objects obj-id])
                [x y w h] (u/obj->box this)
                quarter-width (/ w 4)
                quarter-height (/ h 4)
                this-center [(+ x quarter-width)
                             (+ y quarter-height)
                             (* 2 quarter-width)
                             (* 2 quarter-height)]]]
    (when (u/colliding? this-center (u/obj->box obj))
      (doseq [event ((:on-collide obj) obj this)]
        (u/evt> event))))
  db)

(defn handle-interactions [db]
  (-> db
      handle-dpad
      handle-shoot
      set-rama-state
      handle-movements
      register-collisions!
      handle-collisions!))

(defn next-frame [db]
  (let [{:keys [objects sprites]} db]
    (assoc db :objects
           (into {}
                 (for [[id c] objects
                       :let [{:keys [type state dir pos curr-frame]} c
                             frames (get-in sprites [type state dir :frames])]]
                   [id (update c :curr-frame #(mod (inc %) (count frames)))])))))
