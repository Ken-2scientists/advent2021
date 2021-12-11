(ns advent2021.day06
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse
  [input]
  (->> (str/split (first input) #",")
       (map read-string)))

(def day06-input (parse (u/puzzle-input "day06-input.txt")))

(defn shift
  [counts x]
  (cond
    (#{0 1 2 3 4 5 7} x) (counts (inc x))
    (= 6 x)              (+ (counts 0) (counts 7))
    (= 8 x)              (counts 0)))

(defn step
  [counts]
  (zipmap (keys counts)
          (map (partial shift counts) (keys counts))))

(defn fish-after-n-days
  [input days]
  (let [counts (into (zipmap (range 9) (repeat 0)) (frequencies input))
        final-counts (-> (iterate step counts)
                         (nth days))]
    (reduce + (vals final-counts))))

(defn day06-part1-soln
  []
  (fish-after-n-days day06-input 80))


(defn day06-part2-soln
  []
  (fish-after-n-days day06-input 256))



