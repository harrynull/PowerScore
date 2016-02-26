package com.github.hitgif.powerscore;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class drawer extends Activity {
    private DrawerLayout drawerLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        leftLayout=(RelativeLayout) findViewById(R.id.left);

        initData();
        adapter=new ContentAdapter(this, list);

    }
    private void initData() {
        list=new ArrayList<ContentModel>();
        list.add(new ContentModel(R.drawable.ic_launcher, "新闻"));
        list.add(new ContentModel(R.drawable.ic_launcher, "订阅"));
        list.add(new ContentModel(R.drawable.ic_launcher, "图片"));
        list.add(new ContentModel(R.drawable.ic_launcher, "视频"));
        list.add(new ContentModel(R.drawable.ic_launcher, "跟帖"));
        list.add(new ContentModel(R.drawable.ic_launcher, "投票"));
    }
}