(ns schnaq.academy.pages.ilias
    (:require [schnaq.academy.components :refer [iframe image]]
              [schnaq.academy.pages.base :refer [base]]
              [schnaq.academy.pages.embedding :refer [iframe-code iframe-preview]]
              [schnaq.academy.pages.settings :refer [iframe-height-input
                                                     ui-settings]]))

(defn- ilias-page []
       [base {:page/title "Einbettung in ILIAS"
              :page/description "Lerne hier, wie du schnaq in deinen ILIAS-Kurs einbinden kannst."}
        [:<>
         [:h1 "Einbettung in ILIAS"]
         [:div.columns-1.md:columns-2
          [:div
           [:p "schnaq kann in deine ILIAS-Plattform eingebunden werden, um beispielsweise Fragen von Studierenden zwischen den Veranstaltungen zu sammeln, oder auch nuru den Studierenden einen Raum zu geben sich selbst zu helfen im Peer-to-Peer-Learning-Format. Unten findest du eine bebilderte Anleitung wie das in deiner ILIAS-Instanz funktionieren kann."]
           [:p "Damit das funktioniert, musst du einen schnaq anlegen und kannst ihn dann hier konfigurieren."]]
          [image {:src "https://snq-common.s3.nl-ams.scw.cloud/academy/embeddings/ilias.webp"}]]
         [ui-settings]
         [:div {:class "md:w-1/3"} [iframe-height-input]]
         [iframe-code]
         [:p "Kopiere den Code als Text-Baustein in deine ILIAS-Seiten ein. Wie das geht, siehst du in der Anleitung unten."]
         [:h2 "Anleitung"]
         [iframe "https://pitch.com/embed/a56b7578-43c8-44e7-ab18-5011036fffe9" "660px"]
         [iframe-preview]]])

(defn ilias []
      [ilias-page])
