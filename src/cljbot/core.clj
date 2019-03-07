(ns cljbot.core
  (:gen-class)
  (:require [clojure.string :as s]
            [clojure.core.async :as a]
            [discljord.messaging :as m]
            [discljord.events :as e]
            [discljord.connections :as c]
            [cljbot.event-handler :as eh]))

(def token (s/trim-newline (slurp "token.txt")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [event-ch (a/chan 100)
        connection-ch (c/connect-bot! token event-ch)
        messaging-ch (m/start-connection! token)
        init-state {:connection connection-ch
                    :messaging messaging-ch
                    :event event-ch}]
    (reset! eh/state init-state)
    (e/message-pump! event-ch eh/handle-event)
    (c/disconnect-bot! connection-ch)
    ))
