(ns example.app
  (:require [reagent.core :as r]
            [re-frame.core :as rf] 
            [expo.root :as expo-root]
            [example.render :refer [game]]
            [example.handlers]
            [example.subs]
            [example.util :refer [evt>]]
            [example.constants :as k]))

#_(when js/__DEV__
    (require '["react-native" :as rn])
    (.setHotLoadingEnabled (.-DevSettings rn/NativeModules) false))

(defn start
  {:dev/after-load true}
  []
  (expo-root/render-root (r/as-element [game])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start)
  (js/setInterval #(evt> [:tick])
                  k/FPS))
