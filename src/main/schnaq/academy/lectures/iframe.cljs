(ns schnaq.academy.lectures.iframe
  "Describe iframe embeddings."
  (:require ["prettier/parser-html" :as parserHtml]
            ["prettier/standalone" :as prettier]
            ["react-highlight$default" :as Highlight]
            [cljs.spec.alpha :as s]
            [goog.string :refer [format]]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [reagent.dom.server :refer [render-to-string]]
            [schnaq.academy.config :as config]
            [schnaq.academy.specs :as specs]))

(defn- component->pretty-string
  "Takes a component, converts to a string and prettifies it."
  [component]
  (.replaceAll
   (.format prettier (render-to-string component)
            #js {:parser "html"
                 :plugins #js [parserHtml]})
   ";" "; "))

(rf/reg-event-db
 :iframe/configuration
 (fn [db [_ field value]]
   (assoc-in db [:iframe field] value)))

(rf/reg-sub
 :iframe/share-hash
 (fn [db]
   (let [share-hash (get-in db [:iframe :share-hash])]
     (if (and share-hash (s/valid? ::specs/share-hash share-hash))
       share-hash
       config/default-share-hash))))

(rf/reg-sub
 :iframe/language
 (fn [db]
   (get-in db [:iframe :language] "de")))

(rf/reg-sub
 :iframe/height
 (fn [db]
   (get-in db [:iframe :height] 550)))

;; -----------------------------------------------------------------------------

(defn- embedding [share-hash language height]
  [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top (format "%dpx" height)}}
   [:iframe
    {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
     :src (format "https://schnaq.com/%s/schnaq/%s" language share-hash)}]])

(defn iframe-embedding []
  (let [share-hash @(rf/subscribe [:iframe/share-hash])
        language @(rf/subscribe [:iframe/language])
        height @(rf/subscribe [:iframe/height])]
    [:<>
     [:h2 "schnaq einbetten"]
     [:p "schnaq kann in beliebige Web-Seiten und E-Learningsysteme eingebettet werden. Hier kannst du dir ein Code-Snippet erstellen, den du dann verwenden kannst, um schnaq in deinem E-Learning System oder auf deiner Webseite einzubinden."]
     [:section.grid.grid-cols-3.gap-4.pt-3.mb-5
      [:label
       [:span "Füge hier den share-hash zu deinem schnaq ein. Das ist die lange Zahlenfolge aus deiner Browserzeile."]
       [:input#iframe-share-hash.input
        {:type :text
         :on-change #(rf/dispatch [:iframe/configuration :share-hash (oget % [:target :value])])
         :placeholder share-hash}]]
      [:label
       [:span "Stelle die Sprache ein."]
       [:select#iframe-language.input
        {:type :text
         :on-change #(rf/dispatch [:iframe/configuration :language (oget % [:target :value])])}
        [:option {:value "de" :selected (= language "de")} "de"]
        [:option {:value "en" :selected (= language "en")} "en"]]]
      [:label
       [:span "Höhe des schnaqs in Pixeln."]
       [:input#iframe-height.input
        {:type :number
         :value height
         :on-change #(rf/dispatch [:iframe/configuration :height (oget % [:target :value])])}]]]
     [:h3 "Code zum Einbetten"]
     [:> Highlight {:class "language-html"}
      (component->pretty-string [embedding share-hash language height])]
     [:h3 "Vorschau"]
     [embedding share-hash language height]]))
