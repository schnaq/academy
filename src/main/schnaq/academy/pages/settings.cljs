(ns schnaq.academy.pages.settings
  "Describe iframe embeddings."
  (:require ["@heroicons/react/solid" :refer [ExternalLinkIcon]]
            [goog.string :refer [format]]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.config :as config]
            [schnaq.academy.utils :as utils]))

;; -----------------------------------------------------------------------------
;; Inputs

(defn- language-input
  "Configure the language."
  []
  (let [language @(rf/subscribe [:ui.settings/language])]
    [:label
     [:span "Stelle die Sprache ein."]
     [:select.input
      {:type :text
       :on-change #(rf/dispatch [:ui.settings/field :language (oget % [:target :value])])}
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
       :placeholder config/default-iframe-height
       :step 10
       :on-change #(rf/dispatch [:ui.settings/field :height (oget % [:target :value])])}]]))

(defn num-rows-input
  "Set number of columns in a discussion view."
  []
  (let [num-rows @(rf/subscribe [:ui.settings/field :num-rows])]
    [:label
     [:span "Maximale Anzahl der Spalten auf großen Bildschirmen."]
     [:input.input
      {:type :number
       :min 1
       :value (or num-rows 2)
       :on-change #(rf/dispatch [:ui.settings/field :num-rows (oget % [:target :value])])}]]))

(defn hide-navbar-input []
  (let [hide-navbar @(rf/subscribe [:ui.settings/field :hide-navbar])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-navbar
       :on-change #(rf/dispatch [:ui.settings/field :hide-navbar (oget % [:target :checked])])}]
     [:span "Verstecke die Navigationsleiste."]]))

(defn hide-discussion-options-input []
  (let [hide-discussion-options @(rf/subscribe [:ui.settings/field :hide-discussion-options])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-discussion-options
       :on-change #(rf/dispatch [:ui.settings/field :hide-discussion-options (oget % [:target :checked])])}]
     [:span "Verstecke die Diskussionsoptionen."]]))

(defn- hide-input-input []
  (let [hide-input @(rf/subscribe [:ui.settings/field :hide-input])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-input
       :on-change #(rf/dispatch [:ui.settings/field :hide-input (oget % [:target :checked])])}]
     [:span "Deaktiviere Eingabemöglichkeiten."]]))

(defn- hide-input-replies-input []
  (let [hide-input-replies @(rf/subscribe [:ui.settings/field :hide-input-replies])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value hide-input-replies
       :on-change #(rf/dispatch [:ui.settings/field :hide-input-replies (oget % [:target :checked])])}]
     [:span "Deaktiviere Antwortmöglichkeiten"]]))

;; -----------------------------------------------------------------------------

(defn- copy-link-button []
  (let [url-to-schnaq @(rf/subscribe [:schnaq.url/configured])]
    [:<>
     [utils/highlight-code {:language "clojure"} url-to-schnaq]
     [:button.mr-3 {:on-click #(utils/copy-to-clipboard! url-to-schnaq)} "Link kopieren"]
     [:a {:href url-to-schnaq :target :_blank}
      "schnaq besuchen" [:> ExternalLinkIcon {:class "h-5 inline"}]]]))

(defn- iframe
  "Embed the configured schnaq as an iframe."
  []
  (let [height @(rf/subscribe [:ui.iframe/height])
        height' (if (= "" height) config/default-iframe-height height)
        url-to-schnaq @(rf/subscribe [:schnaq.url/configured])]
    [:div {:style {:position :relative :overflow :hidden :width "100%" :padding-top (format "%dpx" height')}}
     [:iframe
      {:style {:position :absolute :width "100%" :height "100%" :top 0 :bottom 0 :left 0 :right 0}
       :src url-to-schnaq}]]))

(defn iframe-embedding []
  (let [html (utils/component->pretty-html [iframe])]
    [:<>
     [:h3 "Einbettungscode für iFrames"]
     [:p "Viele Plattformen erlauben es andere Websites in Form von iFrames einzubinden, also als ein Seite-in-Seite-Modus. Das ist auch mit schnaq möglich, sodass es nahezu überall verwendet werden kann, wo Webanwendungen verwendet werden."]
     [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie unten angegeben in deine Website einzubetten."]
     [:div {:class "w-1/3"} [iframe-height-input]]
     [utils/highlight-code {:language "html"} html]
     [:button {:on-click #(utils/copy-to-clipboard! html)} "Code kopieren"]]))

(defn ui-settings []
  [:<>
   [:h2 "Interface Einstellungen"]
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
   [copy-link-button]
   [iframe-embedding]
   [:h3 "Vorschau"]
   #_[embedding]])

;; -----------------------------------------------------------------------------

(rf/reg-event-db
 :ui.settings/field
 (fn [db [_ field value]]
   (assoc-in db [:settings field] value)))

(rf/reg-sub
 :ui.settings/field
 (fn [db [_ field fallback]]
   (get-in db [:settings field] fallback)))

(rf/reg-sub
 :ui.settings/language
 (fn [db]
   (get-in db [:settings :language] config/default-language)))

(rf/reg-sub
 :ui.iframe/height
 (fn [db]
   (get-in db [:settings :height] 550)))

(rf/reg-sub
 :schnaq.url/configured
 :<- [:academy/share-hash]
 :<- [:ui.settings/language]
 :<- [:ui.settings/field :num-rows]
 :<- [:ui.settings/field :hide-discussion-options]
 :<- [:ui.settings/field :hide-navbar]
 :<- [:ui.settings/field :hide-input]
 :<- [:ui.settings/field :hide-input-replies]
 (fn [[share-hash language num-rows hide-discussion-options hide-navbar hide-input hide-input-replies]]
   (let [query-parameters {:num-rows num-rows
                           :hide-discussion-options hide-discussion-options
                           :hide-navbar hide-navbar
                           :hide-input hide-input
                           :hide-input-replies hide-input-replies}]
     (utils/build-uri-with-query-params
      (format "%s/%s/schnaq/%s" config/frontend-url language share-hash)
      (utils/remove-falsy query-parameters)))))
