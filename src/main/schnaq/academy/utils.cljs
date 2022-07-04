(ns schnaq.academy.utils
  (:require ["prettier/parser-html" :as parserHtml]
            ["prettier/standalone" :as prettier]
            [cljs.spec.alpha :as s]
            [clojure.string :as str]
            [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [goog.dom :as gdom]
            [goog.dom.classlist :as gdomcl]
            [goog.uri.utils :as uri]
            [hodgepodge.core :refer [local-storage]]
            [re-frame.core :as rf]
            [reagent.dom.server :refer [render-to-string]]))

(>defn build-uri-with-query-params
  "Takes a URL and adds the parameters as query parameters."
  [url params]
  [string? map? => string?]
  (uri/appendParamsFromMap url (clj->js params)))

(>defn copy-to-clipboard!
  "Copies a string to the users clipboard."
  [value]
  [(s/or :string string? :number number?) => any?]
  (let [el (js/document.createElement "textarea")]
    (set! (.-value el) value)
    (.appendChild js/document.body el)
    (.select el)
    (js/document.execCommand "copy")
    (.removeChild js/document.body el)))

(>defn component->pretty-html
  "Takes a component, converts to a string and prettifies it."
  [component]
  [:re-frame/component => string?]
  (-> (.format prettier (render-to-string component)
               #js {:parser "html"
                    :plugins #js [parserHtml]})
      (.replaceAll "&amp;" "&")
      (.replaceAll ";" "; ")))

(>defn remove-falsy
  "Removes all entries from a map that have falsy values."
  [data]
  [associative? :ret associative?]
  (into {} (remove #(not (second %)) data)))

(defn- dark-mode?
  "Check if dark-mode is configured by the user."
  []
  (or (:dark-mode? local-storage)
      (and (nil? (:dark-mode? local-storage))
           (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)")))))

(rf/reg-sub
 :dark-mode?
 (fn [{:keys [db]}]
   (get db :dark-mode? (dark-mode?))))

(rf/reg-event-fx
 :dark-mode/toggle
 (fn [{:keys [db]} _]
   {:db (assoc db :dark-mode? (not (dark-mode?)))
    :fx [[:localstorage/assoc [:dark-mode? (not (dark-mode?))]]
         [:dark-mode/init!]]}))

(rf/reg-event-fx
 :dark-mode/init
 (fn [{:keys [db]} _]
   {:db (assoc db :dark-mode? (dark-mode?))
    :fx [[:dark-mode/init!]]}))

(rf/reg-fx
 :dark-mode/init!
 (fn []
   (if (dark-mode?)
     (gdomcl/add (gdom/getElement "main") "dark")
     (gdomcl/remove (gdom/getElement "main") "dark"))))

(rf/reg-fx
 :localstorage/assoc
 (fn [[key value]]
   (assoc! local-storage key value)))

(rf/reg-fx
 :localstorage/dissoc
 (fn [key]
   (dissoc! local-storage key)))
