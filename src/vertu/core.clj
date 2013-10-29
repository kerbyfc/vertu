(ns vertu.core
  (:use
   [reval.core :as reval]
   [clojure.string :only [trim-newline]]
   [clojure.java.io :as io]
   [clerk.core :as clerk])
  (:import
   [org.rauschig.jarchivelib ArchiverFactory])
    (:gen-class))

(declare evn-path)

(def root )
(def vertx-dir "vert.x-2.0.2-final")
(def vertx-bin (reval/file-path [root vertx-dir "bin"]))
(def vertx-cli (reval/file-path [vertx-bin "vertx"]))
(def env-path  (str (trim-newline (System/getenv "PATH")) ":" vertx-bin))

(defn read-resource
  [res-path]
  (let [thr (Thread/currentThread)
        ldr (.getContextClassLoader thr)]
    (.getResourceAsStream ldr res-path)))

(defn copy-resource [res-path dest-path]
    (io/copy (read-resource res-path) (io/file dest-path)))

(defn deploy
  []
  (let [root (reval/app-root 'vertu.core)
        dir "vert.x-2.0.2-final"
        zip (str vertx-dir ".zip")
        bin (reval/file-path [root dir "bin"])
        target (reval/file-path [root zip])
        cli (reval/file-path [bin "vertx"])
        ]
    (copy-resource zip target)
    (def env-path (str (trim-newline (System/getenv "PATH")) ":" bin))
    (.extract (ArchiverFactory/createArchiver "zip") (io/file target) (io/file root))
    (exec "chmod" "+x" cli)))

(defn vtx-exec
  [command target]
  (exec "/bin/bash" "-c" (str "vertx " command " " target) :PATH env-path))

(defmacro vtx-run
  [target]
  `(vtx-exec "run" ~target))

(defmacro vtx-runmod
  [target]
  `(vtx-exec "runmod" ~target))
