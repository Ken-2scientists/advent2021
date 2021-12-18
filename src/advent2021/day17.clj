(ns advent2021.day17)


;; target area: x=20..30, y=-10..-5
(def day17-sample
  {:xmin 20
   :xmax 30
   :ymin -10
   :ymax -5})

;; target area: x=281..311, y=-74..-54
(def day17-input
  {:xmin 281
   :xmax 311
   :ymin -74
   :ymax -54})

(defn target?
  [{:keys [xmin xmax ymin ymax]} [x y]]
  (and (<= xmin x xmax) (<= ymin y ymax)))

(defn step
  [[[x y] [vx vy]]]
  [[(+ x vx) (+ y vy)]
   [(if (pos? vx) (dec vx) 0) (- vy 1)]])

(defn evolve
  [[vx vy] {:keys [ymin]}]
  (take-while
   #(>= (second (first %)) ymin)
   (iterate step [[0 0] [vx vy]])))

(defn hits-target?
  [target init-velocity]
  (boolean (some (partial target? target)
                 (map first (evolve init-velocity target)))))

(defn min-init-vx
  [{:keys [xmin]}]
  (int (/ (+ 1 (Math/sqrt (+ 1 (* 4 2 xmin)))) 2)))

(defn highest-point
  [vel target]
  (apply max (map (comp second first) (evolve vel target))))

(defn highest-trajectory
  [target]
  (let [vx    26 ;(min-init-vx target)
        cands (map (partial vector vx) (range 4 100))]
    (last (take-while (partial hits-target? target) cands))))

(hits-target? day17-input [25 4])
(min-init-vx day17-input)
(evolve [24 30] day17-input)
(highest-point [24 22] day17-input)
(highest-trajectory day17-input)



