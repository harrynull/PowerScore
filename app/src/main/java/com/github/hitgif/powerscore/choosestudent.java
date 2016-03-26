package com.github.hitgif.powerscore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


public class choosestudent extends Activity {

    ExpandableListView listView;
    EListAdapter adapter;
    public String Null = "NULL";
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent iB = new Intent();
            iB.putExtra("res", "NULL");
            iB.setClass(choosestudent.this, MainActivity.class);
            choosestudent.this.setResult(1, iB);
            choosestudent.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Util.setTranslucent(this);
        setContentView(R.layout.choosestudent);
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new EListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

        findViewById(R.id.backc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView15c)).setTextColor(Color.parseColor("#7fffffff"));
                ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView15c)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

        findViewById(R.id.backc).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iB = new Intent();
                iB.putExtra("result", Null);
                iB.setClass(choosestudent.this, MainActivity.class);
                choosestudent.this.setResult(1, iB);
                choosestudent.this.finish();
            }
        });
        findViewById(R.id.ok).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#7fffffff"));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<EListAdapter.Group> groups=adapter.getGroups();
                String result="";
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {
                            result = groups.get(i).getId() + "|" + groups.get(i).getChildItem(k).getName();
                            break;
                        }
                    }
                    if(!result.isEmpty()) break;
                }
                if (!result.isEmpty()) {
                    Intent i = new Intent();
                    i.putExtra("result", result);
                    i.setClass(choosestudent.this, MainActivity.class);
                    choosestudent.this.setResult(1, i);
                    choosestudent.this.finish();
                } else {
                    new AlertDialogios(choosestudent.this).builder()
                            .setTitle("提示")
                            .setMsg("请选择并只选择一个学生 :)")
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}