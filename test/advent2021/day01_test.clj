(ns advent2021.day01-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day01 :as t]))

(def day01-sample
  [199
   200
   208
   210
   200
   207
   240
   269
   260
   263])

(deftest number-of-increases
  (testing "Finds the number of times the depth increases relative to the previous value"
    (is (= 7 (t/increases day01-sample)))))

(deftest sliding-window-sums
  (testing "Correctly adds up each triplet of 3 values in a sliding window"
    (is [607
         618
         618
         617
         647
         716
         769
         792]
        (t/sliding-window-sum day01-sample))))

(deftest day01-part1-soln
  (testing "Reproduces the answer for day01, part1"
    (is (= 1292 (t/day01-part1-soln)))))

(deftest day01-part2-soln
  (testing "Reproduces the answer for day01, part2"
    (is (= 1262 (t/day01-part2-soln)))))