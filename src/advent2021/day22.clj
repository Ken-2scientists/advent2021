(ns advent2021.day22
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-bounds
  [bounds]
  (update (mapv read-string (str/split (subs bounds 2) #"\.\.")) 1 inc))

(defn parse-coords
  [coords]
  (mapv parse-bounds (str/split coords #",")))

(defn parse-line
  [line]
  (let [[l r] (str/split line #" ")]
    {:cmd (keyword l)
     :bounds (parse-coords r)
     :sign (if (= l "off") -1 1)}))

(defn parse
  [lines]
  (map parse-line lines))

(def day22-input (parse (u/puzzle-input "day22-input.txt")))

(defn volume
  [{:keys [bounds sign]}]
  (* sign (reduce * (map #(- (second %) (first %)) bounds))))

(defn intersect?
  [a b]
  (and (every? identity (map < (map first (:bounds b)) (map second (:bounds a))))
       (every? identity (map < (map first (:bounds a)) (map second (:bounds b))))))

(defn intersection
  [cube-a cube-b]
  (let [bound-pairs (partition 2 (interleave (:bounds cube-a) (:bounds cube-b)))]
    {:cmd :intersect
     :sign (if (= (:sign cube-a) (:sign cube-b))
             (- (:sign cube-a))
             (if (and (= 1 (:sign cube-a)) (= -1 (:sign cube-b)))
               1
               (* (:sign cube-a) (:sign cube-b))))
     :bounds (mapv (fn [[a b]]
                     [(max (first a) (first b))
                      (min (second a) (second b))]) bound-pairs)}))

(defn accumulate
  [processed {:keys [cmd] :as cube}]
  (let [intersections (->> (filter (partial intersect? cube) processed)
                           (map (partial intersection cube)))]
    (case cmd
      :on (into (conj processed cube) intersections)
      :off (into processed intersections))))

(defn init-area?
  [{:keys [bounds]}]
  (let [[[x0 x1] [y0 y1] [z0 z1]] bounds]
    (and (>= x0 -50) (>= y0 -50) (>= z0 -50)
         (<= x1  51) (<= y1  51) (<= z1  51))))

(defn on-cubes
  [cuboids]
  (->> (reduce accumulate [] cuboids)
       (map volume)
       (reduce +)))

(defn on-cubes-in-init-area
  [cuboids]
  (->> (filter init-area? cuboids)
       on-cubes))

(defn day22-part1-soln
  []
  (on-cubes-in-init-area day22-input))

(defn day22-part2-soln
  []
  (on-cubes day22-input))