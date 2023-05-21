(ns clj-drawing-demo.graphing
  (:require [quil.core :as q])
  )

(defn generate-x-arguments [width every-nth]
  (filter #(= (mod % every-nth) 0) (range (- (/ width 2)) (/ width 2))))

(defn draw-axes
  ([xo yo]

   (let [xcenter (/ (q/width) 2.0)
         ycenter (/ (q/height) 2.0)]

     (q/line 0 (- ycenter yo) (q/width) (- ycenter yo))
     (q/line (- xcenter xo) 0 (- xcenter xo) (q/height))
     )
   )

  ([] (draw-axes 0 0))
  )

(defn draw-f
  ([f xo yo zoom e-nth]
   (doseq [x-value (map #(/ (+ xo %) zoom) (generate-x-arguments (q/width) e-nth))]
     (let [y-pixel (try (+ yo (* zoom (f x-value)))
                        (catch Exception e nil))]
       (if (not (nil? y-pixel))
         (q/ellipse
           (+ (* x-value zoom) (- xo) (/ (q/width) 2))
           (- (/ (q/height) 2.0) y-pixel)
           4 4)
         nil
         )
       )
     )
   )
  )
