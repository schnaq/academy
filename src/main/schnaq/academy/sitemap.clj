(ns schnaq.academy.sitemap
  (:require [clojure.spec.alpha :as s]
            [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [reitit.core :as rc]
            [schnaq.academy.config :as config]
            [schnaq.academy.routes :as routes]
            [sitemap.core :refer [generate-sitemap]])
  (:import [java.time.format DateTimeFormatter]
           [java.time LocalDate]))

(defn all-routes
  "Read all route-paths from the router."
  [router]
  (->> router rc/routes (into {}) keys))

(>defn absolute-urls
  "All absolute urls for this application."
  [routes]
  [(s/coll-of string?) => (s/coll-of string?)]
  (map #(format "%s%s" config/academy-url %) routes))

(def today
  (.format (DateTimeFormatter/ofPattern "uuuu-MM-dd")
           (LocalDate/now)))

(defn build-sitemap-xml
  "Takes a reitit router and builds a xml sitemap."
  [router]
  (let [urls (for [url (absolute-urls (all-routes router))]
               {:loc url
                :lastmod today
                :changefreq "weekly"})]
    (generate-sitemap urls)))

(defn generate-sitemap! []
  (spit "resources/public/sitemap.xml"
        (build-sitemap-xml (rc/router routes/routes))))
