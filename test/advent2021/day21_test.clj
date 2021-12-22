(ns advent2021.day21-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day21 :as t]))

;; Sample data
;; Player 1 starting position: 4
;; Player 2 starting position: 8
(def day21-sample [3 7])

(deftest roll-until-win
  (testing "Determines when game ends for sample positions"
    (is (= {:roll 331 :player 1 :spaces [9 2] :score [1000 745]}
           (select-keys (t/play-until-win day21-sample)
                        [:roll :player :spaces :score])))))

(deftest loser-score-times-die-rolls
  (testing "Computes the product of the loser score and number of die rolls"
    (is (= 739785 (t/loser-score-times-die-rolls (t/play-until-win day21-sample))))))

(deftest day21-part1-soln
  (testing "Reproduces the answer for day21, part1"
    (is (= 707784 (t/day21-part1-soln)))))

;; (deftest day21-part2-soln
;;   (testing "Reproduces the answer for day21, part2"
;;     (is (= 2380061249 (t/day21-part2-soln)))))
