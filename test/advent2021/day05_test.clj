(ns advent2021.day05-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day05 :as t]))

(def day05-sample
  (map t/parse-line
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

(deftest non-diagonal-overlapping-points
  (testing "Computes the number of overlapping points in sample data"
    (is (= 5 (t/overlapping-points day05-sample false)))))

(deftest diagonal-line-points
  (testing "Computes the points along a line for diagonals"
    (is (= [[8 0] [7 1] [6 2] [5 3] [4 4] [3 5] [2 6] [1 7] [0 8]]
           (t/line-points (nth day05-sample 1))))
    (is (= [[6 4] [5 3] [4 2] [3 1] [2 0]]
           (t/line-points (nth day05-sample 5))))
    (is (= [[0 0] [1 1] [2 2] [3 3] [4 4] [5 5] [6 6] [7 7] [8 8]]
           (t/line-points (nth day05-sample 8))))
    (is (= [[5 5] [6 4] [7 3] [8 2]]
           (t/line-points (nth day05-sample 9))))))

(deftest all-overlapping-points
  (testing "Counts the number of overlapping points in sample data, including diagonals"
    (is (= 12 (t/overlapping-points day05-sample true)))))

(deftest day05-part1-soln
  (testing "Reproduces the answer for day05, part1"
    (is (= 7674 (t/day05-part1-soln)))))

(deftest day05-part2-soln
  (testing "Reproduces the answer for day05, part2"
    (is (= 20898 (t/day05-part2-soln)))))