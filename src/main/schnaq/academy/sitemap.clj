(ns schnaq.academy.sitemap
  (:require [clojure.spec.alpha :as s]
            [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [reitit.core :as rc]
            [schnaq.academy.config :as config]
            [schnaq.academy.routes :as routes]
            [sitemap.core :refer [generate-sitemap]]))

(defn all-routes
  "Read all route-paths from the router."
  [router]
  (->> router rc/routes (into {}) keys))

(>defn absolute-urls
  "All absolute urls for this application."
  [routes]
  [(s/coll-of string?) => (s/coll-of string?)]
  (map #(format "%s%s" config/academy-url %) routes))

(defn build-sitemap-xml
  "Takes a reitit router and builds a xml sitemap."
  [router]
  (let [urls (for [url (absolute-urls (all-routes router))]
               {:loc url})]
    (generate-sitemap urls)))

(defn generate-sitemap! []
  (spit "resources/public/sitemap.xml"
        (build-sitemap-xml (rc/router routes/routes))))
