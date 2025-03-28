package fr.iutfbleau.dotlink;

import android.content.Context;
import android.widget.Toast;

public class PuzzleCompletionHandler implements OnPuzzleCompleteListener {

    private final Context context;

    public PuzzleCompletionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPuzzleCompleted() {
        Toast.makeText(context, "🎉 Puzzle complété !", Toast.LENGTH_LONG).show();
    }
}
