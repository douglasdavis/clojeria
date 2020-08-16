(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def history (atom []))

(swap! history conj (g/init-from-csv "resources/2020.08.14.csv"))

(defn latest []
  (last @history))

(defn undo []
  (swap! history drop-last)
  (latest))

(defn start []
  (first @history))

(defn ch-cards [player cards]
  (swap! history conj (g/change-cards (latest) player cards))
  (latest))

(defn zero-cards []
  (swap! history conj (g/zero-all-cards (latest)))
  (latest))

(defn rg-win
  ([w1]
   (swap! history conj (g/regular-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/regular-win (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/regular-win (latest) w1 w2 w3))
   (latest)))

(defn sp-win
  ([w1]
   (swap! history conj (g/special-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/special-win  (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/special-win  (latest) w1 w2 w3))
   (latest)))

(defn ll-win
  ([w1]
   (swap! history conj (g/llena-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/llena-win (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/llena-win (latest) w1 w2 w3))
   (latest)))

(defn diffs []
  (g/calculate-bank-differences (latest) (start)))

(defn banks []
  (g/summary-of-column (latest) :bank))

(defn cards []
  (g/summary-of-column (latest) :cards))

(defn spot []
  (:special-pot (latest)))

(defn srp []
  (g/single-round-pot (latest)))

(defn save-banks []
  (let [date-s (.format (java.text.SimpleDateFormat. "yyyy.MM.dd") (new java.util.Date))
        file-s (str "resources/" date-s ".csv")]
    (g/banks-to-csv (latest) file-s)))

(zero-cards)

(ch-cards "Blanca" 6)
(ch-cards "Kristie" 2)
(ch-cards "Alfred" 4)
(ch-cards "Naidu" 4)
(ch-cards "Guelita" 4)
(ch-cards "Guelito" 4)
(ch-cards "Junior" 4)
(ch-cards "Anna" 4)
