package fr.iutfbleau.dotlink.views;

import android.widget.TextView;
import fr.iutfbleau.dotlink.controllers.GameController;

public class ScoreChangeHandler implements OnScoreChangeListener {
    private TextView scoreText;
    private TextView chronoText;
    private GameController controller;

    public ScoreChangeHandler(TextView scoreText, TextView chronoText, GameController controller) {
        this.scoreText = scoreText;
        this.chronoText = chronoText;
        this.controller = controller;
    }

    @Override
    public void onScoreChanged(int completed, int total) {
        scoreText.setText("⭐ Score : " + completed + "/" + total);
        chronoText.setText("⏱ Temps : " + controller.getModel().getSecondsElapsed() + "s");
    }
}