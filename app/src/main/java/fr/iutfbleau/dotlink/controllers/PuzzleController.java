package fr.iutfbleau.dotlink.controllers;

import android.content.Context;
import android.content.Intent;
import fr.iutfbleau.dotlink.models.Puzzle;
import fr.iutfbleau.dotlink.views.GameActivity;

public class PuzzleController implements OnPuzzleClickListener {
    private Context context;

    public PuzzleController(Context context) {
        this.context = context;
    }

    @Override
    public void onPuzzleClick(Puzzle puzzle) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("puzzle", puzzle);
        context.startActivity(intent);
    }
}