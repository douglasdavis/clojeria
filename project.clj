(defproject clojeria "0.1"
  :description "Play Loteria"
  :url "https://github.com/douglasdavis/clojeria"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "1.0.0"]]
  :main ^:skip-aot clojeria.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
