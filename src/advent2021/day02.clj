(ns advent2021.day02
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-line
  [line]
  (let [[cmd val] (str/split line #" ")]
    [(keyword cmd) (read-string val)]))

(def day02-input
  (map parse-line (u/puzzle-input "day02-input.txt")))

(defn part1-rules
  [[pos depth] [cmd val]]
  (case cmd
    :forward [(+ pos val) depth]
    :down    [pos (+ depth val)]
    :up      [pos (- depth val)]))

(defn part2-rules
  [[pos depth aim] [cmd val]]
  (case cmd
    :forward [(+ pos val) (+ depth (* aim val)) aim]
    :down    [pos depth (+ aim val)]
    :up      [pos depth (- aim val)]))

(defn sub-end-state
  [cmds rules]
  (reduce rules [0 0 0] cmds))

(defn day02-part1-soln
  []
  (reduce * (sub-end-state day02-input part1-rules)))

(defn day02-part2-soln
  []
  (reduce * (drop-last (sub-end-state day02-input part2-rules))))