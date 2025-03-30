package fr.iutfbleau.dotlink.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.iutfbleau.dotlink.R;
import fr.iutfbleau.dotlink.controllers.OnPuzzleClickListener;
import fr.iutfbleau.dotlink.models.Puzzle;

import java.util.List;

public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleViewHolder> {
    private List<Puzzle> puzzles;
    private OnPuzzleClickListener listener;

    public PuzzleAdapter(List<Puzzle> puzzles, OnPuzzleClickListener listener) {
        this.puzzles = puzzles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PuzzleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle, parent, false);
        return new PuzzleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuzzleViewHolder holder, int position) {
        Puzzle puzzle = puzzles.get(position);
        holder.textView.setText(puzzle.getNom());
        if(puzzle.isValid()){
            holder.textView.setOnClickListener(new PuzzleClickHandler(puzzle, listener));
        } else {
            holder.textView.setEnabled(false);
            holder.textView.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return puzzles.size();
    }

    private static class PuzzleClickHandler implements View.OnClickListener {
        private Puzzle puzzle;
        private OnPuzzleClickListener listener;

        public PuzzleClickHandler(Puzzle puzzle, OnPuzzleClickListener listener) {
            this.puzzle = puzzle;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onPuzzleClick(puzzle);
        }
    }
}