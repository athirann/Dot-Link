package fr.iutfbleau.dotlink.views;

import android.view.View;

public class PauseClickListener implements View.OnClickListener {

    private PauseHandler pauseHandler;

    public PauseClickListener(PauseHandler pauseHandler) {
        this.pauseHandler = pauseHandler;
    }

    @Override
    public void onClick(View v) {
        pauseHandler.afficherPause();
    }
}
