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

(def day16-sample1 (parse "D2FE28"))
(def day16-sample2 (parse "38006F45291200"))
(def day16-sample3 (parse "EE00D40C823060"))
(def day16-sample4 (parse "8A004A801A8002F478"))
(def day16-sample5 (parse "620080001611562C8802118E34"))
(def day16-sample6 (parse "C0015000016115A2E0802F182340"))
(def day16-sample7 (parse "A0016C880162017C3686B18A3D4780"))


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

(declare decode)

(defn decode-subpackets
  [s limit limit-type]
  ;; (println s limit limit-type)
  (loop [packets [] remainder s total 0]
    ;; (println "-> " remainder total limit (= total limit))
    (if (= total limit)
      {:subpackets packets
       :bits (+ (case limit-type
                  :count  18
                  :length 22)
                (reduce + (map :bits packets))
                ;; (count remainder)
                )}
      (let [next-packet (decode remainder)
            skip  (:bits next-packet)]
        ;; (println "At here" next-packet skip)
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
  ;; (println "Decoding " s)
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