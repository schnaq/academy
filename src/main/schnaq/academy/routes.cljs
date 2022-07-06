(ns schnaq.academy.routes
  (:require [re-frame.core :as rf]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as reitit-frontend]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.pages.embedding :refer [embedding]]
            [schnaq.academy.pages.index :refer [index]]
            [schnaq.academy.specs]
            [spec-tools.data-spec :as ds]))

(def routes
  ["" {:parameters {:query {(ds/opt :url) string?}}
       :controllers [{:parameters {:query [:url]}
                      :start (fn [{:keys [query]}]
                               (rf/dispatch [:init])
                               (rf/dispatch [:settings/from-schnaq-url (:url query)]))}]}
   ["/" {:controllers [{:start (fn [{:keys [query]}]
                                 (rf/dispatch [:routes/navigate :routes/start {} query]))}]}]
   ["/de" {:name :routes/start
           :views index}]
   ["/de"
    ["/einbettung" {:name :routes/embedding
                    :views embedding}]]])

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [:routes/navigated new-match])))

(def router
  (reitit-frontend/router
   routes
   {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))

;; -----------------------------------------------------------------------------

(rf/reg-fx
 :push-state
 (fn [[route query-parameters]]
   (apply #(rfe/push-state % {} query-parameters) route)))

(rf/reg-event-fx
 :routes/navigate
 (fn [{:keys [db]} [_ & route]]
   (let [query-parameters (get-in db [:current-route :parameters :query])]
     {:fx [[:push-state [route query-parameters]]]})))

(rf/reg-event-db
 :routes/navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(rf/reg-sub
 :routes/current-route
 (fn [db]
   (:current-route db)))

(rf/reg-sub
 :routes/query-parameters
 :<- [:routes/current-route]
 (fn [current-route]
   (get-in current-route [:parameters :query])))

(rf/reg-sub
 :routes/current-view
 :<- [:routes/current-route]
 (fn [current-route]
   (get-in current-route [:data :views])))
