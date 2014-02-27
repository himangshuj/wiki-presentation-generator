(ns presentation-generator.model.presentation
  (:require [clj-time [core :as time] [coerce :as coerce]]))

(defrecord Presentation [upDatedOn presentationData script recordingStarted audioRecorded authors])
(defrecord PresentationData [keyVals templateName])
(defn get-new-presentation [presentationData]
  (Presentation. (coerce/to-date (time/now))
    presentationData {} (coerce/to-date (time/now))
    "mute" [{:username "autogenerated@sokratik.com"}]))

(defn get-presentation-data [keyVals templateName]
  (PresentationData. keyVals templateName))

