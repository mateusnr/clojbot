(defproject cljbot "0.1.0-SNAPSHOT"
  :description "A Clojure related discord bot"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"] [org.suskalo/discljord "0.1.7"]]
  :main ^:skip-aot cljbot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
