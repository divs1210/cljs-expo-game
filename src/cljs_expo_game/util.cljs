(ns cljs-expo-game.util
  (:require [re-frame.core :as rf]
            [re-frame.db :refer [app-db]]))

(defn <sub
  [path]
  (rf/subscribe [:get-in path]))

(defn evt>
  [evt]
  (rf/dispatch evt))
