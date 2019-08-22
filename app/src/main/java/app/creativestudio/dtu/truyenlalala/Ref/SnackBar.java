package app.creativestudio.dtu.truyenlalala.Ref;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackBar {
    public static void createSnackBar(View view, String mess){
        Snackbar.make(view, mess, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
