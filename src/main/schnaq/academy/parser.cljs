(ns schnaq.academy.parser
  (:require [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [goog.uri.utils :as uri]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as reitit-frontend]
            [schnaq.academy.specs]))

(def ^:private routes
  (let [configuration {:parameters {:path {:share-hash :discussion/share-hash}
                                    :query :ui.settings/schnaq}}]
    [["/de/schnaq/:share-hash" configuration]
     ["/en/schnaq/:share-hash" configuration]
     ["/schnaq/:share-hash" configuration]]))

(def ^:private router
  (reitit-frontend/router routes {:data {:coercion rss/coercion}}))

(>defn extract-parameters
  "Extract parameters from configured and allowed urls."
  [url]
  [string? => map?]
  (let [path (uri/getPath url)]
    (reitit-frontend/match-by-path router path)))

(comment
  (reitit-frontend/match-by-path router "/de/schnaq/9d3787a8-d6bb-4573-8324-bc9709b56116?foo=42&hide-input=true")

  nil)
