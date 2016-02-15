package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    ArrayList<Group> groups;
    ArrayList<String> results;
    ExpandableListView listView;
    EListAdapter adapter;
    public String result = "";
    public String Null = "NULL";
    private int nothing = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.multiplychoosestudent);
        groups = new ArrayList<Group>();
        getJSONObject();
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new EListAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);

        ((RelativeLayout) findViewById(R.id.back)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.backim)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.backim)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

        ((RelativeLayout)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iB=new Intent();
                iB.putExtra("res", Null);
                iB.setClass(multiplychoosestudent.this, MainActivity.class);
                multiplychoosestudent.this.setResult(1, iB);
                multiplychoosestudent.this.finish();
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
                nothing = 0;
                for (int i = 0; i < groups.size(); i++) {
                    for (int k = 0; k < groups.get(i).getChildrenCount(); k++) {
                        if (groups.get(i).getChildItem(k).getChecked()) {

                            //这里设置返回值
                            result = groups.get(i).getTitle() + "|" + groups.get(i).getChildItem(k).getFullname();
                            results.add(nothing,result);
                            nothing++;
                        }
                    }


                    //  result=result+String.valueOf(i);
                }
                if (nothing != 0) {
                    //返回并传值
                    Intent i=new Intent();
                    i.putExtra("res", results);
                    i.setClass(multiplychoosestudent.this, MainActivity.class);
                    multiplychoosestudent.this.setResult(1, i);
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