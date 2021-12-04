(ns advent2021.day03-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day03 :as t]))

(def day03-sample
  ["00100"
   "11110"
   "10110"
   "10111"
   "10101"
   "01111"
   "00111"
   "11100"
   "10000"
   "11001"
   "00010"
   "01010"])

(deftest power-consumption
  (testing "Computes gamma and epsilon values from sample data"
    (is (= [22 9] (t/power-consumption day03-sample)))))

(deftest gamma-and-epsilon
  (testing "Computes oxygen and co2 values from sample data"
    (is (= [23 10] (t/life-support day03-sample)))))

(deftest day03-part1-soln
  (testing "Reproduces the answer for day03, part1"
    (is (= 3959450 (t/day03-part1-soln)))))

(deftest day03-part2-soln
  (testing "Reproduces the answer for day03, part2"
    (is (= 7440311 (t/day03-part2-soln)))))