import java.util.Arrays;

public class Backtracking {

        public  final int N = 16;
        public  int solutionCount = 0;

//        public  void main(String[] args) {
//            int[][] board = new int[N][N];
//
//            solveNQ(board, 0);
//
//            if (solutionCount == 0) {
//                System.out.println("No solutions found!");
//            } else {
//                System.out.println("Total solutions found: " + solutionCount);
//            }
//        }
// true
    public void display(int[][] board) {
        /*make one dimensional array to view
        0 1 0 0
        0 0 0 1
        1 0 0 0
        0 0 1 0
        [2, 0, 3, 1]
        */
        int[] queenPositions = new int[N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }

        for (int c = 0; c < N; c++) {
            for (int r = 0; r < N; r++) {
                if (board[r][c] == 1) {
                    queenPositions[c] = r;
                }
            }
        }
        System.out.println(Arrays.toString(queenPositions));
        System.out.println();
    }

//        private void display(int[][] board) {
//            int[] queenPositions = new int[N];
//
//            for (int r = 0; r < N; r++) {
//                for (int c = 0; c < N; c++) {
//                    System.out.print(board[r][c] + " ");
//                }
//                System.out.println();
//            }
//            for (int r = 0; r < N; r++) {
//                for (int c = 0; c < N; c++) {
//                    if (board[r][c] == 1) {
//                        queenPositions[r] = c;
//                    }
//                }
//            }
//            System.out.println(Arrays.toString(queenPositions));
//            System.out.println();
//        }


        public boolean solveNQ(int[][] board, int col) {
            if (col >= N) {
                display(board);
                solutionCount++;
                return false; // Continue searching for more solutions
            }

            boolean foundSolution = false;

            for (int i = 0; i < N; i++) {
                if (isSafePos(board, i, col)) {
                    board[i][col] = 1;

                    if (solveNQ(board, col + 1)) {
                        foundSolution = true;
                    }

                    board[i][col] = 0;
                }
            }

            return foundSolution;
        }



        /*
            check for every row at the solve function call issafepos
            that iterate all the colums if have quuen return false
            */
        private boolean isSafePos(int[][] board, int row, int col) {
            // for ROW
            for (int c = 0; c < col; c++) {
                if (board[row][c] == 1) {
                    return false;
                }
            }

           /* for current pos top-left diagonal
           (0 1 2 3)
        [0] 0 1 0 0
        [1] 0 0 0 1
        [2] 1 0 0 0
        [3] 0 0 1 0
            */
            for (int r = row, c = col; r >= 0 && c >= 0; r--, c--) {
                if (board[r][c] == 1) {
                    return false;
                }
            }
            /*for current pos bottom-left diagonal
                       (0 1 2 3)
                    [0] 0 1 0 0
                    [1] 0 0 0 1
                    [2] 1 0 0 0
                    [3] 0 0 1 0
                    */
            for (int r = row, c = col; c >= 0 && r < N; r++, c--) {
                if (board[r][c] == 1) {
                    return false;
                }
            }
            return true;
    }

}
