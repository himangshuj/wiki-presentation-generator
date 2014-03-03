(ns presentation-generator.scraper.wiki "parses a wikipedia page and gives a list of urls to scrape"
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(defn- fetch-url [^String url]
  (html/html-resource (java.net.URL. url)))

(defn- get-reference [node nodes]
  (if-let [id (-> node :content first :attrs :href )]
    (html/select nodes [[:li (html/attr= :id (string/replace id "#" ""))]])))
(defrecord WikiResource [link title summary id])
(defn extract-data [reference]
  (let [node# (-> reference :content last :content first :content )
        summary (if-let [data (last node#)] (string/replace data "\"" ""))
        titlenode (first (html/select node# [[:a (html/attr= :class "external text")]]))
        title (if-let [data (-> titlenode html/text)] (string/replace data "\"" ""))
        link (-> titlenode :attrs :href )
        id (-> reference :attrs :id )
        id# (-> (string/split id #"-") last read-string)]
    (WikiResource. link title summary id#)))

(defn fetch-references "given a wiki page fetches a list of non duplicate references" [^String url]
  (let [nodes (fetch-url url)
        references (html/select nodes [[(html/attr-starts :id "cite")
                                        (html/attr= :class "reference")
                                        (html/but (html/left :sup ))]])
        references# (pmap #(get-reference % nodes) references)
        references# (flatten references#)
        references# (map extract-data references#)
        references# (filter :link references#)]
    (apply sorted-set-by #(< (:id %1) (:id %2)) references#)))