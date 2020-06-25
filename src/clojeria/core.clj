(ns clojeria.core
  (:gen-class)
  (:require [clojeria.game :as cg]))

(def players
  (-> (cg/add-player "Kristie" 4 33)
      (cg/add-player "Naidu" 4 18)
      (cg/add-player "Blanca" 8 55)
      (cg/add-player "Anna" 6 85)
      (cg/add-player "Guelita" 4 33)
      (cg/add-player "Papa" 4 101)
      (cg/add-player "Juan" 0 23)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
