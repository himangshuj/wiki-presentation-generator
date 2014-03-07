(ns presentation-generator.nlp-runner
  (:require [presentation-generator.scraper [wiki :as wk] [image-extractor :as img-ext]]))

(defn convert-wiki-article [wiki-title]
  (let [parsed-wiki (wk/parse-page wiki-title)]))