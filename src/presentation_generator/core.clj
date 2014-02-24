(ns presentation-generator.core
  (:gen-class )
  (:require [monger.core :as mg]
            [presentation-generator.model.presentation :as presentation]))

(defn -main
  "initialize connection"
  [& args]
  (let [uri (get (System/getenv) "MONGODB_URI" "mongodb://sokratik-trial:sokratik-trial@127.0.0.1:10000/sokratik-trial")]
    (monger.core/connect-via-uri! uri)))
