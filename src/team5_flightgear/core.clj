(ns team5-flightgear.core
  (:use [flightgear.api]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn gentlemen-start-your-engines []
  (starter! true)
  (loop []
    (if (engine-running?)
      (starter! false)
      (do
        (Thread/sleep 500)
        (recur)))))

(defn roll []
  (:indicated-roll-deg (indicated-attitude)))

(def current-aileron (atom 0))

(defn update-aileron [val]
  (reset! current-aileron val)
  (aileron! val))

(defn level-plane []
  (cond
   (> -2 (roll))
   (update-aileron 0.1)
   (< 2 (roll))  (update-aileron -0.1)
   :else          (update-aileron 0)))


(defn keep-level []
  (cond
   (< 2000 (indicated-altitude-ft)) (do (elevator! -0.1) (println "aiming down"))
   (> 2000 (indicated-altitude-ft)) (do (elevator! 0.1)  (println "aiming up"))))

(defn master-controls []
  (loop []
    (level-plane)
    (keep-level)
    (Thread/sleep 100)
    (recur)))