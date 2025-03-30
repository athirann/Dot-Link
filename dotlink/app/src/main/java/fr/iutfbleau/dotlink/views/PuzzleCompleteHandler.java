package fr.iutfbleau.dotlink.views;

import android.app.AlertDialog;
import android.content.Context;
import fr.iutfbleau.dotlink.controllers.GameController;

public class PuzzleCompleteHandler implements OnPuzzleCompleteListener {
    private Context context;
    private GameController controller;

    public PuzzleCompleteHandler(Context context, GameController controller) {
        this.context = context;
        this.controller = controller;
    }

    @Override
    public void onPuzzleCompleted() {
        new AlertDialog.Builder(context)
                .setTitle("🎉 Bravo !")
                .setMessage("Tu as terminé le puzzle !")
                .setPositiveButton("OK", new OkClickListener())
                .setCancelable(false)
                .show();
    }
}
