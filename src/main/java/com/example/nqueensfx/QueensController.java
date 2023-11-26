package com.example.nqueensfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QueensController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}