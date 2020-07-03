(ns clojeria.game
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(defrecord Player [name cards bank wins sp-wins llena-wins])
(def cost-per-card 0.25)

(defn pkey
  "Convert an arbitrary player identifier (input can be string or
  keyword) as a purely lower-case keyword."
  [p]
  (keyword (str/lower-case (name p))))

(defn init
  "Initialize a game."
  ([]
   {:players {} :special-pot 0.0})
  ([players]
   {:players players :special-pot 0.0}))

(defn add-player
  "Add a player to game."
  ([existing pname cards bank]
   (let [k (pkey pname)]
     (update existing :players conj {k (->Player pname cards bank 0 0)}))))

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

(defn round-cost
  "Calculate the cost for a player to play a round."
  ([player]
   (* 2 (:cards player) cost-per-card))
  ([game player]
   (let [k (pkey player)]
     (round-cost (get-in game [:players k])))))

(defn- apply-all-vals
  "Apply a function to all values in a map."
  [coll f & args]
  (into {} (for [[k v] coll] [k (apply f v args)])))

(defn single-round-pot
  "Calculate the value of a single round."
  [game]
  (reduce + 0.0
          (map #(* (:cards %) cost-per-card)
               (vals (:players game)))))

(defn charge
  "Charge a player by taking some money from their bank."
  [player]
  (let [ctp (round-cost player)]
    (update player :bank - ctp)))

(defn charge-all
  "Charge all players the amount they owe to play their cards."
  [game]
  (-> game
      (update :players apply-all-vals charge)
      (update :special-pot + (single-round-pot game))))

(defn charge-llena
  "Charge a player by taking some money from their bank."
  [player]
  (let [ctp (:cards player)]
    (update player :bank - ctp)))

(defn charge-all-llena
  "Charge all players the amount they owe to play their cards."
  [game]
  (update game :players apply-all-vals charge-llena))

(defn set-special-pot
  ([game val]
   (assoc game :special-pot val))
  ([game]
   (set-special-pot game 0.0)))

(defn give-winnings
  "Award some winnings to a player."
  [game player pot wtype]
  (let [k (pkey player)]
    (-> game
        (update-in [:players k :bank] + pot)
        (update-in [:players k wtype] inc))))

(defn regular-win
  "Award the WINNER(S) in GAME with a regular round prize."
  ([game w1]
   (let [k1 (pkey w1)
         pot (single-round-pot game)]
     (-> game
         (charge-all)
         (give-winnings k1 pot :wins))))
  ([game w1 w2]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         pot (* 0.5 (single-round-pot game))]
     (-> game
         (charge-all)
         (give-winnings k1 pot :wins)
         (give-winnings k2 pot :wins))))
  ([game w1 w2 w3]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         k3 (pkey w3)
         pot (/ (single-round-pot game) 3.0)]
     (charge-all)
     (give-winnings k1 pot :wins)
     (give-winnings k2 pot :wins)
     (give-winnings k3 pot :wins))))

(defn special-win
  "Award special pot to a player."
  ([game w1]
   (let [k1 (pkey w1)
         pot (:special-pot game)]
     (-> game
         (give-winnings k1 pot :sp-wins)
         (assoc :special-pot 0.0))))
  ([game w1 w2]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         pot (* 0.5 (:special-pot game))]
     (-> game
         (give-winnings k1 pot :sp-wins)
         (give-winnings k2 pot :sp-wins)
         (assoc :special-pot 0.0))))
  ([game w1 w2 w3]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         k3 (pkey w3)
         pot (/ (:special-pot game) 3.0)]
     (-> game
         (give-winnings k1 pot :sp-wins)
         (give-winnings k2 pot :sp-wins)
         (give-winnings k3 pot :sp-wins)
         (assoc :special-pot 0.0)))))

(defn llena-win
  ([game w1]
   (let [k1 (pkey w1)
         pot (* 4.0 (single-round-pot game))]
     (-> game
         (charge-all-llena)
         (give-winnings k1 pot :llena-wins))))
  ([game w1 w2]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         pot (* 0.5 (* 4.0 (single-round-pot game)))]
     (-> game
         (charge-all-llena)
         (give-winnings k1 pot :llena-wins)
         (give-winnings k2 pot :llena-wins))))
  ([game w1 w2 w3]
   (let [k1 (pkey w1)
         k2 (pkey w2)
         k3 (pkey w3)
         pot (/ (* 4.0 (single-round-pot game)) 3.0)]
     (-> game
         (charge-all-llena)
         (give-winnings k1 pot :llena-wins)
         (give-winnings k2 pot :llena-wins)
         (give-winnings k3 pot :llena-wins)))))

(defn- csv-data->maps [csv-data]
  (map
   zipmap
   (->> (first csv-data)
        (map keyword)
        repeat)
   (rest csv-data)))

(defn- players-from-csv [csv-file]
  (with-open [reader (io/reader csv-file)]
    (doall
     (->> (csv/read-csv reader)
          csv-data->maps
          (map (fn [rec]
                 (-> rec
                     (update :cards #(Long/parseLong %))
                     (update :bank #(Float/parseFloat %)))))))))

(defn init-from-csv
  "Initialize a game from a CSV file."
  [csv-file]
  (init
   (into
    {}
    (for [entry (players-from-csv csv-file)]
      [(pkey (:name entry))
       {:name (:name entry)
        :cards (:cards entry)
        :bank (:bank entry)
        :wins 0
        :sp-wins 0
        :llena-wins 0}]))))

(defn calculate-bank-differences
  "Calculate the bank differences between two game maps."
  [final initial]
  (into
   {}
   (for [k (keys (:players final))]
     [k (- (get-in final [:players k :bank])
           (get-in initial [:players k :bank]))])))

(defn summary-of-column
  [game c]
  (into
   {}
   (for [k (keys (:players  game))]
     [k (get-in game [:players k c])])))
