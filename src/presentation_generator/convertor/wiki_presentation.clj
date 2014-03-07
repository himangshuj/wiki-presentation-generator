(ns presentation-generator.convertor.wiki-presentation
  (:require [presentation-generator.model.presentation :as presentation]))

(defn get-presentation-data-from-slide [raw-slide]
  (let [title (:title raw-slide)
        media (:image raw-slide)
        summary (:summary raw-slide)
        keyVals {:title title :image1 media :text1 summary}]
    (presentation/get-presentation-data keyVals "fullImageText")))

(defn get-presentation-from-wiki-data [wiki-data]
  (let [presentation-Data (map get-presentation-data-from-slide (:slides wiki-data))]
    (println "converting to presentation")
    (presentation/get-new-presentation presentation-Data (:summary wiki-data))))

(defn get-presentation-from-wiki-data-nlp [wiki-data]
  (let [presentation-Data (map get-presentation-data-from-slide  wiki-data)]
    (println "converting to presentation")
    (presentation/get-new-presentation presentation-Data (:summary wiki-data))))