(ns schnaq.academy.corecore
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

(defn- main []
  [:<>
   [header "schnaq academy"]
   [:main.container.p-5
    [:h1 "Welcome to the schnaq academy"]
    [:p "Find here interesting resources on how to handle / use schnaq."]
    [:h2 "Embedding schnaq (e.g. into a website, e-learning system (ILIAS, Moodle, ...))"]]])

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
