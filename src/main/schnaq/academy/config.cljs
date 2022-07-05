(ns schnaq.academy.config
  (:require [goog.string :refer [format]]))

(def application-name
  "schnaq academy")

(def frontend-url
  "http://localhost:8700")

(def default-share-hash
  "Point to a default schnaq. In this case the FAQ production schnaq."
  "120900da-912e-422b-8ab2-19983cb63cd8")

(def default-schnaq-url
  (format "%s/de/schnaq/%s" frontend-url default-share-hash))

;; -----------------------------------------------------------------------------

(def default-iframe-height
  "In pixels."
  550)

(def default-language "de")
