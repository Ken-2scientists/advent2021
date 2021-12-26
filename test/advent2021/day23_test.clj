(ns advent2021.day23-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day23 :as t]))

(def day23-sample1
  ;; "#############"
  ;; "#...........#"
  ;; "###B#C#B#D###"
  ;; "  #A#D#C#A#  "
  ;; "  #########  "
  {7  {:type :b}
   8  {:type :a}
   9  {:type :c}
   10 {:type :d}
   11 {:type :b}
   12 {:type :c}
   13 {:type :d}
   14 {:type :a}})

(def day23-sample1-soln
  [[11 2]
   [9 11]
   [10 3]
   [2 10]
   [7 9]
   [13 4]
   [14 5]
   [4 14]
   [3 13]
   [5 7]])

(deftest cost-of-moves
  (testing "Computes the costs of the moves identified in sample solution"
    (is (= 12521 (t/cost-of-moves day23-sample1 day23-sample1-soln)))))

(deftest day23-part1-soln
  (testing "Reproduces the answer for day23, part1"
    (is (= 16489 (t/day23-part1-soln)))))

;; (deftest day23-part2-soln
;;   (testing "Reproduces the answer for day23, part2"
;;     (is (= 2380061249 (t/day23-part2-soln)))))
