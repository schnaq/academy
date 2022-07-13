(ns schnaq.academy.utils
  (:require ["prettier/parser-html" :as parserHtml]
            ["prettier/standalone" :as prettier]
            ["react-syntax-highlighter" :refer [Prism]]
            ["react-syntax-highlighter/dist/esm/styles/prism" :refer [darcula
                                                                      github]]
            [cljs.spec.alpha :as s]
            [clojure.string :as str]
            [com.fulcrologic.guardrails.core :refer [=> >defn ?]]
            [goog.string :refer [format unescapeEntities]]
            [goog.uri.utils :as uri]
            [hodgepodge.core :refer [local-storage]]
            [oops.core :refer [oget oset!]]
            [re-frame.core :as rf]
            [reagent.dom.server :refer [render-to-string]]
            [schnaq.academy.config :as config]))

(>defn build-uri-with-query-params
  "Takes a URL and adds the parameters as query parameters."
  [url params]
  [string? map? => string?]
  (uri/appendParamsFromMap url (clj->js params)))

(>defn copy-to-clipboard!
  "Copies a string to the users clipboard."
  [value]
  [(s/or :string string? :number number?) => any?]
  (.writeText (.-clipboard js/navigator) value))

(>defn remove-unnecessary-whitespace [text]
  [string? => string?]
  (-> text
      (str/replace "\n" "")
      (str/replace "  " " ")
      unescapeEntities))

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

(defn highlight-code
  "Highlight the provided code and provide buttons to copy the content."
  [{:keys [language]} code]
  (let [dark-mode? @(rf/subscribe [:dark-mode?])]
    [:> Prism {:language language :style (if dark-mode? darcula github)}
     code]))

(defn- dark-mode?
  "Check if dark-mode is configured by the user."
  []
  (or (:dark-mode? local-storage)
      (and (nil? (:dark-mode? local-storage))
           (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)")))))

;; -----------------------------------------------------------------------------

(>defn set-website-title!
  "Set a document's website title."
  [title]
  [(? string?) :ret any?]
  (when title
    (let [new-title (format "%s – %s" title config/application-name)]
      (oset! js/document [:title] new-title))))

(>defn set-website-description!
  "Set a document's website meta-description."
  [description]
  [(? string?) :ret any?]
  (when description
    (when-let [selector (.querySelector js/document "meta[name='description']")]
      (.setAttribute selector "content" description))
    (when-let [og-selector (.querySelector js/document "meta[name='og:description']")]
      (.setAttribute og-selector "content" description))))

;; -----------------------------------------------------------------------------

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
     (.add (oget js/document [:documentElement :classList]) "dark")
     (.remove (oget js/document [:documentElement :classList]) "dark"))))

(rf/reg-fx
 :localstorage/assoc
 (fn [[key value]]
   (assoc! local-storage key value)))

(rf/reg-fx
 :localstorage/dissoc
 (fn [key]
   (dissoc! local-storage key)))
