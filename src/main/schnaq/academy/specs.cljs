(ns schnaq.academy.specs
  (:require [cljs.spec.alpha :as s]))

(s/def :re-frame/component vector?)

;; -----------------------------------------------------------------------------
;; Accepted arguments from schnaq via URL

(def uuid-pattern
  #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

(s/def :discussion/share-hash (s/and string? #(re-matches uuid-pattern %)))

(s/def :settings/hide-discussion-options boolean?)
(s/def :settings/hide-navbar boolean?)
(s/def :settings/hide-input boolean?)
(s/def :settings/num-rows nat-int?)
(s/def :settings/hide-input-replies boolean?)

(s/def :settings/schnaq
  (s/keys :opt-un [:settings/hide-discussion-options
                   :settings/hide-navbar
                   :settings/hide-input
                   :settings/num-rows
                   :settings/hide-input-replies]))
