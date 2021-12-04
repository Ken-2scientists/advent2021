(ns advent2021.day02-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day02 :as t]))

(def day02-example
  (map t/parse-line
       ["forward 5"
        "down 5"
        "forward 8"
        "up 3"
        "down 8"
        "forward 2"]))

(deftest sub-moves-correctly-part1
  (testing "Sub ends up in the correct location following the part 1 rules"
    (is (= [15 10] (t/sub-end-state day02-example t/part1-rules)))))

(deftest sub-moves-correctly-part2
  (testing "Sub ends up in the correct location following the part 2 rules"
    (is (= [15 60 10] (t/sub-end-state day02-example t/part2-rules)))))

(deftest day02-part1-soln
  (testing "Reproduces the answer for day02, part1"
    (is (= 2272262 (t/day02-part1-soln)))))

(deftest day02-part2-soln
  (testing "Reproduces the answer for day02, part2"
    (is (= 2134882034 (t/day02-part2-soln)))))