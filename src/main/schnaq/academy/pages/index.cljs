(ns schnaq.academy.pages.index
  (:require [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.config :as config]
            [schnaq.academy.pages.base :refer [base]]))

(defn- index-page []
  [base
   [:<>
    [:h1 "Willkommen in der schnaq academy"]
    [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
    [:div {:class "w-1/3"}
     [:label
      [:span "Füge hier die vollständige Adresse zu deinem schnaq ein."]
      [:input.input
       {:type :text
        :on-change #(rf/dispatch [:settings/from-schnaq-url (oget % [:target :value])])
        :placeholder config/default-schnaq-url}]]]
    [:h2 "Einbettungen"]
    [:p "schnaq kann in beliebige Webkontexte per iFrame eingebunden werden. Lerne hier, wie das funktioniert."]
    [:a {:href (rfe/href :routes/settings)} "Weiterlesen"]]])

(defn index []
  [index-page])
