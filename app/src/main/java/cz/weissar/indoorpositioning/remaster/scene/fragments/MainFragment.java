package cz.weissar.indoorpositioning.remaster.scene.fragments;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.weissar.indoorpositioning.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by petrw on 15.09.2017.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        FragmentPagerAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle(position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private String getTitle(int pos){
        switch (pos){
            case 0: return "Lineární akcelerometr";
            case 1: return "Rotation vector";
            case 2: return "Gyroskop";
            case 3: return "Step detector";
            case 4: return "Akcelerometr";
            default: return "IndoorpositioningApp";
        }
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
            fragmentList.add(SensorFragment.newInstance(Sensor.TYPE_LINEAR_ACCELERATION, 2f));
            fragmentList.add(SensorFragment.newInstance(Sensor.TYPE_ROTATION_VECTOR, 1f));
            fragmentList.add(SensorFragment.newInstance(Sensor.TYPE_GYROSCOPE, 2f));
            fragmentList.add(SensorFragment.newInstance(Sensor.TYPE_STEP_DETECTOR, 1f));
            fragmentList.add(SensorFragment.newInstance(Sensor.TYPE_ACCELEROMETER, 8f));
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList == null ? null : fragmentList.get(position);
        }

    }
}
