package fr.iutfbleau.dotlink;

import android.content.Intent;
import android.view.View;
import android.app.Activity;

public class RetourClickListener implements View.OnClickListener {
    private final Activity activity;

    public RetourClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
