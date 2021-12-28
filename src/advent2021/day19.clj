(ns advent2021.day19
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [advent-utils.core :as u]))

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

(defn parse-sensor
  [sensor]
  (mapv #(read-string (str "[" % "]"))
        (rest (str/split sensor #"\n"))))

(defn parse
  [input]
  (let [sensors (-> (str/join "\n" input)
                    (str/split #"\n\n"))]
    (zipmap (range (count sensors))
            (mapv #(assoc {} :beacons (parse-sensor %)) sensors))))

(defn abs
  [x]
  (Math/abs x))

(defn relative-vectors
  [beacons idx]
  (let [shifted (u/rotate idx beacons)]
    (mapv #(mapv - % (first shifted)) (rest shifted))))

(defn vector-signature
  [vec]
  (set (map abs vec)))

(defn relative-vector-signatures
  [rel-vecs]
  (mapv #(mapv vector-signature %) rel-vecs))

(defn add-relative-vectors
  [{:keys [beacons] :as sensor}]
  (let [indices (range (count beacons))
        rel-vecs (mapv (partial relative-vectors beacons) indices)
        sigs     (relative-vector-signatures rel-vecs)]
    (assoc sensor
           :rel-vecs rel-vecs
           :rel-vec-sigs sigs)))

(def day19-sample
  (u/fmap add-relative-vectors
          (parse
           ["--- scanner 0 ---"
            "0,2"
            "4,1"
            "3,3"
            ""
            "--- scanner 1 ---"
            "-1,-1"
            "-5,0"
            "-2,1"])))

(def day19-input  (u/fmap add-relative-vectors (parse (u/puzzle-input "day19-input.txt"))))

(defn matrix-mult
  "Matrix multiply with v represented as a row (not column) vector"
  [matrix v]
  (mapv #(reduce + (map * % v)) matrix))

(defn dist
  "Manhattan distance"
  [a b]
  (reduce + (map (comp #(Math/abs %) -) a b)))

(defn overlap
  [a b]
  (set/intersection (set a) (set b)))

(defn rel-vec-overlaps
  [sensor-a sensor-b]
  (apply concat
         (map-indexed
          (fn [a-pos a]
            (map-indexed (fn [b-pos b]
                           [[a-pos b-pos] (overlap a b)]) (:rel-vec-sigs sensor-b))) (:rel-vec-sigs sensor-a))))

(defn at-least-twelve-overlap
  [sensor-a sensor-b]
  (->> (rel-vec-overlaps sensor-a sensor-b)
       (filter #(>= ((comp count second) %) 11))
       first))

(defn overlapping-relative-vectors
  [overlap-set sensor offset]
  (set (filter #(overlap-set (vector-signature %)) (get (:rel-vecs sensor) offset))))

(defn find-orientation
  [sensor-a sensor-b [[idx-a idx-b] overlap-set]]
  (let [rel-vecs-a (overlapping-relative-vectors overlap-set sensor-a idx-a)
        rel-vecs-b (overlapping-relative-vectors overlap-set sensor-b idx-b)
        permutations (map-indexed (fn [idx m]
                                    [idx (set (map (fn [v] (matrix-mult m v)) rel-vecs-b))]) all-orientation-permutations)
        match      (ffirst (filter #(= rel-vecs-a (second %)) permutations))]
    (when (nil? match)
      (println rel-vecs-a rel-vecs-b))
    (get all-orientation-permutations match)))

(defn reorient
  [orientation sensor]
  (update sensor :beacons (partial mapv (partial matrix-mult orientation))))

(defn find-offset
  [sensor-a sensor-b [idx-a idx-b]]
  (mapv - (get-in sensor-a [:beacons idx-a]) (get-in sensor-b [:beacons idx-b])))

(defn shift
  [offset sensor]
  (update sensor :beacons (partial mapv #(mapv + offset %))))

(defn orient-sensor-to-known
  [known sensor]
  (let [overlap (at-least-twelve-overlap known sensor)]
    (if (not overlap)
      nil
      (let [orientation (find-orientation known sensor overlap)
            oriented    (reorient orientation sensor)
            offset      (find-offset known oriented (first overlap))
            shifted     (shift offset oriented)]
        (assoc (add-relative-vectors shifted)
               :orientation orientation
               :offset offset)))))

(defn orient-against-all-known
  [known unknown]
  (first (filter some? (map #(orient-sensor-to-known % unknown) (vals known)))))

(defn orient-all-sensors
  [sensors]
  (loop [oriented (select-keys sensors [0])
         to-orient (u/without-keys sensors [0])]
    (if (empty? to-orient)
      oriented
      (let [orientable (u/fmap (partial orient-against-all-known oriented) to-orient)
            fixed      (into {} (filter (comp some? val) orientable))]
        (recur (merge oriented fixed)
               (u/without-keys to-orient (keys fixed)))))))

(defn all-beacons
  [sensors]
  (into #{} (mapcat :beacons (vals (orient-all-sensors sensors)))))

(defn day19-part1-soln
  []
  (count (all-beacons day19-input)))

(defn max-sensor-distance
  [sensors]
  (let [offsets (filter some? (map :offset (vals (orient-all-sensors sensors))))
        indices (range (count offsets))]
    (->> (mapv #(u/rotate % offsets) indices)
         (map #(map (partial dist (first %)) (rest %)))
         flatten
         (apply max))))

(defn day19-part2-soln
  []
  (max-sensor-distance day19-input))