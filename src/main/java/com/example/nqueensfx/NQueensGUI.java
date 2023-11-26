package com.example.nqueensfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class NQueensGUI extends Application {

    private static final int N = 4;
    private int[] queens;
    private int solutionsCount;
    private boolean stopThreads;
    private GridPane gridPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        queens = new int[N];
        solutionsCount = 0;
        stopThreads = false;

        primaryStage.setTitle("N-Queens Solver");

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Create chessboard grid
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Rectangle square = createSquare(i,j);
                gridPane.add(square, j, i);
            }
        }

        Button solveButton = new Button("Solve");
        Button stopButton = new Button("Stop");

        solveButton.setOnAction(e -> solveNQueensMultithreaded());
        stopButton.setOnAction(e -> stopThreads = true);

        gridPane.add(solveButton, N, 0);
        gridPane.add(stopButton, N, 1);

        Scene scene = new Scene(gridPane, 70 * N, 70* N);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solveNQueensMultithreaded() {
    }

    private Rectangle createSquare(int row, int col) {
        Rectangle rectangle = new Rectangle(70,70);
        // Alternate the color of the squares to create a checkerboard pattern
        if ((row + col) % 2 == 0) {
            rectangle.setFill(Color.WHITE);
        } else {
            rectangle.setFill(Color.BLACK);
        }
        return rectangle;
    }
    private void updateChessboard() {
        // Clear the chessboard
        clearChessboard();

        // Update the chessboard with queens
        for (int i = 0; i < N; i++) {
            int col = queens[i];
            Rectangle square = createSquare(i, col);
            gridPane.add(square, col, i);
        }
    }

    private void clearChessboard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Rectangle square = createSquare(i,j);
                gridPane.add(square, j, i);
            }
        }

        queens = new int[N];
    }
}
