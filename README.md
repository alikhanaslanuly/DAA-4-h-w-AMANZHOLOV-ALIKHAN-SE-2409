# Assignment 4 - Smart City / Smart Campus Scheduling

Course: Design and Analysis of Algorithms  
Student: Amanzholov Alikhan  
Group: SE-2409
Instructor: Abay Rakhman  

---

## 1. Overview
This project is about working with graphs in the context of a Smart City or Smart Campus task scheduling system.  
The main idea is to analyze and plan tasks like maintenance, cleaning, or sensor repairs, where some tasks depend on others.  
Some dependencies are cyclic, and some are not. Because of that, I used two main algorithmic topics from the course:  

1. Strongly Connected Components (SCC)  
2. Shortest Paths in Directed Acyclic Graphs (DAGs)

I implemented:
- Tarjan’s algorithm for finding SCCs  
- Condensation graph creation (DAG of components)  
- Topological sort (Kahn’s algorithm)  
- Shortest and longest paths in a DAG

All code is written in Java and organized into clear packages like `graph.scc`, `graph.topo`, and `graph.dagsp`.

---

## 2. Datasets 
To test everything, I created nine datasets with different graph sizes and densities.  
They are stored in the folder `/data/`.

| Category | Files | Nodes | Description |
|-----------|--------|--------|-------------|
| Small | dataset_1.json - dataset_3.json | 6-10 | Simple cases with small cycles or DAGs |
| Medium | dataset_4.json - dataset_6.json | 10-20 | Mixed structures, several SCCs |
| Large | dataset_7.json - dataset_9.json | 20-50 | Dense graphs for performance testing |

There is also `tasks.json` (the sample from the instructor) and two small graphs for JUnit tests.

---

## 3. Implementation
The project uses edge weights to represent the cost or time between tasks.  

Main parts of the program:
- Tarjan’s SCC algorithm works with one DFS and detects all strongly connected components.
- Condensation graph turns SCCs into single nodes and builds a DAG.
- Kahn’s topological sort gives the order of nodes in the DAG.
- Shortest path algorithm finds the minimum distance from a source.
- Longest path algorithm finds the critical path of the DAG.

Metrics I collected:
- DFS calls and visited edges
- Queue operations (for Kahn)
- Number of relaxations
- Execution time with `System.nanoTime()`

---

## 4. Results
All datasets were tested, and the results are saved in `/data/results.csv`.  
There are also graphs in `/data/graphs/` that show how time and SCC count depend on the graph size.

Main results:
- SCC detection time increases with graph density  
- Condensation graph gets smaller when many cycles merge  
- Kahn’s algorithm is always linear  
- Longest path grows when there are more dependencies  
- For all datasets up to 50 nodes, runtime stays under 5 ms  

---

## 5. How to Run
### In IntelliJ IDEA
1. Open the project with `pom.xml`.  
2. Run the main class:
```

graph.App

```
3. To test with another dataset:
```

java -cp target/classes graph.App data/dataset_5.json

```

### Using Maven
```

mvn clean test
mvn package
java -cp target/classes graph.App

```
Make sure the `data/tasks.json` file is present in the project root.
WARNING! 
If the App.java is not running, you should do:
1. Run - Edit Configurations - New Application - In the Main class we choose graph.App
2. Working directory - Copy Path/Reference of 4DAA [assignment4] - in edit configuration we past the Path/Reference directory and apply it.
3. Run the project by this configuration
4. If it says that configuration is still incorrect - press continue anyway.

---

## 6. Testing
There are JUnit 5 tests in the `src/test/java/graph` folder:
- SccTests checks SCC detection on a small cycle.
- DagSpTests checks shortest path correctness on a small DAG.

---

## 7. Conclusion
This project helped me understand how SCC detection, topological sorting, and path algorithms work together.  
Tarjan’s algorithm was the hardest part to get right but also the most interesting.  
Kahn’s algorithm turned out to be very efficient and easy to debug.  
Overall, the algorithms work well even on larger datasets, and the performance results make sense.

---
