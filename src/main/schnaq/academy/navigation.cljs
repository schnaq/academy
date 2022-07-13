(ns schnaq.academy.navigation
  (:require [re-frame.core :as rf]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as reitit-frontend]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.routes :as routes]
            [schnaq.academy.specs]))

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [:navigation/navigated new-match])))

(def router
  (reitit-frontend/router
   routes/routes
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
 :navigation/navigate
 (fn [{:keys [db]} [_ & route]]
   (let [query-parameters (get-in db [:current-route :parameters :query])]
     {:fx [[:push-state [route query-parameters]]]})))

(rf/reg-event-db
 :navigation/navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(rf/reg-sub
 :navigation/current-route
 (fn [db]
   (:current-route db)))

(rf/reg-sub
 :navigation/query-parameters
 :<- [:navigation/current-route]
 (fn [current-route]
   (get-in current-route [:parameters :query])))

(rf/reg-sub
 :navigation/current-view
 :<- [:navigation/current-route]
 (fn [current-route]
   (get-in current-route [:data :views])))
