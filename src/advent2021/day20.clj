(ns advent2021.day20
  (:require [clojure.string :as str]
            [advent-utils.ascii :as a]
            [advent-utils.core :as u]))

;; TODO: Candidate for common utility
(defn split-at-blankline
  [input]
  (let [chunks (-> (str/join "\n" input)
                   (str/split #"\n\n"))]
    (map #(str/split % #"\n") chunks)))

(def char-map {\. 0 \# 1})

(defn parse
  [input]
  (let [[part1 part2] (split-at-blankline input)]
    {:algorithm (mapv char-map (first part1))
     :image (a/ascii->map char-map part2)}))

(def day20-sample
  (parse
   ["..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"
    ""
    "#..#."
    "#...."
    "##..#"
    "..#.."
    "..###"]))

(def day20-input (parse (u/puzzle-input "day20-input.txt")))

;; TODO variant of adj-coords. Consider consolidating
(defn three-by-cell
  [[x y]]
  (for [ny (range (dec y) (+ y 2))
        nx (range (dec x) (+ x 2))]
    [nx ny]))

;; TODO make this a utility fn
(defn binstr->int
  [s]
  (Integer/parseInt s 2))

(defn next-pixel
  [{:keys [algorithm image]} pos]
  (->> (three-by-cell pos)
       (map #(get-in image [:grid %] 0))
       (apply str)
       binstr->int
       (get algorithm)))

(defn enhance
  [{:keys [image] :as input}]
  (let [{:keys [width height]} image
        coords (for [y (range -1 (+ height 1))
                     x (range -1 (+ width 1))]
                 [x y])
        vals   (map (partial next-pixel input) coords)
        adj-coords (map (fn [[a b]] [(inc a) (inc b)]) coords)]
    (assoc input :image
           {:width (+ 2 width)
            :height (+ 2 height)
            :grid (zipmap adj-coords vals)})))

(defn enhance-n-times
  [input n]
  (nth (iterate enhance input) n))

(defn illuminated
  [{:keys [image]}]
  (->> image
       :grid
       vals
       (filter pos?)
       count))

(defn day20-part1-soln
  []
  (illuminated (enhance-n-times day20-input 2)))