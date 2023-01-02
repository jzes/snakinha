(ns snakinha.logic.fruit-test
  (:require [snakinha.logic.fruit :as fruit]
            [clojure.test :refer :all]))

(def bear-fruit-success {:input-board [["x" "x" "x"] ["x" "x" "x"] ["x" "x" "x"]]
                    :input-snake [{:x 0, :y 0} {:x 0, :y 1, :head true}]
                    :input-xy {:x 3, :y 3}
                    :expected {:x 3, :y 3}})

(def bear-fruit-error-out {:input-board [["x" "x" "x"] ["x" "x" "x"] ["x" "x" "x"]]
                         :input-snake [{:x 0, :y 0} {:x 0, :y 1, :head true}]
                         :input-xy {:x 4, :y 4}
                         :expected #"fruit out of bounds"})

(def bear-fruit-error-over {:input-board [["x" "x" "x"] ["x" "x" "x"] ["x" "x" "x"]]
                           :input-snake [{:x 0, :y 0} {:x 0, :y 1, :head true}]
                           :input-xy {:x 0, :y 1}
                           :expected #"fruit cant bear over snake"})

(deftest bear-fruit-test
  (testing "should return fruit coordenates"
    (let [{:keys [input-board
                  input-snake
                  input-xy
                  expected]} bear-fruit-success
          {:keys [x y]} input-xy]
      (is (= expected (fruit/bear-fruit x y input-board input-snake)))))
  (testing "should throw a exception out of bounds"
    (let [{:keys [input-board
                  input-snake
                  input-xy
                  expected]} bear-fruit-error-out
          {:keys [x y]} input-xy]
      (is (thrown-with-msg? java.lang.Exception expected (fruit/bear-fruit x y input-board input-snake)))))
  (testing "should throw a exception cant bear over snake"
    (let [{:keys [input-board
                  input-snake
                  input-xy
                  expected]} bear-fruit-error-over
          {:keys [x y]} input-xy]
      (is (thrown-with-msg? java.lang.Exception expected (fruit/bear-fruit x y input-board input-snake))))))

(def draw-fruit-succes {:input {:board [["x" "x"] ["x" "x"]]
                                :xy {:x 0 :y 0}
                                :sprite "f"}
                        :expected [["f" "x"] ["x" "x"]]})

(deftest draw-fruit-test
  (testing "should draw a fruit in a board correctly"
    (let [{:keys [input expected]} draw-fruit-succes
          {:keys [board xy sprite]} input]
      (is (= expected (fruit/draw-fruit board xy sprite))))))