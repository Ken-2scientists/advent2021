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
  {:h0 {:h1 1  :h2 3  :h3 5 :h4 7 :h5 9 :h6 10 :a0 3  :a1 4  :a2 5  :a3 6  :b0 5  :b1 6 :b2 7  :b3 8  :c0 7  :c1 8  :c2 9  :c3 10 :d0 9  :d1 10 :d2 11 :d3 12}
   :h1 {:h0 1  :h2 2  :h3 4 :h4 6 :h5 8 :h6 9  :a0 2  :a1 3  :a2 4  :a3 5  :b0 4  :b1 5 :b2 6  :b3 7  :c0 6  :c1 7  :c2 8  :c3 9  :d0 8  :d1 9  :d2 10 :d3 11}
   :h2 {:h0 3  :h1 2  :h3 2 :h4 4 :h5 6 :h6 7  :a0 2  :a1 3  :a2 4  :a3 5  :b0 2  :b1 3 :b2 4  :b3 5  :c0 4  :c1 5  :c2 6  :c3 7  :d0 6  :d1 7  :d2 8  :d3 9}
   :h3 {:h0 5  :h1 4  :h2 2 :h4 2 :h5 4 :h6 5  :a0 4  :a1 5  :a2 6  :a3 7  :b0 2  :b1 3 :b2 4  :b3 5  :c0 2  :c1 3  :c2 4  :c3 5  :d0 4  :d1 5  :d2 6  :d3 7}
   :h4 {:h0 7  :h1 6  :h2 4 :h3 2 :h5 2 :h6 3  :a0 6  :a1 7  :a2 8  :a3 9  :b0 4  :b1 5 :b2 6  :b3 7  :c0 2  :c1 3  :c2 4  :c3 5  :d0 2  :d1 3  :d2 4  :d3 5}
   :h5 {:h0 9  :h1 8  :h2 6 :h3 4 :h4 2 :h6 1  :a0 8  :a1 9  :a2 10 :a3 11 :b0 6  :b1 7 :b2 8  :b3 9  :c0 4  :c1 5  :c2 6  :c3 7  :d0 2  :d1 3  :d2 4  :d3 5}
   :h6 {:h0 10 :h1 9  :h2 7 :h3 5 :h4 3 :h5 1  :a0 9  :a1 10 :a2 11 :a3 12 :b0 7  :b1 8 :b2 9  :b3 10 :c0 5  :c1 6  :c2 7  :c3 8  :d0 3  :d1 4  :d2 5  :d3 6}

   :a0 {:h0 3  :h1 2  :h2 2 :h3 4 :h4 6 :h5 8  :h6 9  :a1 1  :a2 2  :a3 3  :b0 4  :b1 5 :b2 6  :b3 7  :c0 6  :c1 7  :c2 8  :c3 9  :d0 8  :d1 9  :d2 10 :d3 11}
   :a1 {:h0 4  :h1 3  :h2 3 :h3 5 :h4 7 :h5 9  :h6 10 :a0 1  :a2 1  :a3 2  :b0 5  :b1 6 :b2 7  :b3 8  :c0 7  :c1 8  :c2 9  :c3 10 :d0 9  :d1 10 :d2 11 :d3 12}
   :a2 {:h0 5  :h1 4  :h2 4 :h3 6 :h4 8 :h5 10 :h6 11 :a0 2  :a1 1  :a3 1  :b0 6  :b1 7 :b2 8  :b3 9  :c0 8  :c1 9  :c2 10 :c3 11 :d0 10 :d1 11 :d2 12 :d3 13}
   :a3 {:h0 6  :h1 5  :h2 5 :h3 7 :h4 9 :h5 11 :h6 12 :a0 3  :a1 2  :a2 1  :b0 7  :b1 8 :b2 9  :b3 10 :c0 9  :c1 10 :c2 11 :c3 12 :d0 11 :d1 12 :d2 13 :d3 14}

   :b0 {:h0 5  :h1 4  :h2 2 :h3 2 :h4 4 :h5 6  :h6 7  :a0 4  :a1 5  :a2 6  :a3 7  :b1 1 :b2 2  :b3 3  :c0 4  :c1 5  :c2 6  :c3 7  :d0 6  :d1 7  :d2 8  :d3 9}
   :b1 {:h0 6  :h1 5  :h2 3 :h3 3 :h4 5 :h5 7  :h6 8  :a0 5  :a1 6  :a2 7  :a3 8  :b0 1 :b2 1  :b3 2  :c0 5  :c1 6  :c2 7  :c3 8  :d0 7  :d1 8  :d2 9  :d3 10}
   :b2 {:h0 7  :h1 6  :h2 4 :h3 4 :h4 6 :h5 8  :h6 9  :a0 6  :a1 7  :a2 8  :a3 9  :b0 2 :b1 1  :b3 1  :c0 6  :c1 7  :c2 8  :c3 9  :d0 8  :d1 9  :d2 10 :d3 11}
   :b3 {:h0 8  :h1 7  :h2 5 :h3 5 :h4 7 :h5 9  :h6 10 :a0 7  :a1 8  :a2 9  :a3 10 :b0 3 :b1 2  :b2 1  :c0 7  :c1 8  :c2 9  :c3 10 :d0 9  :d1 10 :d2 11 :d3 12}

   :c0 {:h0 7  :h1 6  :h2 4 :h3 2 :h4 2 :h5 4  :h6 5  :a0 6  :a1 7  :a2 8  :a3 9  :b0 4 :b1 5  :b2 6  :b3 7  :c1 1  :c2 2  :c3 3  :d0 4  :d1 5  :d2 6  :d3 7}
   :c1 {:h0 8  :h1 7  :h2 5 :h3 3 :h4 3 :h5 5  :h6 6  :a0 7  :a1 8  :a2 9  :a3 10 :b0 5 :b1 6  :b2 7  :b3 8  :c0 1  :c2 1  :c3 2  :d0 5  :d1 6  :d2 7  :d3 8}
   :c2 {:h0 9  :h1 8  :h2 6 :h3 4 :h4 4 :h5 6  :h6 7  :a0 8  :a1 9  :a2 10 :a3 11 :b0 6 :b1 7  :b2 8  :b3 9  :c0 2  :c1 1  :c3 1  :d0 6  :d1 7  :d2 8  :d3 9}
   :c3 {:h0 10 :h1 9  :h2 7 :h3 5 :h4 5 :h5 7  :h6 8  :a0 9  :a1 10 :a2 11 :a3 12 :b0 7 :b1 8  :b2 9  :b3 10 :c0 3  :c1 2  :c2 1  :d0 7  :d1 8  :d2 9  :d3 10}

   :d0 {:h0 9  :h1 8  :h2 6 :h3 4 :h4 2 :h5 2  :h6 3  :a0 8  :a1 9  :a2 10 :a3 11 :b0 6 :b1 7  :b2 8  :b3 9  :c0 4  :c1 5  :c2 6  :c3 7  :d1 1  :d2 2  :d3 3}
   :d1 {:h0 10 :h1 9  :h2 7 :h3 5 :h4 3 :h5 3  :h6 4  :a0 9  :a1 10 :a2 11 :a3 12 :b0 7 :b1 8  :b2 9  :b3 10 :c0 5  :c1 6  :c2 7  :c3 8  :d0 1  :d2 1  :d3 2}
   :d2 {:h0 11 :h1 10 :h2 8 :h3 6 :h4 4 :h5 4  :h6 5  :a0 10 :a1 11 :a2 12 :a3 13 :b0 8 :b1 9  :b2 10 :b3 11 :c0 6  :c1 7  :c2 8  :c3 9  :d0 2  :d1 1  :d3 1}
   :d3 {:h0 12 :h1 11 :h2 9 :h3 7 :h4 5 :h5 5  :h6 6  :a0 11 :a1 12 :a2 13 :a3 14 :b0 9 :b1 10 :b2 11 :b3 12 :c0 7  :c1 8  :c2 9  :c3 10 :d0 3  :d1 2  :d2 1}})


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

