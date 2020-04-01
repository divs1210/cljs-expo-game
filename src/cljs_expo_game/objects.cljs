(ns cljs-expo-game.objects
  (:require [cljs-expo-game.constants :as k]
            [cljs-expo-game.util :as u]))

(def rama
  {:id :rama
   :type :rama
   :pos [(* 4 k/TILE-WIDTH) (* 7 k/TILE-HEIGHT)]
   :state :idle
   :dir :up
   :inventory {}
   :curr-frame 0})

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

                      should-walk? (and (u/ahead-of? rama this)
                                        (> (u/distance rama-pos (:pos this))
                                           (* 0.75 k/TILE-WIDTH)))]
                  [[:set-in [:objects (:id this) :dir] rama-dir]
                   (if should-walk?
                     [:walk this]
                     [:set-in [:objects (:id this) :state] :idle])]))})

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
                     [:set-text {:id id
                                 :speaker "Rishi Vishwamitra"
                                 :speech "Careful, son!"}]
                     [:after-ms 2000 [:clear-text id]]]))}})

(def hut
  {:type :hut
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
  {:type :bonfire
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
  {:type :deer
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
