package fr.iutfbleau.dotlink.views;

import android.app.AlertDialog;
import android.content.Context;
import fr.iutfbleau.dotlink.controllers.GameController;
import fr.iutfbleau.dotlink.controllers.RetourController;

public class PuzzleCompleteHandler implements OnPuzzleCompleteListener {
    private Context context;
    private GameController controller;

    public PuzzleCompleteHandler(Context context, GameController controller) {
        this.context = context;
        this.controller = controller;
    }

    @Override
    public void onPuzzleCompleted() {
        RetourController retourController = new RetourController(context);
        new AlertDialog.Builder(context)
                .setTitle("🎉 Bravo !")
                .setMessage("Vous avez complété le puzzle en " + controller.getModel().getSecondsElapsed() + " secondes.")
                .setPositiveButton("OK", (dialog, which) -> retourController.retourAuMenu())
                .setCancelable(false)
                .show();
    }
}