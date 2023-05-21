(ns clj-drawing-demo.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clj-drawing-demo.v2 :as v2]
            [clj-drawing-demo.graphing :as gr]
            ))

(defn mouse-click-position []
  (if (q/mouse-pressed?) [(q/mouse-x) (q/mouse-y)] nil))

(defn updated-offset-n-mouse-position [offset last-position]
  (let [current-position (mouse-click-position)
        offset-delta (if (and last-position current-position)
                       (v2/minus last-position current-position)
                       [0 0])
        ]
    {
     :offset              (v2/plus offset offset-delta)
     :last-mouse-position current-position
     }
    )
  )

(defn updated-zoom [zoom]
  (case (if (q/key-pressed?) (q/key-as-keyword) nil)
    :up (* zoom 1.05)
    :down (* zoom 0.95)
    zoom
    )
  )

(defn setup []
  (q/frame-rate 30)
  {
   :zoom                50
   :every-nth           2
   :offset              [0 0]
   :last-mouse-position nil
   }
  )

(defn update-state [state]
  (let [updated-offset-data (updated-offset-n-mouse-position (:offset state) (:last-mouse-position state))]
    {
     :zoom                (updated-zoom (:zoom state))
     :every-nth           (:every-nth state)
     :offset              (:offset updated-offset-data)
     :last-mouse-position (:last-mouse-position updated-offset-data)
     }
    )
  )

(defn draw-state [state]
  (q/background 0)

  (q/stroke-weight 2)
  (q/stroke 255)
  (apply gr/draw-axes (:offset state))

  (q/no-stroke)
  (doseq [[fnc fill] (map list
                          [#(/ 1 %) #(* 3 (Math/cos %)) #(Math/pow % 3)]
                          [[52 235 192] [252 88 28] [250 22 193]])]
    (apply q/fill fill)
    (gr/draw-f fnc
               (first (:offset state))
               (second (:offset state))
               (:zoom state)
               (:every-nth state))
    )
  )

(q/defsketch clj-drawing-demo
             :title "Simple graphing"
             :size [1000 1000]
             :setup setup
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
