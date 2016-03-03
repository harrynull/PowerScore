package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class multiplychoosestudent extends Activity {

    ArrayList<EListAdapter.Group> groups;
    ExpandableListView listView;
    EListAdapter adapter;
    public String result = "";
    private int nothing = 0;
    private String Null = "NULL";

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent iB = new Intent();
            iB.putExtra("reason", "NULL");
            iB.setClass(multiplychoosestudent.this, add.class);
            multiplychoosestudent.this.setResult(2, iB);
            multiplychoosestudent.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.multiplychoosestudent);
        groups = new ArrayList<EListAdapter.Group>();
        getJSONObject();
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new EListAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

        findViewById(R.id.back).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iB = new Intent();
                iB.putExtra("reason", Null);
                iB.setClass(multiplychoosestudent.this, add.class);
                multiplychoosestudent.this.setResult(2, iB);
                multiplychoosestudent.this.finish();
            }
        });

        findViewById(R.id.ok).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#9b9b9b"));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> results = new ArrayList<String>();
                nothing = 0;
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {
                            //这里设置返回值
                            result = groups.get(i).getId() + "|" + groups.get(i).getChildItem(k).getName();
                            results.add(nothing,result);
                            nothing++;
                        }
                    }
                    //  result=result+String.valueOf(i);
                }

                if (nothing != 0) {
                    //返回并传值
                    Intent i=new Intent();
                    i.putExtra("mem", results);
                    i.setClass(multiplychoosestudent.this, add.class);
                    multiplychoosestudent.this.setResult(3, i);
                    multiplychoosestudent.this.finish();
                } else {
                    new AlertDialogios(multiplychoosestudent.this).builder()
                            .setTitle("提示")
                            .setMsg("请选择学生 :)")
                            .setNegativeButton("好", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
            }
            // choosestudent.this.finish();

        });
    }

    /** 解悉 JSON 字串 */
    private void getJSONObject() {
        for (final String key : MainActivity.classes.keySet()) { //查找每个班
            Classes c = MainActivity.classes.get(key);
            EListAdapter.Group group = new EListAdapter.Group(key,c.name);
            for (int j = 0; j < c.members.length; j++) {
                Child child = new Child(c.members[j]);
                group.addChildrenItem(child);
            }
            groups.add(group);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}