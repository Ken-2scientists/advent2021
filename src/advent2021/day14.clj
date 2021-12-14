(ns advent2021.day14
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-rule
  [line]
  (let [[[a b] insert] (str/split line #" -> ")]
    [[a b] (first insert)]))

(defn parse
  [input]
  (let [[template rules] (-> (str/join "\n" input)
                             (str/split #"\n\n"))]
    {:template template
     :rules (into {} (map parse-rule (str/split rules #"\n")))}))

(def day14-input (parse (u/puzzle-input "day14-input.txt")))

(defn pair-insert
  [template rules]
  (let [pairs (partition 2 1 template)]
    (apply str
           (->
            (interleave template (mapv (partial get rules) pairs))
            vec
            (conj (last template))))))

(defn step
  [{:keys [template rules]}]
  {:template (pair-insert template rules)
   :rules rules})

(defn direct-most-minus-least-common-at-n
  [input n]
  (let [freqs (->> (nth (iterate step input) n)
                   :template
                   frequencies
                   (sort-by second))
        most  (second (last freqs))
        least (second (first freqs))]
    (- most least)))

(defn day14-part1-soln
  []
  (direct-most-minus-least-common-at-n day14-input 10))

(defn pair-freq-insert
  [rules [[a b] freq]]
  (let [insert (get rules [a b])]
    {[a insert] freq
     [insert b] freq}))

(defn pair-freq-step
  [{:keys [pairs rules]}]
  {:rules rules
   :pairs (apply merge-with +
                 (map (partial pair-freq-insert rules) pairs))})

(defn pair-freq-form
  [{:keys [template rules]}]
  {:rules rules
   :pairs (frequencies (partition 2 1 template))})

(defn pair-counts
  [[[a b] count]]
  (if (= a b)
    {a (* 2 count)}
    {a count b count}))

(defn frequencies-at-n
  [input n]
  (let [pair-form (pair-freq-form input)
        double-freqs (->> (nth (iterate pair-freq-step pair-form) n)
                          :pairs
                          (map pair-counts)
                          (apply merge-with +))]
    (u/fmap #(long (Math/ceil (/ % 2))) double-freqs)))

(defn most-minus-least-common-at-n
  [input n]
  (let [freqs (->> (frequencies-at-n input n)
                   (sort-by second))
        most  (second (last freqs))
        least (second (first freqs))]
    (- most least)))

(defn day14-part2-soln
  []
  (most-minus-least-common-at-n day14-input 40))