(ns clojeria.game
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(defrecord Player [name cards bank wins sp-wins])
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

(defn give-winnings
  "Award some winnings to a player."
  ([game player pot]
   (let [k (pkey player)]
     (update-in game [:players k :bank] + pot)))
  ([game player pot wtype]
   (let [k (pkey player)]
     (-> game
         (update-in [:players k :bank] + pot)
         (update-in [:players k wtype] inc)))))

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

(defn charge-llena
  "Charge a player by taking some money from their bank."
  [player]
  (let [ctp (* 2 (round-cost player))]
    (update player :bank - ctp)))

(defn apply-all-vals
  "Apply a function to all values in a map."
  [coll f & args]
  (into {} (for [[k v] coll] [k (apply f v args)])))

(defn charge-all
  "Charge all players the amount they owe to play their cards."
  [game]
  (update game :players apply-all-vals charge))

(defn charge-all-llena
  "Charge all players the amount they owe to play their cards."
  [game]
  (update game :players apply-all-vals charge-llena))

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
  "Award the WINNER(S) in GAME with a regular round prize."
  ([game winner]
   (let [pot (single-round-pot game)]
     (-> game
         (charge-all)
         (regular-win winner pot true)
         (update-in [:special-pot] + pot))))
  ([game winner1 winner2]
   (let [k1 (pkey winner1)
         k2 (pkey winner2)
         pot (* 0.5 (single-round-pot game))]
     (-> game
         (charge-all)
         (regular-win k1 pot true)
         (regular-win k2 pot true)
         (update-in [:special-pot] + (* 2.0 pot)))))
  ([game winner pot garbage]
   (let [k (pkey winner)]
     (-> game
         (give-winnings k pot)
         (update-in [:players k :wins] inc)))))

(defn regular-win-with-special
  "Award the WINNER(S) in GAME with a regular round prize which includes
  the special pot."
  ([game winner]
   (let [pot (+ (* 2 (single-round-pot game)) (:special-pot game))]
     (-> game
         (charge-all)
         (regular-win-with-special winner pot true)
         (assoc :special-pot 0.0))))
  ([game winner1 winner2]
   (let [k1 (pkey winner1)
         k2 (pkey winner2)
         pot (* 0.5 (+ (* 2 (single-round-pot game)) (:special-pot game)))]
     (-> game
         (charge-all)
         (regular-win-with-special k1 pot true)
         (regular-win-with-special k2 pot true)
         (assoc :special-pot 0.0))))
  ([game winner pot garbage]
   (let [k (pkey winner)]
     (-> game
         (give-winnings k pot)
         (update-in [:players k :wins] inc)
         (update-in [:players k :sp-wins] inc)))))

(defn special-win
  "Award the WINNER in GAME with the special pot."
  ([game winner]
   (let [pot (:special-pot game)]
     (-> game
         (special-win winner pot true)
         (assoc :special-pot 0.0))))
  ([game winner1 winner2]
   (let [k1 (pkey winner1)
         k2 (pkey winner2)
         pot (* 0.5 (:special-pot game))]
     (-> game
         (special-win k1 pot true)
         (special-win k2 pot true)
         (assoc :special-pot 0.0))))
  ([game winner pot garbage]
   (let [k (pkey winner)]
     (-> game
         (give-winnings k pot)
         (update-in [:players k :sp-wins] inc)))))

(defn llena-win
  [game winner]
  (let [k (pkey winner)
        pot (* 4.0 (single-round-pot game))]
    (-> game
        (charge-all-llena)
        (give-winnings k pot))))

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
        :sp-wins 0}]))))

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
