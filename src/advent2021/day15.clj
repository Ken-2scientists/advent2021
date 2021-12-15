(ns advent2021.day15
  (:require [advent-utils.core :as u]
            [advent-utils.maze :as m]
            [advent-utils.graph :as g :refer [Graph ->MapGraph]]))

(defn parse-line
  [line]
  (map (comp read-string str) line))

(defn parse
  [lines]
  (map parse-line lines))

;; TODO: add to common utils
;; Same function used in day09, day11, and now day15
(defn grid-of
  [values]
  (let [coords (for [y (range (count values))
                     x (range (count (first values)))]
                 [x y])]
    (zipmap coords (flatten values))))

(def day15-sample
  (grid-of
   (parse
    ["1163751742"
     "1381373672"
     "2136511328"
     "3694931569"
     "7463417111"
     "1319128137"
     "1359912421"
     "3125421639"
     "1293138521"
     "2311944581"])))

(def day15-input (grid-of (parse (u/puzzle-input "day15-input.txt"))))

;; TODO: possible better implementation for m/neighbors
(defn neighbor-graph
  [grid pos]
  (let [coords (->> (m/adj-coords pos)
                    (filter (comp some? grid)))
        values (map grid coords)]
    (zipmap coords values)))

(defn graph-of
  [grid]
  (->MapGraph
   (zipmap (keys grid)
           (map (partial neighbor-graph grid) (keys grid)))))

(defn max-pos
  [grid]
  (let [maxx (apply max (map first (keys grid)))
        maxy (apply max (map second (keys grid)))]
    [maxx maxy]))

(defn find-path-vals
  [grid]
  (let [path (g/dijkstra (graph-of grid) [0 0] (max-pos grid))]
    (map grid path)))

(defn path-risk
  [input]
  (reduce + (rest (find-path-vals input))))

(defn day15-part1-soln
  []
  (path-risk day15-input))