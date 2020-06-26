(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as g]))

(def game-states (atom [(-> (g/init)
                            (g/add-player "Kristie" 4 27)
                            (g/add-player "Blanca" 8 90)
                            (g/add-player "Anna" 6 33)
                            (g/add-player "Stephanie" 4 71)
                            (g/add-player "Papa" 4 100)
                            (g/add-player "Guelita" 4 99)
                            (g/add-player "Naidu" 4 39))]))

(defn ch-cards [player cards]
  (swap! game-states conj (g/change-cards (last @game-states) player cards))
  (last @game-states))

(defn reg-win [winner]
  (swap! game-states conj (g/regular-win (last @game-states) winner))
  (last @game-states))

(defn reg-win-with-spec [winner]
  (swap! game-states conj (g/regular-win-with-special (last @game-states) winner))
  (last @game-states))

(defn spec-win [winner]
  (swap! game-states conj (g/special-win (last @game-states) winner))
  (last @game-states))

(defn latest []
  (last @game-states))

(reg-win "Kristie")
(reg-win "Kristie")
(reg-win "Blanca")

(spec-win "Naidu")

(reg-win "Naidu")
(reg-win "Kristie")
(reg-win "Stephanie")
(reg-win "Papa")
(reg-win-with-spec "Naidu")

;;(swap! game-states drop-last)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


;; (def game-states
;;   (-> (cg/init-game)
;;       (cg/add-player "Kristie" 4 33)
;;       (cg/add-player "Naidu" 4 18)
;;       (cg/add-player "Blanca" 8 55)
;;       (cg/add-player "Anna" 6 85)
;;       (cg/add-player "Guelita" 4 33)
;;       (cg/add-player "Papa" 4 101)
;;       (cg/add-player "Juan" 0 23)))
