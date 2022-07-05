(ns schnaq.academy.pages.index
  (:require [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [ui-settings]]))

(defn- index-page []
  (let [share-hash @(rf/subscribe [:academy/share-hash])]
    [base
     [:div.container.mx-auto.px-3.pt-3
      [:h1 "Willkommen in der schnaq academy"]
      [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
      [:div.grid.md:grid-cols-3
       [:label
        [:span "Füge hier die vollständige Adresse zu deinem schnaq ein."]
        [:input.input
         {:type :text
          :on-change #(rf/dispatch [:academy/share-hash (oget % [:target :value])])
          :placeholder share-hash}]]]
      [ui-settings]]]))

(defn index []
  [index-page])
