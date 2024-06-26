(ns example.objects
  (:require [example.constants :as k]
            [example.util :as u]))

(def arrow
  {:type :arrow
   :pos [0 0]
   :state :idle
   :dir :down
   :dist 50
   :damage 10
   :curr-frame 0
   :on-update (fn [db this]
                (let [{:keys [id dir pos dist]} this
                      [x y] pos
                      [dx dy] k/ARROW-VEL
                      new-pos (case dir
                                :left [(- x dx) y]
                                :right [(+ x dx) y]
                                :up [x (- y dy)]
                                :down [x (+ y dy)]
                                ;; else
                                [x y])]
                  (if (zero? dist)
                    [[:remove-object id]]
                    [[:set-in [:objects id :pos] new-pos]
                     [:set-in [:objects id :dist] (dec dist)]])))})

(def rama
  {:id :rama
   :type :rama
   :pos [(* 4 k/TILE-WIDTH) (* 7 k/TILE-HEIGHT)]
   :state :idle
   :dir :up
   :hp 100
   :life 100
   :glow? false
   :glow-rate 0
   :curr-frame 0
   :on-update (fn [db this]
                (let [{:keys [pos dir curr-frame]} this
                      shoot-btn-state (-> db :controls :shoot-btn :state)
                      mantra-btn-state (-> db :controls :mantra-btn :state)
                      {:keys [dpad-state dpad-dir]} (u/with-prefix (-> db :controls :dpad)
                                                      :dpad)
                      arrows (-> db :inventory :arrows)
                      should-glow? (= :press mantra-btn-state)
                      should-shoot? (and (= :press shoot-btn-state)
                                         (pos? arrows))
                      should-walk? (= :press dpad-state)]
                  (case [should-glow? (:glow? this)]
                    [true false]
                    [[:set-in [:objects :rama :glow?] true]
                     [:set-in [:objects :rama :glow-rate] 0.1]]

                    [true true]
                    (if (< (:glow-rate this) 0.5)
                      [[:update-in [:objects :rama :glow-rate] + 0.01]]
                      [])

                    [false true]
                    [[:set-in [:objects :rama :glow?] false]]

                    ;; default
                    (cond
                      should-shoot?
                      (let [shoot-frames-count (-> db :sprites :rama :shoot :up :frames count)
                            [x y w h] (u/obj->box this)
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
                                      :right [(+ x (/ w 2)) (+ y (/ h 3))])
                            release-arrow? (= (- shoot-frames-count 2)
                                              curr-frame)]
                        (if release-arrow?
                          [[:add-object (assoc arrow
                                               :pos [ax ay]
                                               :width aw
                                               :height ah
                                               :dir dir)]
                           [:set-in [:inventory :arrows] (dec arrows)]]
                          [[:set-in [:objects :rama :state] :shoot]]))

                      should-walk?
                      [[:set-in [:objects :rama :dir] dpad-dir]
                       [:walk this]]

                      (= :idle dpad-state)
                      [[:set-in [:objects :rama :state] :idle]]))))})

(def lakshmana
  {:id :lakshmana
   :type :lakshmana
   :pos [(* 4.5 k/TILE-WIDTH) (* 7.5 k/TILE-HEIGHT)]
   :state :idle
   :dir :up
   :inventory {}
   :curr-frame 0
   :on-update (fn [db this]
                (let [rama (get-in db [:objects :rama])

                      {:keys [rama-state rama-pos rama-dir]}
                      (u/with-prefix rama :rama)

                      should-walk? (> (u/distance rama-pos (:pos this))
                                      (* 0.75 k/TILE-WIDTH))

                      dir-to-rama (u/dir-to this rama)]
                  (if should-walk?
                    [[:set-in [:objects (:id this) :dir] dir-to-rama]
                     [:walk this]]
                    [[:set-in [:objects (:id this) :dir] (:dir rama)]
                     [:set-in [:objects (:id this) :state] :idle]])))})

