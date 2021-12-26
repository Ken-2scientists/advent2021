(ns advent2021.day19
  (:require [clojure.set :as set]
            [clojure.string :as str]
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

(def day19-sample1 (parse (u/puzzle-input "day19-sample1.txt")))
(def day19-input  (parse (u/puzzle-input "day19-input.txt")))


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

(relative-distances (first day19-sample))

(def seta (into #{} (flatten (relative-distances (first day19-sample1)))))
seta

(def setb (into #{} (flatten (relative-distances (second day19-sample1)))))
setb

(set/intersection seta setb)


(relative-distances day19-sample)

(def foo (first day19-sample))

(map (partial dist (first foo)) (rest foo))


(count (first day19-sample))
(relative-distances (first day19-sample))
