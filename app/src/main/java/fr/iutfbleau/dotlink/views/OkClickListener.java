package fr.iutfbleau.dotlink.views;

import android.content.DialogInterface;

public class OkClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
