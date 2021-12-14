(ns advent2021.day14-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day14 :as t]))

(def day14-sample
  (t/parse
   ["NNCB"
    ""
    "CH -> B"
    "HH -> N"
    "CB -> H"
    "NH -> C"
    "HB -> C"
    "HC -> B"
    "HN -> C"
    "NN -> C"
    "BH -> H"
    "NC -> B"
    "NB -> B"
    "BN -> B"
    "BB -> N"
    "BC -> B"
    "CC -> N"
    "CN -> C"]))

(deftest substitution-rule
  (testing "The pair-insert substitution rule works on sample"
    (is (= "NCNBCHB"
           (:template (t/step day14-sample))))
    (is (= "NBCCNBBBCBHCB"
           (:template (t/step (t/step day14-sample)))))
    (is (= "NBBBCNCCNBBNBNBBCHBHHBCHB"
           (:template (t/step (t/step (t/step day14-sample))))))
    (is (= "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"
           (:template (t/step (t/step (t/step (t/step day14-sample)))))))))

(deftest direct-most-minus-least-common-at-ten
  (testing "Most frequent element minus least common after 10 steps using direct impl"
    (is (= 1588 (t/direct-most-minus-least-common-at-n day14-sample 10)))))

(deftest most-minus-least-common-at-ten
  (testing "Most frequent element minus least common after 10 steps using efficient impl"
    (is (= 1588 (t/most-minus-least-common-at-n day14-sample 10)))
    (is (= 2188189693529 (t/most-minus-least-common-at-n day14-sample 40)))))

(deftest day14-part1-soln
  (testing "Reproduces the answer for day14, part1"
    (is (= 2712 (t/day14-part1-soln)))))

(deftest day14-part2-soln
  (testing "Reproduces the answer for day14, part2"
    (is (= 8336623059567 (t/day14-part2-soln)))))
