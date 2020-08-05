(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def history (atom []))

(swap! history conj (g/init-from-csv "/Users/ddavis/Dropbox/kdshare/Loteria/Loteria_Accounts_06302020.csv"))

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
    (g/to-csv (latest) file-s)))

(zero-cards)

(ch-cards "Blanca" 4)
(ch-cards "Alfred" 4)
(ch-cards "Anna" 4)
(ch-cards "Stephanie" 4)
(ch-cards "Junior" 4)
(ch-cards "Naidu" 4)
(ch-cards "Guelita" 4)

(rg-win "Blanca")
(rg-win "Junior")
(rg-win "Blanca")
(sp-win "Blanca")
(rg-win "Alfred")
(rg-win "Stephanie")
(rg-win "Stephanie")
(rg-win "Naidu")
(sp-win "Anna")
(rg-win "Junior")
(rg-win "Junior" "Naidu")
(rg-win "Stephanie" "Junior")
(rg-win "Stephanie")
(sp-win "Naidu")
(rg-win "Naidu")
(rg-win "Naidu")
(sp-win "Naidu")
(rg-win "Naidu")
(sp-win "Naidu")
(rg-win "Junior")
(sp-win "Junior")
(rg-win "Anna")
(rg-win "Guelita" "Alfred")
(rg-win "Stephanie")
(rg-win "Blanca")
(sp-win "Guelita")
(rg-win "Alfred")
(sp-win "Alfred")
(rg-win "Anna")
(rg-win "Naidu")
(rg-win "Junior")
(sp-win "Junior")
(ll-win "Alfred" "Guelita")

(save-banks)
