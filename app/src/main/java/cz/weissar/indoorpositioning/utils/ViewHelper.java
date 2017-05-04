package cz.weissar.indoorpositioning.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by petrw on 04.05.2017.
 */

public class ViewHelper {

    public static boolean isScreenLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
