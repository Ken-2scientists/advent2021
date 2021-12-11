(ns advent2021.day08
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [advent-utils.core :as u]))

(def digit-mapping
  {"abcefg"  0 ; 6 chars
   "cf"      1 ; 2 chars
   "acdeg"   2 ; 5 chars
   "acdfg"   3 ; 5 chars
   "bcdf"    4 ; 4 chars
   "abdfg"   5 ; 5 chars
   "abdefg"  6 ; 6 chars
   "acf"     7 ; 3 chars
   "abcdefg" 8 ; 7 chars
   "abcdfg"  9 ; 6 chars
   })

(defn parse-line
  [line]
  (let [[l r] (str/split line #" \| ")]
    {:patterns (str/split l #" ")
     :output (str/split r #" ")}))

(def day08-input (map parse-line (u/puzzle-input "day08-input.txt")))

(def day08-sample1
  (parse-line "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"))

(def day08-sample2
  (map parse-line
       ["be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc"
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg"
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb"
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea"
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb"
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe"
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef"
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb"
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"]))

(defn easy-digits-count
  [note]
  (->> (:output note)
       (map count)
       (filter #{2 3 4 7})
       count))

(defn total-easy-digits-count
  [notes]
  (reduce + (map easy-digits-count notes)))

(total-easy-digits-count day08-sample2)

(defn day08-part1-soln
  []
  (total-easy-digits-count day08-input))


(frequencies (apply str (sort-by count (:patterns day08-sample1))))

(def freq-map
  {4 #{\e}
   6 #{\b}
   7 #{\d \g}
   8 #{\a \c}
   9 #{\f}})

(defn freq-rule
  [patterns]
  (let [freqs (->> patterns
                   (apply str)
                   (frequencies))]
    (u/fmap freq-map freqs)))

(defn easy-digit-rule
  [patterns]
  (let [one   (first (filter #(= 2 (count %)) patterns))
        seven (first (filter #(= 3 (count %)) patterns))
        four  (first (filter #(= 4 (count %)) patterns))
        [u v] one
        [x y] (filter (complement #{u v}) four)
        z     (first (filter (complement #{u v}) seven))]
    {u #{\c \f} v #{\c \f} x #{\b \d} y #{\b \d} z #{\a}}))

(map (freq-rule (:patterns day08-sample1)) (keys (easy-digit-rule (:patterns day08-sample1))))

(defn decode-mapping
  [patterns]
  (let [rule-a (freq-rule patterns)
        rule-b (easy-digit-rule patterns)
        combined (into rule-a (zipmap (keys rule-b)
                                      (map #(set/intersection (rule-b %) (rule-a %)) (keys rule-b))))
        known  (apply set/union (vals (filter #(= 1 (count (val %)))  combined)))
        unknown (into {} (filter #(> (count (val %)) 1) combined))]
    (->> (into combined (u/fmap #(set/difference % known) unknown))
         (u/fmap first))))

(defn decode-digit
  [mapping digit]
  (->> (map mapping digit)
       sort
       str/join
       digit-mapping))

(defn decode-notes
  [{:keys [output patterns]}]
  (let [mapping (decode-mapping patterns)
        digit-str (->> (map (partial decode-digit mapping) output)
                       (apply str))]
    (->> (str/replace digit-str #"^0+" "")
         read-string)))

(defn sum-of-decoded-digits
  [input]
  (reduce + (map decode-notes input)))

(defn day08-part2-soln
  []
  (sum-of-decoded-digits day08-input))
