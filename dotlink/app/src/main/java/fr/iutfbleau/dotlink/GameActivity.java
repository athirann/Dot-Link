package fr.iutfbleau.dotlink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnPuzzleCompleteListener, OnScoreChangeListener {

    private TextView chronoText, scoreText;
    private Button resetButton, retourButton;
    private GameView gameView;

    private int secondsElapsed = 0;
    private Handler chronoHandler = new Handler();
    private Runnable chronoRunnable;

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
        if (puzzle != null) {
            gameView.setPuzzle(puzzle);
        } else {
            Toast.makeText(this, "Erreur : aucun puzzle reçu", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Chronomètre
        chronoRunnable = new Runnable() {
            @Override
            public void run() {
                secondsElapsed++;
                chronoText.setText("⏱ Temps : " + secondsElapsed + "s");
                chronoHandler.postDelayed(this, 1000);
            }
        };
        chronoHandler.post(chronoRunnable);

        resetButton.setOnClickListener(new ResetClickListener(this));
        retourButton.setOnClickListener(new RetourClickListener(this));

        gameView.setOnPuzzleCompleteListener(this);
        gameView.setOnScoreChangeListener(this);
        updateScore();
    }

    public void resetGame() {
        gameView.reset();
        secondsElapsed = 0;
        chronoText.setText("Temps : 0s");
        updateScore();
    }


    @Override
    public void onScoreChanged(int completed, int total) {
        scoreText.setText("Score : " + completed + "/" + total);
    }


    private void updateScore() {
        if (gameView != null && gameView.getPuzzle() != null) {
            int total = gameView.getPuzzle().getPaires().size();
            int completed = gameView.getCompletedCount();
            scoreText.setText("⭐ Score : " + completed + "/" + total);
        }
    }

    @Override
    public void onPuzzleCompleted() {
        chronoHandler.removeCallbacks(chronoRunnable);

        new AlertDialog.Builder(this)
                .setTitle("🎉 Bravo !")
                .setMessage("Vous avez complété le puzzle en " + secondsElapsed + " secondes.\nScore final : " +
                        gameView.getCompletedCount() + "/" + gameView.getPuzzle().getPaires().size())
                .setPositiveButton("OK", (dialog, which) -> retourAuMenu())
                .setCancelable(false)
                .show();
    }

    private void retourAuMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chronoHandler.removeCallbacks(chronoRunnable);
    }
}
