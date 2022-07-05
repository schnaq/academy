(ns schnaq.academy.routes
  (:require [re-frame.core :as rf]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as reitit-frontend]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [schnaq.academy.pages.index :refer [index]]
            [schnaq.academy.specs]))

(def routes
  ["" {:controllers [{:start #(rf/dispatch [:init])}]}
   ["/" {:name :routes/start
         :views index}]])

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
 (fn [route]
   (apply rfe/push-state route)))

(rf/reg-event-fx
 :routes/push-state
 (fn [_ [_ & route]]
   {:push-state route}))

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
 :routes/current-view
 :<- [:routes/current-route]
 (fn [current-route]
   (get-in current-route [:data :views])))
