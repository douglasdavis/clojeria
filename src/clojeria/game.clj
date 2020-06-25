(ns clojeria.game
  (:gen-class)
  (:require [clojure.string :as str]))

(defrecord Player [name cards bank])
(def cost-per-card 0.25)
(def special-pot (atom 0.0))

(defn pkey
  "Convert an arbitrary player identifier (input can be string or
  keyword) as a purely lower-case keyword."
  [p]
  (keyword (str/lower-case (name p))))

(defn add-player
  "Add a player to game."
  ([pname cards bank]
   (let [k (pkey pname)]
     {k (->Player pname cards bank)}))
  ([existing pname cards bank]
   (let [k (pkey pname)]
     (conj existing {k (->Player pname cards bank)}))))

(defn change-cards
  "Update the cards of a player in the game."
  [players player new-cards]
  (let [k (pkey player)]
    (assoc-in players [k :cards] new-cards)))

(defn change-bank
  "Manually update the bank value for a player in the game"
  [players player new-bank]
  (let [k (pkey player)]
    (assoc-in players [k :bank] new-bank)))

(defn give-winnings
  "Award some winnings to a player."
  [players player pot]
  (let [k (pkey player)]
    (update-in players [k :bank] + pot)))

(defn round-cost
  "Calculate the cost for a player to play a round."
  [player]
  (* 2 (:cards player) cost-per-card))

(defn charge
  "Charge a player by taking some money from their bank."
  [player]
  (let [ctp (round-cost player)]
    (update player :bank - ctp)))

(defn apply-all-vals
  "Apply a function to all values in a map."
  [coll f & args]
  (into {} (for [[k v] coll] [k (apply f v args)])))

(defn charge-all
  "Charge all players the amount they owe to play their cards."
  [players]
  (apply-all-vals players charge))

(defn single-round-pot
  "Calculate the value of a single round."
  [players]
  (* 0.5
     (reduce + 0.0
             (map #(* 2 (:cards %) cost-per-card)
                  (vals players)))))

(defn empty-special-pot
  []
  (first (reset-vals! special-pot 0.0)))

(defn set-special-pot
  [val]
  (first (reset-vals! special-pot val)))

(defn regular-win
  [players winner]
  (let [pot (single-round-pot players)]
    (do
      (swap! special-pot + pot)
      (-> players
          (charge-all)
          (give-winnings winner pot)))))

(defn regular-win-with-special
  [players winner]
  (let [pot (+ (single-round-pot players)
               (empty-special-pot))]
    (-> players
        (charge-all)
        (give-winnings winner pot))))

(defn special-win
  [players winner]
  (let [pot (empty-special-pot)]
    (give-winnings players winner pot)))
