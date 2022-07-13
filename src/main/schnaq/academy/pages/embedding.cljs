(ns schnaq.academy.pages.embedding
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [schnaq.academy.config :as config]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [iframe-height-input
                                                   ui-settings]]
            [schnaq.academy.utils :as utils]))

(defn- iframe
  "Embed the configured schnaq as an iframe."
  []
  (let [height @(rf/subscribe [:settings.iframe/height])
        height' (if (= "" height) config/default-iframe-height height)
        url-to-schnaq @(rf/subscribe [:settings/schnaq-url])]
    [:iframe {:style {:width "100%" :height (str height' "px") :border 0}
              :src url-to-schnaq}]))

(defn iframe-code
  "Provide the prepared iframe code."
  []
  (let [html (utils/component->pretty-html [iframe])]
    [:<>
     [utils/highlight-code {:language "html"} html]
     [:button.btn {:on-click #(utils/copy-to-clipboard! (utils/remove-unnecessary-whitespace html))} "Code kopieren"]]))

(defn iframe-explanation
  "Explain iframe to the user."
  []
  [:<>
   [:h1 "Einbettungscode für iFrames"]
   [:p "Viele Plattformen erlauben es andere Websites in Form von iFrames einzubinden, also als ein Seite-in-Seite-Modus. Das ist auch mit schnaq möglich, sodass es nahezu überall verwendet werden kann, wo Webanwendungen verwendet werden."]
   [:p "Konfiguriere zunächst deinen schnaq und bereite ihn für die Einbettung vor."]
   [ui-settings]
   [:p "Kopiere den Code in deine Webanwendung, um den schnaq wie angegeben in deine Website einzubetten."]
   [:div {:class "w-1/3"} [iframe-height-input]]
   [iframe-code]])

(defn iframe-preview
  "Enable iframe preview."
  []
  (let [visible? (r/atom false)]
    (fn []
      [:<>
       [:h2 "Vorschau"]
       [:p "Schau dir hier eine Vorschau zu deinem konfigurierten schnaq an. Dies ist genau der Code aus dem iFrame, den du oben konfiguriert hast."]
       [:button.btn {:on-click #(swap! visible? not)} (if @visible? "Vorschau verstecken" "Vorschau anzeigen")]
       (when @visible?
         [iframe])])))

(defn- embedding-page []
  [base {:page/title "Einbettung per iframe"
         :page/description "Lerne hier, wie du schnaq per iframe in deine Webseite einbinden kannst."}
   [:<>
    [iframe-explanation]
    [iframe-preview]]])

(defn embedding []
  [embedding-page])
