(ns presentation-generator.runner.fetch-files
  (:require [clojure.java.io :as io]))

(defn list-unprocessed-files [unprocessed-directory]
  (let [file (io/file unprocessed-directory)
        files (file-seq file)
        files# (remove #(. % (isDirectory)) files)]
    files#))