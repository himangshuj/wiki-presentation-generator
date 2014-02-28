(ns presentation-generator.runner.runner
  (:require [presentation-generator.convertor.zippednews-articles :as article]
            [presentation-generator.runner.fetch-files :as ff]
            [presentation-generator.convertor.articles-presentation :as art_pres]
            [presentation-generator.runner.insertdb :as dbs]))

(defn generate-presentation [presentation-location]
  (let [files (ff/list-unprocessed-files presentation-location)
        articles (pmap article/read-articles-from-file files)
        presentations (pmap art_pres/get-presentation-from-article-list articles)]
    (doall (pmap dbs/insert-presentations presentations))))