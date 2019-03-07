(ns cljbot.event-handler
  (:gen-class)
  (:require [clojure.string :as s]
            [discljord.messaging :as m]
            [cljbot.commands.doc :as doc]))

(def state (atom nil))

(defonce prefix "!")
 
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
    (let [msg (last (s/split content #" "))
          cmd (doc/doc-command! msg)]
      (m/create-message! (:messaging @state) channel-id :content cmd)
  )))
