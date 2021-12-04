(ns advent2021.day04
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def day04-sample
  ["7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"
   ""
   "22 13 17 11  0"
   " 8  2 23  4 24"
   "21  9 14 16  7"
   " 6 10  3 18  5"
   " 1 12 20 15 19"
   ""
   " 3 15  0  2 22"
   " 9 18 13 17  5"
   "19  8  7 25 23"
   "20 11 10 24  4"
   "14 21 16 12  6"
   ""
   "14 21 17 24  4"
   "10 16 15  9 19"
   "18  8 23 26 20"
   "22 11 13  6  5"
   " 2  0 12  3  7"])

(def grid
  (for [y (range 5)
        x (range 5)]
    [x y]))

(defn parse-drawings
  [line]
  (map read-string (str/split line #",")))

(defn parse-board-row
  [row]
  (->> (str/split row #" +")
       (map read-string)))

(defn parse-board
  [s]
  (let [rows (->> (str/split s #"\n")
                  (map (comp parse-board-row str/trim)))]
    {:values (into {} (map vector (flatten rows) grid))
     :drawn (vec (repeat 25 false))}))

(defn parse-input
  [lines]
  (let [groups (str/split (str/join "\n" lines) #"\n\n")]
    {:drawings (parse-drawings (first groups))
     :boards (map parse-board (rest groups))}))

(def day04-input (parse-input (u/puzzle-input "day04-input.txt")))


(defn winning-board?
  [drawn]
  (let [rows (partition 5 drawn)
        cols (->> (map #(u/rotate % drawn) (range 5))
                  (map (partial take-nth 5)))]
    (some true? (map (partial every? true?) (concat rows cols)))))

(defn no-winner?
  [{:keys [boards]}]
  (not-any? winning-board? (map :drawn boards)))

(defn mark-board
  [number {:keys [values drawn] :as board}]
  (if (values number)
    (let [[x y] (values number)
          index (+ (* y 5) x)]
      {:values values
       :drawn (assoc drawn index true)})
    board))

(defn mark-boards
  [{:keys [drawings boards]}]
  {:last-drawn (first drawings)
   :drawings (rest drawings)
   :boards (map (partial mark-board (first drawings)) boards)})

(defn drawn-values
  [{:keys [values drawn]}]
  (let [value->loc (u/invert-map values)
        locs (filter some? (map #(if %2 %1 nil) grid drawn))]
    (map value->loc locs)))

(defn board-score
  [last-value board]
  (let [all-values (keys (:values board))
        selected (set (drawn-values board))]
    (* (reduce + (filter (complement selected) all-values))
       last-value)))

(defn winning-board-score
  [input]
  (let [winning-round (->> (iterate mark-boards input)
                           (drop-while no-winner?)
                           first)
        {:keys [last-drawn boards]} winning-round
        winning-board (first (filter (comp winning-board? :drawn) boards))]
    (board-score last-drawn winning-board)))

(defn day04-part1-soln
  []
  (winning-board-score day04-input))
