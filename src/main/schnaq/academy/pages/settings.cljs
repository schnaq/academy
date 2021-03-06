(ns schnaq.academy.pages.settings
  "Describe iframe embeddings."
  (:require ["@heroicons/react/solid" :refer [ExternalLinkIcon]]
            [cljs.spec.alpha :as s]
            [goog.string :refer [format]]
            [goog.uri.utils :as uri]
            [oops.core :refer [oget]]
            [re-frame.core :as rf]
            [schnaq.academy.config :as config]
            [schnaq.academy.parser :as parser]
            [schnaq.academy.specs]
            [schnaq.academy.utils :as utils]))

;; -----------------------------------------------------------------------------
;; Inputs

(defn- language-input
  "Configure the language."
  []
  (let [language @(rf/subscribe [:settings/language])]
    [:label
     [:span "Stelle die Sprache ein."]
     [:select.input
      {:type :text
       :value language
       :on-change #(rf/dispatch [:settings/language (oget % [:target :value])])}
      [:option {:value "de"} "de"]
      [:option {:value "en"} "en"]]]))

(defn iframe-height-input
  "Configure the iframe height."
  []
  (let [height @(rf/subscribe [:settings.iframe/height])]
    [:label
     [:span "Höhe des schnaqs in Pixeln."]
     [:input.input
      {:type :number
       :value height
       :placeholder config/default-iframe-height
       :step 10
       :on-change #(rf/dispatch [:settings/field :height (oget % [:target :value])])}]]))

(defn- num-rows-input
  "Set number of columns in a discussion view."
  []
  (let [num-rows @(rf/subscribe [:settings/field :num-rows])]
    [:label
     [:span "Maximale Anzahl der Spalten auf großen Bildschirmen."]
     [:input.input
      {:type :number
       :min 1
       :value (or num-rows 2)
       :on-change #(rf/dispatch [:settings/field :num-rows (oget % [:target :value])])}]]))

(defn- hide-navbar-input []
  (let [hide-navbar @(rf/subscribe [:settings/field :hide-navbar])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :checked (or hide-navbar false)
       :on-change #(rf/dispatch [:settings/field :hide-navbar (oget % [:target :checked])])}]
     [:span "Verstecke die Navigationsleiste."]]))

(defn- hide-footer-input []
  (let [hide-footer @(rf/subscribe [:settings/field :hide-footer])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :checked (or hide-footer false)
       :on-change #(rf/dispatch [:settings/field :hide-footer (oget % [:target :checked])])}]
     [:span "Verstecke den Footer (Standard wenn in iFrame eingebunden)."]]))

(defn hide-discussion-options-input []
  (let [hide-discussion-options @(rf/subscribe [:settings/field :hide-discussion-options])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :checked (or hide-discussion-options false)
       :on-change #(rf/dispatch [:settings/field :hide-discussion-options (oget % [:target :checked])])}]
     [:span "Verstecke die Diskussionsoptionen."]]))

(defn- hide-input-input []
  (let [hide-input @(rf/subscribe [:settings/field :hide-input])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :checked (or hide-input false)
       :on-change #(rf/dispatch [:settings/field :hide-input (oget % [:target :checked])])}]
     [:span "Deaktiviere Eingabemöglichkeiten."]]))

(defn- hide-input-replies-input []
  (let [hide-input-replies @(rf/subscribe [:settings/field :hide-input-replies])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :value (or hide-input-replies false)
       :on-change #(rf/dispatch [:settings/field :hide-input-replies (oget % [:target :checked])])}]
     [:span "Deaktiviere Antwortmöglichkeiten."]]))

(defn- hide-activations-input []
  (let [hide-activations @(rf/subscribe [:settings/field :hide-activations])]
    [:label.inline-flex.items-center
     [:input
      {:type :checkbox
       :checked (or hide-activations false)
       :on-change #(rf/dispatch [:settings/field :hide-activations (oget % [:target :checked])])}]
     [:span "Verstecke die Aktivierungen (Wortwolke, Umfragen, ...)"]]))

(defn schnaq-url-input []
  (let [schnaq-url @(rf/subscribe [:settings/schnaq-url])]
    [:label
     [:span "Füge hier die vollständige Adresse zu deinem schnaq ein."]
     [:input.input
      {:type :text
       :on-change (fn [e]
                    (let [content (oget e [:target :value])]
                      (rf/dispatch [:settings/from-schnaq-url content])))
       :placeholder schnaq-url}]]))

;; -----------------------------------------------------------------------------

(defn- copy-link-button []
  (let [url-to-schnaq @(rf/subscribe [:settings/schnaq-url])]
    [:<>
     [utils/highlight-code {:language "clojure"} url-to-schnaq]
     [:button.btn.mr-3 {:on-click #(utils/copy-to-clipboard! url-to-schnaq)} "Link kopieren"]
     [:a.btn {:href url-to-schnaq :target :_blank}
      "schnaq besuchen" [:> ExternalLinkIcon {:class "h-5 inline"}]]]))

(defn configured-schnaq-code
  "Add field with the highlighted schnaq code with configured UI."
  []
  [:<>
   [:p "Hier ist dein generierter Link zu deinem schnaq:"]
   [copy-link-button]])

(defn ui-settings []
  [:<>
   [:h2 "Interface Einstellungen"]
   [:p "Die Einstellungen sind interaktiv und finden nur bei dir im Browser statt. Wenn du den Zugangslink zu deinem schnaq hier eingibst, wird er nicht gespeichert und nur auf deinem Gerät weiterverarbeitet."]
   [schnaq-url-input]
   [:section.grid.md:grid-cols-2.gap-4.pt-3.mb-5
    [language-input]
    [num-rows-input]
    [:div
     [:div [hide-navbar-input]]
     [:div [hide-footer-input]]
     [:div [hide-discussion-options-input]]
     [:div [hide-activations-input]]
     [:div [hide-input-input]]
     [:div [hide-input-replies-input]]]]])

;; -----------------------------------------------------------------------------

(rf/reg-event-db
 :settings/field
 (fn [db [_ field value]]
   (assoc-in db [:settings field] value)))

(rf/reg-event-db
 :settings.field/dissoc
 (fn [db [_ field]]
   (update db :settings dissoc field)))

(rf/reg-sub
 :settings/field
 (fn [db [_ field fallback]]
   (get-in db [:settings field] fallback)))

(rf/reg-sub
 :settings/language
 (fn [db]
   (get-in db [:settings :language] config/default-language)))

(rf/reg-sub
 :settings.iframe/height
 (fn [db]
   (get-in db [:settings :height] 550)))

(rf/reg-sub
 :settings/schnaq-url
 :<- [:settings/share-hash]
 :<- [:settings/field :host]
 :<- [:settings/language]
 :<- [:settings/field :num-rows]
 :<- [:settings/field :hide-discussion-options]
 :<- [:settings/field :hide-navbar]
 :<- [:settings/field :hide-footer]
 :<- [:settings/field :hide-activations]
 :<- [:settings/field :hide-input]
 :<- [:settings/field :hide-input-replies]
 (fn [[share-hash host language num-rows hide-discussion-options hide-navbar hide-footer hide-activations hide-input hide-input-replies]]
   (let [query-parameters {:num-rows num-rows
                           :hide-discussion-options hide-discussion-options
                           :hide-navbar hide-navbar
                           :hide-footer hide-footer
                           :hide-activations hide-activations
                           :hide-input hide-input
                           :hide-input-replies hide-input-replies}]
     (utils/build-uri-with-query-params
      (format "%s/%s/schnaq/%s" (or host config/frontend-url) language share-hash)
      (utils/remove-falsy query-parameters)))))

(rf/reg-sub
 :settings/share-hash
 (fn [db]
   (get-in db [:settings :share-hash] config/default-share-hash)))

(rf/reg-event-db
 :settings/share-hash
 (fn [db [_ share-hash]]
   (when (s/valid? :discussion/share-hash share-hash)
     (assoc-in db [:settings :share-hash] share-hash))))

(rf/reg-event-db
 :settings/language
 (fn [db [_ language]]
   (let [checked-language (if (s/valid? :discussion/language language) language "de")]
     (assoc-in db [:settings :language] checked-language))))

(rf/reg-event-fx
 :settings/from-schnaq-url
 (fn [{:keys [db]} [_ url]]
   (when url
     (let [host (uri/getHost url)
           parameters (parser/extract-parameters url)
           share-hash (get-in parameters [:path :share-hash])
           language (get-in parameters [:path :language])]
       {:db (update db :settings merge (:query parameters))
        :fx [[:dispatch [:settings/share-hash share-hash]]
             [:dispatch [:settings/language language]]
             [:dispatch (if (seq host)
                          [:settings/field :host host]
                          [:settings.field/dissoc :host])]]}))))
