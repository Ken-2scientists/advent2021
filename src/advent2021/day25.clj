(ns advent2021.day25
  (:require [advent-utils.ascii :as ascii]
            [advent-utils.core :as u]))

(def charmap {\v :down \> :right \. :open})

(def day25-input
  (ascii/ascii->map charmap (u/puzzle-input "day25-input.txt")))

(defn available?
  [grid pos]
  (= :open (get grid pos)))

(defn right-from
  [{:keys [width]} [x y]]
  (if (= (inc x) width)
    [0 y]
    [(inc x) y]))

(defn down-from
  [{:keys [height]} [x y]]
  (if (= (inc y) height)
    [x 0]
    [x (inc y)]))

(defn move-dir
  [label direction {:keys [grid] :as state}]
  (let [locs        (map first (filter #(= label (val %)) grid))
        moveto-locs (map (partial direction state) locs)
        moves      (->> (zipmap locs moveto-locs)
                        (filter #(available? grid (val %)))
                        (into {}))]
    (update state :grid merge
            (zipmap (keys moves) (repeat :open))
            (zipmap (vals moves) (repeat label)))))

(defn step
  [state]
  (->> state
       (move-dir :right right-from)
       (move-dir :down down-from)))

(defn evolve-until-stop
  [input]
  (loop [last-state input curr-state (step input) steps 1]
    (if (= last-state curr-state)
      [steps curr-state]
      (recur curr-state (step curr-state) (inc steps)))))

(defn day25-part1-soln
  []
  (first (evolve-until-stop day25-input)))
