package com.example.minky.bigmeet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewPageActivity extends FragmentActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        ViewPager viewPager = (ViewPager)findViewById(R.id.vpPager);
        List<Fragment> fragments = getFragments();
        adapterViewPager = new ViewPageAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapterViewPager);
        Log.d("TAG", String.valueOf(viewPager.getCurrentItem()));



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(getApplicationContext(),"Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(MainFragment.newInstance(0, "Fragment 1"));
        fList.add(MainFragment.newInstance(1, "Fragment 2"));
        fList.add(MainFragment.newInstance(2, "Fragment 3"));
        return fList;
    }
}
