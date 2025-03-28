package fr.iutfbleau.dotlink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        AdapterPuzzleClickListener choisi = new AdapterPuzzleClickListener(this);
        adapter = new PuzzleAdapter(puzzles, choisi);

        recyclerView.setAdapter(adapter);
    }
}
