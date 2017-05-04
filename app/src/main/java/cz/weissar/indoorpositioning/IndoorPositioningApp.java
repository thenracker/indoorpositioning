package cz.weissar.indoorpositioning;

import android.app.Application;
import android.content.Context;

/**
 * Created by petrw on 04.05.2017.
 */

public class IndoorPositioningApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
