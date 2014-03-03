(ns presentation-generator.html-fetcher
  (:require [clojure.test :refer :all ]
            [presentation-generator.scraper.html-fetcher :as hf]))
(deftest a-test
  (testing "fetching Aron Schwartz"
    (is (= 1 (hf/scrape-data "http://www.haaretz.com/jewish-world/jewish-world-news/repairing-the-world-was-aaron-swartz-s-calling.premium-1.509494")))))