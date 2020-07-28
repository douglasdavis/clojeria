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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; day 1

(ch-cards "Naidu" 4)
(ch-cards "Anna" 4)
(ch-cards "Alma" 4)
(ch-cards "Blanca" 4)
(ch-cards "Kristie" 2)
(ch-cards "Stephanie" 4)
(ch-cards "Guelita" 4)

(rg-win "Alma")
(ch-cards "Junior" 4)
(rg-win "Blanca")
(rg-win "Alma")
(sp-win "Naidu")
(rg-win "Naidu")
(rg-win "Blanca")
(rg-win "Blanca")
(sp-win "Junior")
(ch-cards "Alfred"  4)
(rg-win "Stephanie")
(rg-win "Blanca")
(rg-win "Junior")
(sp-win "Junior")
(rg-win "Stephanie")
(rg-win "Junior")
(rg-win "Naidu" "Anna")
(sp-win "Alma" "Anna")
(ll-win "Naidu")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; day 2

(ch-cards "Junior" 0)
(ch-cards "Alfred"  0)
(ch-cards "Naidu" 0)
(ch-cards "Anna" 0)
(ch-cards "Alma" 0)
(ch-cards "Blanca" 0)
(ch-cards "Kristie" 0)
(ch-cards "Stephanie" 0)
(ch-cards "Guelita" 0)

(ch-cards "Anna" 4)
(ch-cards "Stephanie" 4)
(ch-cards "Blanca" 4)
(ch-cards "Alfred" 4)
(ch-cards "Naidu" 4)
(ch-cards "Guelita" 4)
(ch-cards "Kristie" 4)
(ch-cards "Junior" 0)
(ch-cards "Alma" 4)

(rg-win "Alma" "Stephanie")
(rg-win "alfred")
(rg-win "Anna")
(sp-win "kristie")
(rg-win "naidu")
(rg-win "guelita")
(rg-win "guelita")
(sp-win "alfred")
(ch-cards "Stephanie" 0)
(rg-win "alma")
(rg-win "alfred")
(rg-win "naidu")
(sp-win "alfred")
(rg-win "alfred")
(rg-win "alfred")
(rg-win "alma")
(sp-win "kristie")
(ll-win "alfred")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; day 3

(ch-cards "Anna" 0)
(ch-cards "Stephanie" 0)
(ch-cards "Blanca" 0)
(ch-cards "Alfred" 0)
(ch-cards "Naidu" 0)
(ch-cards "Guelita" 0)
(ch-cards "Kristie" 0)
(ch-cards "Junior" 0)
(ch-cards "Alma" 0)

(ch-cards "Kristie" 4)
(ch-cards "Anna" 4)
(ch-cards "Naidu" 4)
(ch-cards "Guelita" 4)
(ch-cards "Juan" 4)
(ch-cards "Blanca" 4)
(ch-cards "Alma" 4)
(ch-cards "Junior" 4)

(rg-win "kristie")
(rg-win "guelita")
(rg-win "naidu")
(rg-win "Alma")

(sp-win "Alma")

(rg-win "Naidu")
(rg-win "Naidu")
(rg-win "guelita")
(rg-win "junior")

(sp-win "Blanca")

(rg-win "Juan")
(rg-win "Guelita")
(rg-win "Junior")
(rg-win "Alma")

(sp-win "Alma")

(rg-win "Kristie")
(rg-win "Alma")
(rg-win "Guelita")
(rg-win "Blanca")

(sp-win "Alma")

(rg-win "Blanca")
(rg-win "Guelita")
(rg-win "Kristie")

(sp-win "Juan")
(ll-win "Guelita")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;; day 4

(zero-cards)

(ch-cards "Blanca" 4)
(ch-cards "Alma" 4)
(ch-cards "Naidu" 4)
(ch-cards "Anna" 4)
(ch-cards "Stephanie" 4)
(ch-cards "Kristie" 4)
(ch-cards "Guelita" 4)

(rg-win "Naidu")
(ch-cards "Juan" 4)
(rg-win "Alma")
(rg-win "Naidu")
(rg-win "Blanca")
(sp-win "Kristie")
(rg-win "Stephanie")
(sp-win "Stephanie")
(rg-win "Alma")
(rg-win "Stephanie")
(rg-win "Juan")
(sp-win "Guelita")
(rg-win "Stephanie")
(rg-win "Anna" "Stephanie")
(rg-win "Anna")
(sp-win "Guelita")
(ch-cards "Naidu" 0)
(rg-win "Juan")
(sp-win "Juan")
(rg-win "Guelita")
(rg-win "Juan")
(rg-win "Juan")
(rg-win "Kristie")
(sp-win "Anna" "Alma")
(ll-win "Guelita")

;;;;;;;;;;;;;;;;;;;;;;;;  day 5

(zero-cards)

(ch-cards "Anna" 4)
(ch-cards "Stephanie" 4)
(ch-cards "Naidu" 4)
(ch-cards "Alfred" 4)
(ch-cards "Guelita" 4)
(ch-cards "Kristie" 2)
(ch-cards "Alma" 4)
(ch-cards "Blanca" 4)

(rg-win "Stephanie")
(rg-win "Blanca")
(rg-win "Stephanie")
(rg-win "Anna")
(sp-win "Stephanie")
(rg-win "Anna")
(rg-win "Naidu")
(rg-win "Blanca")
(rg-win "Blanca")
(sp-win "Guelita")
(rg-win "Alma")
(rg-win "Alfred")
(rg-win "Guelita")
(rg-win "Naidu")
(sp-win "Guelita")
(rg-win "Stephanie")
(rg-win "Anna")
(rg-win "Stephanie")
(rg-win "Alma" "Guelita")
(sp-win "Anna")
(rg-win "Guelita")
(rg-win "Blanca")
(rg-win "Naidu")
(sp-win "Naidu")
(rg-win "Blanca")
(rg-win "Kristie")
(rg-win "Blanca")
(sp-win "Anna")
(ll-win "Alfred")

;; guelita -> kristie 35
