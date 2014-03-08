(ns presentation-generator.scraper.wiki "parses a wikipedia page and gives a list of urls to scrape"
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(defn fetch-url [^String url]
  (html/html-resource (java.net.URL. url)))

(defn- get-reference [node nodes]
  (if-let [id (-> node :content first :attrs :href )]
    (html/select nodes [[:li (html/attr= :id (string/replace id "#" ""))]])))
(defrecord WikiResource [link title summary id])
(defn- extract-data "given a reference node, extracts link title and summary" [reference]
  (let [node# (-> reference :content last :content first :content )
        summary (if-let [data (html/text (last node#))] (string/replace data "\"" ""))
        titlenode (first (html/select node# [[:a (html/attr= :class "external text")]]))
        title (if-let [data (-> titlenode html/text)] (string/replace data "\"" ""))
        link (-> titlenode :attrs :href )
        id (-> reference :attrs :id )
        id# (-> (string/split id #"-") last read-string)]
    (WikiResource. link title summary id#)))

(defn fetch-references "given a wiki page fetches a list of non duplicate references" [^String wiki-title]
  (let [nodes (fetch-url (str "http://en.wikipedia.org/wiki/" wiki-title))
        references (html/select nodes [[(html/attr-starts :id "cite")
                                        (html/attr= :class "reference")
                                        (html/but (html/left :sup ))]])
        references# (pmap #(get-reference % nodes) references)
        references# (flatten references#)
        references# (map extract-data references#)
        references# (filter :link references#)]
    (apply sorted-set-by #(< (:id %1) (:id %2)) references#)))
(defn- get-reference-link [nodes href]
  (let [li-node (first (html/select nodes [[:li (html/attr= :id (string/replace href #"#" ""))]]))
        href-node (first (html/select li-node [[:a (html/attr= :class "external text")]]))]
    (-> href-node :attrs :href)))
(defn- expan-url [url-node nodes url]
  (let [href (-> url-node :attrs :href )
        type (if (. href (startsWith "/wiki")) :wiki :external )
        href# (if (= type :wiki )
                (str "http://" (. url (getHost)) href)
                (get-reference-link nodes href))]
    {:url href# :type type}))
(defn parse-urls [url-str nodes url]
  (map #(expan-url % nodes url) url-str))
(defn- process-wiki-node-fn-generator [url nodes]
  (fn [wiki-node]
    (let [title (-> wiki-node first first html/text)
          body (-> wiki-node second)
          body# (map #(hash-map :urls (parse-urls (html/select % [:a ]) nodes url)
                        :content (string/replace (html/text %) #"\[\d+\]|\"" "")) body)]
      {:title title :body body#})))

(defn parse-page "given a wiki page fetches the paragraph data for english language" [^String wiki-title]
  (let [url (java.net.URL. (str "http://en.wikipedia.org/wiki/" wiki-title))
        nodes (html/html-resource url)
        process-wiki-node-fn (process-wiki-node-fn-generator url nodes)
        nodes# (html/select nodes #{[#{:h3 :h2 } [[:span (html/attr= :class "mw-headline")]]] [:p ]})
        nodes# (partition-by #(= (-> % :attrs :class ) "mw-headline") nodes#)
        nodes# (rest nodes#)
        nodes# (partition 2 nodes#)
        nodes# (map process-wiki-node-fn nodes#)
        nodes# (remove #(-> % :body first :content string/blank?) nodes#)]
    nodes#))