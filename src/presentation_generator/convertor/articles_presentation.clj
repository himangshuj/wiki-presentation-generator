(ns presentation-generator.convertor.articles-presentation
  (:require [presentation-generator.model.presentation :as presentation]))

(defn get-presentation-data-from-article [article]
  (let [title (:summary article)
        media (:media article)
        keyVals {:title title :image1 media}]
    (presentation/get-presentation-data keyVals "1imageText")))

(defn get-presentation-from-article-list [articles]
  (let [presentation-Data (map get-presentation-data-from-article articles)]
    (presentation/get-new-presentation presentation-Data)))