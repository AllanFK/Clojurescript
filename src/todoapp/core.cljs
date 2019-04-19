(ns todoapp.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-state 
  (atom {
    :text "Todo App in Clojurescript"
    :todos [
      {:name "todo1" :checked false}
      {:name "todo2" :checked false}
      {:name "todo3" :checked false}
    ]
    :todo {:name "" :checked false}
    :filterMode "all"
  }))

(defn newTodo [e]
  (swap! app-state assoc-in [:todo :name] e.target.value))

(defn addTodo []
  (swap! app-state update :todos conj (:todo @app-state)))

(defn checkTodo [i]
  (swap! app-state update :todos assoc-in [i :checked] true))

(defn view [type]
  (swap! app-state assoc :filterMode type))

(defn filterTodo? [todo]
  (case (:filterMode @app-state)
    "all" true
    "completed" (= (get todo :checked) true)
    "missing" (= (get todo :checked) false) 
  ))

(defn filteredTodos [] 
  (filter filterTodo? (:todos @app-state)))

(defn getTodos []
  (map-indexed (fn [i todo] 
    [:li {:on-click #(checkTodo i)} 
      (get todo :name)
      (if (= (get todo :checked) true) " (completed)" "")
    ]) 
    (filteredTodos)))

(defn ^:export exportedFunc []
  (println "exported"))

(defn getTodosReduceWay []
  (reduce (fn [total todo] 
    (into total [:li (get todo :name)]) )
    ; (total conj [:li (get todo :name)])) 
    []
    (:todos @app-state)))

(defn app []
  [:div
    [:h1 (:text @app-state)]
    [:span {:on-click #(view "all")} "all "]
    [:span {:on-click #(view "completed")} "completed "]
    [:span {:on-click #(view "missing")} "missing "]
    [:h3 "Add todos"]
    [:ul (getTodosReduceWay)]
    [:br]
    [:input {:on-change newTodo :placeholder "Write todo"}]
    [:br][:br]
    [:button {:on-click addTodo} "Add todo"]
  ]
)

(reagent/render-component [app]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
