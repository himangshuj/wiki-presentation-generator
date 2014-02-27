(ns presentation-generator.convertor.zippednews-articles
  (:require [clojure.data.json :as json]
            [clj-time.format :as fmt]
            [presentation-generator.model.articles :as article]))

(def date_format (fmt/formatter "yyy,M,dd"))
(defn- flatten-article [article]
  (article/get-article (fmt/parse date_format ( article :startDate )) (:headline article) (-> article :asset :media)))

(defn read-articles-from-file "given an absoulte filename this returns the json" [file-name ]
  (let [json-map (json/read-str (slurp file-name) :key-fn keyword)
       summary (:text json-map)
       articles (:date json-map)
       articles# (map flatten-article articles)
       articles# (filter :media articles#)
       articles# (sort-by :published articles#)]
  articles#))