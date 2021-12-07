(ns advent2021.day07
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse
  [line]
  (->> (str/split (first line) #",")
       (map read-string)))

(def day07-input (parse (u/puzzle-input "day07-input.txt")))

(defn mean
  [vals]
  (/ (reduce + vals) (count vals)))

(defn median
  [vals]
  (let [sorted-vals (sort vals)
        n           (count vals)
        half        (int (/ n 2))]
    (if (odd? half)
      (nth sorted-vals half)
      (/ (+ (nth sorted-vals half)
            (nth sorted-vals (dec half)))
         2))))

(defn min-fuel
  [positions]
  (let [med (median positions)]
    (->> positions
         (map #(Math/abs (- % med)))
         (reduce +))))

(defn day07-part1-soln
  []
  (min-fuel day07-input))

(defn gauss-sum
  [n]
  (/ (* n (inc n)) 2))

(defn fuel-part2
  [positions dest]
  (->> positions
       (map #(Math/abs (- % dest)))
       (map gauss-sum)
       (reduce +)))

(defn min-fuel-part2
  [positions]
  (let [mn (int (mean positions))]
    (min (fuel-part2 positions mn)
         (fuel-part2 positions (inc mn)))))

(defn day07-part2-soln
  []
  (min-fuel-part2 day07-input))