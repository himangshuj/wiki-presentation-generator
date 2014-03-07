(ns presentation-generator.wiki
  (:require [clojure.test :refer :all ]
            [presentation-generator.scraper.wiki :as wiki]))

(deftest fetching-wiki
  (testing "fetching Aaron Swartz"
    (is (= 1 (:id (first (wiki/fetch-references "Aaron_Swartz")))))))
(deftest understanding-wiki
  (testing "parsing Aaron Swartz"
    (is (= "Life and works" (:title (first (wiki/parse-page "Aaron_Swartz" )))))))