(defproject presentation-generator "0.1.0-SNAPSHOT"
  :description "Given a sequence of images and text it creates a sokratik presentation"
  :url "http://lab.sokratik.com"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.novemberain/monger "1.7.0"]
                 [org.clojure/data.json "0.2.4"]
                 [clj-time "0.6.0"]
                 [enlive "1.1.5"]
                 [net.sourceforge.htmlunit/htmlunit "2.14"]]
  :main ^:skip-aot presentation-generator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
