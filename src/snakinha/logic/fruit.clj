(ns snakinha.logic.fruit
  (:require [snakinha.logic.snake :as snake]))

(defn bear-fruit
  [x y board snake]
  (let [board-x-len (count board)
        board-y-len (count (get board 0))
        new-fruit {:x x :y y}]
    (when (or (> x board-x-len) (> y board-y-len))
      (throw (Exception. "fruit out of bounds")))
    (when (snake/colide? snake new-fruit) ;; TODO <-------- talvez o ideal seja isso não estar
      (throw (Exception. "fruit cant bear over snake"))) ;;  aqui, ou recebermos como dep injetada
    new-fruit))

(defn bear-randon-fruit
  [board snake]
  (let [board-x-len (count board)
        board-y-len (count (get board 0))
        randon-x (rand-int board-x-len) ;; TODO receber func de random injetada
        randon-y (rand-int board-y-len)]
    (bear-fruit randon-x randon-y board snake)))

(defn draw-fruit
  [board
   {:keys [x y]} ;as fruit
   fruit-sprite]
  (update-in board [x y] (fn [_] fruit-sprite)))