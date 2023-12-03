import java.util.Arrays;
import java.util.Scanner;

public class Main {
 //   private static AtomicBoolean solutionFound = new AtomicBoolean(false);

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        System.out.println("enter size of your chessboard");
        int size= input.nextInt();
        char[][] chessBoard = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(chessBoard[i], '_');
        }
        NqueenLogic game = new NqueenLogic(size, chessBoard);


        Thread[] threads = new Thread[size];

        for (int i = 0; i < size; i++) {
            final int startCol = i;
            threads[i] = new Thread(() -> game.solution(0,startCol));
            threads[i].start();
        }


        for (Thread thread : threads) {
            try {
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    }
