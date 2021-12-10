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

(deftest completion-scores
  (testing "Computes completion scores for sample data"
    (is (= '(288957 5566 1480781 995444 294)
           (t/completion-scores day10-sample)))))

(deftest middle-completion-score
  (testing "Finds the median completion score for sample data"
    (is (= 288957 (t/middle-completion-score day10-sample)))))

(deftest day10-part1-soln
  (testing "Reproduces the answer for day10, part1"
    (is (= 394647 (t/day10-part1-soln)))))

(deftest day10-part2-soln
  (testing "Reproduces the answer for day10, part2"
    (is (= 2380061249 (t/day10-part2-soln)))))