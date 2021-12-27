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
     :bounds (parse-coords r)}))

(defn parse
  [lines]
  (map parse-line lines))

(def day22-sample1
  (parse
   ["on x=10..12,y=10..12,z=10..12"
    "on x=11..13,y=11..13,z=11..13"
    "off x=9..11,y=9..11,z=9..11"
    "on x=10..10,y=10..10,z=10..10"]))

(def day22-sample2
  (parse
   ["on x=-20..26,y=-36..17,z=-47..7"
    "on x=-20..33,y=-21..23,z=-26..28"
    "on x=-22..28,y=-29..23,z=-38..16"
    "on x=-46..7,y=-6..46,z=-50..-1"
    "on x=-49..1,y=-3..46,z=-24..28"
    "on x=2..47,y=-22..22,z=-23..27"
    "on x=-27..23,y=-28..26,z=-21..29"
    "on x=-39..5,y=-6..47,z=-3..44"
    "on x=-30..21,y=-8..43,z=-13..34"
    "on x=-22..26,y=-27..20,z=-29..19"
    "off x=-48..-32,y=26..41,z=-47..-37"
    "on x=-12..35,y=6..50,z=-50..-2"
    "off x=-48..-32,y=-32..-16,z=-15..-5"
    "on x=-18..26,y=-33..15,z=-7..46"
    "off x=-40..-22,y=-38..-28,z=23..41"
    "on x=-16..35,y=-41..10,z=-47..6"
    "off x=-32..-23,y=11..30,z=-14..3"
    "on x=-49..-5,y=-3..45,z=-29..18"
    "off x=18..30,y=-20..-8,z=-3..13"
    "on x=-41..9,y=-7..43,z=-33..15"
    "on x=-54112..-39298,y=-85059..-49293,z=-27449..7877"
    "on x=967..23432,y=45373..81175,z=27513..53682"]))

(def day22-input (parse (u/puzzle-input "day22-input.txt")))
(def day22-sample3 (parse (u/puzzle-input "day22-sample3.txt")))

(defn volume
  [bounds]
  (reduce * (map #(- (second %) (first %)) bounds)))

(defn identify-splits
  [cuboids]
  (let [all-bounds (map :bounds cuboids)
        dims  (count (first all-bounds))]
    (for [n (range dims)]
      (sort (into #{} (mapcat #(nth % n) all-bounds))))))

(defn split-cuboid
  [[xplanes yplanes zplanes] {:keys [cmd bounds]}]
  (let [[[x0 x1] [y0 y1] [z0 z1]]  bounds
        xsplits (filter #(<= x0 % x1) xplanes)
        ysplits (filter #(<= y0 % y1) yplanes)
        zsplits (filter #(<= z0 % z1) zplanes)
        newbounds (for [z (partition 2 1 zsplits)
                        y (partition 2 1 ysplits)
                        x (partition 2 1 xsplits)]
                    [(vec x) (vec y) (vec z)])]
    (map #(assoc {:cmd cmd} :bounds %) newbounds)))

(defn prepare-geometry
  [cuboids]
  (let [splits (identify-splits cuboids)]
    (mapcat (partial split-cuboid splits) cuboids)))

(defn apply-cmd
  [cubes {:keys [cmd bounds]}]
  (case cmd
    :on  (conj cubes bounds)
    :off (disj cubes bounds)))

(defn on-cubes
  [cuboids]
  (->> (prepare-geometry cuboids)
       (reduce apply-cmd #{})
       (map volume)
       (reduce +)))

(defn init-area?
  [{:keys [bounds]}]
  (let [[[x0 x1] [y0 y1] [z0 z1]] bounds]
    (and (>= x0 -50) (>= y0 -50) (>= z0 -50)
         (<= x1  51) (<= y1  51) (<= z1  51))))

(defn on-cubes-in-init-area
  [cuboids]
  (->> (filter init-area? cuboids)
       on-cubes))

(defn day22-part1-soln
  []
  (on-cubes-in-init-area day22-input))

(defn intersect-volumes
  [a-bounds b-bounds]
  (when (every? identity (map <= (map first b-bounds) (map second a-bounds)))
    (let [foo (partition 2 (interleave a-bounds b-bounds))]
      (mapv (fn [[a b]]
              [(max (first a) (first b))
               (min (second a) (second b))]) foo))))

;; (defn accumulate
;;   [{:keys [on on-overlap] :as acc} {:keys [cmd bounds]}]
;;   (case cmd
;;     :on (merge acc {:on (conj on bounds)
;;                     :on-overlap (into on-overlap (map (partial intersect-volumes bounds) on))})))

;; (def sample-2d
;;   [{:cmd :on :bounds [[0 8] [4 10]]}
;;    {:cmd :on :bounds [[5 11] [7 13]]}
;;    {:cmd :on :bounds [[1 3] [0 15]]}
;;    {:cmd :off :bounds [[5 10] [3 6]]}
;;    {:cmd :off :bounds [[4 7] [6 9]]}])

(defn day22-part2-soln
  []
  (on-cubes day22-input))