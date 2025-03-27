package fr.iutfbleau.flowfree;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.gameView);

        String puzzleNom = getIntent().getStringExtra("puzzle_nom");
        List<Puzzle> puzzles = PuzzleLoader.chargerPuzzles(this);

        for (Puzzle p : puzzles) {
            if (p.getNom().equals(puzzleNom)) {
                gameView.setPuzzle(p);
                break;
            }
        }
    }
}