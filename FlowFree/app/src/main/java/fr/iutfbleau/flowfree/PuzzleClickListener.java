package fr.iutfbleau.flowfree;

import android.view.View;

public class PuzzleClickListener implements View.OnClickListener {
    private Puzzle puzzle;
    private OnPuzzleClickListener listener;

    public PuzzleClickListener(Puzzle puzzle, OnPuzzleClickListener listener) {
        this.puzzle = puzzle;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onPuzzleClick(puzzle);
        }
    }
}
