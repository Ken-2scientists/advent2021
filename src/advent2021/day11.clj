(ns advent2021.day11
  (:require [advent-utils.maze :as m]
            [advent-utils.core :as u]))

(defn parse-line
  [line]
  (map (comp read-string str) line))

(def day11-input
  (->> (u/puzzle-input "day11-input.txt")
       (map parse-line)
       m/grid-of
       :grid))

(defn to-flash
  [grid flashed]
  (->> (filter #(> (val %) 9) grid)
       keys
       (filter (complement flashed))))

(defn flash
  [grid flashers flashed]
  (let [valid?     (set (keys grid))
        adjacents (->> (mapcat #(m/adj-coords % :include-diagonals true) flashers)
                       (filter valid?))
        next-grid (reduce #(update %1 %2 inc) grid adjacents)
        next-flashed (into flashed flashers)]
    [next-grid
     (to-flash next-grid next-flashed)
     next-flashed]))

(defn step
  [[total-flashed grid]]
  (loop [next-grid (u/fmap inc grid)
         flashers (to-flash next-grid #{})
         flashed #{}]
    (if (empty? flashers)
      [(+ total-flashed (count flashed))
       (reduce #(assoc %1 %2 0) next-grid flashed)]
      (let [[a b c] (flash next-grid flashers flashed)]
        (recur a b c)))))

(defn flashes-after-100-steps
  [grid]
  (first (nth (iterate step [0 grid]) 100)))

(defn day11-part1-soln
  []
  (flashes-after-100-steps day11-input))

(defn steps-until-sync
  [grid]
  (->> (iterate step [0 grid])
       (map-indexed
        (fn [idx [_ new-grid]]
          [idx (every? zero? (vals new-grid))]))
       (filter second)
       ffirst))
(defn day11-part2-soln
  []
  (steps-until-sync day11-input))