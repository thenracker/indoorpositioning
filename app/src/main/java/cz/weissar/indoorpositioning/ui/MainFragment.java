package cz.weissar.indoorpositioning.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private List<Float> heights;

    private SurfaceView surfaceView;
    private SurfaceView surfaceView2;
    private SurfaceHolder holder;
    private SurfaceHolder holder2;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            positions.clear();
            heights.clear();

            LocationHelper.get().clear();

            Canvas canvas = holder.lockCanvas();
            clearOnly(canvas);
            holder.unlockCanvasAndPost(canvas);

            canvas = holder2.lockCanvas();
            clearOnly(canvas);
            holder2.unlockCanvasAndPost(canvas);
        }
        return super.onOptionsItemSelected(item);
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
        surfaceView2 = ((SurfaceView) view.findViewById(R.id.surfaceView2));
        surfaceView2.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                MainFragment.this.holder2 = holder;
                Canvas canvas = holder2.lockCanvas();
                clearOnly(canvas);
                holder2.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                MainFragment.this.holder2 = null;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positions = new ArrayList<>();
        heights = new ArrayList<>();
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
                for (MapUtils.LatLng position : positions) {
                    drawCircle(canvas, Color.GRAY, position);
                }
            }

            //now point
            drawCircle(canvas, Color.RED, newPosition);

            positions.add(0, newPosition);

            holder.unlockCanvasAndPost(canvas);
        }

        if (holder2 != null) {
            Canvas canvas = holder2.lockCanvas();

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

            heights.add(LocationHelper.get().getHeightDiff());

            for (int i = 0; i < heights.size(); i++) {
                Float height = heights.get(i);
                drawCircleHoriz(canvas, Color.RED, new MapUtils.LatLng(i*4, height*4));
            }

            holder2.unlockCanvasAndPost(canvas);
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

    private void drawCircleHoriz(Canvas canvas, int color, MapUtils.LatLng pos) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle((float) (/*(surfaceView2.getWidth() / 2) + */pos.getLat()), (float) ((surfaceView2.getHeight() / 2) - pos.getLng()), 3, paint);
    }

    private void drawLine(Canvas canvas, int color, MapUtils.LatLng latLng, MapUtils.LatLng newPosition) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawLine((float) ((surfaceView.getWidth() / 2) + latLng.getLat()), (float) ((surfaceView.getHeight() / 2) + latLng.getLng()),
                (float) ((surfaceView.getWidth() / 2) + newPosition.getLat()), (float) (surfaceView.getHeight() / 2 + newPosition.getLng()), paint);
    }

    private void clearOnly(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }
}
