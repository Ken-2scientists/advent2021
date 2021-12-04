(ns advent2021.day01
  (:require [advent-utils.core :as u]))

(def day01-input
  (map read-string (u/puzzle-input "day01-input.txt")))

(defn increases
  [depths]
  (->> depths
       (partition 2 1)
       (map (fn [[a b]] (> b a)))
       (filter identity)
       count))

(defn day01-part1-soln
  []
  (increases day01-input))

(defn sliding-window-sum
  [depths]
  (->> depths
       (partition 3 1)
       (map #(reduce + %))))

(defn day01-part2-soln
  []
  (increases (sliding-window-sum day01-input)))