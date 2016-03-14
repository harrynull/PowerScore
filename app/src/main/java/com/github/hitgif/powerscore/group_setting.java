package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.ActionBar;

public class group_setting extends Activity {
    public static ArrayList<Group> groups=MainActivity.groups;
    private ListView lv;

    private void updateList(){((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group_setting);
        setupActionBar();
        lv = (ListView) findViewById(R.id.listViewddd);
        lv.setAdapter(new MyAdapter(this));

        findViewById(R.id.back).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView24)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.imageView40)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView24)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.imageView40)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                group_setting.this.finish();
            }
        });

        findViewById(R.id.addgroup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(), set_group.class);
                intent.putExtra("editgroup", -1);
                startActivityForResult(intent, 1);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int pos, long arg3) {

                new ActionSheetDialog(group_setting.this).builder()
                        .setTitle("选择操作")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        new AlertDialogios(group_setting.this).builder()
                                                .setTitle("确认删除该组？")
                                                .setPositiveButton("删除", new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        groups.remove(pos);
                                                        updateList();
                                                        Toast.makeText(group_setting.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton("取消", null).show();
                                    }
                                })
                        .addSheetItem("修改", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent=new Intent(getApplication(), set_group.class);
                                        intent.putExtra("editgroup", pos);
                                        startActivityForResult(intent, 1);
                                    }
                                })
                        .show();
            }
        });

    }

    private ArrayList<HashMap<String, Object>> getData(){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i!=groups.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", groups.get(i).groupName);
            String[] members=groups.get(i).groupMembers.split("\\|");
            String strMember="";
            int ii=0;
            for (String member : members) {
                ii++;
                if(ii%2!=0) continue;
                strMember+=member+",";
            }
            map.put("ItemText", strMember.substring(0,strMember.length()-1));
            listItem.add(map);
        }
        return listItem;

    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return getData().size();
        }

        @Override
        public Object getItem(int position) {
            return getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_view_g, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            String s=(String)(getData().get(position).get("ItemText"));
            if(s.length()>12) s=s.substring(0,12)+"…";
            holder.text.setText(s);
            holder.title.setText((String)(getData().get(position).get("ItemTitle")));
            return convertView;
        }



    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;
        public TextView text;
    }
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateList();
    }
}


