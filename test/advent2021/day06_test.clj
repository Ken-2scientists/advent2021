(ns advent2021.day06-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day06 :as t]))

(def day06-sample
  (t/parse ["3,4,3,1,2"]))

(deftest fish-after-n-days
  (testing "Computes the number of fish after n days with sample data"
    (is (= 26   (t/fish-after-n-days day06-sample 18)))
    (is (= 5934 (t/fish-after-n-days day06-sample 80)))))

(deftest day06-part1-soln
  (testing "Reproduces the answer for day06, part1"
    (is (= 386755 (t/day06-part1-soln)))))

;; (deftest day06-part2-soln
;;   (testing "Reproduces the answer for day06, part2"
;;     (is (= 20898 (t/day06-part2-soln)))))


