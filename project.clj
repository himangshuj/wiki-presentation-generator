(defproject presentation-generator "0.1.0-SNAPSHOT"
  :description "Given a sequence of images and text it creates a sokratik presentation"
  :url "http://lab.sokratik.com"
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main ^:skip-aot presentation-generator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
