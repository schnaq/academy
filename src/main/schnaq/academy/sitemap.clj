(ns schnaq.academy.sitemap
  (:require [clojure.data.xml :as xml]
            [clojure.spec.alpha :as s]
            [com.fulcrologic.guardrails.core :refer [=> >defn]]
            [reitit.core :as rc]
            [schnaq.academy.config :as config]
            [schnaq.academy.routes :as routes]))

(defn all-routes
  "Read all route-paths from the router."
  [router]
  (->> router rc/routes (into {}) keys))

(>defn absolute-urls
  "All absolute urls for this application."
  [routes]
  [(s/coll-of string?) => (s/coll-of string?)]
  (map #(format "%s%s" config/academy-url %) routes))

(defn url->xml
  "Build XML representation of a url."
  [url]
  (xml/element :url {}
               (xml/element :loc {} url)))

(defn build-sitemap-xml
  "Takes a reitit router and builds a xml sitemap."
  [router]
  (let [urls (map url->xml (absolute-urls (all-routes router)))]
    (xml/emit-str (xml/element* :urlset {:xmlns "http://www.w3.org/1999/xhtml"}
                                urls))))

(defn generate-sitemap! []
  (spit "resources/public/sitemap.xml"
        (build-sitemap-xml (rc/router routes/routes))))
