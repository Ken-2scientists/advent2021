(ns advent2021.day10
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def day10-input (u/puzzle-input "day10-input.txt"))

(def illegal-char-points
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(def closing-char-points
  {\) 1
   \] 2
   \} 3
   \> 4})

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

(defn incomplete-segment
  [s]
  (let [collapsed-s (collapse s)]
    (if (first-illegal-char collapsed-s)
      nil
      collapsed-s)))

(def close-bracket
  {\( \)
   \{ \}
   \[ \]
   \< \>})

(defn closing-chars
  [s]
  (->> (reverse s)
       (map close-bracket)))

(defn completion-score-calc
  [acc c]
  (+ (* acc 5) (closing-char-points c)))

(defn completion-score
  [chars]
  (reduce completion-score-calc 0 chars))

(defn completion-scores
  [input]
  (->> input
       (map incomplete-segment)
       (filter some?)
       (map closing-chars)
       (map completion-score)))

(defn middle-completion-score
  [input]
  (let [scores (sort (completion-scores input))
        num    (count scores)]
    (nth scores (int (/ num 2)))))

(defn day10-part2-soln
  []
  (middle-completion-score day10-input))