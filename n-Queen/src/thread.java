public class thread implements Runnable {
    private Backtracking backtracking;
    private static volatile boolean solutionFound = false;

    public thread(Backtracking backtracking) {
        this.backtracking = backtracking;
    }

    @Override
    public void run() {
        int[][] board = new int[backtracking.N][backtracking.N];
        backtracking.solveNQ(board, 0);

        // Check if a solution has been found
        if (!solutionFound) {
            synchronized (thread.class) {
                // Set the solutionFound flag to true
                solutionFound = true;
                // Interrupt all threads
                Thread.currentThread().interrupt();
            }

            // Print the solution
            backtracking.display(board);
        }
    }


    public static void main(String[] args) {
        Backtracking backtracking = new Backtracking();

        // Create two threads
        Thread thread1 = new Thread(new thread(backtracking));
        Thread thread2 = new Thread(new thread(backtracking));

        // Start the threads
        thread1.start();

        try {
            // Wait for both threads to finish
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (backtracking.solutionCount == 0) {
            System.out.println("No solutions found!");
        } else {
            System.out.println("Total solutions found: " + backtracking.solutionCount);
        }
    }
}
