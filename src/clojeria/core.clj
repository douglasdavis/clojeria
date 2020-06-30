(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def game-states (atom []))

(swap! game-states conj (g/init-from-csv "/Users/ddavis/Dropbox/kdshare/Loteria/Loteria_Accounts_06272020.csv"))

(defn ch-cards [player cards]
  (swap! game-states conj (g/change-cards (last @game-states) player cards))
  (last @game-states))

(defn reg-win
  ([winner]
   (swap! game-states conj (g/regular-win (last @game-states) winner))
   (last @game-states))
  ([winner1 winner2]
   (swap! game-states conj (g/regular-win (last @game-states) winner1 winner2))
   (last @game-states)))

(defn reg-win-with-spec [winner]
  (swap! game-states conj (g/regular-win-with-special (last @game-states) winner))
  (last @game-states))

(defn spec-win [winner]
  (swap! game-states conj (g/special-win (last @game-states) winner))
  (last @game-states))

(defn llena [winner]
  (swap! game-states conj (g/llena-win (last @game-states) winner))
  (last @game-states))

(defn undo []
  (swap! game-states drop-last)
  (last @game-states))

(defn latest []
  (last @game-states))

(defn start []
  (first @game-states))

(ch-cards "guelita" 4)
(reg-win-with-spec "kristie")
(reg-win "kristie" "blanca")
(spec-win "Blanca")
(reg-win "Kristie")
(reg-win "Blanca")
(reg-win "Anna")
(reg-win "Alma")
(spec-win "Anna")
(reg-win "guelita")
(reg-win "Alma")
(reg-win-with-spec "Stephanie")
(reg-win "Anna")
(reg-win "Alma")
(reg-win "Blanca")
(reg-win "Naidu")
(spec-win "alma")
(ch-cards "guelito" 4)
(reg-win-with-spec "Blanca")
(reg-win "Guelito")
(reg-win "Blanca")
(reg-win "Anna")
(reg-win "Anna")
(spec-win "Guelita")
(reg-win "Guelita")
(reg-win-with-spec "Naidu")
(llena "Alma")
