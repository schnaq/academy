(ns schnaq.academy.core
  (:require ["@heroicons/react/solid" :refer [SunIcon]]
            ["react-dom/client" :refer [createRoot]]
            [goog.dom :as gdom] ;; required for goog.string. We need to require it once in our project.
            [goog.dom.classlist :as gdomcl]
            [goog.string.format]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [schnaq.academy.lectures.iframe :refer [iframe-embedding]]))

(defn- header
  "Define the academy header."
  [title]
  [:nav.flex.bg-blue.dark:bg-blue-dark.p-6.text-white
   [:div.flex.items-center.flex-no-shrink.mr-6
    [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
    [:span.font-semibold.text-xl.tracking-tight
     title]]
   [:div.ml-auto]
   [:> SunIcon {:class "h-7 my-auto pr-3 cursor-pointer"
                :on-click #(rf/dispatch [:dark/toggle])}]
   [:a.p-3.bg-white.rounded-full {:href "https://schnaq.com"} "schnaq.com"]])

(rf/reg-event-fx
 :dark/toggle
 (fn [_ _]
   {:fx [[:dark/toggle!]]}))

(rf/reg-fx
 :dark/toggle!
 (fn []
   (gdomcl/toggle (gdom/getElement "main") "dark")))

;; -----------------------------------------------------------------------------

(defn- main []
  [:main#main
   [:div.dark:bg-gray-700.dark:text-white
    [header "schnaq academy"]
    [:div.container.mx-auto.px-3.pt-3
     [:h1 "Willkommen in der schnaq academy"]
     [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
     [iframe-embedding]]]])

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
