package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class OpenSettingsListener implements View.OnClickListener {
    private Activity activity;

    public OpenSettingsListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
