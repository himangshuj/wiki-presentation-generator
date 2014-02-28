(ns presentation-generator.runner.insertdb
  (:require [monger.collection :as mc]))

(defn insert-presentations [presentation]
  (mc/insert-and-return "presentations" presentation))
