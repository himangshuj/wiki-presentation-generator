(ns presentation-generator.core
  (:gen-class )
  (:require [monger.core :as mg]
            [presentation-generator.model.presentation :as presentation]
            [presentation-generator.runner.runner :as runner]
            [presentation-generator.runner.wiki-runner :as wiki]))

(defn -main
  "initialize connection"
  [& args]
  (let [uri (get (System/getenv) "MONGODB_URI" "mongodb://sokratik-trial:sokratik-trial@127.0.0.1:10000/sokratik-trial")
        location (get (System/getenv) "zipped_news" "/tmp/zipped_news")]
    (monger.core/connect-via-uri! uri)
    (wiki/generate-presentation ["Digit_ratio" "Federal_Emergency_Management_Agency" "Serie_A" "University_of_Vermont"
])))
