(ns schnaq.academy.pages.index
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [schnaq-url-input]]))

(defn- index-page []
  (let [query-parameters @(rf/subscribe [:routes/query-parameters])]
    [base
     [:<>
      [:h1 "Willkommen in der schnaq academy"]
      [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
      [:div {:class "w-1/3"}
       [schnaq-url-input]]
      [:h2 "Einbettung"]
      [:p "schnaq kann in beliebige Webkontexte per iFrame eingebunden werden. Lerne hier, wie das funktioniert."]
      [:a {:href (rfe/href :routes/embedding {} query-parameters)} "Weiterlesen"]]]))

(defn index []
  [index-page])
