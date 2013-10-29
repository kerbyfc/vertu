(ns vertu.core
  (:use
   [reval.core :as reval]
   [clojure.string :only [trim-newline]]
   [clojure.java.io :as io]
   [clerk.core :as clerk])
  (:import
   [org.rauschig.jarchivelib ArchiverFactory])
    (:gen-class))

(def root (reval/app-root 'vertu.core))
(def vertx-dir "vert.x-2.0.2-final")
(def vertx-zip (str vertx-dir ".zip"))
(def vertx-bin (reval/file-path [root vertx-dir "bin"]))
(def vertx-cli (reval/file-path [vertx-bin "vertx"]))
(def env-path  (str (trim-newline (System/getenv "PATH")) ":" vertx-bin))

(defn load-resource
  [path]
  (let [rsc-name path
        thr (Thread/currentThread)
        ldr (.getContextClassLoader thr)]
    (.getResourceAsStream ldr rsc-name)))

(defn deploy
  []
  (def target (reval/file-path [root vertx-zip]))
  (defn copy-resource [source-path dest-path]
    (io/copy (load-resource source-path) (io/file dest-path)))
  (copy-resource vertx-zip target)
  (.extract (ArchiverFactory/createArchiver "zip") (io/file target) (io/file root))
  (exec "chmod" "+x" vertx-cli))

(defn vtx-exec
  [command target]
  (exec "/bin/bash" "-c" (str "vertx " command " " target) :PATH env-path))

(defmacro vtx-run
  [target]
  `(vtx-exec "run" ~target))

(defmacro vtx-runmod
  [target]
  `(vtx-exec "runmod" ~target))
