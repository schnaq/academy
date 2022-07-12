(ns schnaq.academy.components)

(defn iframe
  "Embed an iframe."
  [src height]
  [:iframe
   {:style {:border 0}
    :height height :width "100%"
    :allowFullScreen true :allow "fullscreen"
    :src src}])

(defn card
  "Build a lecture card."
  [title body cta href]
  [:article.p-6.max-w-sm.bg-white.rounded-lg.border.border-gray-200.shadow-md.dark:bg-gray-800.dark:border-gray-700
   [:h3.mb-2.font-bold.tracking-tight [:a.border-none {:href href} title]]
   [:p.mb-3 body]
   [:a.btn {:href href} cta]])
