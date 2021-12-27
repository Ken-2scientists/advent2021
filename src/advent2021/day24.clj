(ns advent2021.day24
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-line
  [line]
  (let [[a b c] (str/split line #" ")]
    (if c
      (if (number? (read-string c))
        [a b (read-string c)]
        [a b c])
      [a b])))

(defn parse
  [input]
  (map parse-line input))

(def day24-sample1
  (parse
   ["inp x"
    "mul x -1"]))

(def day24-sample2
  (parse
   ["inp z"
    "inp x"
    "mul z 3"
    "eql z x"]))

(def day24-sample3
  (parse
   ["inp w"
    "add z w"
    "mod z 2"
    "div w 2"
    "add y w"
    "mod y 2"
    "div w 2"
    "add x w"
    "mod x 2"
    "div w 2"
    "mod w 2"]))

(def day24-input (parse (u/puzzle-input "day24-input.txt")))

(defn arg
  [regs s]
  (if (number? s) s (regs s)))

(defn cmd-execute
  [{:keys [regs input]} [cmd a b]]
  (merge {:regs regs :input input}
         (case cmd
           "inp" {:regs (assoc regs a (first input))
                  :input (rest input)}
           "add" {:regs (update regs a + (arg regs b))}
           "mul" {:regs (update regs a * (arg regs b))}
           "div" {:regs (update regs a quot (arg regs b))}
           "mod" {:regs (update regs a mod (arg regs b))}
           "eql" {:regs (assoc regs a (if (= (regs a) (arg regs b)) 1 0))})))

(quot 17 26)

(def init-regs {"w" 0 "x" 0 "y" 0 "z" 0})
(defn prog-execute
  [program input]
  (reduce cmd-execute {:regs init-regs
                       :input input} program))

(defn largest-model-number
  []
  (let [candidates (->> (range 99999999999999 11111111111110 -1)
                        (map str)
                        (map #(map (comp read-string str) %))
                        (filter (partial every? pos?)))]
    (loop [output (prog-execute day24-input (first candidates))
           remainder (rest candidates)]
      (if (zero? (get-in output [:regs "z"]))
        (Long/parseLong (str/join (first remainder)))
        (recur (prog-execute day24-input (first remainder))
               (rest remainder))))))

(defn form-a
  [shift {:keys [input]}]
  (let [n (first input)]
    {:regs {"w" n "x" 1 "y" (+ shift n) "z" (+ shift n)}
     :input (rest input)}))

(defn form-b
  [shift {:keys [regs input]}]
  (let [n (first input)]
    {:regs {"w" n "x" 1 "y" (+ shift n) "z" (+ shift n (* 26 (regs "z")))}
     :input (rest input)}))

(defn form-c
  [mod-shift shift {:keys [regs input]}]
  (let [n (first input)
        z (regs "z")
        x (if (= n (- (mod z 26) mod-shift)) 0 1)
        mult (if (zero? x) 1 26)
        y (* x (+ n shift))]
    {:regs {"w" n "x" x "y" y "z" (+ y (* mult (quot z 26)))}
     :input (rest input)}))

(defn char-01 [input] (form-a 10 input))
(defn char-02 [input] (form-b 16 input))
(defn char-03 [input] (form-b 0 input))
(defn char-04 [input] (form-b 13 input))
(defn char-05 [input] (form-c 14 7 input))
(defn char-06 [input] (form-c 4 11 input))
(defn char-07 [input] (form-b 11 input))
(defn char-08 [input] (form-c 3 10 input))
(defn char-09 [input] (form-b 16 input))
(defn char-10 [input] (form-c 12 8 input))
(defn char-11 [input] (form-b 15 input))
(defn char-12 [input] (form-c 12 2 input))
(defn char-13 [input] (form-c 15 5 input))
(defn char-14 [input] (form-c 12 10 input))

(defn monad
  [input]
  (-> {:regs init-regs :input input}
      char-01
      char-02
      char-03
      char-04
      char-05
      char-06
      char-07
      char-08
      char-09
      char-10
      char-11
      char-12
      char-13
      char-14))

(def largest-input [9 8 9 9 8 5 1 9 5 9 6 9 9 7])
(monad largest-input)

(defn day24-part1-soln
  []
  98998519596997)