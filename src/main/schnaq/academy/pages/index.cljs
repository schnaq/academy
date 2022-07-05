(ns schnaq.academy.pages.index
  (:require [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.parser :as parser]))

(defn- index-page []
  (let [share-hash @(rf/subscribe [:settings/share-hash])]
    [base
     [:<>
      [:h1 "Willkommen in der schnaq academy"]
      [:p "Finde hier Anleitungen, Beispiele und Konfigurationen, wie du schnaq für dich verwenden kannst."]
      [:div.grid.md:grid-cols-3
       [:label
        [:span "Füge hier die vollständige Adresse zu deinem schnaq ein."]
        [:input.input
         {:type :text
          :on-change #(rf/dispatch [:academy/schnaq-address (oget % [:target :value])])
          :placeholder share-hash}]]]
      #_[ui-settings]]]))

(defn index []
  [index-page])

(rf/reg-event-fx
 :academy/schnaq-address
 (fn [{:keys [db]} [_ url]]
   (let [parameters (parser/extract-parameters url)
         share-hash (get-in parameters [:path :share-hash])]
     (assoc-in db [:academy :share-hash] share-hash)
     #_(prn (parser/extract-parameters url)))))
