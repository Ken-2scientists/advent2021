(ns advent2021.day06
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse
  [input]
  (->> (str/split (first input) #",")
       (map read-string)))

(def day06-input (parse (u/puzzle-input "day06-input.txt")))

(defn fish-step
  [fish]
  (case fish
    0 [6 8]
    [(dec fish)]))

(defn step
  [input]
  (mapcat fish-step input))

(defn fish-after-n-days
  [input days]
  (-> (iterate step input)
      (nth days)
      count))

(defn day06-part1-soln
  []
  (fish-after-n-days day06-input 80))
