(ns advent2021.day21
  (:require [advent-utils.core :as u]))

;; Sample data
;; Player 1 starting position: 4
;; Player 2 starting position: 8
(def day21-sample [3 7])

;; (def day21-input (u/puzzle-input "day21-input.txt"))
(def day21-input [8 9])

(defn deterministic-die
  [roll]
  (take 3 (u/rotate (* 3 roll) (range 1 101))))

(defn turn
  [{:keys [roll player spaces score die]}]
  (let [move     (reduce + (die roll))
        newspace (mod (+ move (get spaces player)) 10)
        newscore (+ 1 newspace (get score player))]
    {:roll   (inc roll)
     :player (if (zero? player) 1 0)
     :spaces (assoc spaces player newspace)
     :score  (assoc score player newscore)
     :die    die}))

(defn play
  [input]
  (let [start {:roll 0
               :player 0
               :spaces input
               :score [0 0]
               :die deterministic-die}]
    (iterate turn start)))

(defn no-winner?
  [{:keys [score]}]
  (< (apply max score) 1000))

(defn play-until-win
  [input]
  (first (drop-while no-winner? (play input))))

(defn loser-score-times-die-rolls
  [{:keys [score roll]}]
  (* 3 roll (apply min score)))

(defn day21-part1-soln
  []
  (loser-score-times-die-rolls (play-until-win day21-input)))
