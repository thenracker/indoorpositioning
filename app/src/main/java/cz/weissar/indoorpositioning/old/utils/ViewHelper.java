package cz.weissar.indoorpositioning.old.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by petrw on 04.05.2017.
 */

public class ViewHelper {

    public static boolean isScreenLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
