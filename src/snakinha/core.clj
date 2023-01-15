(ns snakinha.core
  (:require [snakinha.logic.board :as board]
            [snakinha.logic.snake :as snake]
            [snakinha.logic.fruit :as fruit])
  (:gen-class))

(defn clear-screen []
  (Thread/sleep 70)
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (let [board (board/make-board 6 6 "x") 
        snake (atom (snake/raise-snake 4))
        fruit (fruit/bear-randon-fruit board @snake)]
    (println "---------start------------")
    (println (board/render-board (fruit/draw-fruit (snake/draw-snake @snake board) fruit "f")))
    (swap! snake (partial snake/move-snake-to "down"))
    (println "---------down-------------")
    (println (board/render-board (fruit/draw-fruit (snake/draw-snake @snake board) fruit "f")))))
