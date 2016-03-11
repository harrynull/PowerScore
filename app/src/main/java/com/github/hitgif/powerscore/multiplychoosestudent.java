package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class multiplychoosestudent extends Activity {

    ExpandableListView listView;
    EListAdapter adapter;
    public String result = "";
    private int nothing = 0;
    private String Null = "NULL";
    public static ArrayList<Group> groups=MainActivity.groups;

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
        EListAdapter.getDataArray();
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new EListAdapter(this);
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
        findViewById(R.id.group).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((Button) findViewById(R.id.group)).setTextColor(Color.parseColor("#9b9b9b"));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((Button) findViewById(R.id.group)).setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> results = new ArrayList<String>();
                ArrayList<EListAdapter.Group> groups = adapter.getGroups();
                nothing = 0;
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {
                            result = groups.get(i).getId() + "|" + groups.get(i).getChildItem(k).getName();
                            results.add(nothing, result);
                            nothing++;
                        }
                    }
                }

                if (nothing != 0) {
                    //返回并传值
                    Intent i = new Intent();
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
        findViewById(R.id.group).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActionSheetDialog ASD = new ActionSheetDialog(multiplychoosestudent.this).builder()
                        .setTitle("选择组")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true);
                for (final Group group : groups) {
                    ASD.addSheetItem(group.groupName, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    String[] members = group.groupMembers.split("\\|");
                                    ArrayList<EListAdapter.Group> listGroups = adapter.getGroups();
                                    for (int i = 0; i < members.length; i += 2) {
                                        for (int j = 0; j < listGroups.size(); j++) {
                                            if (!listGroups.get(j).getTitle().equals(members[i]))
                                                continue;
                                            for (int k = 0; k < listGroups.get(j).getChildrenCount(); k++) {
                                                if (listGroups.get(j).getChildItem(k).getName().equals(members[i + 1])) {
                                                    listGroups.get(j).getChildItem(k).setChecked(true);
                                                    boolean allChecked=true;
                                                    for (int k2 = 0; k2 < listGroups.get(j).getChildrenCount(); k2++) {
                                                        if(!listGroups.get(j).getChildItem(k2).getChecked()){
                                                            allChecked=false;
                                                            break;
                                                        }
                                                    }
                                                    listGroups.get(j).setChecked(allChecked);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                                }
                            });
                }
                ASD.show();
            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}