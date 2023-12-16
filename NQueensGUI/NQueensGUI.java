import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class NQueensGUI extends JFrame {
    private CyclicBarrier barrier;
    public static int N = 0;
    private char[][][] boards;
    private int solutionsCount;
    private boolean stopThreads;
    private volatile boolean solutionFound= false;
    private char[][] final_solution;
    private List<nqeen> threads = new ArrayList<>();
    private JButton showFinalSolutionButton;
    private JButton solveButton;
    Color lightColor = new Color(255, 206, 158);
    Color darkColor = new Color(209, 139, 71);

    public NQueensGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        String input = JOptionPane.showInputDialog("Enter the value of N:");
        if (input == null || input.trim().isEmpty()) {
            // User canceled or entered an empty string
            System.exit(0);
        }
        try {
            N = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        this.barrier = new CyclicBarrier(N);
        this.N = N;
        this.boards = new char[N][N][N];
        this.solutionsCount = 0;
        this.stopThreads = false;
        setTitle("N-Queens Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 700);
        setLayout(new GridLayout(2, 1));
        for (int i = 0; i < N; i++) {
            JPanel chessboardPanel = createChessboardPanel();
            add(chessboardPanel);
        }

        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createChessboardPanel() {
        //JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel chessboardPanel = new JPanel(new GridLayout(N, N));
        chessboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int squareSize = 10;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(squareSize, squareSize));
                //square.setMaximumSize(new Dimension(squareSize, squareSize));
                //square.setMinimumSize(new Dimension(squareSize, squareSize));
                square.setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
                square.setOpaque(true);
                chessboardPanel.add(square);
            }
        }

        return chessboardPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        solveButton = new JButton("Solve");
        showFinalSolutionButton = new JButton("Show Final Solution");
        showFinalSolutionButton.setEnabled(false);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveNQueensMultithreaded();
            }
        });

        showFinalSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    showFinalSolution();
            }
        });

        controlPanel.add(solveButton);
        controlPanel.add(showFinalSolutionButton);


        return controlPanel;
    }

    private void solveNQueensMultithreaded() {

        solveButton.setEnabled(false);
        solveButton.setBackground(Color.RED);
        clearChessboards();
        stopThreads = false;
        solutionsCount = 0;
        setTitle("N-Queens Solver");
        int numThreads = Thread.activeCount();
        System.out.println("Number of threads running-1: " + numThreads);
        for (int i = 0; i < N; i++) {
            char[][] board = boards[i];
            int finalI = i;
            Thread thread = new nqeen(i,board,N,this.barrier);
            thread.start();
            threads.add((nqeen) thread);
        }
        int x = Thread.activeCount();
        System.out.println("Threads after creating n threads: " + x);
    }
    public void updateChessboard(char[][] board, int index) {
        JPanel chessboardPanel = (JPanel) getContentPane().getComponent(index);
        Component[] components = chessboardPanel.getComponents();
        for (Component component : components) {
            JButton square = (JButton) component;
            square.setIcon(null);
        }
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                JButton square = (JButton) chessboardPanel.getComponent(row * N + col);
                if (board[row][col] == 'Q') {
                    ImageIcon queenIcon = new ImageIcon("queen.png");
                    //int squareSize = square.getPreferredSize().width;
                    Image resizedQueenImage = queenIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    ImageIcon resizedQueenIcon = new ImageIcon(resizedQueenImage);
                    square.setIcon(resizedQueenIcon);
                }
                else
                    square.setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
            }
        }
    }




    private void clearChessboards() {
        for (char[][] board : boards) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    board[i][j]='_';
                }
            }
        }
    }
    public void printBoard(char[][] arr) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    private char[][] final_solution() {
        boolean foundSolution = false;
        int id = -1;
        for (nqeen thread : threads) {
            synchronized (thread) {
                if (thread.get_sol_id() == thread.get_id()) {
                    foundSolution = true;
                    id = thread.get_id();
                    break;
                }
            }
        }
        final_solution = nqeen.final_sol;
        return final_solution;
    }
    private void showFinalSolution() {
        showFinalSolutionButton.setEnabled(true);
        JFrame resultFrame = new JFrame("Final Solution");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(400, 600); // Increased height to accommodate the text

        ImageIcon celebrate = new ImageIcon("Happy Well Done GIF by Top Talent - Find & Share on GIPHY.gif");
        JLabel celebration = new JLabel(celebrate);
        resultFrame.add(celebration);

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultFrame.getContentPane().removeAll();

                // Create a JPanel to hold the chessboard and text
                JPanel mainPanel = new JPanel(new BorderLayout());
                String text = "Final solution found by Thread " + nqeen.id_of_thread;

// Create a JLabel for the text
                String styledText = "<html><span style='font-size:30pt;margin-left: 10px; font-family:\" Arial Rounded MT Bold\", sans-serif;'>";
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    String color = "";
                    switch (i % 6) {
                        case 0:
                            color = "red";
                            break;
                        case 1:
                            color = "orange";
                            break;
                        case 2:
                            color = "pink";
                            break;
                        case 3:
                            color = "green";
                            break;
                        case 4:
                            color = "blue";
                            break;
                        case 5:
                            color = "purple";
                            break;
                    }
                    styledText += "<span style='color:" + color + "; border: 1px solid black; padding: 5px;'>" + c + "</span>";
                }
                styledText += "</span></html>";

                JLabel textLabel = new JLabel(styledText);
                textLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(textLabel, BorderLayout.NORTH);


                // Create the chessboard panel
                JPanel chessboardPanel = new JPanel(new GridLayout(N, N));
                chessboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                int squareSize = 50;

                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        JButton square = new JButton();
                        square.setPreferredSize(new Dimension(squareSize, squareSize));
                        square.setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
                        if (final_solution[row][col] == 'Q') {
                            ImageIcon queenIcon = new ImageIcon("queen.png");
                            Image resizedQueenImage = queenIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                            ImageIcon resizedQueenIcon = new ImageIcon(resizedQueenImage);
                            square.setIcon(resizedQueenIcon);
                        }
                        square.setOpaque(true);
                        chessboardPanel.add(square);
                    }
                }

                JPanel chessboardWrapper = new JPanel(new BorderLayout());
                chessboardWrapper.add(chessboardPanel, BorderLayout.CENTER);
                mainPanel.add(chessboardWrapper, BorderLayout.CENTER);

                // Create another GIF panel at the south-right
                ImageIcon anotherGifIcon = new ImageIcon("ezgif.com-resize.gif");
                JLabel anotherGifLabel = new JLabel(anotherGifIcon);
                ImageIcon HH = new ImageIcon("ezgif.com-resize (1).gif");
                JLabel ii = new JLabel(HH);
                JPanel gifPanel = new JPanel(new BorderLayout());
                gifPanel.add(anotherGifLabel,BorderLayout.EAST);
                gifPanel.add(ii,BorderLayout.WEST);
                mainPanel.add(gifPanel, BorderLayout.SOUTH);
                resultFrame.add(mainPanel);
                resultFrame.revalidate();
                resultFrame.repaint();
            }
        });

        timer.setRepeats(false);
        timer.start();

        resultFrame.setVisible(true);
    }

    public class nqeen  extends Thread {
        private Semaphore mutex = new Semaphore(1);
        private CyclicBarrier barrier;
        int id ;
        char[][] board;
        int N ;
        public int[] threadSolution;
        private boolean hasSolution = false;
        private static volatile int id_of_thread;
        private static final AtomicBoolean firstSolutionFound = new AtomicBoolean(false);
        private static volatile AtomicInteger count = new AtomicInteger(0);

        private static char[][] final_sol;
        public nqeen(int id,char[][]board,int N,CyclicBarrier barrier){
            this.id=id;
            this.board=board;
            this.N=N;
            this.barrier=barrier;
        }
        public void run() {
            if (Thread.interrupted()) {
                System.out.println("ONE THREAD INTERRUPTED" + Thread.currentThread().getName());
                // Handle interruption or return early
                return;
            }
            try {
                barrier.await();
                System.out.println("process waiting: "+Thread.currentThread().getName());
            }catch (InterruptedException | BrokenBarrierException e){
                System.out.println("ERROR IN THRED NUMBER :"+Thread.currentThread().getName());
                return;
            }
            solveNQueens(0, this.id);
            System.out.println("hhhh");
            synchronized (this) {
                if (this.hasSolution == true && firstSolutionFound.compareAndSet(false, true))
                {
                    System.out.println(Thread.currentThread().getName() + "has found solution");
                    id_of_thread = this.id;
                    nqeen.final_sol = this.board;
                    solutionFound = true;
                }
            }
            try {
                mutex.acquire();
                System.out.println("mutex acuired"+Thread.currentThread().getName());
                synchronized (count) {
                    if (count.incrementAndGet() == N) {
                        System.out.println(count);
                        char[][] x = final_solution();
                        printBoard(x);
                        showFinalSolution();
                    } else System.out.println("loool");
                }
            } catch (InterruptedException e) {
                // exception handling code
            } finally {
                mutex.release();
                System.out.println("mutex released"+Thread.currentThread().getName());
            }

        }
        private int get_id(){
            return this.id;
        }

        private char[][] get_final_solution(){
            return nqeen.final_sol;
        }
        public int get_sol_id() {
            return nqeen.id_of_thread;
        }
        public boolean hasSolution() {
            return hasSolution;
        }
        private boolean solveNQueens(int row, int intial_col) {
            if (Thread.currentThread().isInterrupted()) { // Check if the thread has been interrupted
                return false;
            }

            if (row == N) {
                this.hasSolution = true;
                return true;
            }

            for (int col = 0; col < N; col++) {
                if (isSafe(row, col)) {
                    if(row==0){
                        board[row][intial_col]='Q';
                        updateChessboard(this.board,this.id);
                    }
                    else
                        board[row][col]='Q';
                    updateChessboard(this.board,this.id);
                    try {
                        this.sleep(274); // Adjust this value for the visualization speed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(solveNQueens(row+1,col)){
                        return true;
                    }
                    if(col==intial_col && row==0){
                        board[row][intial_col]='_';
                        updateChessboard(this.board,this.id); try {
                            this.sleep(274); // Adjust this value for the visualization speed
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    } //it means that no posssible sol
                    board[row][col]='_';
                    updateChessboard(this.board,this.id);
                    try {
                        this.sleep(274); // Adjust this value for the visualization speed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }


        private boolean isSafe(int row, int col) {
            for (int r=0;r<N;r++){
                if(board[r][col]=='Q'){
                    return false;
                }
            }

            // row check
            for (int c=0;c<N;c++){
                if(board[row][c]=='Q'){
                    return false;
                }
            }
            // Diagonal checks
            for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
                if (board[i][j] == 'Q') {
                    return false;
                }
            }
            for (int i = row, j = col; i >= 0 && j < N; i--, j++) {
                if (board[i][j] == 'Q') {
                    return false;
                }
            }
            return true;
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NQueensGUI().setVisible(true));
    }
}