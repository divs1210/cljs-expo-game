(ns env.index
  (:require [env.dev :as dev]))

;; undo main.js goog preamble hack
(set! js/window.goog js/undefined)

(-> (js/require "../../../js/figwheel-bridge")
    (.withModules #js {"./assets/images/water.jpeg" (js/require "../../../assets/images/water.jpeg"), "./assets/images/beach_tm_grass.png" (js/require "../../../assets/images/beach_tm_grass.png"), "./assets/icons/loading.png" (js/require "../../../assets/icons/loading.png"), "./assets/images/beach_bl_grass.png" (js/require "../../../assets/images/beach_bl_grass.png"), "./assets/images/beach_br_grass.png" (js/require "../../../assets/images/beach_br_grass.png"), "expo" (js/require "expo"), "./assets/images/cljs.png" (js/require "../../../assets/images/cljs.png"), "./assets/images/beach_lm_grass.png" (js/require "../../../assets/images/beach_lm_grass.png"), "./assets/icons/app.png" (js/require "../../../assets/icons/app.png"), "./assets/images/grass.png" (js/require "../../../assets/images/grass.png"), "react-native" (js/require "react-native"), "./assets/images/beach_bm_grass.png" (js/require "../../../assets/images/beach_bm_grass.png"), "react" (js/require "react"), "./assets/images/beach_tl_grass.png" (js/require "../../../assets/images/beach_tl_grass.png"), "./assets/images/sand.png" (js/require "../../../assets/images/sand.png"), "create-react-class" (js/require "create-react-class"), "@expo/vector-icons" (js/require "@expo/vector-icons"), "./assets/images/beach_rm_grass.png" (js/require "../../../assets/images/beach_rm_grass.png")}
)
    (.start "main" "expo" "192.168.1.102"))
