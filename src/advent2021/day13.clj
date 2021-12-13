(ns advent2021.day13
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-coord
  [line]
  (mapv read-string (str/split line #",")))

(defn parse-fold
  [line]
  (let [[l r] (str/split line #"=")]
    {:axis (keyword (str (last l)))
     :loc  (read-string r)}))

(defn parse
  [input]
  (let [[coords folds] (-> (str/join "\n" input)
                           (str/split #"\n\n"))]
    {:coords (map parse-coord (str/split coords #"\n"))
     :folds  (map parse-fold  (str/split folds #"\n"))}))

(def day13-input (parse (u/puzzle-input "day13-input.txt")))

(defn fold-y
  [loc [x y]]
  (if (<= y loc)
    [x y]
    [x (- (* 2 loc) y)]))

(defn fold-x
  [loc [x y]]
  (if (<= x loc)
    [x y]
    [(- (* 2 loc) x) y]))

(defn fold
  [coords {:keys [axis loc]}]
  (case axis
    :y (set (map (partial fold-y loc) coords))
    :x (set (map (partial fold-x loc) coords))))

(defn dots-after-first-fold
  [{:keys [coords folds]}]
  (count (fold coords (first folds))))

(defn day13-part1-soln
  []
  (dots-after-first-fold day13-input))

(defn complete-folds
  [{:keys [coords folds]}]
  (reduce fold coords folds))

(defn pprint-paper
  [coords]
  (let [xmax (apply max (map first coords))
        ymax (apply max (map second coords))
        chars (for [y (range (inc ymax))
                    x (range (inc xmax))]
                (if (coords [x y]) \# \ ))]
    (str/join "\n"
              (map #(apply str %)
                   (partition (inc xmax) chars)))))

(defn day13-part2-soln
  []
  (->> (complete-folds day13-input)
       pprint-paper
       println))