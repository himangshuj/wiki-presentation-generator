(ns
  ^{:author "Himangshu",
    :doc "scrapes a url and other html links in the page and returns html"}
  presentation-generator.scraper.html-fetcher
  (:import [com.gargoylesoftware.htmlunit WebClient SilentCssErrorHandler WebClientOptions]
           [com.gargoylesoftware.htmlunit.html HtmlPage]
           [java.util.logging Logger Level]
           [java.io StringReader])

  (:require [net.cgrand.enlive-html :as html]))

(defn- set-logger []
  (let [log (Logger/getLogger "com.gargoylesoftware.htmlunit")]
    (. log (setLevel Level/INFO))))

(def ^{:private true} silentCss (SilentCssErrorHandler.))
(set-logger)


(defn- get-html-page "given a url fetches the htmlpage" [url]
  (let [wc (WebClient.)
        wc-options (. wc (getOptions))]
    (. wc-options (setJavaScriptEnabled false))
    (. wc-options (setUseInsecureSSL true))
    (. wc-options (setTimeout 30000))
    (. wc-options (setThrowExceptionOnFailingStatusCode false))
    (. wc (setCssErrorHandler silentCss))
    (try (. wc (getPage url)) (catch Throwable _ nil))))
(defrecord ScrapeData [targetHtml controlSet page])
(defn- get-html-page-as-node [url]
  (let [page (get-html-page url)]
    (if (instance? HtmlPage page) (html/html-resource (StringReader. (. page (asXml)))))))

(defn scrape-data "given a base url fetches the contents of that url and 1 level links" [^String base-url-str]
  (let [base-page (get-html-page base-url-str)]
    (if (instance? HtmlPage base-page)
      (let [anchors (. base-page (getAnchors))
            anchors# (map #(. % (getHrefAttribute)) anchors)
            anchors# (map #(. base-page (getFullyQualifiedUrl %)) anchors#)
            base-url (. base-page (getUrl))
            base-host (. base-url (getHost))
            anchors# (filter #(= base-host (. % (getHost))) anchors#)
            anchors# (distinct anchors#)
            anchors# (remove #(= base-url %) anchors#)
            anchors# (remove #(. (. % (toString)) (contains "#")) anchors#)
            otherPages (pmap get-html-page-as-node anchors#)
            otherPages# (remove nil? otherPages)]
        (ScrapeData. (html/html-resource (StringReader. (. base-page (asXml)))) otherPages# base-page)))))