(ns presentation-generator.article-presentation-convert
  (:require [clojure.test :refer :all ]
            [presentation-generator.convertor.articles-presentation :as art_pres]
            [presentation-generator.convertor.zippednews-articles :as article]))

(deftest template-name-test
  (testing "templateName for article"
    (is (= "fullImageText" (:templateName (art_pres/get-presentation-data-from-article {:summary "summary" :media "media"}))))))
(def sample-article (article/read-articles-from-file "/home/vulcan/work/sokratik/presentation-generator/resources/aap.json"))
(deftest full-presentation-test
  (testing "author for article"
    (is (= "autogenerated@sokratik.com" (->  (art_pres/get-presentation-from-article-list sample-article) :authors first :username)))))