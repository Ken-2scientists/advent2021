(ns advent2021.day12-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day12 :as t]))

(def day12-sample1
  (t/parse
   ["start-A"
    "start-b"
    "A-c"
    "A-b"
    "b-d"
    "A-end"
    "b-end"]))

(def day12-sample2
  (t/parse
   ["dc-end"
    "HN-start"
    "start-kj"
    "dc-start"
    "dc-HN"
    "LN-dc"
    "HN-end"
    "kj-sa"
    "kj-HN"
    "kj-dc"]))

(def day12-sample3
  (t/parse
   ["fs-end"
    "he-DX"
    "fs-he"
    "start-DX"
    "pj-DX"
    "end-zg"
    "zg-sl"
    "zg-pj"
    "pj-he"
    "RW-he"
    "fs-DX"
    "pj-RW"
    "zg-RW"
    "start-pj"
    "he-WI"
    "zg-he"
    "pj-fs"
    "start-RW"]))

(deftest count-of-map-paths
  (testing "Correctly counts the number of unique paths in sample data"
    (is (= 10  (count (t/map-cave day12-sample1))))
    (is (= 19  (count (t/map-cave day12-sample2))))
    (is (= 226 (count (t/map-cave day12-sample3))))))

(deftest day12-part1-soln
  (testing "Reproduces the answer for day12, part1"
    (is (= 5874 (t/day12-part1-soln)))))

;; (deftest day12-part2-soln
;;   (testing "Reproduces the answer for day12, part2"
;;     (is (= 2380061249 (t/day12-part2-soln)))))
