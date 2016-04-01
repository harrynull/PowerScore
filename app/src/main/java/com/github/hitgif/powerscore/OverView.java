package com.github.hitgif.powerscore;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverView extends Activity {
    private  List<String> groupArray;
    private  List<List<String>> childArray;
    private String filter="";
    private int hahaha = 0;
    ExpandableListView lv;
    public void doSearch(){
        filter = ((EditText) (findViewById(R.id.editText))).getText().toString();

        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();

        ArrayList<Integer> needExpand = new ArrayList<Integer>();

        for (final String key : MainActivity.classes.keySet()) {
            Classes c = MainActivity.classes.get(key);
            List<String> tempArray = new ArrayList<String>();
            for (int j = 0; j < c.members.length; j++) {
                if (!filter.isEmpty() && !c.members[j].contains(filter)) continue;
                tempArray.add(c.members[j] + "|" + (c.scores[j]/10.0));
            }
            if (!tempArray.isEmpty()) {
                groupArray.add(c.name);
                childArray.add(tempArray);
                needExpand.add(groupArray.size() - 1);
            }
        }

        lv.setAdapter(new ExpandableAdapter(OverView.this));

        for (int id : needExpand) {
            lv.expandGroup(id);
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main);//通知栏所需颜色
        }
        setContentView(R.layout.overview);
        ViewTreeObserver vto = findViewById(R.id.textView38).getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                findViewById(R.id.textView38).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hahaha = findViewById(R.id.textView38).getWidth();
                lv.setAdapter(new ExpandableAdapter(OverView.this));
            }
        });
        lv=(ExpandableListView)findViewById(R.id.expandableListView);
        findViewById(R.id.scearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
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
                OverView.this.finish();
                overridePendingTransition(R.anim.slide_in_froml, R.anim.slide_out_fromr);
            }
        });
        for (final String key : MainActivity.classes.keySet()) {
            Classes c = MainActivity.classes.get(key);
            groupArray.add(c.name);
            List<String> tempArray = new ArrayList<String>();
            for (int j = 0; j < c.members.length; j++) {
                tempArray.add(c.members[j]+"|"+(c.scores[j]/10.0));
            }
            childArray.add(tempArray);
        }

        lv.setAdapter(new ExpandableAdapter(this));

        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        final int childPosition, long id) {
                Classes c = null;
                String Key="";
                int cid = 0;
                for (final String key : MainActivity.classes.keySet()) { //查找每个班
                    if (cid == groupPosition) {
                        c = MainActivity.classes.get(key);
                        Key=key;
                        break;
                    }
                    cid++;
                }
                final String fKey=Key;
                if(c==null) return false;
                int sid=0;
                String name=childArray.get(groupPosition).get(childPosition).split("\\|")[0];
                for (final String student : c.members) { //查找每个班
                    if (student.equals(name)) break;
                    sid++;
                }
                final int fSid=sid;
                new ActionSheetDialog(OverView.this).builder()
                        .setTitle(String.valueOf(c.members[fSid]))
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("修改分数", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        if(MainActivity.MainActivityPointer.isSync){
                                            new AlertDialogios(OverView.this).builder()
                                                    .setTitle("提示")
                                                    .setMsg("抱歉，数据同步中，修改分数暂不可用，请等待同步完成 :(")
                                                    .setNegativeButton("好的", null).show();
                                            return;
                                        }
                                        final EditText text = new EditText(OverView.this);
                                        text.setText(String.valueOf(MainActivity.classes.get(fKey).scores[fSid] / 10.0));
                                        new AlertDialog.Builder(OverView.this)
                                                .setTitle("请设置分数")
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setView(text)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Classes c = MainActivity.classes.get(fKey);
                                                        try {
                                                            int change = (int) (Double.parseDouble(text.getText().toString()) * 10) - c.scores[fSid];
                                                            Date d = new Date();
                                                            c.histories.add(new History(change, c.members[fSid],
                                                                    "修改分数", d, getSharedPreferences("data", Activity.MODE_PRIVATE).getString("Username", "未登录用户")));
                                                            c.unsyncHistories.add(new History(change, c.members[fSid],
                                                                    "修改分数", d, getSharedPreferences("data", Activity.MODE_PRIVATE).getString("username", "未登录用户")));
                                                            c.scores[fSid] += change;
                                                            showToast("设置分数成功");
                                                        } catch (Exception e) {
                                                            showToast("设置分数失败:必须输入数字");
                                                            e.printStackTrace();
                                                        }
                                                        for (final String key : MainActivity.classes.keySet()) {
                                                            Classes c2 = MainActivity.classes.get(key);
                                                            groupArray.add(c2.name);
                                                            List<String> tempArray = new ArrayList<String>();
                                                            for (int j = 0; j < c2.members.length; j++) {
                                                                tempArray.add(c2.members[j] + "|" + (c2.scores[j] / 10.0));
                                                            }
                                                            childArray.add(tempArray);
                                                        }

                                                        lv.setAdapter(new ExpandableAdapter(OverView.this));
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                    }
                                })
                        .addSheetItem("查看记录", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_froml, R.anim.slide_out_fromr);
                                        MainActivity.MainActivityPointer.jumpToStudent(fKey, MainActivity.classes.get(fKey).members[fSid]);
                                    }
                                })
                        .show();
                return true;
            }
        });

        ((EditText) (findViewById(R.id.editText))).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                doSearch();
                return false;
            }
        });
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
                    ViewGroup.LayoutParams.FILL_PARENT, hahaha );
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

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
