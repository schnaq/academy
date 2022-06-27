(ns schnaq.academy.utils
  (:require ["prettier/parser-html" :as parserHtml]
            ["prettier/standalone" :as prettier]
            [cljs.spec.alpha :as s]
            [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [reagent.dom.server :refer [render-to-string]]))

(>defn copy-to-clipboard!
  "Copies a string to the users clipboard."
  [value]
  [(s/or :string string? :number number?) => any?]
  (let [el (js/document.createElement "textarea")]
    (set! (.-value el) value)
    (.appendChild js/document.body el)
    (.select el)
    (js/document.execCommand "copy")
    (.removeChild js/document.body el)))

(>defn component->pretty-html
  "Takes a component, converts to a string and prettifies it."
  [component]
  [:re-frame/component => string?]
  (-> (.format prettier (render-to-string component)
               #js {:parser "html"
                    :plugins #js [parserHtml]})
      (.replaceAll ";" "; ")))
