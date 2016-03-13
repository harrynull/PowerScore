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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class set_group extends Activity {
    ExpandableListView listView;
    EListAdapter adapter;
    private String Null = "NULL";
    private EditText group_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_student_for_group);

        final int editGroup=getIntent().getIntExtra("editgroup", -1);

        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        group_name = (EditText) findViewById(R.id.group_name);
        adapter = new EListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

        if(editGroup!=-1){
            final Group group=MainActivity.groups.get(editGroup);
            group_name.setText(group.groupName);
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
                iB.setClass(set_group.this, add.class);
                set_group.this.setResult(2, iB);
                set_group.this.finish();
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
                String members = "";
                ArrayList<EListAdapter.Group> groups=adapter.getGroups();
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {
                            members += groups.get(i).getTitle() + "|" + groups.get(i).getChildItem(k).getName()  + "|";
                        }
                    }
                }
                String name=group_name.getText().toString();
                if (name.isEmpty()) {
                    new AlertDialogios(set_group.this).builder()
                            .setTitle("提示")
                            .setMsg("请填写组名 :)")
                            .setNegativeButton("好", null).show();
                } else if(members.isEmpty() ){
                    new AlertDialogios(set_group.this).builder()
                            .setTitle("提示")
                            .setMsg("请选择学生 :)")
                            .setNegativeButton("好", null).show();
                }else{
                    if(editGroup==-1)
                        MainActivity.groups.add(new Group(name,members));
                    else
                        MainActivity.groups.set(editGroup, new Group(name, members));
                    set_group.this.finish();
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