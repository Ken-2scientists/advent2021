(ns advent2021.day04
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def bingo-grid
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
    {:values (into {} (map vector (flatten rows) bingo-grid))
     :drawn (vec (repeat 25 false))}))

(defn parse-input
  [lines]
  (let [groups (str/split (str/join "\n" lines) #"\n\n")]
    {:drawings (parse-drawings (first groups))
     :boards (mapv parse-board (rest groups))}))

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
        locs (filter some? (map #(if %2 %1 nil) bingo-grid drawn))]
    (map value->loc locs)))

(defn board-score
  [last-drawn board]
  (let [all-numbers (keys (:values board))
        called-numbers (set (drawn-values board))
        uncalled-numbers (filter (complement called-numbers) all-numbers)]
    (* (reduce + uncalled-numbers) last-drawn)))

(defn more-rounds?
  [input]
  (seq (:drawings input)))

(defn all-rounds
  [input]
  (->> (iterate mark-boards input)
       (take-while more-rounds?)))

(defn next-winning-round
  [input]
  (->> (all-rounds input)
       (drop-while no-winner?)
       first))

(defn first-winning-board-score
  [input]
  (let [{:keys [last-drawn boards]} (next-winning-round input)
        winning-board (first (filter (comp winning-board? :drawn) boards))]
    (board-score last-drawn winning-board)))

(defn day04-part1-soln
  []
  (first-winning-board-score day04-input))

(defn last-winning-board-score
  [input]
  (loop [round (next-winning-round input)]
    (if (= 1 (count (:boards round)))
      (board-score (:last-drawn round) (first (:boards round)))
      (let [losing-boards (filter (complement (comp winning-board? :drawn)) (:boards round))]
        (recur (next-winning-round (assoc round :boards losing-boards)))))))

(defn day04-part2-soln
  []
  (last-winning-board-score day04-input))