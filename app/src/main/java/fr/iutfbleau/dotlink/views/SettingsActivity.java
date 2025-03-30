package fr.iutfbleau.dotlink.views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Button;

import android.widget.Toast;
import android.widget.CompoundButton;

import fr.iutfbleau.dotlink.R;

public class SettingsActivity extends Activity {

    private static final String PREFS_NAME = "DotLinkPrefs";
    private static final String ACHROMATE_KEY = "modeAchromate";
    private Switch achromateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(new RetourListener(this));

        achromateSwitch = findViewById(R.id.switchAchromate);
        achromateSwitch.setOnCheckedChangeListener(new SwitchAchromateListener(this));

        boolean isAchromate = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(ACHROMATE_KEY, false);
        achromateSwitch.setChecked(isAchromate);
    }

    public static boolean isAchromateMode(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(ACHROMATE_KEY, false);
    }
}
