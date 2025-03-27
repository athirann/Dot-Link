package fr.iutfbleau.flowfree;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PuzzleViewHolder extends RecyclerView.ViewHolder {
    Button button;

    public PuzzleViewHolder(@NonNull View itemView) {
        super(itemView);
        button = (Button) itemView;
    }
}

