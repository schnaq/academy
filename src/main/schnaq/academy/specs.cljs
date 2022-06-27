(ns schnaq.academy.specs
  (:require [cljs.spec.alpha :as s]
            [schnaq.academy.config :as config]))

(s/def ::share-hash
  (s/and #(.includes % "-")
         #(= (count config/default-share-hash) (.-length %))))
