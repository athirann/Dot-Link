package fr.iutfbleau.dotlink;

import android.view.View;

public class ResetClickListener implements View.OnClickListener {
    private final GameActivity activity;

    public ResetClickListener(GameActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.resetGame();
    }
}
