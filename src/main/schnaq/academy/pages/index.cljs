(ns schnaq.academy.pages.index
    (:require ["@heroicons/react/outline" :refer [PresentationChartBarIcon GlobeIcon]]
              [re-frame.core :as rf]
              [reitit.frontend.easy :as rfe]
              [schnaq.academy.components :refer [card]]
              [schnaq.academy.pages.base :refer [base]]))

(defn- index-page []
       (let [query-parameters @(rf/subscribe [:navigation/query-parameters])]
            [base {:page/title "Start"
                   :page/description "Lerne hier in der academy, wie du schnaq verwenden und einbinden kannst."}
             [:<>
              [:h1 "Willkommen in der schnaq academy"]
              [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
              [:h2 "Einbettung"]
              [:p "schnaq auf einer eigenen Plattform zu betreiben ist zwar schön, aber noch schöner ist es, wenn wir schnaq direkt zu dir auf deine Webseiten und Anwendungen bringen. Lerne hier, wie das funktionieren kann."]
              [:div.grid.md:grid-cols-2.xl:grid-cols-3.gap-4
               [card
                [:<> [:> GlobeIcon {:class "h-7 inline mr-2"}] "iFrames"]
                "schnaq kann in beliebige Webkontexte per iFrame eingebunden werden."
                "Weiterlesen"
                (rfe/href :routes/embedding {} query-parameters)]
               [card
                [:<> [:> PresentationChartBarIcon {:class "h-7 inline mr-2"}] "Powerpoint"]
                "Live Fragen in deinen Folien? Wir zeigen dir wie das mit Powerpoint möglich ist!"
                "Weiterlesen"
                (rfe/href :routes.embedding/powerpoint {} query-parameters)]
               [card
                [:<> [:img.h-10.inline.mr-2 {:src "https://snq-common.s3.nl-ams.scw.cloud/academy/logos/ilias.webp"}] "ILIAS"]
                "Studierenden direkt in einer Plattform sich selbst helfen lassen? Binde schnaq in deinen ILIAS-Kurs ein!"
                "Weiterlesen"
                (rfe/href :routes.embedding/ilias {} query-parameters)]
               [card
                [:<> [:img.h-10.inline.mr-2 {:src "https://snq-common.s3.nl-ams.scw.cloud/academy/logos/moodle.svg"}] "Moodle"]
                "Nutze schnaq direkt in einer der größen Lernmanagementsysteme der Welt!"
                "Weiterlesen"
                (rfe/href :routes.embedding/moodle {} query-parameters)]]]]))

(defn index []
      [index-page])
