package fr.iutfbleau.dotlink;

import android.content.Context;
import android.content.Intent;

public class AdapterPuzzleClickListener implements OnPuzzleClickListener{
    private Context context;

    public AdapterPuzzleClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onPuzzleClick(Puzzle puzzle) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("puzzle", puzzle);
        context.startActivity(intent);
    }

}
