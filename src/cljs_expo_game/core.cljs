(ns cljs-expo-game.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [oops.core :refer [ocall]]
            [cljs-expo-game.engine :refer [game]]
            [cljs-expo-game.handlers]
            [cljs-expo-game.subs]
            [cljs-expo-game.util :refer [evt>]]
            [cljs-expo-game.constants :as k]))

(defn app-root []
  [game])

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (ocall (js/require "expo")
         "registerRootComponent"
         (r/reactify-component app-root))
  (js/setInterval #(evt> [:tick])
                  k/FPS))
