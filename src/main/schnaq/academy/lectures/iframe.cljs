(ns schnaq.academy.lectures.iframe
  "Describe iframe embeddings."
  (:require ["react-highlight$default" :as Highlight]
            [goog.string :refer [format]]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.utils :as utils]))

(rf/reg-event-db
 :iframe/configuration
 (fn [db [_ field value]]
   (assoc-in db [:iframe field] value)))

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
  (let [share-hash @(rf/subscribe [:academy/share-hash])
        language @(rf/subscribe [:iframe/language])
        height @(rf/subscribe [:iframe/height])
        html (utils/component->pretty-html [embedding share-hash language height])]
    [:<>
     [:h2 "schnaq einbetten"]
     [:p "schnaq kann in beliebige Web-Seiten und E-Learningsysteme eingebettet werden. Hier kannst du dir ein Code-Snippet erstellen, den du dann verwenden kannst, um schnaq in deinem E-Learning System oder auf deiner Webseite einzubinden."]
     [:section.grid.grid-cols-3.gap-4.pt-3.mb-5
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
         :step 10
         :on-change #(rf/dispatch [:iframe/configuration :height (oget % [:target :value])])}]]]
     [:h3 "Code zum Einbetten"]
     [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie unten angegeben in deine Website einzubetten."]
     [:> Highlight {:class "language-html"} html]
     [:button {:on-click #(utils/copy-to-clipboard! html)} "Code kopieren"]
     [:h3 "Vorschau"]
     [embedding share-hash language height]]))
