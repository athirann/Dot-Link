package fr.iutfbleau.dotlink.controllers;

public class ResetController {
    private GameController gameController;

    public ResetController(GameController gameController) {
        this.gameController = gameController;
    }

    public void reset() {
        gameController.resetGame();
    }
}