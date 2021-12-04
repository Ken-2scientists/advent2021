(ns advent2021.day04-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day04 :as t]))

(def day04-sample
  (t/parse-input
   ["7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"
    ""
    "22 13 17 11  0"
    " 8  2 23  4 24"
    "21  9 14 16  7"
    " 6 10  3 18  5"
    " 1 12 20 15 19"
    ""
    " 3 15  0  2 22"
    " 9 18 13 17  5"
    "19  8  7 25 23"
    "20 11 10 24  4"
    "14 21 16 12  6"
    ""
    "14 21 17 24  4"
    "10 16 15  9 19"
    "18  8 23 26 20"
    "22 11 13  6  5"
    " 2  0 12  3  7"]))

(deftest sample-winning-score
  (testing "Computes the board score of the sample data winner"
    (is (= 4512 (t/winning-board-score day04-sample)))))

;; (deftest gamma-and-epsilon
;;   (testing "Computes oxygen and co2 values from sample data"
;;     (is (= [23 10] (t/life-support day04-sample)))))

(deftest day04-part1-soln
  (testing "Reproduces the answer for day04, part1"
    (is (= 23177 (t/day04-part1-soln)))))

;; (deftest day04-part2-soln
;;   (testing "Reproduces the answer for day04, part2"
;;     (is (= 7440311 (t/day04-part2-soln)))))