package fr.iutfbleau.dotlink;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PuzzleViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public PuzzleViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.puzzleName);    }
}
