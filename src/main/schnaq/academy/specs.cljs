(ns schnaq.academy.specs
  (:require [cljs.spec.alpha :as s]
            [schnaq.academy.config :as config]))

(s/def ::share-hash
  (s/and #(.includes % "-")
         #(= (count config/default-share-hash) (.-length %))))

(s/def :re-frame/component vector?)

;; -----------------------------------------------------------------------------
;; Accepted arguments from schnaq via URL

(def uuid-pattern
  #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

(s/def :discussion/share-hash (s/and string? #(re-matches uuid-pattern %)))

(s/def :ui.settings/hide-discussion-options boolean?)
(s/def :ui.settings/hide-navbar boolean?)
(s/def :ui.settings/hide-input boolean?)
(s/def :ui.settings/num-rows nat-int?)
(s/def :ui.settings/hide-input-replies boolean?)

(s/def :ui.settings/schnaq
  (s/keys :opt-un [:ui.settings/hide-discussion-options
                   :ui.settings/hide-navbar
                   :ui.settings/hide-input
                   :ui.settings/num-rows
                   :ui.settings/hide-input-replies]))
