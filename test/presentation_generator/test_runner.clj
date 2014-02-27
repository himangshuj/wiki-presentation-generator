(ns presentation-generator.test-runner
  (:require [clojure.test :refer :all ]
            [presentation-generator.convertor.zippednews-articles :as article]
            [presentation-generator.runner.fetch-files :as ff]
            [presentation-generator.convertor.articles-presentation :as art_pres]
            [presentation-generator.runner.insertdb :as dbs]))
(def files (ff/list-unprocessed-files "/home/vulcan/work/sokratik/presentation-generator/resources") )
(def articles (pmap article/read-articles-from-file files ))
(def presentations (pmap art_pres/get-presentation-from-article-list articles))
(let [uri (get (System/getenv) "MONGODB_URI" "mongodb://sokratik-trial:sokratik-trial@127.0.0.1:10000/sokratik-trial")]
  (monger.core/connect-via-uri! uri))
(deftest insert-test
  (testing "insert to db"
    (is (= "Aam Aadmi Party, India" (pmap dbs/insert-presentations presentations)))))

