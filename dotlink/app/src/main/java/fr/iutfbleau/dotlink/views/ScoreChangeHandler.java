package fr.iutfbleau.dotlink.views;

import android.widget.TextView;
import fr.iutfbleau.dotlink.controllers.GameController;

public class ScoreChangeHandler implements OnScoreChangeListener {
    private TextView scoreText;
    private GameController controller;

    public ScoreChangeHandler(TextView scoreText, GameController controller) {
        this.scoreText = scoreText;
        this.controller = controller;
    }

    @Override
    public void onScoreChanged(int completed, int total) {
        if (scoreText != null) {
            scoreText.setText("⭐ Score : " + completed + "/" + total);
        }
    }
}