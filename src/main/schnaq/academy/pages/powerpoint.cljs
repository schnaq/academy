(ns schnaq.academy.pages.powerpoint
  (:require [schnaq.academy.components :refer [iframe]]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [configured-schnaq-code
                                                   ui-settings]]))

(defn- powerpoint-page []
  [base
   [:<>
    [:h1 "Einbettung in Powerpoint"]
    [:p "schnaq kann in deine Powerpoint Präsentation eingebunden werden, um beispielsweise live alle Fragen zu sammeln und anzeigen zu lassen. Unten findest du eine bebilderte Anleitung."]
    [:p "Damit das funktioniert, musst du einen schnaq anlegen und kannst ihn dann hier konfigurieren. Wir empfehlen in dem Fall alles zu deaktivieren, was nicht unbedingt notwendig ist."]
    [ui-settings]
    [configured-schnaq-code]
    [:p "Kopiere den Code in deine Powerpoint-Präsentation, um die Fragen der Teilnehmenden dort anzuzeigen."]
    [:h2 "Anleitung"]
    [iframe "https://pitch.com/embed/7609ace7-cab6-4886-9788-f372f5c1ee49" "660px"]]])

(defn powerpoint []
  [powerpoint-page])
