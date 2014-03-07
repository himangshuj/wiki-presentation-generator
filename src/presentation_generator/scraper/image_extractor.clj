(ns ^{:doc "scrapes through websites and extracts the relevant image"}
  presentation-generator.scraper.image-extractor
  (:require [net.cgrand.enlive-html :as html]
            [opennlp.nlp :as nlp]
            [clojure.string :as string]
            [clojure.set :as clj-set]
            [opennlp.tools.filters :as filters]))

(def ^{:private true} tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def ^{:private true} pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))

(defn- parse-wiki-image [wiki-image-tuple key-tokens]
  (let [image-tokens (-> wiki-image-tuple :text string/lower-case tokenize pos-tag filters/nouns)
        image-tokens# (set (map first image-tokens))
        image-tokens-weight (- (count (clj-set/intersection image-tokens# key-tokens)))]
    (merge wiki-image-tuple {:weight image-tokens-weight})))
(defn- construct-absolute-url [^java.net.URL url relative-url]
  (cond
    (. (string/lower-case relative-url) (startsWith "http")) relative-url
    (. (string/lower-case relative-url) (startsWith "//")) relative-url
    (. (string/lower-case relative-url) (startsWith "/")) (str "http://" (. url (getHost)) relative-url)
    :else (str url "/" relative-url)))

(defn- get-wiki-image [url key-tokens]
  (let [url# (java.net.URL. url)
        nodes (html/html-resource url#)
        thumbnails (html/select nodes [[:div (html/attr= :class "thumbcaption")]])
        thumbnails# (map #(hash-map :href (-> % :content second :content first :attrs :href ) :text (html/text %)) thumbnails)
        thumbnails# (remove #(-> % :href nil?) thumbnails#)
        thumbnails# (map #(parse-wiki-image % key-tokens) thumbnails#)
        thumbnails# (map #(merge % {:href (construct-absolute-url url# (:href %))}) thumbnails#)
        image-url (-> thumbnails# first :href )
        image-nodes (html/html-resource (java.net.URL. image-url))
        image-node (html/select image-nodes [[:div (html/attr= :class "fullMedia")]])
        image-link (html/select image-node [[:a (html/attr= :class "internal")]])
        image-link# {:url (-> image-link first :attrs :href ) :weight (-> thumbnails# first :weight )}]
    image-link#))


(defn- get-image [url key-tokens]
  (let [url# (java.net.URL. url)
        nodes (html/html-resource url#)
        img-nodes (html/select nodes [:img ])
        img-data (map #(select-keys (:attrs %) [:src :alt ]) img-nodes)
        img-data# (filter #(#{"jpg","png","gif"} (apply str (take-last 3 (:src %)))) img-data)
        img-data# (map #(hash-map :src (:src %)
                          :alt (-
                                 (count
                                   (clj-set/intersection
                                     (set (string/split
                                            (if-let [alt (:alt %)]
                                              (string/lower-case alt) "")
                                            #"\s")) key-tokens)))) img-data#)
        img-data# (first (sort-by :alt img-data#))]
    (if img-data#
      {:url (construct-absolute-url url# (:src img-data#)) :weight (:alt img-data#)})))
(defn- extract-image [url-node key-tokens]
  ((if (= (:type url-node) :wiki )
     get-wiki-image
     get-image) (:url url-node) key-tokens))

(defn extract-images [url-nodes key-string]
  (let [key-tokens (filters/nouns (pos-tag (tokenize (string/lower-case key-string))))
        key-tokens# (set (map first key-tokens))]
    (pmap #(extract-image % key-tokens#) url-nodes)))