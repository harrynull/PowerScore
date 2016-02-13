/*
以下功能待完成：
1.记录按日分栏 参考网址：http://www.codeceo.com/article/android-listview-group.html
2.按加/减分筛选（可与其他筛选条件叠加）
3.按人名筛选（可与其他筛选条件叠加）
 */






package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.content.Context;
import android.content.DialogInterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends Activity {

    public static ArrayList<History> historys=new ArrayList<History>();
    public static ArrayList<Integer> scores = new ArrayList<Integer>();
    public static String[] strs;

    private long timeStemp=0;
    private boolean superflag = true;
    private int pmon = 0;

    private String d="不限";
    private String showyear;
    private String showmonth;
    private String showday;

    private ListView lv;
    private ImageView dr;
    private RelativeLayout genlayout;
    private RelativeLayout perlayout;
    private LinearLayout choose;
    private ArrayList<History> histories=MainActivity.historys;

    Button gen;
    Button per;
    Button ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
        gen = (Button) findViewById(R.id.gen);
        per = (Button) findViewById(R.id.per);
        genlayout = (RelativeLayout) findViewById(R.id.genlayout);
        perlayout = (RelativeLayout) findViewById(R.id.perlayout);
        choose = (LinearLayout) findViewById(R.id.choose);
        genlayout.setVisibility(View.VISIBLE);
        perlayout.setVisibility(View.GONE);
        gen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gen.setBackgroundColor(Color.parseColor("#ffffff"));
                gen.setTextColor(Color.parseColor("#262a3b"));
                per.setBackgroundColor(Color.parseColor("#262a3b"));
                per.setTextColor(Color.parseColor("#ffffff"));
                genlayout.setEnabled(true);
                genlayout.setVisibility(View.VISIBLE);
                perlayout.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
            }
        });

        ls = (Button) findViewById(R.id.ls);
        ls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                historys.add(new History(+10, "王安海", "2333", new Date()));
                saveScores(MainActivity.this);
                Save(MainActivity.this);
                ((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
            }
        });

        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                per.setBackgroundColor(Color.parseColor("#ffffff"));
                per.setTextColor(Color.parseColor("#262a3b"));
                gen.setBackgroundColor(Color.parseColor("#262a3b"));
                gen.setTextColor(Color.parseColor("#ffffff"));
                genlayout.setEnabled(false);
                perlayout.setVisibility(View.VISIBLE);
                genlayout.setVisibility(View.GONE);
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), choosestudent.class);
                startActivity(myIntent);
            }
        });
        lv = (ListView) findViewById(R.id.listView3);
        dr = (ImageView) findViewById(R.id.drop);
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {
                History h = histories.get(getPos(position));
                final String reason = (h.reason);
                new ActionSheetDialog(MainActivity.this).builder()
                        .setTitle(reason)
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        new AlertDialogios(MainActivity.this).builder()
                                                .setTitle("删除记录")
                                                .setMsg("确认删除记录“" + reason + "”吗?")
                                                .setPositiveButton("删除", new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        History h = histories.get(getPos(position));
                                                        final int change = h.score;
                                                        final String names = h.names;

                                                        new AlertDialogios(MainActivity.this).builder()
                                                                .setTitle("撤销分数")
                                                                .setMsg("是否撤销该记录所改动的分数?")
                                                                .setPositiveButton("撤销", new OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {

                                                                        for (int i = 0; i != MainActivity.strs.length; i++) {
                                                                            if (names.indexOf(MainActivity.strs[i]) != -1) {
                                                                                MainActivity.scores.set(i, MainActivity.scores.get(i) - change);
                                                                            }
                                                                        }
                                                                        MainActivity.saveScores(MainActivity.this);
                                                                    }
                                                                })
                                                                .setNegativeButton("不撤销", new OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                    }
                                                                    }).show();
                                                        histories.remove(histories.get(getPos(position)));
                                                        MyAdapter mAdapter = new MyAdapter(MainActivity.this);//得到一个MyAdapter对象
                                                        lv.setAdapter(mAdapter);
                                                        MainActivity.Save(MainActivity.this);
                                                    }
                                                })
                                                .setNegativeButton("取消", new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                    }
                                                }).show();
                                    }

                                })
                        .addSheetItem("查看详细", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        TextView tv = new TextView(MainActivity.this);
                                        History h = histories.get(getPos(position));
                                        new AlertDialogios(MainActivity.this).builder()
                                                .setTitle("详细记录")
                                                .setMsg("理由: "+h.reason + "\n" +
                                                        "成员: "+h.names + "\n" +
                                                        "分数: "+h.getScore() + "\n" +
                                                        "时间: "+h.getDate())
                                                .setNegativeButton("返回", new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                    }
                                                }).show();

                                    }
                                })
                                //可添加多个SheetItem
                        .show();
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {

                                /*
                                 * 点击列表项时触发onItemClick方法，四个参数含义分别为
                                 * arg0：发生单击事件的AdapterView
                                 * arg1：AdapterView中被点击的View
                                 * position：当前点击的行在adapter的下标
                                 * id：当前点击的行的id
                                 */

            }
        });

        ((Button) findViewById(R.id.pick)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.year)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.month)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.day)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.textView5)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.textView7)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.drop)).setImageResource(R.drawable.dropdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.year)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.month)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.day)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.textView5)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.textView7)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.drop)).setImageResource(R.drawable.drop);
                }
                return false;
            }
        });
        ((Button) findViewById(R.id.pickpm)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.pm)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.drop2)).setImageResource(R.drawable.dropdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.pm)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.drop2)).setImageResource(R.drawable.drop);
                }
                return false;
            }
        });
        //筛选加减分
        ((Button)findViewById(R.id.pickpm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ActionSheetDialog(MainActivity.this).builder()
                        .setTitle("筛选加/减分")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("加分", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pmon = 1;
                                        ((TextView) findViewById(R.id.pm)).setText("  + ");
                                        ((TextView) findViewById(R.id.pm)).setTextSize(30);

                                        //此处筛选加分
                                    }
                                })
                        .addSheetItem("减分", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pmon = 2;
                                        ((TextView) findViewById(R.id.pm)).setText(" — ");
                                        ((TextView) findViewById(R.id.pm)).setTextSize(30);
                                    }
                                })
                        .addSheetItem("不限", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pmon = 0;
                                        ((TextView) findViewById(R.id.pm)).setText("不限");
                                        ((TextView) findViewById(R.id.pm)).setTextSize(20);
                                    }
                                })
                                //可添加多个SheetItem
                        .show();
            }

        });

