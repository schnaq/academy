(ns schnaq.academy.core
  (:require [goog.dom :as gdom]
            [goog.string.format] ;; required for goog.string. We need to require it once in our project.
            [re-frame.core :as rf]
            [reagent.dom]
            [schnaq.academy.navigation :as navigation]
            [schnaq.academy.pages.base :as base]
            [schnaq.academy.utils]))

(defn- render []
  (reagent.dom/render [base/root] (gdom/getElement "app")))

(defn init
  []
  (navigation/init-routes!)
  (render))

(defn- ^:dev/after-load clear-cache-and-render!
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (rf/clear-subscription-cache!)
  (navigation/init-routes!)
  (render))

(rf/reg-event-fx
 :init
 (fn [_ _]
   {:fx [[:dispatch [:dark-mode/init]]]}))
