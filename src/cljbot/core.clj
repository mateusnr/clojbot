(ns cljbot.core
  (:gen-class)
  (:require [clojure.string :as s]
            [clojure.core.async :as a]
            [discljord.messaging :as m]
            [discljord.events :as e]
            [discljord.connections :as c]))

(def token (s/trim-newline (slurp "token.txt")))

(def state (atom nil))

(defonce prefix "!")

(defn- fn-doc-str [s]
  "Retrieves a Clojure function's documentation"
  (load-string (str "(:doc (meta #'" s "))")))

(defn- doc-discord [s]
  "Formatted output to be sent as a Discord message"
  (str "```css\n" s "\n```"))

(defn doc-command! [s]
  (doc-discord (fn-doc-str s)))

(defmulti handle-event
  (fn [event-type event-data]
    event-type))

(defmethod handle-event :default
  [event-type event-data])

(defn command? [author content]
  (and (s/starts-with? content prefix) (nil? (:bot author)))
  )

(defmethod handle-event :message-create
  [event-type {{bot :bot :as author} :author :keys [channel-id content]}]
  (when (and (nil? bot) (s/starts-with? content "!doc"))
    (let [msg (last (s/split content #" "))]
      (m/create-message! (:messaging @state) channel-id :content (doc-command! msg))
  )))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [event-ch (a/chan 100)
        connection-ch (c/connect-bot! token event-ch)
        messaging-ch (m/start-connection! token)
        init-state {:connection connection-ch
                    :messaging messaging-ch
                    :event event-ch}]
    (reset! state init-state)
    (e/message-pump! event-ch handle-event)
    (c/disconnect-bot! connection-ch)
    ))
