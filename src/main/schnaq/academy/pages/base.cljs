(ns schnaq.academy.pages.base
  (:require ["@heroicons/react/solid" :refer [SunIcon MoonIcon]]
            [re-frame.core :as rf]
            [schnaq.academy.config :as config]))

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

(defn root
  "Root view to initialize and render the application."
  []
  (let [current-view @(rf/subscribe [:routes/current-view])]
    [:div
     (when current-view
       [current-view])]))

(defn base [body]
  [:main#main
   [:div.dark:bg-gray-700.dark:text-white
    [header config/application-name]
    [:div.container.mx-auto.px-3.pt-3 body]
    [footer config/application-name]]])
