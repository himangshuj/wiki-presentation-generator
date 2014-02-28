(ns presentation-generator.model.articles)
(defrecord Article [published summary media] )
(defn get-article [published summary media]
  (Article. published summary media))