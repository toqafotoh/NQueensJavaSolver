import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int n = 4; // Change this to the desired board size
        NQueenSolver_thread solver = new NQueenSolver_thread(n);
        int numThreads = Thread.activeCount();
        System.out.println("Number of threads running-1: " + numThreads);
        solver.solve();
        numThreads = Thread.activeCount();
        System.out.println("Number of threads running-2: " + numThreads);
        numThreads = Thread.activeCount();
        System.out.println("Number of threads running-3: " + numThreads);


    }
//public static void main(String[] args) {
//    Backtracking thread = new Backtracking();
//    int[][] board = new int[thread.N][thread.N];
//
//    thread.solveNQ(board, 0);
//
//    if (thread.solutionCount == 0) {
//        System.out.println("No solutions found!");
//    } else {
//        System.out.println();
//        System.out.println("Total solutions found: " + thread.solutionCount);
//    }
//}

}
