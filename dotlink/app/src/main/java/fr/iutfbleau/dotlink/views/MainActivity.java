package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.iutfbleau.dotlink.R;
import fr.iutfbleau.dotlink.controllers.PuzzleController;
import fr.iutfbleau.dotlink.models.Puzzle;
import fr.iutfbleau.dotlink.models.PuzzleLoader;

import java.util.List;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private PuzzleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Puzzle> puzzles = PuzzleLoader.chargerPuzzles(this);
        PuzzleController controller = new PuzzleController(this);
        adapter = new PuzzleAdapter(puzzles, controller);

        recyclerView.setAdapter(adapter);
    }
}