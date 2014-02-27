(ns presentation-generator.fetch-file
  (:require [clojure.test :refer :all ]
            [presentation-generator.runner.fetch-files :as ff]))
(deftest a-test
  (testing "list files in directory"
    (is (= "/home/vulcan/work/sokratik/presentation-generator/resources/aap.json" (. (first (ff/list-unprocessed-files "/home/vulcan/work/sokratik/presentation-generator/resources")) getPath)))))

