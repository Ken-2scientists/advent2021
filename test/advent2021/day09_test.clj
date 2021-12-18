(ns advent2021.day09-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.maze :as m]
            [advent2021.day09 :as t]))

(def day09-sample
  (m/grid-of
   (map t/parse-line
        ["2199943210"
         "3987894921"
         "9856789892"
         "8767896789"
         "9899965678"])))

(deftest risk-level-sum
  (testing "Adds up the 'risk levels' of the lowest points in sample"
    (is (= 15 (t/risk-level-sum day09-sample)))))

(deftest three-largest-basins-product
  (testing "multiplies the basin size of the three largest basins in sample"
    (is (= 1134 (t/three-largest-basins-product day09-sample)))))

(deftest day09-part1-soln
  (testing "Reproduces the answer for day09, part1"
    (is (= 588 (t/day09-part1-soln)))))

(deftest day09-part2-soln
  (testing "Reproduces the answer for day09, part2"
    (is (= 964712 (t/day09-part2-soln)))))