//筛选日期
        ((Button)findViewById(R.id.pick)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                superflag =true;
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        ((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
                        if (superflag==true) {
                            showyear = new StringBuilder().append(year).append("年").toString();
                            showmonth =((month + 1) < 10) ?
                                    new StringBuilder().append("0").append(month + 1).toString():
                                    new StringBuilder().append(month + 1).toString();
                            showday =(day < 10) ?
                                    new StringBuilder().append("0").append(day).toString():
                                    new StringBuilder().append(day).toString();
                            ((TextView) findViewById(R.id.year)).setText(showyear);
                            ((TextView) findViewById(R.id.month)).setText(showmonth);
                            ((TextView) findViewById(R.id.day)).setText(showday);
                            ((TextView) findViewById(R.id.textView5)).setText("月");
                            ((TextView) findViewById(R.id.textView7)).setText("日");
                            ((TextView) findViewById(R.id.month)).setTextSize(30);
                            d=new StringBuilder().append(year).append('-')
                                    .append(month + 1)
                                    .append('-')
                                    .append(day)
                                    .toString();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            try {
                                date = simpleDateFormat.parse(d);
                            } catch (ParseException e) {
                                //e.printStackTrace();
                            }
                            timeStemp = date.getTime() / 1000;
                            lv.setAdapter(new MyAdapter(MainActivity.this));
                            ((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(lv.getCount()));
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "不限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d = "不限";
                        ((TextView) findViewById(R.id.month)).setText(d);
                        ((TextView) findViewById(R.id.year)).setText("");
                        ((TextView) findViewById(R.id.day)).setText("");
                        ((TextView) findViewById(R.id.textView5)).setText("");
                        ((TextView) findViewById(R.id.textView7)).setText("");
                        ((TextView) findViewById(R.id.month)).setTextSize(20);
                        superflag = false;
                        timeStemp = 0;
                        lv.setAdapter(new MyAdapter(MainActivity.this));
                    }
                });
                dpd.show();
            }
        });
    }

    public void onResume(){
        super.onResume();
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        for (int i = histories.size()-1; i >= 0; i--) {
            if(d.compareTo("不限")!=0&&(histories.get(i).date.getTime()/1000-86400>timeStemp||histories.get(i).date.getTime()/1000<=timeStemp)) continue;
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", (histories.get(i).reason.length()>6?histories.get(i).reason.substring(0,6)+"…":histories.get(i).reason));
            map.put("ItemText", (histories.get(i).names.length()>10?histories.get(i).names.substring(0,18)+"…":histories.get(i).names));
            map.put("mark", (histories.get(i).score>0?"+":"") + (histories.get(i).score/10.0));
            map.put("positive", (histories.get(i).score>0?"green":"red"));
            listItem.add(map);
        }

        return listItem;

    }

    private int getPos(int position){
        int count = 0, i;
        for (i = histories.size()-1; i >=0 ; i--) {
            if(d.compareTo("不限")!=0&&(histories.get(i).date.getTime()/1000-86400>timeStemp||histories.get(i).date.getTime()/1000<=timeStemp))
                continue;

            if (count < position) {
                count++;
                continue;
            }
            break;
        }
        return i;
    }

    class MyAdapter extends BaseAdapter {

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
            MainActivity.MyAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.itemmoreinfo,
                        null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
                holder.mark = (TextView) convertView.findViewById(R.id.mark);
                holder.positive = (ImageView) convertView.findViewById(R.id.positive);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
/*
            holder.bt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getId() == R.id.ItemButton) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            TextView tv = new TextView(MainActivity.this);
                            History h = histories.get(getPos(position));
                            tv.setText(h.names + "\r\n" +
                                    h.reason + "\r\n" +
                                    h.getScore() + "\r\n" +
                                    h.getDate());
                            new AlertDialog.Builder(MainActivity.this).setTitle("详细记录").setView(null).setView(tv).setNegativeButton("返回", null).show();
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
                            new AlertDialog.Builder(MainActivity.this).setTitle("确认删除吗？")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            History h = histories.get(getPos(position));
                                            final int change = h.score;
                                            final String names = h.names;

                                            new AlertDialog.Builder(MainActivity.this).setTitle("是否撤销所修改的分数")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setPositiveButton("撤销", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            for (int i = 0; i != MainActivity.strs.length; i++) {
                                                                if (names.indexOf(MainActivity.strs[i]) != -1) {
                                                                    MainActivity.scores.set(i, MainActivity.scores.get(i) - change);
                                                                }
                                                            }
                                                            MainActivity.saveScores(MainActivity.this);
                                                        }
                                                    })
                                                    .setNegativeButton("不撤销", null).show();
                                            histories.remove(histories.get(getPos(position)));
                                            MyAdapter mAdapter = new MyAdapter(MainActivity.this);//得到一个MyAdapter对象
                                            lv.setAdapter(mAdapter);
                                            MainActivity.Save(MainActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }

                    }
                    return false;
                }

            });
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText((String) (getData().get(position).get("ItemTitle")));
            String s = (String) (getData().get(position).get("ItemText"));
            holder.text.setText(s);
            holder.mark.setText((String) (getData().get(position).get("mark")));
            String posi = (String) (getData().get(position).get("positive"));
            if(posi=="green") {
                holder.positive.setImageResource(R.drawable.green);
            }else {
                holder.positive.setImageResource(R.drawable.red);
            }
            return convertView;
        }


        final class ViewHolder {
            public TextView title;
            public TextView text;
            public TextView mark;
            public ImageView positive;
        }
    }
    public static void saveScores(Context c){
        FileOutputStream outputStream;
        try {
            outputStream = c.openFileOutput("scores.txt", Activity.MODE_PRIVATE);
            for (int i = 0; i != scores.size(); i++) {
                outputStream.write((scores.get(i).toString() + (i==scores.size()-1?"":" ")).getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void Save(Context c){
        FileOutputStream outputStream;
        try {
            outputStream = c.openFileOutput("historys.txt", Activity.MODE_PRIVATE);
            for(int i=0;i!=historys.size();i++){
                outputStream.write((historys.get(i).score+"|").getBytes());
                outputStream.write((historys.get(i).names +"|").getBytes());
                outputStream.write((historys.get(i).reason +"|").getBytes());
                outputStream.write((historys.get(i).date.toGMTString() + "|").getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
/*        MenuItem share = menu.findItem(R.id.share);
        MenuItem undo = menu.findItem(R.id.undo);
        MenuItem redo = menu.findItem(R.id.redo);
        MenuItem save = menu.findItem(R.id.save);
        share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        undo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        redo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case R.id.undo:
                showToast(R.string.undo);
                break;
            case R.id.redo:
                showToast(R.string.redo);
                break;
            case R.id.save:
                showToast(R.string.save);
                break;
            case R.id.share:
                showToast(R.string.share);
                break;
            default:
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void showToast(int msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}