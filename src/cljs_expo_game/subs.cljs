(ns cljs-expo-game.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-in
 (fn [app-db [_ path]]
   (get-in app-db path)))
