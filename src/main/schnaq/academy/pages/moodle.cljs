(ns schnaq.academy.pages.moodle
  (:require [schnaq.academy.components :refer [iframe image]]
            [schnaq.academy.pages.base :refer [base]]
            [schnaq.academy.pages.embedding :refer [iframe-code iframe-preview]]
            [schnaq.academy.pages.settings :refer [iframe-height-input
                                                   ui-settings]]))

(defn- moodle-page []
  [base {:page/title "Einbettung in Moodle"
         :page/description "Lerne hier wie du schnaq in deinem Moodle-Kurs verwenden kannst."}
   [:<>
    [:h1 "Einbettung in Moodle"]
    [:div.columns-1.md:columns-2
     [:div
      [:p "schnaq kann in deine Moodle-Plattform eingebunden werden, um beispielsweise Fragen von Studierenden zwischen den Veranstaltungen zu sammeln, oder auch nur den Studierenden einen Raum zu geben sich selbst zu helfen im Peer-to-Peer-Learning-Format. Unten findest du eine bebilderte Anleitung wie das in deiner Moodle-Instanz funktionieren kann."]
      [:p "Damit das funktioniert, musst du einen schnaq anlegen und kannst ihn dann hier konfigurieren."]]
     [image {:src "https://s3.schnaq.com/academy/embeddings/moodle.webp"}]]
    [ui-settings]
    [:div {:class "md:w-1/3"} [iframe-height-input]]
    [iframe-code]
    [:p "Kopiere den Code als Text-Baustein in deine Moodle-Seiten ein. Wie das geht, siehst du in der Anleitung unten."]
    [:h2 "Anleitung"]
    [iframe "https://pitch.com/embed/a53cd132-5c55-4022-b291-4b1e627eafdf" "660px"]
    [iframe-preview]]])

(defn moodle []
  [moodle-page])
