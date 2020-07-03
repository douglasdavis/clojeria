(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def history (atom []))

(swap! history conj (g/init-from-csv "/Users/ddavis/Dropbox/kdshare/Loteria/Loteria_Accounts_06302020.csv")

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

(defn r-win
  ([w1]
   (swap! history conj (g/regular-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/regular-win (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/regular-win (latest) w1 w2 w3))
   (latest)))

(defn s-win
  ([w1]
   (swap! history conj (g/special-win (latest) w1))
   (latest))
  ([w1 w2]
   (swap! history conj (g/special-win  (latest) w1 w2))
   (latest))
  ([w1 w2 w3]
   (swap! history conj (g/special-win  (latest) w1 w2 w3))
   (latest)))

(defn l-win
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

;; (r-win "Stephanie")
;; (r-win "Blanca")
;; (r-win "Blanca")
;; (r-win "Kristie" "Naidu")
;; (s-win "Blanca")
;; (r-win "Blanca" "Anna")
;; (r-win "Stephanie")
;; (r-win "Junior")
;; (r-win "Guelita")
;; (s-win "Blanca")
;; (r-win "Anna" "Junior")
;; (s-win "Anna")
;; (r-win "Naidu")
;; (r-win "Guelita")
;; (s-win "Guelita")
;; (r-win "Naidu")
;; (r-win "Blanca")
;; (r-win "Junior")
;; (r-win "Stephanie")
;; (s-win "Naidu")
;; (r-win "Kristie")
;; (r-win "Blanca")
;; (r-win "Alfred")
;; (r-win "Alfred")
;; (s-win "Naidu")
;; (l-win "Kristie")

;; (r-win "Kristie")
;; (l-win "Naidu" "Blanca")
;; (r-win "Kristie")
;; (s-win "Kristie")
;; (r-win "Blanca")
;; (r-win "Blanca")
;; (r-win "Naidu")
;; (r-win "Naidu" "Blanca")
;; (s-win "Naidu" "Anna" "Stephanie")
;; (l-win "Naidu" "Blanca" "Kristie")
