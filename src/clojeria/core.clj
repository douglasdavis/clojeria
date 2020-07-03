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

(rg-win "Stephanie")
(rg-win "Anna")
(rg-win "Anna")
(rg-win "Kristie")
(sp-win "Kristie")
(rg-win "Naidu")
(rg-win "Guelito" "Blanca")
(rg-win "Naidu" "Kristie")
(rg-win "Guelito")
(sp-win "Naidu")
(ch-cards "Janet" 2)
(rg-win "Anna")
(rg-win "Stephanie")
(rg-win "Anna")
(rg-win "Kristie")
(sp-win "Naidu")
(ch-cards "Guelito" 0)
(rg-win "Janet")
(rg-win "Naidu")
(rg-win "Blanca")
(rg-win "Naidu")
(sp-win "Kristie" "Guelita")
(rg-win "Naidu")
(rg-win "Guelita")
(rg-win "Anna")
(rg-win "Stephanie")
(sp-win "Janet")
(ll-win "Kristie")

;; (rg-win "Stephanie")
;; (rg-win "Blanca")
;; (rg-win "Blanca")
;; (rg-win "Kristie" "Naidu")
;; (sp-win "Blanca")
;; (rg-win "Blanca" "Anna")
;; (rg-win "Stephanie")
;; (rg-win "Junior")
;; (rg-win "Guelita")
;; (sp-win "Blanca")
;; (rg-win "Anna" "Junior")
;; (sp-win "Anna")
;; (rg-win "Naidu")
;; (rg-win "Guelita")
;; (sp-win "Guelita")
;; (rg-win "Naidu")
;; (rg-win "Blanca")
;; (rg-win "Junior")
;; (rg-win "Stephanie")
;; (sp-win "Naidu")
;; (rg-win "Kristie")
;; (rg-win "Blanca")
;; (rg-win "Alfred")
;; (rg-win "Alfred")
;; (sp-win "Naidu")
;; (ll-win "Kristie")
