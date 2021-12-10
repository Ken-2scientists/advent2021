(ns advent2021.day10
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def day10-sample
  ["[({(<(())[]>[[{[]{<()<>>"
   "[(()[<>])]({[<{<<[]>>("
   "{([(<{}[<>[]}>{[]{[(<()>"
   "(((({<>}<{<{<>}{[]{[]{}"
   "[[<[([]))<([[{}[[()]]]"
   "[{[{({}]{}}([{[{{{}}([]"
   "{<[[]]>}<{[{[{[]{()[[[]"
   "[<(<(<(<{}))><([]([]()"
   "<{([([[(<>()){}]>(<<{{"
   "<{([{{}}[<[[[<>{}]]]>[]]"])

(def day10-input (u/puzzle-input "day10-input.txt"))

(def illegal-char-points
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(defn simplify
  [s]
  (str/replace s #"\(\)|\{\}|\[\]|\<\>" ""))

(defn collapse
  [s]
  (if (= (simplify s) s)
    s
    (collapse (simplify s))))

(defn first-illegal-char
  [s]
  (second (re-find #"[\(\{\[\<][\)\]\}\>]" s)))

(defn syntax-error-score
  [input]
  (->> input
       (map collapse)
       (map first-illegal-char)
       (filter some?)
       (map illegal-char-points)
       (reduce +)))

(defn day10-part1-soln
  []
  (syntax-error-score day10-input))
