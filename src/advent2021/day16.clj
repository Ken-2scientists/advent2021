(ns advent2021.day16
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

;; From https://groups.google.com/g/clojure-dev/c/NaAuBz6SpkY?pli=1
(defn take-until
  "Returns a lazy sequence of successive items from coll until
   (pred item) returns true, including that item. pred must be
   free of side-effects."
  [pred coll]
  (lazy-seq
   (when-let [s (seq coll)]
     (if (pred (first s))
       (cons (first s) nil)
       (cons (first s) (take-until pred (rest s)))))))

(declare decode)
(def hex-sub
  {\0 "0000"
   \1 "0001"
   \2 "0010"
   \3 "0011"
   \4 "0100"
   \5 "0101"
   \6 "0110"
   \7 "0111"
   \8 "1000"
   \9 "1001"
   \A "1010"
   \B "1011"
   \C "1100"
   \D "1101"
   \E "1110"
   \F "1111"})

(defn parse
  [s]
  (str/join (mapcat hex-sub s)))

(defn binstr->long
  [s]
  (Long/parseLong s 2))

(def day16-input (parse (first (u/puzzle-input "day16-input.txt"))))

(defn decode-literal
  [s]
  (let [chunks (take-until #(= \0 (first %))
                           (partition 5 s))
        bits   (* 5 (count chunks))]
    {:bits (+ 6 bits)
     :value (-> (mapcat rest chunks)
                str/join
                binstr->long)}))

(defn decode-subpackets
  [s limit limit-type]
  (loop [packets [] remainder s total 0]
    (if (= total limit)
      {:subpackets packets
       :bits (+ (case limit-type
                  :count  18
                  :length 22)
                (reduce + (map :bits packets)))}
      (let [next-packet (decode remainder)
            skip  (:bits next-packet)]
        (recur (conj packets next-packet)
               (subs remainder skip)
               (case limit-type
                 :length (+ total skip)
                 :count  (inc total)))))))

(defn decode-operator
  [s]
  (let [length-type (binstr->long (subs s 0 1))]
    (case length-type
      0 (let [length (binstr->long (subs s 1 16))]
          (assoc (decode-subpackets (subs s 16) length :length)
                 :length length))
      1 (let [count (binstr->long (subs s 1 12))]
          (assoc (decode-subpackets (subs s 12) count :count)
                 :count count)))))

(defn decode
  [s]
  (let [version (binstr->long (subs s 0 3))
        type    (binstr->long (subs s 3 6))
        result  {:version version
                 :type    type}]
    (case type
      4 (merge result (decode-literal (subs s 6)))
      (merge result (decode-operator (subs s 6))))))

(defn version-sum
  [accumulator decoded]
  (if (:subpackets decoded)
    (+ (:version decoded)
       (reduce version-sum accumulator (:subpackets decoded)))
    (+ accumulator (:version decoded))))

(defn day16-part1-soln
  []
  (version-sum 0 (decode day16-input)))

(defn apply-operator
  [decoded]
  (let [subvals (map apply-operator (:subpackets decoded))]
    (case (:type decoded)
      0 (reduce + subvals)
      1 (reduce * subvals)
      2 (apply min subvals)
      3 (apply max subvals)
      4 (:value decoded)
      5 (if (> (first subvals) (second subvals)) 1 0)
      6 (if (< (first subvals) (second subvals)) 1 0)
      7 (if (= (first subvals) (second subvals)) 1 0))))

(defn day16-part2-soln
  []
  (apply-operator (decode day16-input)))