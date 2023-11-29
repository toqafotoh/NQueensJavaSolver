import java.util.ArrayList;
import java.util.List;

public class NQueenSolver {
    private int n;
    private List<List<Integer>> solutions;

    public NQueenSolver(int n) {
        this.n = n;
        this.solutions = new ArrayList<>();
    }

    public List<List<Integer>> solve() {
        solveNQueen(0, new ArrayList<>());
        return solutions;
    }

    private void solveNQueen(int row, List<Integer> solution) {
        if (row == n) {
            solutions.add(new ArrayList<>(solution));
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

    private boolean isValidPosition(int row, int col, List<Integer> solution) {
        for (int i = 0; i < solution.size(); i++) {
            int prevRow = i;
            int prevCol = solution.get(i);

            if (col == prevCol || Math.abs(row - prevRow) == Math.abs(col - prevCol)) {
                return false;
            }
        }
        return true;
    }
}
