(ns schnaq.academy.components
  (:require ["framer-motion/dist/framer-motion" :refer [motion]]
            [reagent.core :as r]))

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

(defn image
  "Create an image, which zooms in and out on click.

  Usage: `[image {:src \"path-to-file\" :class \"additional-classes\"}]`"
  [_properties]
  (let [open? (r/atom false)
        transition {:type :spring
                    :damping 25
                    :stiffness 120}]
    (fn [properties]
      [:div.image-container {:class (when @open? "open")
                             :style {:z-index (when @open? 1000)}}
       [:> (.-div motion)
        {:animate {:opacity (if @open? 1 0)}
         :transition transition
         :class "shade"
         :on-click #(reset! open? false)}]
       [:> (.-img motion)
        (merge
         {:on-click #(swap! open? not)
          :layout true
          :transition transition}
         properties)]])))
