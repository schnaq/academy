(ns schnaq.academy.core
  (:require ["@heroicons/react/solid" :refer [SunIcon]]
            ["react-dom/client" :refer [createRoot]]
            [cljs.spec.alpha :as s]
            [goog.dom :as gdom] ;; required for goog.string. We need to require it once in our project.
            [goog.dom.classlist :as gdomcl]
            [goog.string.format]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [schnaq.academy.config :as config]
            [schnaq.academy.lectures.iframe :refer [iframe-embedding]]
            [schnaq.academy.specs :as specs]))

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
   [:a.p-3.bg-white.rounded-full.text-blue.dark:text-blue-dark
    {:href "https://schnaq.com"} "schnaq.com"]])

(rf/reg-event-fx
 :dark/toggle
 (fn [_ _]
   {:fx [[:dark/toggle!]]}))

(rf/reg-fx
 :dark/toggle!
 (fn []
   (gdomcl/toggle (gdom/getElement "main") "dark")))

;; -----------------------------------------------------------------------------

(rf/reg-sub
 :academy/share-hash
 (fn [db]
   (let [share-hash (get-in db [:academy :share-hash])]
     (if (and share-hash (s/valid? ::specs/share-hash share-hash))
       share-hash
       config/default-share-hash))))

(rf/reg-event-db
 :academy/share-hash
 (fn [db [_ share-hash]]
   (assoc-in db [:academy :share-hash] share-hash)))

(defn- main []
  (let [share-hash @(rf/subscribe [:academy/share-hash])]
    [:main#main
     [:div.dark:bg-gray-700.dark:text-white
      [header "schnaq academy"]
      [:div.container.mx-auto.px-3.pt-3
       [:h1 "Willkommen in der schnaq academy"]
       [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
       [:div.grid.grid-cols-3
        [:label
         [:span "Füge hier deinen share-hash ein, wenn du die Demos mit deinem eigenen schnaq sehen möchtest. Das ist die lange Zahlenfolge aus deiner Browserzeile."]
         [:input#iframe-share-hash.input
          {:type :text
           :on-change #(rf/dispatch [:academy/share-hash (oget % [:target :value])])
           :placeholder share-hash}]]]
       [iframe-embedding]]]]))

;; -----------------------------------------------------------------------------

(defonce root (createRoot (gdom/getElement "app")))

(defn init
  []
  (.render root (r/as-element [main])))

(defn- ^:dev/after-load clear-cache-and-render!
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (rf/clear-subscription-cache!)
  (init))
