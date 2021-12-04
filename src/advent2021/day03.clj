(ns advent2021.day03
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def day03-input
  (u/puzzle-input "day03-input.txt"))

(defn most-common-nth-bit
  [signals bit]
  (let [freqs (frequencies (map #(nth % bit) signals))]
    (if (= (val (first freqs)) (val (second freqs)))
      \1
      (ffirst (sort-by val > freqs)))))

(defn least-common-nth-bit
  [signals bit]
  (let [freqs (frequencies (map #(nth % bit) signals))]
    (if (= (val (first freqs)) (val (second freqs)))
      \0
      (ffirst (sort-by val < freqs)))))

(defn most-common-bits
  [signals]
  (map (partial most-common-nth-bit signals) (range (count (first signals)))))

(defn negate
  [bits]
  (map {\0 \1 \1 \0} bits))

(defn power-consumption
  [signals]
  (let [gamma (most-common-bits signals)
        epsilon (negate gamma)]
    (->> [gamma epsilon]
         (map str/join)
         (map #(Integer/parseInt % 2)))))

(defn day03-part1-soln
  []
  (reduce * (power-consumption day03-input)))

(defn bit-criteria
  [signals bit-check]
  (loop [bit-pos 0 remaining signals]
    (if (= 1 (count remaining))
      remaining
      (let [common-bit (bit-check remaining bit-pos)]
        (recur (inc bit-pos) (filter #(= common-bit (nth % bit-pos)) remaining))))))

(defn oxygen-generator
  [signals]
  (first (bit-criteria signals most-common-nth-bit)))

(defn co2-scrubber
  [signals]
  (first (bit-criteria signals least-common-nth-bit)))

(defn life-support
  [signals]
  [(Integer/parseInt (oxygen-generator signals) 2)
   (Integer/parseInt (co2-scrubber signals) 2)])

(defn day03-part2-soln
  []
  (reduce * (life-support day03-input)))