(defn unfold-input
  [input]
  (-> (select-keys input [:a0 :b0 :c0 :d0])
      (assoc :a3 (get input :a1))
      (assoc :b3 (get input :b1))
      (assoc :c3 (get input :c1))
      (assoc :d3 (get input :d1))
      (merge {:a1 {:type :d}
              :a2 {:type :d}
              :b1 {:type :c}
              :b2 {:type :b}
              :c1 {:type :b}
              :c2 {:type :a}
              :d1 {:type :a}
              :d2 {:type :c}})))

;; I sketched this solution out on paper and it turned out to work!
(def day23-input-soln-part2
  [[:d0 :h0]
   [:d1 :h1]
   [:d2 :h6]
   [:d3 :h5]
   [:b0 :d3]
   [:a0 :d2]
   [:a1 :d1]
   [:a2 :d0]
   [:a3 :h4]
   [:h1 :a3]
   [:h0 :a2]
   [:c0 :a1]
   [:c1 :h1]
   [:c2 :a0]
   [:c3 :h2]
   [:h4 :c3]
   [:b1 :c2]
   [:b2 :h4]
   [:b3 :c1]
   [:h2 :b3]
   [:h1 :b2]
   [:h4 :b1]
   [:h5 :b0]
   [:h6 :c0]])

(defn day23-part2-soln
  []
  (cost-of-moves (unfold-input day23-input) day23-input-soln-part2))