(ns
  ^{:author "Himangshu",
    :doc "scrapes a url and other html links in the page and returns html"}
  presentation-generator.scraper.html-fetcher
  (:import [com.gargoylesoftware.htmlunit WebClient SilentCssErrorHandler WebClientOptions]
           [java.util.logging Logger Level]
           [java.io StringReader])

  (:require [net.cgrand.enlive-html :as html]))

(defn- set-logger []
  (let [log (Logger/getLogger "com.gargoylesoftware.htmlunit")]
    (. log (setLevel Level/SEVERE))))

(def ^{:private true} silentCss (SilentCssErrorHandler.))
(set-logger)


(defn- get-html-page "given a url fetches the htmlpage" [url]
  (let [wc (WebClient.)
        wc-options (. wc (getOptions))]
    (. wc-options (setJavaScriptEnabled false))
    (. wc-options (setUseInsecureSSL true))
    (. wc-options (setThrowExceptionOnFailingStatusCode false))
    (. wc (setCssErrorHandler silentCss))
    (. wc (getPage url))))
(defrecord ScrapeData [targetHtml controlSet])
(defn- get-html-page-as-node [url]
  (html/html-resource (StringReader. (. (get-html-page url) (asXml)))))

(defn scrape-data "given a base url fetches the contents of that url and 1 level links" [^String base-url]
  (let [base-page (get-html-page base-url)
        anchors (. base-page (getAnchors))
        anchors# (map #(. % (getHrefAttribute)) anchors)
        anchors# (map #(. base-page (getFullyQualifiedUrl %)  ) anchors#)
        base-host (. (. base-page (getUrl)) (getHost))
        anchors# (filter #(= base-host (. % (getHost))) anchors#)]
    (ScrapeData. (html/html-resource (StringReader. (. base-page (asXml)))) (pmap get-html-page-as-node anchors#) )))