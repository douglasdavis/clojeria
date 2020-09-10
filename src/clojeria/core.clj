(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def history (atom []))

(swap! history conj (g/init-from-csv "resources/2020.09.10.csv"))

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

(defn zero-cards
  "Zero all cards."
  []
  (swap! history conj (g/zero-all-cards (latest)))
  (latest))

(defn rg-win
  "Regular win."
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
  "Special win."
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
  "Llena win"
  ([w1]
   (swap! history conj (g/llena-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/llena-win (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/llena-win (latest) w1 w2 w3))
   (latest)))

(defn diffs
  "Show current game bank differences."
  []
  (g/calculate-bank-differences (latest) (start)))

(defn banks
  "List current bank balances."
  []
  (g/summary-of-column (latest) :bank))

(defn cards
  "List current cards being played"
  []
  (g/summary-of-column (latest) :cards))

(defn spot
  "Show current special pot value"
  []
  (:special-pot (latest)))

(defn srp
  "Show value of a single round pot."
  []
  (g/single-round-pot (latest)))

(defn save-banks
  "Save current bank balances to csv file."
  []
  (let [date-s (.format (java.text.SimpleDateFormat. "yyyy.MM.dd") (new java.util.Date))
        file-s (str "resources/" date-s ".csv")]
    (g/banks-to-csv (latest) file-s)))

(zero-cards)

(ch-cards "Blanca" 4)
(ch-cards "Alfred" 4)
(ch-cards "Guelita" 4)
(ch-cards "Guelito" 4)
(ch-cards "Naidu" 4)
(ch-cards "Kristie" 4)
