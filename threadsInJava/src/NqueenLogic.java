import java.lang.reflect.Array;
import java.util.Arrays;

public class NqueenLogic {
  static   private int size;
    char[][] chessBoard;


    public  NqueenLogic(int size, char[][] chessBoard) {
        this.size = size;
        this.chessBoard = chessBoard;
    }

    public  void printBoard(){

        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                System.out.print(chessBoard[i][j]+"  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean solution(int row,int startCol){

        if(row==size){
            printBoard();
            initializeChess();
            System.out.println(Thread.currentThread().getName());
            return true;
        }

        else {
            for (int col=0;col<size;col++){


                if(isSafe(row,col)){
                    if(row==0){
                        chessBoard[row][startCol]='Q';
                    }
                    else
                       chessBoard[row][col]='Q';

                   if( solution(row+1,col)){
                       return true;
                   }

                    if(col==startCol && row==0){
                        chessBoard[row][startCol]='_';
                        break;
                    }
                   chessBoard[row][col]='_';
                }
            }
        }
        return false;
    }

    private  boolean isSafe(int row, int col) {
     // column check
        for (int r=0;r<size;r++){
            if(chessBoard[r][col]=='Q'){
                return false;
            }
        }

        // row check
        for (int c=0;c<size;c++){
            if(chessBoard[row][c]=='Q'){
                return false;
            }
        }

//        // check two diagonal
//        for (int i=row,j=col;i>=0 && j>=0;i--,j--){
//            if(chessBoard[i][j]=='Q'){
//                return false;
//            }
//        }
//        for (int i=row,j=col;i>=0 && j<size  ;i++,j--){
//                if (chessBoard[i][j] == 'Q') {
//                    return false;
//                }
//
//        }
        // Diagonal checks
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (chessBoard[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = row, j = col; i >= 0 && j < size; i--, j++) {
            if (chessBoard[i][j] == 'Q') {
                return false;
            }
        }
        return true;
    }

  public void initializeChess(){
      for (int i = 0; i < size; i++) {
          Arrays.fill(chessBoard[i], '_');
      }
  }

}
