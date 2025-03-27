package fr.iutfbleau.flowfree;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PuzzleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuzzleViewHolder holder, int position) {
        Puzzle puzzle = puzzles.get(position);
        Log.d("DEBUG", "Affichage du puzzle : " + puzzle.getNom());
        holder.button.setText(puzzle.getNom());

        if (puzzle.getPaires().isEmpty()) {
            holder.button.setEnabled(false); // Désactiver les puzzles invalides
        }

        Log.d("DEBUG", "Puzzle affiché: " + puzzle.getNom());


        PuzzleClickListener clickListener = new PuzzleClickListener(puzzle,listener);
        holder.button.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return puzzles.size();
    }


}
