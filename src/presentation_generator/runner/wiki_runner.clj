(ns presentation-generator.runner.wiki-runner
  (:require [presentation-generator.scraper [wiki :as wiki] [html-fetcher :as hf]]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.set :as set-lib]))

(defn- extract-image-src [img-nodes]
  (let [srcs (map #(-> % :attrs :src ) (html/select img-nodes [:img ]))
        srcs# (filter #(#{"jpg"} (apply str (take-last 3 %))) srcs)]
    (apply hash-set srcs#)))
(defn- extract-image [url]
  (if-let [sd (hf/scrape-data url)]
    (let [targets (extract-image-src (:targetHtml sd))
          controls (pmap extract-image-src (take 10 (:controlSet sd)))
          image-url (first (apply set-lib/difference targets controls)) ;;todo first is not best solution need to do based on image size
          image-url# (. (:page sd) (getFullyQualifiedUrl image-url))]
      (str image-url#))))
(defstruct raw-presentation :summary :slides )
(defstruct slide :title :summary :image :id )
(defn- create-slide [wiki-reference]
  (println "creating slide for " wiki-reference)
  (try (if-let [image (extract-image (:link wiki-reference))]

    (struct slide (:title wiki-reference) (:summary wiki-reference) image (:id wiki-reference)))(catch Throwable _ (println wiki-reference))))
(defn extract-raw-presentation "given wiki title name creates a presentation" [page-title]
  (let [references (wiki/fetch-references page-title)
        slides (pmap create-slide references)
        slides# (remove nil? slides)
        slides# (sort-by :id slides#)]
    (struct raw-presentation page-title slides#)))

