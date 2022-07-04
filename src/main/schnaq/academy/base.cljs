(ns schnaq.academy.base
  (:require ["@heroicons/react/solid" :refer [SunIcon MoonIcon]]
            [cljs.spec.alpha :as s]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.config :as config]
            [schnaq.academy.lectures.iframe :refer [ui-settings]]
            [schnaq.academy.specs :as specs]
            [schnaq.academy.utils]))

(defn- header
  "Define the academy header."
  [title]
  (let [dark-mode? @(rf/subscribe [:dark-mode?])]
    [:nav.flex.bg-blue.dark:bg-blue-dark.p-6.text-white
     [:div.flex.items-center.flex-no-shrink.mr-6
      [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
      [:span.font-semibold.text-xl.tracking-tight
       title]]
     [:div.ml-auto]
     [:> (if dark-mode? MoonIcon SunIcon)
      {:class "h-7 my-auto pr-3 cursor-pointer"
       :on-click #(rf/dispatch [:dark-mode/toggle])}]
     [:a.p-3.bg-white.rounded-full.text-blue.dark:text-blue-dark
      {:href "https://schnaq.com"} "schnaq.com"]]))

(defn- footer
  "Define the academy footer."
  [title]
  [:nav.flex.bg-blue-dark.p-6.text-white.mt-5
   [:div.flex.items-center.flex-no-shrink.mr-6
    [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
    [:span.font-semibold.text-xl.tracking-tight title]]
   [:div.ml-auto]
   [:a.text-white.mr-3 {:target :_blank :href "https://schnaq.com/de/legal-note"} "Impressum"]
   [:a.text-white {:target :_blank :href "https://schnaq.com/de/privacy"} "Datenschutz"]])

;; -----------------------------------------------------------------------------

(defn main []
  (let [share-hash @(rf/subscribe [:academy/share-hash])]
    [:main#main
     [:div.dark:bg-gray-700.dark:text-white
      [header config/application-name]
      [:div.container.mx-auto.px-3.pt-3
       [:h1 "Willkommen in der schnaq academy"]
       [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
       [:div.grid.md:grid-cols-3
        [:label
         [:span "Füge hier deinen share-hash ein, wenn du die Demos mit deinem eigenen schnaq sehen möchtest. Das ist die lange Zahlenfolge aus deiner Browserzeile."]
         [:input#iframe-share-hash.input
          {:type :text
           :on-change #(rf/dispatch [:academy/share-hash (oget % [:target :value])])
           :placeholder share-hash}]]]
       [ui-settings]]
      [footer config/application-name]]]))

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
