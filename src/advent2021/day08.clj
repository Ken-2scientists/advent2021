(ns advent2021.day08
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(def seven-segment-digits
  {0 "abcefg"  ; 6
   1 "cf"      ; 2
   2 "acdeg"   ; 5
   3 "acdfg"   ; 5
   4 "bcdf"    ; 4
   5 "abdfg"   ; 5
   6 "abdefg"  ; 6
   7 "acf"     ; 3
   8 "abcdefg" ; 7
   9 "abcdfg"  ; 6
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