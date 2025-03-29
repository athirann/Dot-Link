package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SwitchAchromateListener implements CompoundButton.OnCheckedChangeListener {

    private Activity activity;
    private static final String PREFS_NAME = "DotLinkPrefs";
    private static final String ACHROMATE_KEY = "modeAchromate";

    public SwitchAchromateListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        prefs.edit().putBoolean(ACHROMATE_KEY, isChecked).apply();
        Toast.makeText(activity, "Mode achromate " + (isChecked ? "activé" : "désactivé"), Toast.LENGTH_SHORT).show();
    }
}
