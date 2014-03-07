(ns presentation-generator.nlp-runner
  (:require [clojure.test :refer :all ]
            [presentation-generator.runner.nlp-runner :as nlr]
            [presentation-generator.convertor.wiki-presentation :as wp]
            [presentation-generator.runner.insertdb :as dbs]))

(def a (nlr/convert-wiki-article "Aaron_Swartz"))

(println (last a))
(println a)
(deftest aaron-parse
  (testing "Aaron "
    (is (= 1 1))))