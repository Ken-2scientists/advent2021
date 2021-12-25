(ns advent2021.day17-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day17 :as t]))

;; target area: x=20..30, y=-10..-5
(def day17-sample
  {:xmin 20
   :xmax 30
   :ymin -10
   :ymax -5})

(deftest hits-target?
  (testing "Correctly identifies whether probe ever hits target"
    (is (= true  (t/will-hit-target? day17-sample [7 2])))
    (is (= true  (t/will-hit-target? day17-sample [6 3])))
    (is (= true  (t/will-hit-target? day17-sample [9 0])))
    (is (= true  (t/will-hit-target? day17-sample [6 9])))
    (is (= false (t/will-hit-target? day17-sample [17 -4])))))

(deftest viable-init-vels
  (testing "Identifies the viable initial velocities that hit the sample target"
    (is (= 112 (t/viable-init-vels day17-sample 10)))))

(deftest day17-part1-soln
  (testing "Reproduces the answer for day17, part1"
    (is (= 2701 (t/day17-part1-soln)))))

(deftest day17-part2-soln
  (testing "Reproduces the answer for day17, part2"
    (is (= 1070 (t/day17-part2-soln)))))
