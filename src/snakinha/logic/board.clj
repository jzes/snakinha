(ns snakinha.logic.board
  (:require [clojure.string :as str]))

(defn make-board
  [x y background]
  (->> background
       (repeat x)
       (into [])
       (repeat y)
       (into [])))

(defn render-board
  [board]
  (str/join "\n" (map (fn [x] (str/join " " x)) board)))