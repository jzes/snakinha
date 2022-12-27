(ns snakinha.core
  (:gen-class))

;; TODO separar em um logic 
;; TODO criar testes
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

(defn bear-fruit ;; TODO mudar pro mesmo esquema da snake (lista de coordenadas) 
  ([x y board]    ;; e não fazer direto no board, fica mais facil checar
   (bear-fruit x y board "f"))
  ([x y board fruit]
   (update-in board [x y] (fn [_] fruit))))

(defn draw-snake
  ([snake board] (draw-snake snake board "s"))
  ([snake board snake-sprite]
   (reduce
    (fn [board snake-point] ;; TODO fazer um destructuring aqui
      (update-in board
                 [(:x snake-point) (:y snake-point)]
                 (fn [_] (if (:head snake-point)
                           "H"
                           snake-sprite))))
    board
    snake)))

(defn colide?
  [snake
   {:keys [x y]}]
  (boolean (some (fn [p] (and (= (:x p) x) (= (:y p) y))) snake)))

(defn move-snake
  [coordinate-update-fn coordinate-update-axis snake]
  (let [head (first (filter :head snake))
        body (into [] (map #(update % :head (fn [_] false)) (rest snake))) ;; TODO transformar em 2 passos
        new-head (assoc head coordinate-update-axis (coordinate-update-fn (coordinate-update-axis head))) ;; TODO transformas em threading last
        colide (colide? snake new-head)]
    (if (or (= (coordinate-update-axis new-head) 0) colide)
      snake
      (conj body new-head))))

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
        fruited-board (bear-fruit 1 1 board)
        snake (atom (raise-snake 4))]
    (println "---------start------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down-------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "up"))
    (println "---------up--colide---------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "right"))
    (println "---------right---------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "up"))
    (println "---------up---------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "down"))
    (println "---------down--colide---------------")
    (println (render-board (draw-snake @snake board)))
    (swap! snake (partial move-snake-to "left"))
    (println "---------left---------------")
    (println (render-board (draw-snake @snake board)))))
