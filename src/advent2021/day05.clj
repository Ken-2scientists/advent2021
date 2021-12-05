(ns advent2021.day05
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-point
  [point]
  (->> (str/split point #",")
       (mapv read-string)))

(defn parse-line
  [line]
  (mapv parse-point (str/split line #" -> ")))

(def day05-sample
  (map parse-line
       ["0,9 -> 5,9"
        "8,0 -> 0,8"
        "9,4 -> 3,4"
        "2,2 -> 2,1"
        "7,0 -> 7,4"
        "6,4 -> 2,0"
        "0,9 -> 2,9"
        "3,4 -> 1,4"
        "0,0 -> 8,8"
        "5,5 -> 8,2"]))

(def day05-input (map parse-line (u/puzzle-input "day05-input.txt")))

(defn diagonal?
  [[[x1 y1] [x2 y2]]]
  (and (not= x1 x2) (not= y1 y2)))

(defn line-points
  [[[x1 y1] [x2 y2]]]
  (if (and (not= x1 x2) (not= y1 y2))
    (let [shiftx (if (> x2 x1) 1 -1)
          shifty (if (> y2 y1) 1 -1)]
      (for [dx (range 0 (+ (- x2 x1) shiftx) shiftx)]
        [(+ x1 dx) (+ y1 (* shifty shiftx dx))]))
    (if (= y1 y2)
      (let [shift (if (> x2 x1) 1 -1)]
        (for [x (range x1 (+ x2 shift) shift)] [x y1]))
      (let [shift (if (> y2 y1) 1 -1)]
        (for [y (range y1 (+ y2 shift) shift)] [x1 y])))))

(defn overlapping-points
  [input include-diagonal?]
  (let [lines (if include-diagonal?
                input
                (filter (complement diagonal?) input))]
    (->> lines
         (mapcat line-points)
         frequencies
         (filter #(>= (val %) 2))
         count)))

(defn day05-part1-soln
  []
  (overlapping-points day05-input false))

(defn day05-part2-soln
  []
  (overlapping-points day05-input true))

