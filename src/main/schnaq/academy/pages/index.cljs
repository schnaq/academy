(ns schnaq.academy.pages.index
  (:require ["@heroicons/react/outline" :refer [PresentationChartBarIcon GlobeIcon]]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.components :refer [card]]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [schnaq-url-input]]))

(defn- index-page []
  (let [query-parameters @(rf/subscribe [:routes/query-parameters])]
    [base
     [:<>
      [:h1 "Willkommen in der schnaq academy"]
      [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
      [:div {:class "md:w-1/3"}
       [schnaq-url-input]]
      [:h2 "Einbettung"]
      [:p "schnaq auf einer eigenen Plattform zu betreiben ist zwar schön, aber noch schöner ist es, wenn wir schnaq direkt zu dir auf deine Webseiten und Anwendungen bringen. Lerne hier, wie das funktionieren kann."]
      [:div.grid.md:grid-cols-3
       [card
        [:<> [:> GlobeIcon {:class "h-7 inline mr-2"}] "iFrames"]
        "schnaq kann in beliebige Webkontexte per iFrame eingebunden werden."
        "Weiterlesen"
        (rfe/href :routes/embedding {} query-parameters)]
       [card
        [:<> [:> PresentationChartBarIcon {:class "h-7 inline mr-2"}] "Powerpoint"]
        "Live Fragen in deinen Folien? Wir zeigen dir wie das mit Powerpoint möglich ist!"
        "Weiterlesen"
        (rfe/href :routes.embedding/powerpoint {} query-parameters)]]]]))

(defn index []
  [index-page])
