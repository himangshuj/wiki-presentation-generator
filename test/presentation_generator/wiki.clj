(ns presentation-generator.wiki
  (:require [clojure.test :refer :all ]
            [presentation-generator.scraper.wiki :as wiki]))

(deftest fetching-wiki
  (testing "fetching Aaron Swartz"
    (is (= 1 (:id (first (wiki/fetch-references "Aaron_Swartz")))))))

(def parsed-wiki (wiki/parse-page "Aaron_Swartz" ))

(deftest understanding-wiki
  (testing "parsing Aaron Swartz"
    (is (= "Life and works" (:title (first (:nodes parsed-wiki) ))))
    (is (= "Aaron Swartz" (:title  (:title parsed-wiki) )))))

(def parsed-wiki-buch (wiki/parse-page "History_of_Bucharest" ))

(deftest understanding-wiki-bucharest
  (testing "parsing Bucharest"
    (is (= "Ancient times" (:title (first (:nodes parsed-wiki-buch) ))))
    (is (= "History of Bucharest" (:title  (:title parsed-wiki-buch) )))))