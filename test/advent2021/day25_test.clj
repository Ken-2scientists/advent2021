(ns advent2021.day25-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.ascii :as ascii]
            [advent2021.day25 :as t]))

(def day25-sample
  (ascii/ascii->map t/charmap
                    ["v...>>.vv>"
                     ".vv>>.vv.."
                     ">>.>v>...v"
                     ">>v>>.>.v."
                     "v>v.vv.v.."
                     ">.>>..v..."
                     ".vv..>.>v."
                     "v.v..>>v.v"
                     "....v..v.>"]))

(deftest steps-until-done-changing
  (testing "Computes the number of steps until no more changes occur"
    (is (= 58 (first (t/evolve-until-stop day25-sample))))))

(deftest day25-part1-soln
  (testing "Reproduces the answer for day25, part1"
    (is (= 429 (t/day25-part1-soln)))))

