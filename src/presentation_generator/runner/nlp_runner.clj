(ns presentation-generator.runner.nlp-runner
  (:require [presentation-generator.scraper [wiki :as wk] [image-extractor :as img-ext]]
            [presentation-generator.convertor.wiki-presentation :as wp]
            [presentation-generator.runner.insertdb :as dbs]
            [clojure.string :as string]))


(defstruct intermediate-article :title :summary :image :weight )
(defn- parse-node [wiki-node]
  (println "parsing node" wiki-node)
  (let [title (:title wiki-node)
        body (:body wiki-node)
        images (map #(first (sort-by :weight (img-ext/extract-images (:urls %) (:content %)))) body)]
    (map #(struct intermediate-article title (:content %1) (:url %2) (:weight %2)) body images)))

(defn convert-wiki-article [wiki-title]
  (let [parsed-wiki (wk/parse-page wiki-title)
        title-slide (:title parsed-wiki)
        _ (println "title slide" title-slide)
        slides (flatten (pmap parse-node (:nodes parsed-wiki)))
        _ (println "Raw slides " (count slides))

        slides# (filter :image slides)
        slides# (filter #(< (:weight %) 0) slides#)
        _ (println "non empty  and related slides " (count slides#))

        ]
    (if title-slide
      (let
        [title-slide# (struct intermediate-article (:title title-slide) "created by sokratik" (:image title-slide))
         slides# (filter #(try (let [image-url (:image %)
                                     image-url# (string/replace image-url #"^//" "http://")
                                     url (java.net.URL. image-url#)]
                                 (with-open [stream (. url (openStream))]
                                   (> (. stream (available)) 500))) (catch Throwable _ false)) slides#)
         _ (println "valid slides " (count slides#))
         slides# (cons title-slide# slides#)
         _ (println "title " title-slide#)
         presentation (wp/get-presentation-from-wiki-data-nlp slides#)]
        (dbs/insert-presentations presentation)))))