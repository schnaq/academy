(ns schnaq.academy.core
  (:require ["react-dom/client" :refer [createRoot]]
            ["@heroicons/react/solid" :refer [SunIcon]]
            [goog.dom :as gdom]
            [goog.dom.classlist :as gdomcl]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [reagent.core :as r]))

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

(rf/reg-event-db
 :iframe/share-hash
 (fn [db [_ share-hash]]
   (assoc-in db [:iframe :share-hash] share-hash)))

(defn iframe-embedding []
  [:<>
   [:h2 "schnaq einbetten"]
   [:p "schnaq kann in beliebige Web-Seiten und E-Learningsysteme eingebettet werden. Hier kannst du dir ein Code-Snippet erstellen, den du dann verwenden kannst, um schnaq in deinem E-Learning System oder auf deiner Webseite einzubinden."]
   [:section.configurator
    [:label {:for "iframe-share-hash"}
     "Füge hier den share-hash zu deinem schnaq ein. Das ist die lange Zahlenfolge aus deiner Browserzeile."]
    [:input#iframe-share-hash {:on-change (fn [e]
                                            (rf/dispatch [:iframe/share-hash (oget e [:target :value])]))
                               :placeholder "e8f54922-0d88-4953-8f43-ddc819d7f201"}]]
   [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top "500px"}}
    [:iframe
     {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
      :src "https://schnaq.com/de/schnaq/e8f54922-0d88-4953-8f43-ddc819d7f201"}]]])

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
