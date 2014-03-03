(ns presentation-generator.wiki
  (:require [clojure.test :refer :all ]
            [presentation-generator.scraper.wiki :as wiki]))
(deftest fetching-wiki
(testing "fetching Aron Schwartz"
  (is (= 1 (:id (first (wiki/fetch-references "Aaron_Swartz")))))))