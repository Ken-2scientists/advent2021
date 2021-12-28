(ns advent2021.day19
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [clojure.core.matrix :as matrix]
            [advent-utils.core :as u]))

(defn parse-sensor
  [sensor]
  (mapv #(read-string (str "[" % "]"))
        (rest (str/split sensor #"\n"))))

(defn parse
  [input]
  (let [sensors (-> (str/join "\n" input)
                    (str/split #"\n\n"))]
    (mapv parse-sensor sensors)))

(def day19-sample
  (parse
   ["--- scanner 0 ---"
    "0,2"
    "4,1"
    "3,3"
    ""
    "--- scanner 1 ---"
    "-1,-1"
    "-5,0"
    "-2,1"]))

;; TODO Candidates for a common math utility
(def rotation-x-90
  [[1 0  0]
   [0 0 -1]
   [0 1  0]])

(def rotation-y-90
  [[0 0 1]
   [0 1 0]
   [-1 0 0]])

(def rotation-z-90
  [[0 -1 0]
   [1 0  0]
   [0 0 1]])

(def all-orientation-permutations
  [[[1  0 0] [0  1  0] [0  0  1]]; identity
   [[1  0 0] [0 -1  0] [0  0 -1]]; rot-x rot-x
   [[1  0 0] [0  0  1] [0 -1  0]]; rot-x rot-x rot-x
   [[1  0 0] [0  0 -1] [0  1  0]]; rot-x

   [[-1 0 0] [0  1  0] [0  0 -1]]; rot-x rot-x rot-z rot-z
   [[-1 0 0] [0 -1  0] [0  0  1]]; rot-z rot-z
   [[-1 0 0] [0  0  1] [0  1  0]]; rot-x rot-x rot-x rot-z rot-z
   [[-1 0 0] [0  0 -1] [0 -1  0]]; rot-x rot-z rot-z

   [[0  1 0] [1  0  0] [0  0 -1]]; rot-x rot-x rot-z rot-z rot-z
   [[0  1 0] [-1 0  0] [0  0  1]]; rot-z rot-z rot-z
   [[0  1 0] [0  0  1] [1  0  0]]; rot-x rot-x rot-x rot-z rot-z rot-z rot-z
   [[0  1 0] [0  0 -1] [-1 0  0]]; rot-x rot-z rot-z rot-z

   [[0 -1 0] [1  0  0] [0  0  1]]; rot-z
   [[0 -1 0] [-1 0  0] [0  0 -1]]; rot-x rot-x rot-z
   [[0 -1 0] [0  0  1] [-1 0  0]]; rot-x rot-x rot-x rot-z
   [[0 -1 0] [0  0 -1] [1  0  0]]; rot-x rot-z

   [[0  0 1] [1  0  0] [0  1 0]]; rot-x rot-y
   [[0  0 1] [-1 0  0] [0 -1 0]]; rot-x rot-x rot-x rot-y
   [[0  0 1] [0  1  0] [-1 0 0]]; rot-y
   [[0  0 1] [0 -1  0] [1  0 0]]; rot-x rot-x rot-y

   [[0 0 -1] [1  0  0] [0 -1 0]]; rot-x rot-x rot-x rot-y rot-y rot-y
   [[0 0 -1] [-1 0  0] [0  1 0]]; rot-x rot-y rot-y rot-y
   [[0 0 -1] [0  1  0] [1  0 0]]; rot-y rot-y rot-y
   [[0 0 -1] [0 -1  0] [-1 0 0]]; rot-x rot-x rot-y rot-y rot-y 
   ])

(def day19-sample1 (parse (u/puzzle-input "day19-sample1.txt")))
(def day19-input  (parse (u/puzzle-input "day19-input.txt")))

(comment
  (matrix/mmul rotation-x-90 rotation-x-90 rotation-x-90 rotation-y-90 rotation-y-90 rotation-y-90))

(defn matrix-mult
  "Matrix multiply with v represented as a row (not column) vector"
  [matrix v]
  (mapv #(reduce + (map * % v)) matrix))


;; Overall thoughts on the approach.
;;  * The relative position vector between any two beacons that can both be seen
;;    by a sensor will be the same, modulo the orientation of the sensors.
;;  * Relative distance isn't affected by orientation of the sensors
;;  * Figure out which beacons *might* be both seen by different sensors by:
;;    * Compute the relative distances between all beacons seen by sensor A
;;    * For each other sensor, find out which distances are in common
;;  * For distances in common, compute all rotations of the relative position vectors
;;    * One set of the relative position vectors must match (Now we know the orientation)
;;    * For a given pair from each sensor with the same relative position vector, we can
;;      then compute the office.
;;  * Repeat for all the other sensors until we've uniquely mapped everything into 
;;    a common coordinate system

(defn abs
  [x]
  (Math/abs x))

(defn dist
  "Manhattan distance"
  [a b]
  (reduce + (map (comp #(Math/abs %) -) a b)))

(defn dist-to-rest
  [items]
  (map (partial dist (first items)) (rest items)))

(defn relative-distances
  [beacons]
  (let [indices (range (count beacons))]
    (->> (mapv #(u/rotate % beacons) indices)
         (map dist-to-rest))))

(defn relative-vectors
  [beacons idx]
  (let [shifted (u/rotate idx beacons)]
    (map #(mapv - % (first shifted)) (rest shifted))))

(defn overlap
  [a b]
  (set/intersection (set a) (set b)))

(defn rel-dist-overlaps
  [rel-dist-a rel-dist-b]
  (apply concat
         (map-indexed
          (fn [a-pos a]
            (map-indexed (fn [b-pos b]
                           [[a-pos b-pos] (overlap a b)]) rel-dist-b)) rel-dist-a)))

(defn overlapping-relative-vectors
  [overlap-set beacons offset]
  (set (filter #(overlap-set (reduce + (map abs %))) (relative-vectors beacons offset))))

(defn at-least-twelve-overlap
  [rel-dist-a rel-dist-b]
  (->> (rel-dist-overlaps rel-dist-a rel-dist-b)
       (filter #(>= ((comp count second) %) 11))
       first))

(defn find-orientation
  [beacons-a beacons-b [[idx-a idx-b] overlap-set]]
  (let [rel-vecs-a (overlapping-relative-vectors overlap-set beacons-a idx-a)
        rel-vecs-b (overlapping-relative-vectors overlap-set beacons-b idx-b)
        permutations (map-indexed (fn [idx m]
                                    [idx (set (map (fn [v] (matrix-mult m v)) rel-vecs-b))]) all-orientation-permutations)]
    (get all-orientation-permutations (ffirst (filter #(= rel-vecs-a (second %)) permutations)))))

(defn reorient
  [orientation beacons]
  (map (partial matrix-mult orientation) beacons))

(def rel-a (relative-distances (first day19-sample1)))
(def rel-b (relative-distances (second day19-sample1)))
(def overlap-set #{188 141 1233 2346 2112 1489 2357 1361 1331 1372 1380})
(def orientation (find-orientation (first day19-sample1) (second day19-sample1) (at-least-twelve-overlap rel-a rel-b)))
(reorient orientation (second day19-sample1))


