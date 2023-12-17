# N-Queens Java Solver

**N-Queens Java Solver** is a multithreaded Java application designed to find solutions to the classic N-Queens problem. It features a concurrent backtracking algorithm and a graphical user interface (GUI) for real-time visualization.

## Introduction

The N-Queens problem involves placing N queens on an N×N chessboard in such a way that no two queens threaten each other. This application solves the problem using multiple threads and provides a GUI to observe the solving process.

## Features

- **Multithreaded Solving:** Efficiently searches for solutions using concurrent threads.
- **Real-Time Visualization:** GUI displays the N-Queens solving process step by step.
- **Dynamic Board Size:** Users can specify the size of the chessboard for different problem sizes.
- **Solution Highlighting:** Highlights the final solution found by the first completing thread.

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/toqafotoh/NQueensJavaSolver.git
2. **Compile and Run:**
   ```bash
   cd NQueensJavaSolver
   javac NQueensGUI.java
   java NQueensGUI
