(ns clojeria.game
  (:require [clojure.string :as str]))

(defrecord Player [name cards bank wins sp-wins])
(def cost-per-card 0.25)

(defn pkey
  "Convert an arbitrary player identifier (input can be string or
  keyword) as a purely lower-case keyword."
  [p]
  (keyword (str/lower-case (name p))))

(defn init-game
  "Initialize a game."
  []
  {:players {} :special-pot 0.0})

(defn add-player
  "Add a player to game."
  ([pname cards bank]
   (let [k (pkey pname)]
     {k (->Player pname cards bank 0 0)}))
  ([existing pname cards bank]
   (let [k (pkey pname)]
     (update existing :players conj (add-player pname cards bank)))))

(defn change-cards
  "Update the cards of a player in the game."
  [game player new-cards]
  (let [k (pkey player)]
    (assoc-in game [:players k :cards] new-cards)))

(defn change-bank
  "Manually update the bank value for a player in the game"
  [game player new-bank]
  (let [k (pkey player)]
    (assoc-in game [:players k :bank] new-bank)))

(defn give-winnings
  "Award some winnings to a player."
  [game player pot]
  (let [k (pkey player)]
    (update-in game [:players k :bank] + pot)))

(defn round-cost
  "Calculate the cost for a player to play a round."
  ([player]
   (* 2 (:cards player) cost-per-card))
  ([game player]
   (let [k (pkey player)]
     (round-cost (get-in game [:players k])))))

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
  [game]
  (update game :players apply-all-vals charge))

(defn single-round-pot
  "Calculate the value of a single round."
  [game]
  (reduce + 0.0
          (map #(* (:cards %) cost-per-card)
               (vals (:players game)))))

(defn set-special-pot
  ([game val]
   (assoc game :special-pot val))
  ([game]
   (set-special-pot game 0.0)))

(defn regular-win
  "Award the WINNER in GAME with a regular round prize."
  [game winner]
  (let [pot (single-round-pot game)
        k (pkey winner)]
    (-> game
        (charge-all)
        (give-winnings k pot)
        (update-in [:players k :wins] inc)
        (update-in [:special-pot] + pot))))

(defn regular-win-with-special
  "Award the WINNER in GAME with a regular round prize which includes
  the special pot."
  [game winner]
  (let [pot (+ (single-round-pot game) (:special-pot game))
        k (pkey winner)]
    (-> game
        (charge-all)
        (give-winnings k pot)
        (update-in [:players k :wins] inc)
        (update-in [:players k :sp-wins] inc)
        (assoc :special-pot 0.0))))

(defn special-win
  "Award the WINNER in GAME with the special pot."
  [game winner]
  (let [pot (:special-pot game)
        k (pkey winner)]
    (-> game
        (give-winnings k pot)
        (update-in [:players k :sp-wins] inc)
        (assoc :special-pot 0.0))))
