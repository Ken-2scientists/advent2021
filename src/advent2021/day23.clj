(ns advent2021.day23
  (:require [advent-utils.core :as u]))

;; Position labeling scheme
;; h's are the hallway positions
;; the other letters reflect the desired room for each type
;;
;; h0 h1    h2    h3    h4    h5 h6
;;       a0    b0    c0    d0
;;       a1    b1    c1    d1
;;       a2    b2    c2    d2
;;       a3    b3    c3    d3

(def adjacencies-part1
  {:h0 {:h1 1}
   :h1 {:h0 1 :a0 2 :h2 2}
   :h2 {:h1 1 :a0 2 :b0 2 :h3 2}
   :h3 {:h2 2 :b0 2 :c0 2 :h4 2}
   :h4 {:h3 2 :c0 2 :d0 2 :h5 2}
   :h5 {:h4 2 :d0 2 :h6 1}
   :h6 {:h5 1}
   :a0 {:h1 2 :h2 2 :a1 1}
   :a1 {:a0 1}
   :b0 {:h2 2 :h3 2 :b1 1}
   :b1 {:b0 1}
   :c0 {:h3 2 :h4 2 :c1 1}
   :c1 {:c0 1}
   :d0 {:h4 2 :h5 2 :d1 1}
   :d1 {:d0 1}})

(def adjacencies-part2
  (merge adjacencies-part1
         {:a1 {:a0 1 :a2 1}
          :a2 {:a1 1 :a3 1}
          :a3 {:a2 1}
          :b1 {:b0 1 :b2 1}
          :b2 {:b1 1 :b3 1}
          :b3 {:b2 1}
          :c1 {:c0 1 :c2 1}
          :c2 {:c1 1 :c3 1}
          :c3 {:c2 1}
          :d1 {:d0 1 :d2 1}
          :d2 {:d1 1 :d3 1}
          :d3 {:d2 1}}))

(def day23-sample1
  ;; "#############"
  ;; "#...........#"
  ;; "###B#C#B#D###"
  ;; "  #A#D#C#A#  "
  ;; "  #########  "
  {:a0 {:type :b}
   :a1 {:type :a}
   :b0 {:type :c}
   :b1 {:type :d}
   :c0 {:type :b}
   :c1 {:type :c}
   :d0 {:type :d}
   :d1 {:type :a}})

(def day23-input
;; "#############"
;; "#...........#"
;; "###D#D#A#A###"
;; "  #C#C#B#B#  "
;; "  #########  "
  {:a0 {:type :d}
   :a1 {:type :c}
   :b0 {:type :d}
   :b1 {:type :c}
   :c0 {:type :a}
   :c1 {:type :b}
   :d0 {:type :a}
   :d1 {:type :b}})

(def distances
  {:h0 {:h1 1  :h2 3 :h3 5 :h4 7 :h5 9 :h6 10 :a0 3  :a1 4  :b0 5  :b1 6 :c0 7 :c1 8 :d0 9 :d1 10}
   :h1 {:h0 1  :h2 2 :h3 4 :h4 6 :h5 8 :h6 9  :a0 2  :a1 3  :b0 4  :b1 5 :c0 6 :c1 7 :d0 8 :d1 9}
   :h2 {:h0 3  :h1 2 :h3 2 :h4 4 :h5 6 :h6 7  :a0 2  :a1 3  :b0 2  :b1 3 :c0 4 :c1 5 :d0 6 :d1 7}
   :h3 {:h0 5  :h1 4 :h2 2 :h4 2 :h5 4 :h6 5  :a0 4  :a1 5  :b0 2  :b1 3 :c0 2 :c1 3 :d0 4 :d1 5}
   :h4 {:h0 7  :h1 6 :h2 4 :h3 2 :h5 2 :h6 3  :a0 6  :a1 7  :b0 4  :b1 5 :c0 2 :c1 3 :d0 2 :d1 3}
   :h5 {:h0 9  :h1 8 :h2 6 :h3 4 :h4 2 :h6 1  :a0 8  :a1 9  :b0 6  :b1 7 :c0 4 :c1 5 :d0 2 :d1 3}
   :h6 {:h0 10 :h1 9 :h2 7 :h3 5 :h4 3 :h5 1  :a0 9  :a1 10 :b0 7  :b1 8 :c0 5 :c1 6 :d0 3 :d1 4}
   :a0 {:h0 3  :h1 2 :h2 2 :h3 4 :h4 6 :h5 8  :h6 9  :a1 1  :b0 4  :b1 5 :c0 6 :c1 7 :d0 8 :d1 9}
   :a1 {:h0 4  :h1 3 :h2 3 :h3 5 :h4 7 :h5 9  :h6 10 :a0 1  :b0 5  :b1 6 :c0 7 :c1 8 :d0 9 :d1 10}
   :b0 {:h0 5  :h1 4 :h2 2 :h3 2 :h4 4 :h5 6  :h6 7  :a0 4  :a1 5  :b1 1 :c0 4 :c1 5 :d0 6 :d1 7}
   :b1 {:h0 6  :h1 5 :h2 3 :h3 3 :h4 5 :h5 7  :h6 8  :a0 5  :a1 6  :b0 1 :c0 5 :c1 6 :d0 7 :d1 8}
   :c0 {:h0 7  :h1 6 :h2 4 :h3 2 :h4 2 :h5 4  :h6 5  :a0 6  :a1 7  :b0 4 :b1 5 :c1 1 :d0 4 :d1 5}
   :c1 {:h0 8  :h1 7 :h2 5 :h3 3 :h4 3 :h5 5  :h6 6  :a0 7  :a1 8  :b0 5 :b1 6 :c0 1 :d0 5 :d1 6}
   :d0 {:h0 9  :h1 8 :h2 6 :h3 4 :h4 2 :h5 2  :h6 3  :a0 8  :a1 9  :b0 6 :b1 7 :c0 4 :c1 5 :d1 1}
   :d1 {:h0 10 :h1 9 :h2 7 :h3 5 :h4 3 :h5 3  :h6 4  :a0 9  :a1 10 :b0 7 :b1 8 :c0 5 :c1 6 :d0 1}})

(defn initialize
  [input]
  (u/fmap #(assoc % :moves 0 :cost 0) input))

;; (defn next-moves-from-pos
;;   [open pos]
;;   (map first (filter #(every? open (second %)) (get paths pos))))

;; (defn next-moves
;;   [state]
;;   (let [open (complement (set (keys state)))]
;;     (map (partial next-moves-from-pos open) (keys state))))

;; I just sketched this solution out by hand on paper
(def day23-input-soln
  [[:d0 :h1]
   [:d1 :h5]
   [:b0 :d1]
   [:a0 :d0]
   [:c0 :h4]
   [:c1 :h2]
   [:b1 :c1]
   [:h2 :b1]
   [:a1 :c0]
   [:h1 :a1]
   [:h4 :a0]
   [:h5 :b0]])

(def move-multiplier
  {:a 1
   :b 10
   :c 100
   :d 1000})

(defn new-state
  [state [from to]]
  (let [{:keys [type moves cost]} (get state from)
        dist (get-in distances [from to])]
    (-> state
        (dissoc from)
        (assoc to {:type type
                   :moves (inc moves)
                   :cost (+ cost (* (move-multiplier type) dist))}))))

(defn cost-of-moves
  [input path]
  (->> (reduce new-state (initialize input) path)
       vals
       (map :cost)
       (reduce +)))

(defn day23-part1-soln
  []
  (cost-of-moves day23-input day23-input-soln))