(ns advent2021.day15
  (:require [advent-utils.core :as u]
            [advent-utils.maze :as m]
            [advent-utils.graph :as g]
            [advent2021.gridgraph :refer [->GridGraph]]))

(defn parse-line
  [line]
  (map (comp read-string str) line))

(defn parse
  [lines]
  (map parse-line lines))

(def day15-sample
  (m/grid-of
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

(def day15-input (m/grid-of (parse (u/puzzle-input "day15-input.txt"))))

(defn find-path-vals
  [{:keys [width height grid] :as input}]
  (let [start  [0 0]
        end    [(dec width) (dec height)]
        path (g/dijkstra (->GridGraph input) start end)]
    (map grid path)))

(defn path-risk
  [input]
  (reduce + (rest (find-path-vals input))))

(defn day15-part1-soln
  []
  (path-risk day15-input))