package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class RetourListener implements View.OnClickListener {

    private Activity activity;

    public RetourListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
}
