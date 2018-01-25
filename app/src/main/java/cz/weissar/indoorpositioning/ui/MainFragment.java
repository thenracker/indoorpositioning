package cz.weissar.indoorpositioning.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.weissar.indoorpositioning.R;
import cz.weissar.indoorpositioning.utils.LocationHelper;
import cz.weissar.indoorpositioning.utils.LocationListener;
import cz.weissar.indoorpositioning.utils.MapUtils;

/**
 * Created by petrw on 24.01.2018.
 */

public class MainFragment extends Fragment implements LocationListener {

    private MapUtils.LatLng centre;
    private List<MapUtils.LatLng> positions;

    private SurfaceView surfaceView;
    private SurfaceHolder holder;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        surfaceView = ((SurfaceView) view.findViewById(R.id.surfaceView));
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                MainFragment.this.holder = holder;
                Canvas canvas = holder.lockCanvas();
                clearOnly(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                MainFragment.this.holder = null;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positions = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationHelper.get().shouldStartComputing(true, getContext(), true);
        LocationHelper.register(this);
    }

    @Override
    public void onPause() {
        LocationHelper.get().unregister(this);
        LocationHelper.get().shouldStartComputing(false, getContext(), true);
        super.onPause();
    }

    @Override
    public void onFloorDetected(int floor) {

    }

    @Override
    public void onPositionDetected(MapUtils.LatLng newPosition) {

        if (holder != null) {

            Canvas canvas = holder.lockCanvas();

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);


            if (positions.isEmpty()) {
                centre = newPosition;
            } else {
                /*drawLine(canvas, Color.RED, positions.get(0), newPosition);
                for (int i = 1; i < positions.size(); i++) {
                    drawLine(canvas, Color.RED, positions.get(i), positions.get(i - 1));
                }*/
                for (MapUtils.LatLng position : positions) {
                    drawCircle(canvas, Color.GRAY, position);
                }
            }

            //now point
            drawCircle(canvas, Color.RED, newPosition);

            positions.add(0, newPosition);

            holder.unlockCanvasAndPost(canvas);
        }

    }

    @Override
    public void onValuesMeasured(String... measuredValues) {

    }

    private void drawCircle(Canvas canvas, int color, MapUtils.LatLng pos) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle((float) ((surfaceView.getWidth() / 2) + pos.getLat()), (float) ((surfaceView.getHeight() / 2) - pos.getLng()), 3, paint);
    }

    private void drawLine(Canvas canvas, int color, MapUtils.LatLng latLng, MapUtils.LatLng newPosition) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawLine((float) ((surfaceView.getWidth() / 2) + latLng.getLat()), (float) ((surfaceView.getHeight() / 2) + latLng.getLng()),
                (float) ((surfaceView.getWidth() / 2) + newPosition.getLat()), (float) (surfaceView.getHeight() / 2 + newPosition.getLng()), paint);
    }

    private void clearOnly (Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }
}
