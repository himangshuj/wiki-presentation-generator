(ns presentation-generator.model.zippednews)
(defrecord TimeLine summary articles)
(defn get-timeline "given json data returns timeline object" [summary articles]
  (Timeline. summary articles))