(ns schnaq.academy.pages.ilias
  (:require [schnaq.academy.components :refer [iframe]]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.settings :refer [configured-schnaq-code
                                                   ui-settings]]))

(defn- ilias-page []
  [base
   [:<>
    [:h1 "Einbettung in ILIAS"]
    [:p "schnaq kann in deine ILIAS-Plattform eingebunden werden, um beispielsweise Fragen von Studierenden zwischen den Veranstaltungen zu sammeln, oder auch nuru den Studierenden einen Raum zu geben sich selbst zu helfen im Peer-to-Peer-Learning-Format. Unten findest du eine bebilderte Anleitung wie das in deiner ILIAS-Instanz funktionieren kann."]
    [:p "Damit das funktioniert, musst du einen schnaq anlegen und kannst ihn dann hier konfigurieren."]
    [ui-settings]
    [configured-schnaq-code]
    [:p "Kopiere den Code als Text-Baustein in deine ILIAS-Seiten ein. Wie das geht, siehst du in der Anleitung unten."]
    [:h2 "Anleitung"]
    [iframe "https://pitch.com/embed/a56b7578-43c8-44e7-ab18-5011036fffe9" "660px"]]])

(defn ilias []
  [ilias-page])
