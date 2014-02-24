(ns presentation-generator.model.presentation
  (:import [java.util Date]))
(defrecord Presentation [upDatedOn presentationData script recordingStarted audioRecorded authors] )
(defn get-new-presentation []
  (Presentation. (Date.) {} {} (Date.) "mute" {:username "auto-sokratik-creator"}))
