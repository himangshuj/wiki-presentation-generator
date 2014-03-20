(ns presentation-generator.zippednews-convert-test
  (:require [clojure.test :refer :all ]
            [presentation-generator.convertor.zippednews-articles :as article]))

(def sample-article (article/read-articles-from-file "/home/vulcan/work/sokratik/presentation-generator/resources/aap.json"))
(deftest a-test
  (testing "FIXME, I fail."
    (is (= "Aam Aadmi Party, India" (:summary (first sample-article))))))

