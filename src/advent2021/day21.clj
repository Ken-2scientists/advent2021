(ns advent2021.day21
  (:require [advent-utils.core :as u]))

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

(def dirac-rolls
  {3 1
   4 3
   5 6
   6 7
   7 6
   8 3
   9 1})

(defn move
  [player [[score pos] cnt] [shift shift-cnt]]
  (let [newpos (mod (+ shift (get pos player)) 10)]
    {[(update score player + 1 newpos)
      (assoc pos player newpos)]
     (* cnt shift-cnt)}))

(defn move-all
  [player universe]
  (apply merge-with + (map (partial move player universe) dirac-rolls)))

(def winning-score 21)
(defn done?
  [[[score _] _]]
  (boolean (some #(>= % winning-score) score)))

(defn tally
  [acc [[[a b] _] cnt]]
  (if (> a b)
    (update acc 0 + cnt)
    (update acc 1 + cnt)))

(defn win-counts
  [universes player]
  (let [next-us    (->> universes
                        (map (partial move-all player))
                        (apply merge-with +))
        outcomes   (group-by done? next-us)
        winners    (get outcomes true)
        remaining  (into {} (get outcomes false))
        win-tally  (reduce tally [0 0] winners)]
    (if (empty? remaining)
      win-tally
      (mapv + win-tally (win-counts remaining (mod (inc player) 2))))))

(defn day21-part2-soln
  []
  (apply max (win-counts {[[0 0] day21-input] 1} 0)))