(def vishwamitra
  {:id :vishwamitra
   :type :vishwamitra
   :pos [(* 2.4 k/TILE-WIDTH) (* 4.5 k/TILE-HEIGHT)]
   :width (* 0.6 k/TILE-WIDTH)
   :height (* 1.1 k/TILE-HEIGHT)
   :state :idle
   :dir :down
   :curr-frame 0
   :on-collide {:rama
                (fn [db this rama dir]
                  (let [id (gensym)]
                    [[:uncollide this rama dir]
                     [:set-text {:id id
                                 :speaker "Rishi Vishwamitra"
                                 :speech "You are invading my personal space, Prince Rama!"}]
                     [:after-ms 2000 [:clear-text id]]]))

                :arrow
                (fn [db this arrow _]
                  (let [id (gensym)]
                    [[:remove-object (:id arrow)]
                     [:set-text {:speaker "Rishi Vishwamitra"
                                 :speech "Careful, son!"}]
                     [:after-ms 2000 [:clear-text id]]]))}})

(def tadaka
  {:id :tadaka
   :type :tadaka
   :pos [(* 4 k/TILE-WIDTH) (* 6 k/TILE-HEIGHT)]
   :walk-vel (map #(* 0.5 %) k/WALK-VEL)
   :width (* k/TILE-WIDTH 2)
   :height (* k/TILE-HEIGHT 2)
   :hp 500
   :life 500
   :state :idle
   :dir :left
   :curr-frame 0
   :frozen-for 0
   :on-collide {:rama
                (fn [db this rama dir]
                  [[:uncollide this rama dir]
                   [:retreat rama]
                   [:retreat this]])

                :arrow
                (fn [db this arrow dir]
                  [[:remove-object (:id arrow)]
                   [:set-in [:objects (:id this) :life] (- (:life this)
                                                           (:damage arrow))]
                   [:retreat this]
                   [:freeze-for 750 this]])}
   :on-update (fn [db this]
                (let [rama (-> db :objects :rama)
                      dist-to-rama (u/distance (-> this u/obj->box u/center)
                                               (-> rama u/obj->box u/center))
                      rama-in-range? (< dist-to-rama (* 0.5 (:width this)))
                      dir-to-rama (u/dir-to this rama)
                      frozen-for (:frozen-for this)]
                  (cond
                    (pos? frozen-for)
                    [[:set-in [:objects (:id this) :state] :idle]
                     [:update-in [:objects (:id this) :frozen-for] - k/TICK-MS]]

                    (not= dir-to-rama (:dir this))
                    [[:set-in [:objects (:id this) :state] :idle]
                     [:after-ms 750
                      [:set-in [:objects (:id this) :dir] dir-to-rama]]]

                    rama-in-range?
                    [[:set-in [:objects (:id this) :state] :idle]
                     [:set-in [:objects (:id rama) :life] (- (:life rama) 1)]]

                    (not rama-in-range?)
                    [[:walk this]]

                    :else
                    [])))})

(def hut
  {:id :hut
   :type :hut
   :pos [(* 0.8 k/TILE-WIDTH) (* 4.7 k/TILE-HEIGHT)]
   :width (* k/TILE-WIDTH 1.2)
   :height (* k/TILE-HEIGHT 1.3)
   :state :idle
   :dir :down
   :curr-frame 0
   :on-collide {:rama
                (fn [db this rama dir]
                  [[:uncollide this rama dir]])

                :arrow
                (fn [db this arrow _]
                  [[:remove-object (:id arrow)]])}})

(def bonfire
  {:id :bonfire
   :type :bonfire
   :pos [(* 3.5 k/TILE-WIDTH) (* 5 k/TILE-HEIGHT)]
   :width (/ k/TILE-WIDTH 2)
   :height (/ k/TILE-HEIGHT 2)
   :state :idle
   :dir :down
   :curr-frame 0
   :on-collide {:rama
                (fn [db this rama dir]
                  [[:uncollide this rama dir]])}})

(def bow-pickup
  {:type :bow-pickup
   :pos [(* 2.4 k/TILE-WIDTH) (* 6 k/TILE-HEIGHT)]
   :rot 0
   :width (/ k/TILE-WIDTH 2)
   :height (/ k/TILE-HEIGHT 2)
   :state :idle
   :dir :down
   :curr-frame 0
   :on-collide {:rama
                (fn [db this rama _]
                  [[:remove-object (:id this)]
                   [:set-text {:speaker "Rishi Vishwamitra"
                               :speech (str "Now shoot at the scarecrow.\n"
                                            "Long press to pull back the string and release an arrow.\n"
                                            "Try shooting from all directions.\n"
                                            "Once you are done, move over to the sacrificial fire.")}]
                   [:add-to-inventory :bow {}]])}})

(def deer
  {:id :deer
   :type :deer
   :pos [(- k/TILE-WIDTH) (* 10 k/TILE-HEIGHT)]
   :width (* k/TILE-WIDTH 0.8)
   :height (* k/TILE-HEIGHT 0.8)
   :state :run
   :dir :right
   :curr-frame 0
   :on-update (fn [db this]
                (let [rama (get-in db [:objects :rama])
                      {:keys [id dir pos]} this
                      new-pos (u/ahead-of this k/WALK-VEL)
                      [rama-row rama-col] (u/obj->grid rama)
                      [row col] (u/obj->grid this)]
                  (cond
                    (< col 0)
                    [[:set-in [:objects id :pos] new-pos]
                     [:set-in [:objects id :dir] :right]]

                    (> col 5)
                    [[:set-in [:objects id :pos] new-pos]
                     [:set-in [:objects id :dir] :left]]

                    (= col rama-col)
                    (cond
                      (< row rama-row)
                      [[:set-in [:objects id :state] :idle]
                       [:set-in [:objects id :dir] :down]]

                      (>= row rama-row)
                      [[:set-in [:objects id :state] :idle]
                       [:set-in [:objects id :dir] :up]])

                    (or (= :down dir)
                        (= :up dir))
                    [[:set-in [:objects id :state] :run]
                     [:set-in [:objects id :dir] (rand-nth [:left :right])]]

                    :else
                    [[:set-in [:objects id :pos] new-pos]])))
   :on-collide {:rama
                (fn [db this rama dir]
                  [[:uncollide this rama dir]])

                :arrow
                (fn [db this arrow _]
                  (let [id (gensym)]
                    [[:remove-object (:id arrow)]
                     [:set-text {:id id
                                 :speaker "Rishi Vishwamitra"
                                 :speech "Hey, Rama!"}]
                     [:after-ms 2000 [:clear-text id]]]))}})

(def collision-area
  {:type :collision-area
   :pos [0 0]
   :state :idle
   :dir :down
   :width (* k/TILE-WIDTH 2)
   :height (* k/TILE-HEIGHT 2)
   :curr-frame 0
   :on-collide {}})

(def scarecrow
  {:type :scarecrow
   :pos [(* 2.5 k/TILE-WIDTH) (* 8 k/TILE-HEIGHT)]
   :width (* k/TILE-WIDTH 0.8)
   :height (* k/TILE-HEIGHT 0.8)
   :state :idle
   :dir :down
   :curr-frame 0
   :on-collide {:rama
                (fn [db this rama dir]
                  [[:uncollide this rama dir]])

                :arrow
                (fn [db this arrow _]
                  (let [id (gensym)
                        complement (rand-nth ["Good shot!"
                                              "Nice!"
                                              "Great aim!"])]
                    [[:remove-object (:id arrow)]
                     [:set-text {:id id
                                 :speaker "Rishi Vishwamitra"
                                 :speech complement}]
                     [:after-ms 2000 [:clear-text id]]
                     (if (get-in db [:logic :collision-area-set?])
                       [:no-op]
                       [:add-object (assoc collision-area
                                           :pos [(* 2.75 k/TILE-WIDTH)
                                                 (* 4.2 k/TILE-HEIGHT)]
                                           :on-collide {:rama
                                                        (fn [db this rama _]
                                                          (let [id (gensym)]
                                                            [[:remove-object (:id this)]
                                                             [:set-text {:id id
                                                                         :speaker "Rishi Vishwamitra"
                                                                         :speech "AUUUUUUUUUMMMMMM!"}]
                                                             [:after-ms 2000 [:clear-text id]]
                                                             [:add-object deer]]))})])
                     [:set-in [:logic :collision-area-set?] true]]))}})
