(ns schnaq.academy.lectures.iframe
  "Describe iframe embeddings."
  (:require ["react-syntax-highlighter" :refer [Prism]]
            ["react-syntax-highlighter/dist/esm/styles/prism" :refer [darcula
                                                                      github]]
            [goog.string :refer [format]]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.config :as config]
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

(defn- embedding [share-hash language height query-parameters]
  [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top (format "%dpx" height)}}
   [:iframe
    {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
     :src (utils/build-uri-with-query-params
           (format "%s/%s/schnaq/%s" config/frontend-url language share-hash)
           (utils/remove-falsy query-parameters))}]])

(defn iframe-embedding []
  (let [share-hash @(rf/subscribe [:academy/share-hash])
        language @(rf/subscribe [:iframe/configuration :language "de"])
        height @(rf/subscribe [:iframe/configuration :height 550])
        num-rows @(rf/subscribe [:iframe/configuration :num-rows 2])
        hide-discussion-options @(rf/subscribe [:iframe/configuration :hide-discussion-options])
        hide-navbar @(rf/subscribe [:iframe/configuration :hide-navbar])
        hide-input @(rf/subscribe [:iframe/configuration :hide-input])
        hide-input-replies @(rf/subscribe [:iframe/configuration :hide-input-replies])
        dark-mode? @(rf/subscribe [:dark-mode?])
        embedding-comp [embedding share-hash language height {:num-rows num-rows
                                                              :hide-discussion-options hide-discussion-options
                                                              :hide-navbar hide-navbar
                                                              :hide-input hide-input
                                                              :hide-input-replies hide-input-replies}]
        html (utils/component->pretty-html embedding-comp)]
    [:<>
     [:h2 "schnaq Einstellungen"]
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
         :min 1
         :value num-rows
         :on-change #(rf/dispatch [:iframe/configuration :num-rows (oget % [:target :value])])}]]
      [:label.inline-flex.items-center
       [:input
        {:type :checkbox
         :value hide-navbar
         :on-change #(rf/dispatch [:iframe/configuration :hide-navbar (oget % [:target :checked])])}]
       [:span "Verstecke die Navigationsleiste"]]
      [:label.inline-flex.items-center
       [:input
        {:type :checkbox
         :value hide-discussion-options
         :on-change #(rf/dispatch [:iframe/configuration :hide-discussion-options (oget % [:target :checked])])}]
       [:span "Verstecke die Diskussionsoptionen"]]
      [:label.inline-flex.items-center
       [:input
        {:type :checkbox
         :value hide-input
         :on-change #(rf/dispatch [:iframe/configuration :hide-input (oget % [:target :checked])])}]
       [:span "Deaktiviere Eingabemöglichkeiten"]]
      [:label.inline-flex.items-center
       [:input
        {:type :checkbox
         :value hide-input-replies
         :on-change #(rf/dispatch [:iframe/configuration :hide-input-replies (oget % [:target :checked])])}]
       [:span "Deaktiviere Antwortmöglichkeiten"]]]
     [:h3 "Einbettungscode"]
     [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie unten angegeben in deine Website einzubetten."]
     [:> Prism {:language "html" :style (if dark-mode? darcula github)}
      html]
     [:button {:on-click #(utils/copy-to-clipboard! html)} "Code kopieren"]
     [:h3 "Vorschau"]
     #_embedding-comp]))
