(ns schnaq.academy.lectures.iframe
  "Describe iframe embeddings."
  (:require ["react-syntax-highlighter" :refer [Prism]]
            ["react-syntax-highlighter/dist/esm/styles/prism" :refer [darcula github]]
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

(rf/reg-sub
 :ui.settings/language
 (fn [db]
   (get-in db [:iframe :language] "de")))

(rf/reg-sub
 :ui.iframe/height
 (fn [db]
   (get-in db [:iframe :height] 550)))

;; -----------------------------------------------------------------------------

(defn- embedding
  "Embed the configured schnaq as an iframe."
  []
  (let [share-hash @(rf/subscribe [:academy/share-hash])
        language @(rf/subscribe [:ui.settings/language])
        height @(rf/subscribe [:ui.iframe/height])
        num-rows @(rf/subscribe [:iframe/configuration :num-rows])
        hide-discussion-options @(rf/subscribe [:iframe/configuration :hide-discussion-options])
        hide-navbar @(rf/subscribe [:iframe/configuration :hide-navbar])
        hide-input @(rf/subscribe [:iframe/configuration :hide-input])
        hide-input-replies @(rf/subscribe [:iframe/configuration :hide-input-replies])
        query-parameters {:num-rows num-rows
                          :hide-discussion-options hide-discussion-options
                          :hide-navbar hide-navbar
                          :hide-input hide-input
                          :hide-input-replies hide-input-replies}]
    [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top (format "%dpx" height)}}
     [:iframe
      {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
       :src (utils/build-uri-with-query-params
             (format "%s/%s/schnaq/%s" config/frontend-url language share-hash)
             (utils/remove-falsy query-parameters))}]]))

(defn- language-input
  "Configure the language."
  []
  (let [language @(rf/subscribe [:ui.settings/language])]
    [:label
     [:span "Stelle die Sprache ein."]
     [:select.input
      {:type :text
       :on-change #(rf/dispatch [:iframe/configuration :language (oget % [:target :value])])}
      [:option {:value "de" :defaultValue (= language "de")} "de"]
      [:option {:value "en" :defaultValue (= language "en")} "en"]]]))

(defn iframe-height-input
  "Configure the iframe height."
  []
  (let [height @(rf/subscribe [:ui.iframe/height])]
    [:label
     [:span "Höhe des schnaqs in Pixeln."]
     [:input.input
      {:type :number
       :value height
       :step 10
       :on-change #(rf/dispatch [:iframe/configuration :height (oget % [:target :value])])}]]))

(defn num-rows-input
  "Set number of columns in a discussion view."
  []
  (let [num-rows @(rf/subscribe [:iframe/configuration :num-rows])]
    [:label
     [:span "Maximale Anzahl der Spalten auf großen Bildschirmen"]
     [:input.input
      {:type :number
       :min 1
       :value (or num-rows 2)
       :on-change #(rf/dispatch [:iframe/configuration :num-rows (oget % [:target :value])])}]]))

(defn hide-navbar-input []
  (let [hide-navbar @(rf/subscribe [:iframe/configuration :hide-navbar])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-navbar
       :on-change #(rf/dispatch [:iframe/configuration :hide-navbar (oget % [:target :checked])])}]
     [:span "Verstecke die Navigationsleiste"]]))

(defn hide-discussion-options-input []
  (let [hide-discussion-options @(rf/subscribe [:iframe/configuration :hide-discussion-options])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-discussion-options
       :on-change #(rf/dispatch [:iframe/configuration :hide-discussion-options (oget % [:target :checked])])}]
     [:span "Verstecke die Diskussionsoptionen"]]))

(defn- hide-input-input []
  (let [hide-input @(rf/subscribe [:iframe/configuration :hide-input])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-input
       :on-change #(rf/dispatch [:iframe/configuration :hide-input (oget % [:target :checked])])}]
     [:span "Deaktiviere Eingabemöglichkeiten"]]))

(defn- hide-input-replies-input []
  (let [hide-input-replies @(rf/subscribe [:iframe/configuration :hide-input-replies])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-input-replies
       :on-change #(rf/dispatch [:iframe/configuration :hide-input-replies (oget % [:target :checked])])}]
     [:span "Deaktiviere Antwortmöglichkeiten"]]))

(defn iframe-embedding []
  (let [dark-mode? @(rf/subscribe [:dark-mode?])
        embedding-comp [embedding]
        html (utils/component->pretty-html embedding-comp)]
    [:<>
     [:h2 "schnaq UI Einstellungen"]
     [:p "Um schnaq in verschiedene Web-Kontexte einzubinden (bspw. Websites, E-Learningplattformen, Powerpoint-Präsentationen, ...), kann das Interface angepasst werden. Hier können diese Einstellungen vorgenommen werden, um einen Code zu generieren, der dann in die entsprechenden Web-Bereiche verwendet zu werden."]
     [:p "Die Einstellungen sind interaktiv und finden nur bei dir im Browser statt. Wenn du den Zugangslink zu deinem schnaq hier eingibst, wird er nicht gespeichert und nur auf deinem Gerät weiterverarbeitet."]
     [:section.grid.md:grid-cols-3.gap-4.pt-3.mb-5
      [language-input]
      [num-rows-input]
      [:div]
      [:div
       [hide-navbar-input]
       [hide-discussion-options-input]
       [hide-input-input]
       [hide-input-replies-input]]]
     [:h3 "Einbettungscode für iFrames"]
     [:p "Viele Plattformen erlauben es andere Websites in Form von iFrames einzubinden, also als ein Seite-in-Seite-Modus. Das ist auch mit schnaq möglich, sodass es nahezu überall verwendet werden kann, wo Webanwendungen verwendet werden."]
     [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie unten angegeben in deine Website einzubetten."]
     [iframe-height-input]
     [:> Prism {:language "html" :style (if dark-mode? darcula github)}
      html]
     [:button {:on-click #(utils/copy-to-clipboard! html)} "Code kopieren"]
     [:h3 "Vorschau"]
     embedding-comp]))
