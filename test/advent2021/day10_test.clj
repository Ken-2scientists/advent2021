(ns advent2021.day10-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day10 :as t]))

(def day10-sample
  ["[({(<(())[]>[[{[]{<()<>>"
   "[(()[<>])]({[<{<<[]>>("
   "{([(<{}[<>[]}>{[]{[(<()>"
   "(((({<>}<{<{<>}{[]{[]{}"
   "[[<[([]))<([[{}[[()]]]"
   "[{[{({}]{}}([{[{{{}}([]"
   "{<[[]]>}<{[{[{[]{()[[[]"
   "[<(<(<(<{}))><([]([]()"
   "<{([([[(<>()){}]>(<<{{"
   "<{([{{}}[<[[[<>{}]]]>[]]"])

(deftest syntax-error-score
  (testing "Sum of illegal char scores in sample"
    (is (= 26397 (t/syntax-error-score day10-sample)))))

(deftest day10-part1-soln
  (testing "Reproduces the answer for day10, part1"
    (is (= 394647 (t/day10-part1-soln)))))

;; (deftest day10-part2-soln
;;   (testing "Reproduces the answer for day10, part2"
;;     (is (= 964712 (t/day10-part2-soln)))))