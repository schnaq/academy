(ns schnaq.academy.pages.base
  (:require ["@heroicons/react/solid" :refer [MoonIcon SunIcon]]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.config :as config]))

(defn- header
  "Define the academy header."
  []
  (let [dark-mode? @(rf/subscribe [:dark-mode?])]
    [:nav.flex.bg-blue.dark:bg-blue-dark.p-6.text-white
     [:div.flex.items-center.flex-no-shrink.mr-6.cursor-pointer
      {:on-click #(rf/dispatch [:routes/navigate :routes/start])}
      [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
      [:span.font-semibold.text-xl.tracking-tight
       config/application-name]]
     [:a.header-link {:href (rfe/href :routes/start)} "Start"]
     [:a.header-link {:href (rfe/href :routes/settings)} "Einstellungen"]
     [:div.ml-auto]
     [:> (if dark-mode? MoonIcon SunIcon)
      {:class "h-7 my-auto pr-3 cursor-pointer"
       :on-click #(rf/dispatch [:dark-mode/toggle])}]
     [:a.header-link-external
      {:href "https://schnaq.com"} "schnaq.com"]]))

(defn- footer
  "Define the academy footer."
  []
  [:nav.flex.bg-blue-dark.p-6.text-white.mt-5
   [:div.flex.items-center.flex-no-shrink.mr-6
    [:img.h-8.pr-2 {:src "https://s3.schnaq.com/schnaq-common/logos/schnaqqifant_white.svg"}]
    [:span.font-semibold.text-xl.tracking-tight config/application-name]]
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
    [header]
    [:div.container.mx-auto.px-3.pt-3 body]
    [footer]]])
