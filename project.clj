(defproject vertu "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["java.net" "http://download.java.net/maven/2"]
                 ["sonatype" "https://oss.sonatype.org/content/repositories/releases/"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.rauschig/jarchivelib "0.3.0"]
                 [reval "0.1.2-SNAPSHOT"]
                 [clerk "0.1.3-SNAPSHOT"]]
  :main vertu.core
  :profiles {:uberjar {:aot :all}})
