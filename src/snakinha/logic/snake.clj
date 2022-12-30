(ns snakinha.logic.snake)

(defn colide? ;; TODO descontar se for o ultimo tail
  [snake      ;;      pq na real o ultimo tail vai
   {:keys [x y]}] ;;  ser deslocado
  (boolean (some (fn [p] (and (= (:x p) x) (= (:y p) y))) snake)))

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

(defn raise-snake
  [size]
  (let [snake (map (fn [y] {:x 0 :y y}) (range size))
        head (last snake)
        body (into [] (butlast snake))]
    (conj body (assoc head :head true))))