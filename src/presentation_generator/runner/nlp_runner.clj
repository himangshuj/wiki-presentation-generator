(ns presentation-generator.runner.nlp-runner
  (:require [presentation-generator.scraper [wiki :as wk] [image-extractor :as img-ext]]
            [presentation-generator.convertor.wiki-presentation :as wp]
            [presentation-generator.runner.insertdb :as dbs]))


(defstruct intermediate-article :title :summary :image )
(defn- parse-node [wiki-node]
  (println "parsing node" wiki-node)
  (let [title (:title wiki-node)
        body (:body wiki-node)
        images (map #(first (sort-by :weight (img-ext/extract-images (:urls %) (:content %)))) body)]
    (map #(struct intermediate-article title (:content %1) (:url %2))  body  images)))

(defn convert-wiki-article [wiki-title]
  (let [parsed-wiki (wk/parse-page wiki-title)
        slides (flatten (pmap parse-node parsed-wiki))
        presentation (wp/get-presentation-from-wiki-data-nlp slides)]
    (dbs/insert-presentations presentation)))