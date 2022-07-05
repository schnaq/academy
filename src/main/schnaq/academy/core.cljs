(ns schnaq.academy.core
  (:require ["react-dom/client" :refer [createRoot]]
            [goog.dom :as gdom]
            [goog.string.format] ;; required for goog.string. We need to require it once in our project.
            [re-frame.core :as rf]
            [reagent.core :as r]
            [schnaq.academy.base :as base]
            [schnaq.academy.routes :as routes]
            [schnaq.academy.utils]))

(defonce root (createRoot (gdom/getElement "app")))

(defn- render []
  (.render root (r/as-element [base/root])))

(defn init
  []
  (routes/init-routes!)
  (render))

(defn- ^:dev/after-load clear-cache-and-render!
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (rf/clear-subscription-cache!)
  (routes/init-routes!)
  (render))

(rf/reg-event-fx
 :init
 (fn [_ _]
   {:fx [[:dispatch [:dark-mode/init]]]}))
