package fr.iutfbleau.flowfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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