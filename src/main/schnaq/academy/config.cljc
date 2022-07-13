(ns schnaq.academy.config)

(def application-name
  "schnaq academy")

#?(:cljs (goog-define academy-url "http://localhost:8000")
   :clj (def academy-url (or (System/getenv "ACADEMY_URL") "http://localhost:8000")))
#?(:cljs (goog-define frontend-url "https://app.staging.schnaq.com"))
#?(:cljs (goog-define default-share-hash "6586e787-8704-4b4b-9221-5821d15626b5"))

(def default-iframe-height
  "In pixels."
  550)

(def default-language "de")
