(ns ^{:doc "scrapes through websites and extracts the relevant image"}
  presentation-generator.scraper.image-extractor
  (:require [net.cgrand.enlive-html :as html]
            [opennlp.nlp :as nlp]
            [clojure.string :as string]
            [clojure.set :as clj-set]
            [opennlp.tools.filters :as filters]))

(System/setProperty "sun.net.client.defaultConnectTimeout" "5000")
(System/setProperty "sun.net.client.defaultReadTimeout" "5000")


(def ^{:private true} tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def ^{:private true} pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))

(defn- parse-wiki-image [wiki-image-tuple key-tokens key-proper-tokens]
  (let [image-tokens (-> wiki-image-tuple :text string/lower-case tokenize pos-tag filters/nouns)
        image-tokens# (set (map first image-tokens))
        image-tokens-weight (- 0 (count (clj-set/intersection image-tokens# key-tokens))
                              (* 2 (count (clj-set/intersection image-tokens# key-proper-tokens))))]
    (merge wiki-image-tuple {:weight image-tokens-weight})))
(defn- construct-absolute-url [^java.net.URL url relative-url]
  (cond
    (. (string/lower-case relative-url) (startsWith "http")) relative-url
    (. (string/lower-case relative-url) (startsWith "//")) relative-url
    (. (string/lower-case relative-url) (startsWith "/")) (str "http://" (. url (getHost)) relative-url)
    :else (str url "/" relative-url)))

(defn- get-wiki-image [url key-tokens key-proper-tokens]
  (println "extracting wiki image" url)
  (try (let [url# (java.net.URL. url)
             nodes (html/html-resource url#)
             thumbnails (html/select nodes [[:div (html/attr= :class "thumbcaption")]])
             thumbnails# (map #(hash-map :href (-> % :content second :content first :attrs :href ) :text (html/text %)) thumbnails)
             thumbnails# (remove #(-> % :href nil?) thumbnails#)
             thumbnails# (map #(parse-wiki-image % key-tokens key-proper-tokens) thumbnails#)
             thumbnails# (map #(merge % {:href (construct-absolute-url url# (:href %))}) thumbnails#)
             image-url (-> thumbnails# first :href )
             image-nodes (html/html-resource (java.net.URL. image-url))
             image-node (html/select image-nodes [[:div (html/attr= :class "fullMedia")]])
             image-link (html/select image-node [[:a (html/attr= :class "internal")]])
             image-link# {:url (-> image-link first :attrs :href ) :weight (-> thumbnails# first :weight ) :source url}]
         image-link#) (catch Throwable _ nil)))


(defn- get-image [url key-tokens key-proper-tokens]
  (println "extracting image" url)
  (try (let [url# (java.net.URL. url)
             nodes (html/html-resource url#)
             img-nodes (html/select nodes [:img ])
             img-data (map #(select-keys (:attrs %) [:src :alt ]) img-nodes)
             img-data# (filter #(#{"jpg","png","gif"} (apply str (take-last 3 (:src %)))) img-data)
             img-data# (map #(hash-map :src (:src %)
                               :alt (let [image-tokens
                                          (set
                                            (string/split
                                              (if-let [alt (:alt %)]
                                                (string/lower-case alt) "") #"\s"))]
                                      (- 0
                                        (count
                                          (clj-set/intersection
                                            image-tokens
                                            key-tokens))
                                        (* 2 (count
                                               (clj-set/intersection
                                                 image-tokens
                                                 key-proper-tokens)))))) img-data#)
             img-data# (first (sort-by :alt img-data#))]
         (if img-data#
           {:url (construct-absolute-url url# (:src img-data#)) :weight (:alt img-data#) :source url})) (catch Throwable t nil)))
(defn- extract-image [url-node key-tokens key-proper-tokens]
  ((if (= (:type url-node) :wiki )
     get-wiki-image
     get-image) (:url url-node) key-tokens key-proper-tokens))

(defn extract-images [url-nodes key-string]
  (let [key-tokens (filters/nouns (pos-tag (tokenize (string/lower-case key-string))))
        key-tokens# (set (map first key-tokens))
        key-proper-tokens (filters/proper-nouns (pos-tag (tokenize  key-string)))
        key-proper-tokens# (set (map #(-> % first string/lower-case) key-proper-tokens))
        images (pmap #(extract-image % key-tokens# key-proper-tokens#) url-nodes)]
    (println "extracting image for " key-string)
    (remove nil? images)))