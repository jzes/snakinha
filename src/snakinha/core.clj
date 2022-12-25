(ns snakinha.core
  (:gen-class))

(defn clear-screen []
  (Thread/sleep 70)
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn make-board
  [x y background]
  (->> background
       (repeat x)
       (into [])
       (repeat y)
       (into [])))

(defn bear-fruit
  ([x y board]
   (bear-fruit x y board "f"))
  ([x y board fruit]
   (update-in board [x y] (fn [_] fruit))))

(defn draw-snake
  ([snake board] (draw-snake snake board "s"))
  ([snake board snake-sprite]
   (reduce
    (fn [board snake-point]
      (update-in board
                 [(:x snake-point) (:y snake-point)]
                 (fn [_] (if (:head snake-point)
                           "H"
                           snake-sprite))))
    board
    snake)))

(defn move-snake
  [direction snake]
  (let [head (first (filter :head snake))
        body (into [] (map #(update % :head (fn [_] false)) (rest snake)))]
    (cond
      (= direction "right") (conj body (update head :y inc))
      (= direction "down") (conj body (update head :x inc))
      (= direction "up") (conj body (update head :x (fn [x] (if (= x 0)
                                                              0
                                                              (+ x 1))))))))

(defn render-board
  [board]
  (clojure.string/join "\n" (map (fn [x] (clojure.string/join " " x)) board)))

(defn raise-snake
  [size]
  (let [snake (map (fn [y] {:x 0 :y y}) (range size))
        head (last snake)
        body (into [] (butlast snake))]
    (conj body (assoc head :head true))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [board (make-board 6 6 "x")
        fruited-board (bear-fruit 1 1 board)
        snake (atom (raise-snake 3))]
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake "down"))
    (println "--------------------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake "down"))
    (println "--------------------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake "down"))
    (println "--------------------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake "right"))
    (println "--------------------------")
    (println (render-board (draw-snake @snake board)))
    ;;(draw-snake @snake board) 
    ))
