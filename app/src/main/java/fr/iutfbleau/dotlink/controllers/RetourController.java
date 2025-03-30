package fr.iutfbleau.dotlink.controllers;

import android.content.Context;
import android.content.Intent;
import fr.iutfbleau.dotlink.views.MainActivity;

public class RetourController {
    private Context context;

    public RetourController(Context context) {
        this.context = context;
    }

    public void retourAuMenu() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }
}