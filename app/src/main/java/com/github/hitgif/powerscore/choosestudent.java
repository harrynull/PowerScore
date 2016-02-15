package com.github.hitgif.powerscore;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class choosestudent extends Activity {

    ArrayList<Group> groups;
    ExpandableListView listView;
    EListAdapter adapter;
    public String result = "";
    public String Null = "NULL";
    private int onlyone = 0;
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
        setContentView(R.layout.choosestudent);
        groups = new ArrayList<Group>();
        getJSONObject();
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new EListAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

        ((RelativeLayout) findViewById(R.id.backc)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView15c)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView15c)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

        ((RelativeLayout)findViewById(R.id.backc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iB=new Intent();
                iB.putExtra("res", Null);
                iB.setClass(choosestudent.this, MainActivity.class);
                choosestudent.this.setResult(1, iB);
                choosestudent.this.finish();
            }
        });
        ((Button) findViewById(R.id.ok)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#9b9b9b"));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((Button) findViewById(R.id.ok)).setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });
        ((Button)findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onlyone = 0;
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {
                            result = groups.get(i).getTitle() + "|" + groups.get(i).getChildItem(k).getFullname();
                            onlyone++;
                        }
                    }


                    //  result=result+String.valueOf(i);
                }
                if (onlyone == 1) {
                    Intent i=new Intent();
                    i.putExtra("res", result);
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

    /** 解悉 JSON 字串 */
    private void getJSONObject() {
        String jsonStr = "{'CommunityUsersResult':[{'CommunityUsersList':[{'fullname':'a111','userid':11,'username':'a1'}"
                + ",{'fullname':'b222','userid':12,'username':'a1'}],'id':1,'title':'人事部'},{'CommunityUsersList':[{'fullname':"
                + "'c333','userid':13,'username':'c3'},{'fullname':'d444','userid':14,'username':'d4'},{'fullname':'e555','userid':"
                + "15,'username':'e5'}],'id':2,'title':'開發部'}]}";

        try {
            JSONObject CommunityUsersResultObj = new JSONObject(jsonStr);
            JSONArray groupList = CommunityUsersResultObj.getJSONArray("CommunityUsersResult");

            for (int i = 0; i < groupList.length(); i++) {
                JSONObject groupObj = (JSONObject) groupList.get(i);
                Group group = new Group(groupObj.getString("id"), groupObj.getString("title"));
                JSONArray childrenList = groupObj.getJSONArray("CommunityUsersList");

                for (int j = 0; j < childrenList.length(); j++) {
                    JSONObject childObj = (JSONObject) childrenList.get(j);
                    Child child = new Child(childObj.getString("userid"), childObj.getString("fullname"),
                            childObj.getString("username"));
                    group.addChildrenItem(child);
                }

                groups.add(group);
            }
        } catch (JSONException e) {
            Log.d("allenj", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}