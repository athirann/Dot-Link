package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import fr.iutfbleau.dotlink.controllers.GameController;

public class PauseHandler {
    private Activity activity;
    private GameController controller;


    public PauseHandler(Activity activity, GameController controller) {
        this.activity = activity;
        this.controller = controller;
    }

    public void afficherPause() {

        new AlertDialog.Builder(activity)
                .setTitle("⏸ Jeu en pause")
                .setMessage("Reprendre la partie ?")
                .setCancelable(false)
                .setPositiveButton("Reprendre", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
