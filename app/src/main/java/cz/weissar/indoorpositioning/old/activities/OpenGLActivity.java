package cz.weissar.indoorpositioning.old.activities;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import cz.weissar.indoorpositioning.old.utils.MyGLSurfaceView;

/**
 * Created by petrw on 02.09.2017.
 */

public class OpenGLActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
}
