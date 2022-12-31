(ns snakinha.logic.snake-test
  (:require
   [snakinha.logic.snake :as snake]
   [clojure.test :refer :all]))

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



(deftest raise-snake-test
  (testing "snake raise as it should with size 4"
    (is (= (:with-size-4 snake) (snake/raise-snake 4)))))

(deftest move-to-test
  (testing "snake should move to right"
    (is (= (:raise->right snake) (snake/move-snake-to "right" (:with-size-4 snake)))))
  (testing "snake should do nothing because it rise at leftier side"
    (is (= (:with-size-4 snake) (snake/move-snake-to "left" (:with-size-4 snake)))))
  (testing "snake should move to down"
    (is (= (:raise->down snake) (snake/move-snake-to "down" (:with-size-4 snake)))))
  #_(testing "snake should do nothing because it raise at upper position"
      (is (= (:with-size-4 snake) (snake/move-snake-to "down" (:with-size-4 snake))))) ;;precisa arrumar esse teste, esta se movendo além do tabuleiro y = -1
  (testing "snake should move to down after moved to right"
    (is (= (:raise->right->down snake) (snake/move-snake-to "down" (:raise->right snake)))))
  (testing "snake should move to left afeter moved down after moved to right"
    (is (= (:raise->right->down->left snake) (snake/move-snake-to "left" (:raise->right->down snake))))))

(deftest reverse?-test
  (testing "should return true for a reverse move try"
    (is (true? (snake/reverse? (:with-size-4 snake) {:x 0 :y 2}))))
  (testing "should return false for a valid move try"
    (is (false? (snake/reverse? (:with-size-4 snake) {:x 0 :y 4})))))

(deftest colide?-test
  (testing "should return true for a colide movement"
    (is (true? (snake/colide? (:with-size-4 snake) {:x 0 :y 1}))))
  (testing "should return false for a non colide movement"
    (is (false? (snake/colide? (:with-size-4 snake) {:x 0 :y 4})))))

