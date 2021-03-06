(ns presentation-generator.model.presentation
  (:require [clj-time [core :as time] [coerce :as coerce]])
  (:import java.util.UUID))

(defrecord Presentation [upDatedOn presentationData script recordingStarted audioRecorded authors audioId summary fallBackScript])
(defrecord PresentationData [keyVals templateName])
(defn get-new-presentation ([presentationData summary fallBackScript]
  (Presentation. (coerce/to-date (time/now))
    presentationData {} (coerce/to-date (time/now))
    "mute" [{:username "autogenerated-nlp@sokratik.com"}]
    (str (UUID/randomUUID)) summary fallBackScript))
  ([presentationData summary]
    (get-new-presentation presentationData summary []))
  ([presentationData]
    (get-new-presentation presentationData "")))

(defn- get-fallback-script-for-keyVals [keyVals page startTime]
  (cons {:fnName "changeState" :args {:subState "active", :params {:page page}},
         :actionInitiated (+ startTime 40000),
         :module "dialogue"}
    (map #(hash-map
            :fnName "makeVisible"
            :actionInitiated %2
            :args {:index %3}
            :module "dialogue") keyVals (iterate  #(+ 10000  %) startTime) (iterate inc 0))))
(defn get-fallback-script [presentationData]
  (flatten
    (map  get-fallback-script-for-keyVals (:keyVals presentationData) (iterate inc 0) (iterate  #(+ 50000  %) 500000))) )

(defn get-presentation-data [keyVals templateName]
  (PresentationData. keyVals templateName))

