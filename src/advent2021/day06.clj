(ns advent2021.day06
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse
  [input]
  (->> (str/split (first input) #",")
       (map read-string)))

(def day06-input (parse (u/puzzle-input "day06-input.txt")))
(def zero-counts (vec (take 9 (repeat 0))))

(defn step
  [counts]
  (-> (u/rotate 1 counts)
      vec
      (update 6 + (get counts 0))))

(defn fish-after-n-days
  [input days]
  (let [counts (reduce #(assoc %1 (key %2) (val %2)) zero-counts (frequencies input))]
    (reduce + (nth (iterate step counts) days))))

(defn day06-part1-soln
  []
  (fish-after-n-days day06-input 80))


(defn day06-part2-soln
  []
  (fish-after-n-days day06-input 256))