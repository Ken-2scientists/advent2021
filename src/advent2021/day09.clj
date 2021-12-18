(ns advent2021.day09
  (:require [advent-utils.core :as u]
            [advent-utils.maze :as m]))

(defn parse-line
  [line]
  (map (comp read-string str) line))

(def day09-input
  (m/grid-of (map parse-line (u/puzzle-input "day09-input.txt"))))

(defn low-point?
  [grid pos]
  (let [val (grid pos)
        coords (m/adj-coords pos)
        n-vals (->> (map grid coords)
                    (filter some?))]
    (every? #(< val %) n-vals)))

(defn risk-level
  [val]
  (inc val))

(defn risk-level-sum
  [input]
  (let [low-points (filter (partial low-point? input) (keys input))
        low-vals   (map input low-points)
        risks      (map risk-level low-vals)]
    (reduce + risks)))

(defn day09-part1-soln
  []
  (risk-level-sum day09-input))

(defn non-nine-neighbors
  [grid pos]
  (let [coords (m/adj-coords pos)]
    (->> (filter (comp some? grid) coords)
         (filter #(not= 9 (grid %))))))

(defn basin
  [grid low-point]
  (loop [basin #{low-point} next (non-nine-neighbors grid low-point)]
    (if (empty? next)
      basin
      (recur (into basin next)
             (->> (mapcat (partial non-nine-neighbors grid) next)
                  (filter (complement basin)))))))

(defn basin-sizes
  [grid]
  (let [low-points (filter (partial low-point? grid) (keys grid))
        basins     (map (partial basin grid) low-points)]
    (map count basins)))

(defn three-largest-basins-product
  [grid]
  (->> (basin-sizes grid)
       (sort >)
       (take 3)
       (reduce *)))

(defn day09-part2-soln
  []
  (three-largest-basins-product day09-input))