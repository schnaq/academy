(ns schnaq.academy.lectures.iframe
  "Describe iframe embeddings."
  (:require ["react-syntax-highlighter" :refer [Prism]]
            ["react-syntax-highlighter/dist/esm/styles/prism" :refer [github darcula]]
            [goog.string :refer [format]]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.utils :as utils]))

(rf/reg-event-db
 :iframe/configuration
 (fn [db [_ field value]]
   (assoc-in db [:iframe field] value)))

(rf/reg-sub
 :iframe/configuration
 (fn [db [_ field fallback]]
   (get-in db [:iframe field] fallback)))

;; -----------------------------------------------------------------------------

(defn- embedding [share-hash language height num-rows]
  [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top (format "%dpx" height)}}
   [:iframe
    {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
     :src (format "http://localhost:8700/%s/schnaq/%s" language share-hash)}]])

(defn iframe-embedding []
  (let [share-hash @(rf/subscribe [:academy/share-hash])
        language @(rf/subscribe [:iframe/configuration :language "de"])
        height @(rf/subscribe [:iframe/configuration :height 550])
        num-rows @(rf/subscribe [:iframe/configuration :num-rows 2])
        dark-mode? @(rf/subscribe [:dark-mode?])
        html (utils/component->pretty-html [embedding share-hash language height num-rows])]
    [:<>
     [:h2 "schnaq einbetten"]
     [:p "schnaq kann in beliebige Web-Seiten und E-Learningsysteme eingebettet werden. Hier kannst du dir ein Code-Snippet erstellen, den du dann verwenden kannst, um schnaq in deinem E-Learning System oder auf deiner Webseite einzubinden."]
     [:section.grid.md:grid-cols-3.gap-4.pt-3.mb-5
      [:label
       [:span "Stelle die Sprache ein."]
       [:select#iframe-language.input
        {:type :text
         :on-change #(rf/dispatch [:iframe/configuration :language (oget % [:target :value])])}
        [:option {:value "de" :defaultValue (= language "de")} "de"]
        [:option {:value "en" :defaultValue (= language "en")} "en"]]]
      [:label
       [:span "Höhe des schnaqs in Pixeln."]
       [:input#iframe-height.input
        {:type :number
         :value height
         :step 10
         :on-change #(rf/dispatch [:iframe/configuration :height (oget % [:target :value])])}]]
      [:label
       [:span "Anzahl der Spalten auf großen Bildschirmen"]
       [:input#iframe-num-rows.input
        {:type :number
         :value num-rows
         :on-change #(rf/dispatch [:iframe/configuration :num-rows (oget % [:target :value])])}]]]
     [:h3 "Code"]
     [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie unten angegeben in deine Website einzubetten."]
     [:> Prism {:language "html" :style (if dark-mode? darcula github)}
      html]
     [:button {:on-click #(utils/copy-to-clipboard! html)} "Code kopieren"]
     [:h3 "Vorschau"]
     #_[embedding share-hash language height]]))
