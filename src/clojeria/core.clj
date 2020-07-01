(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def history (atom []))

(swap! history conj (g/init-from-csv "/Users/ddavis/Dropbox/kdshare/Loteria/Loteria_Accounts_06302020.csv"))

(defn ch-cards [player cards]
  (swap! history conj (g/change-cards (last @history) player cards))
  (last @history))

(defn r-win
  ([winner]
   (swap! history conj (g/regular-win (last @history) winner))
   (last @history))
  ([winner1 winner2]
   (swap! history conj (g/regular-win (last @history) winner1 winner2))
   (last @history)))

(defn rs-win [winner]
  (swap! history conj (g/regular-win-with-special (last @history) winner))
  (last @history))

(defn s-win
  ([winner]
  (swap! history conj (g/special-win (last @history) winner))
   (last @history))
  ([winner1 winner2]
   (swap! history conj (g/special-win  (last @history) winner1 winner2))
   (last @history)))

(defn l-win [winner]
  (swap! history conj (g/llena-win (last @history) winner))
  (last @history))

(defn undo []
  (swap! history drop-last)
  (last @history))

(defn latest []
  (last @history))

(defn start []
  (first @history))

(defn diffs []
  (g/calculate-bank-differences (latest) (start)))

(defn banks []
  (g/summary-of-column (latest) :bank))

(defn cards []
  (g/summary-of-column (latest) :cards))

(defn spot []
  (:special-pot (latest)))

(r-win "Kristie")
(s-win "Kristie")
(r-win "Blanca")
(r-win "Blanca")
(r-win "Naidu")
(s-win "Naidu" "Blanca")
(r-win "Anna")
(s-win "Anna")
(r-win "Blanca")
(r-win "Blanca")
(r-win "Anna")
(r-win "Guelita")
(s-win "Stephanie")
(r-win "Jenny")
(r-win "Blanca")
(r-win "Kristie")
(r-win "Kristie")
(s-win "Anna")
(r-win "Anna")
(r-win "Jenny")
(s-win "Jenny" "Anna")
(ch-cards "Guelito" 4)
(r-win "Jenny")
(r-win "Kristie")
(r-win "Jenny")
(r-win "Guelita")
(r-win "Blanca")
(s-win "Naidu")
