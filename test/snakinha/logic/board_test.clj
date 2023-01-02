(ns snakinha.logic.board-test
  (:require [snakinha.logic.board :as board]
            [clojure.test :refer :all]))

(def board-test-table {:input {:x 2 :y 2 :back "x"}
                       :expected [["x" "x"] ["x" "x"]]})

(deftest make-board-test
  (testing "should return a correct 2x2 board"
    (let [{:keys [expected input]} board-test-table
          {:keys [x y back]} input]
      (is (= expected (board/make-board x y back))))))

(def render-board-tt {:input [["x" "x"] ["x" "x"]]
                      :expected "x x\nx x"})
(deftest render-board-test
  (testing "should render a correct 2x2 board"
    (let [{:keys [input expected]} render-board-tt]
      (is (= expected (board/render-board input))))))