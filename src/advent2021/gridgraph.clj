(ns advent2021.gridgraph
  (:require [advent-utils.maze :as m]
            [advent-utils.graph :as g :refer [Graph vertices edges distance]]))

(defrecord GridGraph [grid]
  Graph
  (vertices
    [_]
    (keys (:grid grid)))

  (edges
    [_ v]
    (->> (m/adj-coords v)
         (filter (comp some? (:grid grid)))))

  (distance
    [_ _ v2]
    (get-in grid [:grid v2])))

(defrecord TiledGridGraph [grid tile-count]
  Graph
  (vertices
    [_]
    (let [[maxx maxy] [(dec (:width grid)) (dec (:height grid))]]
      (for [y (range (* tile-count maxy))
            x (range (* tile-count maxx))]
        [x y])))

  (edges
    [_ v]
   ;; TODO fix this impl
    (->> (m/adj-coords v)
         (filter (comp some? grid))))

  (distance
    [_ _ v2]
   ;; TODO fix this impl
    (get grid v2)))