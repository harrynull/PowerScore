package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.BaseAdapter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.ActionBar;

public class group_setting extends Activity {
    public static ArrayList<Groupd> groups=new ArrayList<Groupd>();
    private ListView lv;
    private static String members=new String();

    void Save() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("groups.txt", Activity.MODE_PRIVATE);
            for (int i = 0; i != groups.size(); i++) {
                outputStream.write((groups.get(i).groupName + "|").getBytes());
                outputStream.write((groups.get(i).groupMembers + "|").getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group_setting);
        setupActionBar();
        SharedPreferences sharedata = getSharedPreferences("data", 0);
        members= sharedata.getString("members", "");
        lv = (ListView) findViewById(R.id.listViewddd);

        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);

        final LayoutInflater inflater = (LayoutInflater) group_setting.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        ((RelativeLayout) findViewById(R.id.back)).setOnTouchListener(new View.OnTouchListener() {
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

        ((RelativeLayout)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialogios(group_setting.this).builder()
                        .setTitle("警告")
                        .setMsg("直接返回将放弃所有未完成的修改 :(")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                group_setting.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();

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

                //完成并保存！！！！！！！！！！！
            }
            // choosestudent.this.finish();

        });

        //添加组!!!!!!!!!!!!!!!!!!
        ImageView b2= (ImageView) findViewById(R.id.addgroup);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(group_setting.this,set_group.class);
                group_setting.this.startActivityForResult(intent, 1);
            }
        });




        //从文件中读入组数据
        try {
            FileInputStream inputStream = this.openFileInput("groups.txt");
            byte[] bytes = new byte[inputStream.available()];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());
            String[] result=content.split("\\|");
            groups.clear();
            for(int i=0;i<result.length-1;i+=2){
                groups.add(new Groupd(result[i],result[i+1]));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //  if ((lv.getLayoutParams().height)>400)
        //     lv.getLayoutParams().height=400;
        // Log.ScriptEditor("2333333333",String.valueOf(lv.getLayoutParams().height));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

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
                                                        groups.remove(arg2);
                                                        MyAdapter mAdapter = new MyAdapter(group_setting.this);//得到一个MyAdapter对象
                                                        lv.setAdapter(mAdapter);
                                                        Save();
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

                                        //修改组!!!!!!!!!!!!!!!!!!
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
            map.put("ItemText", groups.get(i).groupMembers);
            listItem.add(map);
        }
        return listItem;

    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
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
        /*书中详细解释该方法*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_view_g, null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
             //   holder.bt = (ImageButton) convertView.findViewById(R.id.ItemButton);
             //   holder.btd = (ImageButton) convertView.findViewById(R.id.ItemButtond);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            String s=(String)(getData().get(position).get("ItemText"));
            if(s.length()>12) s=s.substring(0,12)+"…";
            holder.text.setText(s);
            holder.title.setText((String)(getData().get(position).get("ItemTitle")));
            /*为Button添加点击事件*/

/*
            holder.bt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getId() == R.id.ItemButton) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            final LayoutInflater inflater = (LayoutInflater) group_setting.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            final View edittextdv = inflater.inflate(R.layout.edittextd, null);
                            final String[] strs=members.split(" ");
                            ((EditText) edittextdv.findViewById(R.id.editText1)).setText((String)getData().get(position).get("ItemTitle"));
                            ListView lv1=((ListView)edittextdv.findViewById(R.id.listView2));
                            lv1.setAdapter(new ArrayAdapter<String>(group_setting.this, android.R.layout.simple_list_item_1, strs));
                            lv1.setAdapter(new ArrayAdapter<String>(group_setting.this, android.R.layout.simple_list_item_multiple_choice, strs));
                            lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                            String[] Checked=((String)getData().get(position).get("ItemText")).split(" ");
                            for(int i=0;i!=strs.length;i++){
                                for(int j=0;j!=Checked.length;j++){
                                    if(strs[i].compareTo(Checked[j])==0) lv1.setItemChecked(i,true);
                                }
                            }
                            AlertDialog.Builder ad = new AlertDialog.Builder(group_setting.this).setTitle("修改组").setView(null).setView(edittextdv).setPositiveButton("修改",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            EditText nameEditText = (EditText) edittextdv.findViewById(R.id.editText1);
                                            String gn = nameEditText.getText().toString();
                                            String gm=new String();
                                            ListView lv1=((ListView)edittextdv.findViewById(R.id.listView2));
                                            for(int i=0;i!=strs.length;i++){
                                                if(lv1.getCheckedItemPositions().get(i)){ //如果选中了
                                                    gm+=strs[i]+" ";
                                                }
                                            }
                                            if(gn.isEmpty()||gm.isEmpty()) {
                                                Toast.makeText(group_setting.this, "修改组失败:组名和组员不能为空", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                boolean canadd=true;
                                                for (int j=0;j!=groups.size();j++){
                                                    if(j!=position&&groups.get(j).groupName.compareTo(gn)==0){

                                                        canadd = false;
                                                        Toast.makeText(group_setting.this, "修改组失败:组名已使用", Toast.LENGTH_SHORT).show();
                                                        break;

                                                    }
                                                }
                                                if(canadd) {
                                                    groups.set(position, new Groupd(gn, gm));
                                                    Toast.makeText(group_setting.this, "修改组成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            MyAdapter mAdapter = new MyAdapter(group_setting.this);//得到一个MyAdapter对象
                                            lv.setAdapter(mAdapter);
                                            Save();

                                        }
                                    }).setNegativeButton("返回",null);
                            ad.show();
                        }
                    }
                    return false;
                }

            });
            holder.btd.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getId() == R.id.ItemButtond) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            new AlertDialog.Builder(group_setting.this).setTitle("确认删除该组？")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            groups.remove(position);
                                            MyAdapter mAdapter = new MyAdapter(group_setting.this);//得到一个MyAdapter对象
                                            lv.setAdapter(mAdapter);
                                            Save();
                                            Toast.makeText(group_setting.this, "删除成功", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }

                    }
                    return false;
                }

            });
*/

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
}


