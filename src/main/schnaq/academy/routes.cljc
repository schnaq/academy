(ns schnaq.academy.routes
  #?(:cljs (:require [re-frame.core :as rf]
                     [schnaq.academy.pages.embedding :refer [embedding]]
                     [schnaq.academy.pages.ilias :refer [ilias]]
                     [schnaq.academy.pages.index :refer [index]]
                     [schnaq.academy.pages.moodle :refer [moodle]]
                     [schnaq.academy.pages.powerpoint :refer [powerpoint]]
                     [schnaq.academy.specs]
                     [spec-tools.data-spec :as ds])))

(def routes
  ["" #?(:cljs {:parameters {:query {(ds/opt :url) string?}}
                :controllers [{:parameters {:query [:url]}
                               :start (fn [{:keys [query]}]
                                        (rf/dispatch [:init])
                                        (rf/dispatch [:settings/from-schnaq-url (:url query)]))}]})
   ["/" #?(:cljs {:controllers [{:start (fn [{:keys [query]}]
                                          (rf/dispatch [:navigation/navigate :routes/start {} query]))}]})]
   ["/de"
    ["" #?(:cljs {:name :routes/start
                  :views index})]
    ["/embedding"
     ["" #?(:cljs {:name :routes/embedding
                   :views embedding})]
     ["/powerpoint" #?(:cljs {:name :routes.embedding/powerpoint
                              :views powerpoint})]
     ["/ilias" #?(:cljs {:name :routes.embedding/ilias
                         :views ilias})]
     ["/moodle" #?(:cljs {:name :routes.embedding/moodle
                          :views moodle})]]]])
