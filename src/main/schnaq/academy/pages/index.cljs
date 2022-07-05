(ns schnaq.academy.pages.index
  (:require [cljs.spec.alpha :as s]
            [goog.uri.utils :as uri]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.config :as config]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.parser :as parser]))

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
        :on-change #(rf/dispatch [:academy/schnaq-address (oget % [:target :value])])
        :placeholder config/default-schnaq-url}]]]
    [:h2 "Einbettungen"]
    [:p "schnaq kann in beliebige Webkontexte per iFrame eingebunden werden. Lerne hier, wie das funktioniert."]
    [:a {:href (rfe/href :routes/settings)} "Weiterlesen"]]])

(defn index []
  [index-page])

(rf/reg-event-db
 :academy/schnaq-address
 (fn [db [_ url]]
   (let [host (uri/getHost url)
         parameters (parser/extract-parameters url)
         share-hash (get-in parameters [:path :share-hash])]
     (cond-> db
       (s/valid? :discussion/share-hash share-hash) (assoc-in [:settings :share-hash] share-hash)
       (seq host) (assoc-in [:settings :host] host)
       true (update :settings merge (:query parameters))))))
