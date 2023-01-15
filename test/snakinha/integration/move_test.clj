(ns snakinha.integration.move-test
  (:require [clojure.test :as t]
            [snakinha.logic.board :as b]
            [snakinha.logic.snake :as s]
            [snakinha.logic.fruit :as f]))

(def test-table (array-map
                 :start {:message "first state of game should be correct"
                         :output "s s s H x x\nx x x x x x\nx x x x x x\nx x x x x x\nx x x x x x\nx x x x x f"}
                 :down {:message "move from first state to down"
                        :input "down"
                        :output "x s s s x x\nx x x H x x\nx x x x x x\nx x x x x x\nx x x x x x\nx x x x x f"}
                 :down-second {:message "move another time to down"
                               :input "down"
                               :output "x x s s x x\nx x x s x x\nx x x H x x\nx x x x x x\nx x x x x x\nx x x x x f"}
                 :down-third {:message "move third time to down"
                              :input "down"
                              :output "x x x s x x\nx x x s x x\nx x x s x x\nx x x H x x\nx x x x x x\nx x x x x f"}
                 :up-colide {:message "move to backward should do nothing"
                             :input "up"
                             :output "x x x s x x\nx x x s x x\nx x x s x x\nx x x H x x\nx x x x x x\nx x x x x f"}
                 :right {:message "should move snake to right"
                         :input "right"
                         :output "x x x x x x\nx x x s x x\nx x x s x x\nx x x s H x\nx x x x x x\nx x x x x f"}
                 :up {:message "should move snake to up"
                      :input "up"
                      :output "x x x x x x\nx x x x x x\nx x x s H x\nx x x s s x\nx x x x x x\nx x x x x f"}
                 :down-colide {:message "move to backward should do nothing"
                               :input "down"
                               :output "x x x x x x\nx x x x x x\nx x x s H x\nx x x s s x\nx x x x x x\nx x x x x f"}
                 :left {:message "should move to left without colide"
                        :input "left"
                        :output "x x x x x x\nx x x x x x\nx x x H s x\nx x x s s x\nx x x x x x\nx x x x x f"}
                 :up-second {:message "should up again"
                             :input "up"
                             :output "x x x x x x\nx x x H x x\nx x x s s x\nx x x x s x\nx x x x x x\nx x x x x f"}))


(t/deftest movement-test
  (let [board (b/make-board 6 6 "x")
        snake (atom (s/raise-snake 4))
        fruit (f/bear-fruit 5 5 board @snake)]

    (t/is (empty? (->>
                   (map (fn [test]
                          (let [test-key (key test)
                                test-value (val test)
                                {:keys [message input output]} test-value
                                result (do (when (not= test-key :start)
                                             (swap! snake (partial s/move-snake-to input)))
                                           (t/testing message
                                             (t/is (= output (b/render-board (f/draw-fruit (s/draw-snake @snake board) fruit "f"))))))]
                            {:test-id test-key :result result})) test-table)
                   (filter (fn [test-result]
                             (false? (:result test-result)))))))))

