(ns presentation-generator.core
  (:gen-class )
  (:require [monger.core :as mg]
            [presentation-generator.model.presentation :as presentation]
            [presentation-generator.runner.runner :as runner]
            [presentation-generator.runner.wiki-runner :as wiki]
            [presentation-generator.runner.nlp-runner :as nlp]))

(defn -main
  "initialize connection"
  [& args]
  (let [uri (get (System/getenv) "MONGODB_URI" "mongodb://sokratik-trial:sokratik-trial@127.0.0.1:10000/sokratik-trial")
        location (get (System/getenv) "zipped_news" "/tmp/zipped_news")]
    (monger.core/connect-via-uri! uri)
    (doall (map nlp/convert-wiki-article ["International_Space_Station" "Aaron_Swartz" "San_Diego_International_Airport"
                                   "Early_Christianity" "Kelo_v._City_of_New_London" "Doom_(video_game)" "South_Georgia_and_the_South_Sandwich_Islands"
                                   "1999_Rugby_World_Cup"]))))
