(ns cljbot.commands.doc
  (:gen-class))

(defn- fn-doc-str [s]
  "Retrieves a Clojure function's documentation"
  (load-string (str "(:doc (meta #'" s "))")))

(defn- doc-discord [s]
  "Formatted output to be sent as a Discord message"
  (str "```css\n" s "\n```"))

(defn doc-command! [s]
  (doc-discord (fn-doc-str s)))
