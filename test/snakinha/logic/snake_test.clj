(ns snakinha.logic.snake-test
  (:require
   [snakinha.logic.snake :as snake]
   [clojure.test :as t]))

(def snake {:with-size-4 [{:x 0, :y 0}
                          {:x 0, :y 1}
                          {:x 0, :y 2}
                          {:x 0, :y 3, :head true}]
            :raise->right [{:x 0, :y 1, :head false}
                           {:x 0, :y 2, :head false}
                           {:x 0, :y 3, :head false}
                           {:x 0, :y 4, :head true}]
            :raise->down [{:x 0, :y 1, :head false}
                          {:x 0, :y 2, :head false}
                          {:x 0, :y 3, :head false}
                          {:x 1, :y 3, :head true}]
            :raise->right->down [{:x 0, :y 2, :head false}
                                 {:x 0, :y 3, :head false}
                                 {:x 0, :y 4, :head false}
                                 {:x 1, :y 4, :head true}]
            :raise->right->down->left [{:x 0, :y 3, :head false}
                                       {:x 0, :y 4, :head false}
                                       {:x 1, :y 4, :head false}
                                       {:x 1, :y 3, :head true}]})

(def draw-snake-test-table {:input-board [["x" "x" "x"] ["x" "x" "x"] ["x" "x" "x"]]
                            :snake [{:x 0, :y 0} {:x 0, :y 1, :head true}]
                            :expected-board [["s" "H" "x"] ["x" "x" "x"] ["x" "x" "x"]]})


(t/deftest raise-snake-test
  (t/testing "snake raise as it should with size 4"
    (t/is (= (:with-size-4 snake) (snake/raise-snake 4)))))

(t/deftest move-to-test
  (t/testing "snake should move to right"
    (t/is (= (:raise->right snake) (snake/move-snake-to "right" (:with-size-4 snake)))))
  (t/testing "snake should do nothing because it rise at leftier side"
    (t/is (= (:with-size-4 snake) (snake/move-snake-to "left" (:with-size-4 snake)))))
  (t/testing "snake should move to down"
    (t/is (= (:raise->down snake) (snake/move-snake-to "down" (:with-size-4 snake)))))
  #_(t/testing "snake should do nothing because it raise at upper position"
      (t/is (= (:with-size-4 snake) (snake/move-snake-to "down" (:with-size-4 snake))))) ;;precisa arrumar esse teste, esta se movendo além do tabuleiro y = -1
  (t/testing "snake should move to down after moved to right"
    (t/is (= (:raise->right->down snake) (snake/move-snake-to "down" (:raise->right snake)))))
  (t/testing "snake should move to left afeter moved down after moved to right"
    (t/is (= (:raise->right->down->left snake) (snake/move-snake-to "left" (:raise->right->down snake))))))

(t/deftest reverse?-test
  (t/testing "should return true for a reverse move try"
    (t/is (true? (snake/reverse? (:with-size-4 snake) {:x 0 :y 2}))))
  (t/testing "should return false for a valid move try"
    (t/is (false? (snake/reverse? (:with-size-4 snake) {:x 0 :y 4})))))

(t/deftest colide?-test
  (t/testing "should return true for a colide movement"
    (t/is (true? (snake/colide? (:with-size-4 snake) {:x 0 :y 1}))))
  (t/testing "should return false for a non colide movement"
    (t/is (false? (snake/colide? (:with-size-4 snake) {:x 0 :y 4})))))

(t/deftest draw-snake-test
  (t/testing "should draw a board correctly"
    (t/is (= (:expected-board draw-snake-test-table) 
           (snake/draw-snake 
            (:snake draw-snake-test-table) 
            (:input-board draw-snake-test-table))))))
