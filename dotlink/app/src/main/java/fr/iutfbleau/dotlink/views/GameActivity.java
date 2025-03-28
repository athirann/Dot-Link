package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import fr.iutfbleau.dotlink.R;
import fr.iutfbleau.dotlink.controllers.GameController;
import fr.iutfbleau.dotlink.controllers.ResetController;
import fr.iutfbleau.dotlink.controllers.RetourController;
import fr.iutfbleau.dotlink.models.GameModel;
import fr.iutfbleau.dotlink.models.Puzzle;

public class GameActivity extends Activity {
    private TextView chronoText, scoreText;
    private Button resetButton, retourButton;
    private GameView gameView;
    private GameController controller;
    private PuzzleCompleteHandler completeHandler;
    private ScoreChangeHandler scoreHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chronoText = findViewById(R.id.chronoText);
        scoreText = findViewById(R.id.scoreText);
        resetButton = findViewById(R.id.resetButton);
        retourButton = findViewById(R.id.backButton);
        gameView = findViewById(R.id.gameView);

        Puzzle puzzle = (Puzzle) getIntent().getSerializableExtra("puzzle");
        if (puzzle == null) {
            finish();
            return;
        }

        GameModel model = new GameModel(puzzle);
        gameView.setPuzzle(puzzle);
        controller = new GameController(model, gameView, this);
        gameView.setController(controller);

        completeHandler = new PuzzleCompleteHandler(this, controller);
        scoreHandler = new ScoreChangeHandler(scoreText, chronoText, controller);

        controller.addPuzzleCompleteListener(completeHandler);
        controller.addScoreChangeListener(scoreHandler);

        ResetController resetController = new ResetController(controller);
        RetourController retourController = new RetourController(this);

        resetButton.setOnClickListener(v -> resetController.reset());
        retourButton.setOnClickListener(v -> retourController.retourAuMenu());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}