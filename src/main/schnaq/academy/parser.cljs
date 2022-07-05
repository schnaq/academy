(ns schnaq.academy.parser
  (:require [clojure.string :as str]
            [com.fulcrologic.guardrails.core :refer [=> >defn ?]]
            [goog.uri.utils :as uri]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as reitit-frontend]
            [schnaq.academy.specs]))

(def ^:private routes
  (let [configuration {:parameters {:path {:language string?
                                           :share-hash :discussion/share-hash}
                                    :query :settings/schnaq}}]
    [["/:language/schnaq/:share-hash" configuration]
     ["/schnaq/:share-hash" configuration]]))

(def ^:private router
  (reitit-frontend/router routes {:data {:coercion rss/coercion}}))

(>defn extract-parameters
  "Extract parameters from configured and allowed urls. Only coerced and defined
  values are provided in the result."
  [url]
  [string? => (? map?)]
  (let [path (str/replace url (uri/getHost url) "")]
    (try
      (:parameters (reitit-frontend/match-by-path router path))
      (catch js/Object _e))))
