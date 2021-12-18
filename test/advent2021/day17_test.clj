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
    (is (= true  (t/hits-target? day17-sample [7 2])))
    (is (= true  (t/hits-target? day17-sample [6 3])))
    (is (= true  (t/hits-target? day17-sample [9 0])))
    (is (= true  (t/hits-target? day17-sample [6 9])))
    (is (= false (t/hits-target? day17-sample [17 -4])))))

;; (deftest day17-part1-soln
;;   (testing "Reproduces the answer for day17, part1"
;;     (is (= 394647 (t/day17-part1-soln)))))

;; (deftest day17-part2-soln
;;   (testing "Reproduces the answer for day17, part2"
;;     (is (= 2380061249 (t/day17-part2-soln)))))
