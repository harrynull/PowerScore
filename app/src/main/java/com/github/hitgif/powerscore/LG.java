package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by 王安海 on 01/17/2016.
 */
public class LG extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setTranslucent(this);
        setContentView(R.layout.lg);
        initViewPager();
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.lg1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.lg2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.lg3, null);

        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);


        MYViewPagerAdapter adapter = new MYViewPagerAdapter();
        adapter.setViews(views);
        viewPager.setAdapter(adapter);
    }

    public class MYViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public void setViews(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {

            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }
    }
    public void enter(View v) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),login.class);
        startActivity(intent);
        LG.this.finish();

    }
}
