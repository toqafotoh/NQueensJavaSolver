import java.util.ArrayList;
import java.util.List;

public class NQueenSolver_thread {
    private int n;
    private List<Integer> solution;
    private volatile boolean foundSolution;

    public NQueenSolver_thread(int n) {
        this.n = n;
        this.solution = new ArrayList<>();
        this.foundSolution = false;
    }

    public List<Integer> solve() throws InterruptedException {
        List<NQueenThread> threads = new ArrayList<>();
        int numThreads = n; // Create a number of threads equal to the N Queen
        int rowsPerThread = 1;

        // Create and start the threads
        for (int i = 0; i < numThreads; i++) {
            NQueenThread thread = new NQueenThread(i);
            thread.start();
            threads.add(thread);
        }

        List<Integer> solution = new ArrayList<>(); // Initialize the solution list

        // Wait for all threads to finish
        boolean foundSolution = false;
        for (NQueenThread thread : threads) {
            thread.join();
            if (thread.hasSolution()) {
                solution = thread.getSolution();
                foundSolution = true;
                // Interrupt all the remaining threads without a solution
                for (NQueenThread remainingThread : threads) {
                    if (remainingThread != thread && !remainingThread.hasSolution()) {
                        remainingThread.interrupt();
                    }
                }
                break;
            }
        }

        if (foundSolution) {
            System.out.println("Solution: " + solution);
        } else {
            System.out.println("No solution found.");
        }

        return solution;
    }


    private class NQueenThread extends Thread {
        private int row;
        private List<Integer> threadSolution;
        private boolean hasSolution;

        public NQueenThread(int row) {
            this.row = row;
            this.threadSolution = new ArrayList<>();
            this.hasSolution = false;
        }

        public List<Integer> getSolution() {
            return threadSolution;
        }

        public boolean hasSolution() {
            return hasSolution;
        }

        @Override
        public void run() {
            solveNQueen(row, new ArrayList<>());
            if (hasSolution) { // Terminate other threads if a solution has been found
                for (Thread thread : Thread.getAllStackTraces().keySet()) {
                    if (thread != this && thread instanceof NQueenThread) {
                        ((NQueenThread) thread).interrupt();
                    }
                }
            }
        }

        private void solveNQueen(int row, List<Integer> solution) {
            if (interrupted()) { // Check if the thread has been interrupted
                return;
            }

            if (row == n) {
                threadSolution = new ArrayList<>(solution);
                hasSolution = true;
                return;
            }

            for (int col = 0; col < n; col++) {
                if (isValidPosition(row, col, solution)) {
                    solution.add(col);
                    solveNQueen(row + 1, solution);
                    solution.remove(solution.size() - 1);
                }
            }
        }

        private boolean isValidPosition(int row, int col, List<Integer> queens) {
            for (int i = 0; i < row; i++) {
                int queenRow = i;
                int queenCol = queens.get(i); // Add a check to ensure that queens is not empty before accessing its elements
                if (queenCol == col || queenRow + queenCol == row + col || queenRow - queenCol == row - col) {
                    return false;
                }
            }
            return true;
        }

    }

}
