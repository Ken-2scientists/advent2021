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

(defn prog-execute
  [program input]
  (reduce cmd-execute {:regs {"w" 0 "x" 0 "y" 0 "z" 0}
                       :input input} program))

(prog-execute day24-sample2 [2 3])

(prog-execute day24-sample3 [15])

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

(largest-model-number)

(filter (partial every? pos?)
        (map (comp read-string str) "99999999999999"))