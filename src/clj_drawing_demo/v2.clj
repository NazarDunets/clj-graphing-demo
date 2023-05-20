(ns clj-drawing-demo.v2)

(defn length [v]
  (Math/sqrt (+ (Math/pow (v 0) 2) (Math/pow (v 1) 2))))

(defn normalized [v]
  (let [length (length v)]
    (if (== length 0) [0 0] [(/ (v 0) length) (/ (v 1) length)])))

(defn plus [v1 v2]
  [(+ (first v1) (first v2)) (+ (second v1) (second v2))])

(defn minus [v1 v2]
  [(- (first v1) (first v2)) (- (second v1) (second v2))])

(defn scale [v s]
  [(* (first v) s) (* (second v) s)])
