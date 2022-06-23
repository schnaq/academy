(ns schnaq.academy.core
  (:require ["react-dom/client" :refer [createRoot]]
            [goog.dom :as gdom]
            [reagent.core :as r]))

(defn- header
  "Define the academy header."
  [title]
  [:nav.flex.items-center.justify-between.bg-blue.p-6.text-white
   [:div.flex.items-center.flex-no-shrink.mr-6
    [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
    [:span.font-semibold.text-xl.tracking-tight
     title]]
   [:a.p-3.bg-white.rounded-full {:href "https://schnaq.com"} "schnaq.com"]])

;; -----------------------------------------------------------------------------

(defn iframe-embedding []
  [:<>
   [:h2 "Embedding schnaq (e.g. into a website, e-learning system (ILIAS, Moodle, ...))"]
   [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top "500px"}}
    [:iframe
     {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
      :src "https://schnaq.com/de/schnaq/e8f54922-0d88-4953-8f43-ddc819d7f201"}]]])


;; -----------------------------------------------------------------------------

(defn- main []
  [:<>
   [header "schnaq academy"]
   [:main.container.p-5
    [:h1 "Welcome to the schnaq academy"]
    [:p "Find here interesting resources on how to handle / use schnaq."]
    [iframe-embedding]]])

;; -----------------------------------------------------------------------------

(defonce root (createRoot (gdom/getElement "app")))

(defn init
  []
  (.render root (r/as-element [main])))

(defn- ^:dev/after-load re-render
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (init))
