(ns snakinha.core
  (:gen-class))

;; TODO separar em um logic 
;; TODO criar testes
(defn clear-screen []
  (Thread/sleep 70)
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn colide? ;; TODO descontar se for o ultimo tail
  [snake      ;;      pq na real o ultimo tail vai
   {:keys [x y]}] ;;  ser deslocado
  (boolean (some (fn [p] (and (= (:x p) x) (= (:y p) y))) snake)))

(defn make-board
  [x y background]
  (->> background
       (repeat x)
       (into [])
       (repeat y)
       (into [])))

(defn bear-fruit
  [x y board snake]
  (let [board-x-len (count board)
        board-y-len (count (get board 0))
        new-fruit {:x x :y y}]
    (when (or (> x board-x-len) (> y board-y-len))
      (throw (Exception. "fruit out of bounds")))
    (when (colide? snake new-fruit)
      (throw (Exception. "fruit cant bear over snake")))
    new-fruit))

(defn bear-randon-fruit
  [board snake]
  (let [board-x-len (count board)
        board-y-len (count (get board 0))
        randon-x (rand-int board-x-len)
        randon-y (rand-int board-y-len)]
    (bear-fruit randon-x randon-y board snake)))

(defn draw-fruit
  [board 
   {:keys [x y]} ;as fruit
   fruit-sprite]
  (update-in board [x y] (fn [_] fruit-sprite)))

(defn draw-snake
  ([snake board] (draw-snake snake board "s"))
  ([snake board snake-sprite]
   (reduce
    (fn [board {:keys [x y] :as snake-point}]
      (update-in board
                 [x y]
                 (fn [_] (if (:head snake-point)
                           "H"
                           snake-sprite))))
    board
    snake)))

(defn reverse? 
  [snake
   {:keys [x y]}]
  (let [first-body (->> snake
                        (remove :head)
                        last)] 
    (and (= (:x first-body) x) (= (:y first-body) y))))

(defn move-snake
  [coordinate-update-fn coordinate-update-axis snake]
  (let [head (first (filter :head snake))
        raw-body (rest snake)
        cleaned-body (into [] (map #(update % :head (fn [_] false)) raw-body))
        new-head (assoc head coordinate-update-axis (coordinate-update-fn (coordinate-update-axis head))) ;; TODO transformas em threading last
        reverse (reverse? snake new-head)]
    (if (or (= (coordinate-update-axis new-head) 0) reverse)
      snake
      (conj cleaned-body new-head))))

(defn move-snake-to ;TODO transformar as directions em keywords e checar se existe um enum em clojure
  [direction snake]
  (cond
    (= direction "right") (move-snake inc :y snake)
    (= direction "down") (move-snake inc :x snake)
    (= direction "up") (move-snake dec :x snake)
    (= direction "left") (move-snake dec :y snake)))

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
        snake (atom (raise-snake 4))
        fruit (bear-randon-fruit board @snake)]
    (println "---------start------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "up"))
    (println "---------up--colide---------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "right"))
    (println "---------right---------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "up"))
    (println "---------up---------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down--colide---------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))
    (swap! snake (partial move-snake-to "left"))
    (println "---------left---------------")
    (println (render-board (draw-fruit (draw-snake @snake board) fruit "f")))))
