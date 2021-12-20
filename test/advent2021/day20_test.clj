(ns advent2021.day20-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2021.day20 :as t]))

(def day20-sample
  (t/parse
   ["..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"
    ""
    "#..#."
    "#...."
    "##..#"
    "..#.."
    "..###"]))

(deftest illuminated-after-2-enhance
  (testing "Computes the number of pixels lit up after 2 enhancements"
    (is (= 35 (t/illuminated (t/enhance-n-times day20-sample 2))))))

(deftest day20-part1-soln
  (testing "Reproduces the answer for day20, part1"
    (is (= 5186 (t/day20-part1-soln)))))

;; (deftest day20-part2-soln
;;   (testing "Reproduces the answer for day20, part2"
;;     (is (= 2380061249 (t/day20-part2-soln)))))
