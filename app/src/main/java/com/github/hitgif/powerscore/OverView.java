package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OverView extends Activity {
    private  List<String> groupArray;
    private  List<List<String>> childArray;
    private String filter="";
    ExpandableListView lv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.overview);

        lv=(ExpandableListView)findViewById(R.id.expandableListView);

        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
        findViewById(R.id.backc).setOnTouchListener(new View.OnTouchListener() {
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

        findViewById(R.id.backc).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OverView.this.finish();
            }
        });
        for (final String key : MainActivity.classes.keySet()) {
            Classes c = MainActivity.classes.get(key);
            groupArray.add(c.name);
            List<String> tempArray = new ArrayList<String>();
            for (int j = 0; j < c.members.length; j++) {
                tempArray.add(c.members[j]+"|"+c.scores[j]);
            }
            childArray.add(tempArray);
        }

        lv.setAdapter(new ExpandableAdapter(this));

        ((EditText) (findViewById(R.id.editText))).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                filter=((EditText) (findViewById(R.id.editText))).getText().toString();

                groupArray = new ArrayList<String>();
                childArray = new ArrayList<List<String>>();

                for (final String key : MainActivity.classes.keySet()) {
                    Classes c = MainActivity.classes.get(key);
                    groupArray.add(c.name);
                    List<String> tempArray = new ArrayList<String>();
                    for (int j = 0; j < c.members.length; j++) {
                        if(!filter.isEmpty()&&!c.members[j].contains(filter)) continue;
                        tempArray.add(c.members[j]+"|"+c.scores[j]);
                    }
                    childArray.add(tempArray);
                }

                ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
                return false;
            }
        });
    }
    public  class  ExpandableAdapter extends BaseExpandableListAdapter
    {
        Activity activity;

        public ExpandableAdapter(Activity a)
        {
            activity = a;
        }
        public Object getChild(int  groupPosition, int  childPosition)
        {
            return  childArray.get(groupPosition).get(childPosition);
        }
        public long getChildId(int  groupPosition, int  childPosition)
        {
            return  childPosition;
        }
        public int getChildrenCount(int  groupPosition)
        {
            return  childArray.get(groupPosition).size();
        }
        public View getChildView(int  groupPosition, int  childPosition,
                                  boolean  isLastChild, View convertView, ViewGroup parent)
        {
            String[] strings = childArray.get(groupPosition).get(childPosition).split("\\|");

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.itemoverview, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.itemName);
            tv.setText(strings[0]);

            TextView tv2 = (TextView) convertView.findViewById(R.id.Totalmark);
            tv2.setText(strings[1]);

            return convertView;
        }
        // group method stub
        public  Object getGroup(int  groupPosition)
        {
            return  groupArray.get(groupPosition);
        }
        public  int  getGroupCount()
        {
            return  groupArray.size();
        }
        public  long  getGroupId(int  groupPosition)
        {
            return  groupPosition;
        }
        public  View getGroupView(int  groupPosition, boolean  isExpanded,
                                  View convertView, ViewGroup parent)
        {
            String string = groupArray.get(groupPosition);
            AbsListView.LayoutParams layoutParams = new  AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 84 );
            TextView text = new  TextView(activity);
            text.setLayoutParams(layoutParams);
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            text.setPadding(65 , 0 , 0 , 0 );
            text.setText(string);
            text.setTextSize(17);
            return  text;
        }
        public  boolean  hasStableIds()
        {
            return  false ;
        }
        public  boolean  isChildSelectable(int  groupPosition, int  childPosition)
        {
            return  true ;
        }

    }
}
