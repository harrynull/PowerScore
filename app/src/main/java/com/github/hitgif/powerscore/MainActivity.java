/*
以下功能待完成：
1.记录按日分栏 参考网址：http://www.codeceo.com/article/android-listview-group.html
2.按人名筛选（可与其他筛选条件叠加）
3.返回值后获取分数
 */

package com.github.hitgif.powerscore;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class MainActivity extends Activity {

    public static TreeMap<String, Classes> classes = new TreeMap<String, Classes>();
    SharedPreferences spReader;
    SharedPreferences.Editor spEditor;

    private long timeStemp=0;
    private boolean superflag = true;
    private boolean isflit = false;
    private boolean issync = false;
    private int lsi = 2333;

    private String classNow="-1";
    private String d="不限";
    private String showyear;
    private String showmonth;
    private String showday;
    private String filterClass;
    private String filterName;

    private int scoreFilter=-1;

    //布局
    private ListView lv;
    private ImageView dr;
    private RelativeLayout genlayout;
    private RelativeLayout perlayout;
    private RelativeLayout choose;
    private ImageView add;
    private int sbar = 0;
    private DrawerLayout drawerLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;


    Button gen;
    Button per;
    Button ls;

    private void updateList(){
        ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            sbar = getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height")
                    .get(c.newInstance()).toString()));
        } catch(Exception e1) {
            e1.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main);//通知栏所需颜色

        }

        //初始化
        spReader= getSharedPreferences("data", Activity.MODE_PRIVATE);
        spEditor = spReader.edit();
        //布局初始化

        setContentView(R.layout.drawer);
        //((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
        gen = (Button) findViewById(R.id.gen);
        per = (Button) findViewById(R.id.per);
        genlayout = (RelativeLayout) findViewById(R.id.genlayout);
        perlayout = (RelativeLayout) findViewById(R.id.perlayout);
        choose = (RelativeLayout) findViewById(R.id.choose);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = findViewById(R.id.ds).getLayoutParams();
            lp.width = 1;
            lp.height = sbar;
            findViewById(R.id.ds).setLayoutParams(lp);
        }
        add = (ImageView) findViewById(R.id.add);
        final  ImageView sync = (ImageView)findViewById(R.id.sync);
        final  Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        genlayout.setVisibility(View.VISIBLE);
        perlayout.setVisibility(View.GONE);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        leftLayout=(RelativeLayout) findViewById(R.id.left);

        sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!issync) {
                    //开始转
                    sync.startAnimation(operatingAnim);
                    issync = true;
                } else {
                    //停止转
                    sync.clearAnimation();
                    issync = false;
                }
            }
        });
        gen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gen.setBackgroundColor(Color.parseColor("#ffffff"));
                gen.setTextColor(getResources().getColor(R.color.main));
                per.setBackgroundColor(getResources().getColor(R.color.main));
                per.setTextColor(Color.parseColor("#ffffff"));
                genlayout.setEnabled(true);
                genlayout.setVisibility(View.VISIBLE);
                perlayout.setVisibility(View.GONE);
                findViewById(R.id.flit).setVisibility(View.VISIBLE);
                //((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
            }
        });


        ls = (Button) findViewById(R.id.ls);
        ls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classes.get(classNow).histories.add(new History(+10, "王安海", String.valueOf(lsi), new Date()));
                //((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
                lsi++;
                updateList();

            }
        });

        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                per.setBackgroundColor(Color.parseColor("#ffffff"));
                per.setTextColor(getResources().getColor(R.color.main));
                gen.setBackgroundColor(getResources().getColor(R.color.main));
                gen.setTextColor(Color.parseColor("#ffffff"));
                genlayout.setEnabled(false);
                perlayout.setVisibility(View.VISIBLE);
                genlayout.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.flit)).setImageResource(R.drawable.fliter);
                findViewById(R.id.flit).setBackgroundColor(getResources().getColor(R.color.main));
                findViewById(R.id.fliter).setVisibility(View.GONE);
                findViewById(R.id.flit).setVisibility(View.GONE);
                d = "不限";
                ((TextView) findViewById(R.id.month)).setText(d);
                ((TextView) findViewById(R.id.year)).setText("");
                ((TextView) findViewById(R.id.day)).setText("");
                ((TextView) findViewById(R.id.textView5)).setText("");
                ((TextView) findViewById(R.id.textView7)).setText("");
                // ((TextView) findViewById(R.id.month)).setTextSize(20);
                //superflag = false;
                timeStemp = 0;
                updateList();

                ((TextView) findViewById(R.id.pm)).setText("不限");
                //  ((TextView) findViewById(R.id.pm)).setTextSize(20);
                scoreFilter = -1;

                isflit = false;

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, chooseclass.class), 1);
            }

        });
        findViewById(R.id.personal).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((DrawerLayout) findViewById(R.id.drawerlayout)).openDrawer(leftLayout);
            }
        });
        findViewById(R.id.flit).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.flit).setBackgroundColor(getResources().getColor(R.color.press));

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isflit) {
                        d = "不限";
                        ((TextView) findViewById(R.id.month)).setText(d);
                        ((TextView) findViewById(R.id.year)).setText("");
                        ((TextView) findViewById(R.id.day)).setText("");
                        ((TextView) findViewById(R.id.textView5)).setText("");
                        ((TextView) findViewById(R.id.textView7)).setText("");
                        // ((TextView) findViewById(R.id.month)).setTextSize(20);
                        //superflag = false;
                        //timeStemp = 0;
                        //lv.setAdapter(new MyAdapter(MainActivity.this));

                        ((TextView) findViewById(R.id.pm)).setText("不限");
                        //  ((TextView) findViewById(R.id.pm)).setTextSize(20);
                        scoreFilter = -1;
                        findViewById(R.id.fliter).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.flit)).setImageResource(R.drawable.fliter);
                        findViewById(R.id.flit).setBackgroundColor(getResources().getColor(R.color.main));
                        isflit = false;
                    } else {
                        findViewById(R.id.fliter).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.flit)).setImageResource(R.drawable.nonflit);
                        findViewById(R.id.flit).setBackgroundColor(getResources().getColor(R.color.press));
                        isflit = true;
                    }
                }
                return false;
            }
        });
        findViewById(R.id.flit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lv = (ListView) findViewById(R.id.listView3);
        dr = (ImageView) findViewById(R.id.droppp);
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int position, long id) {
                final Classes c = classes.get(classNow);
                final ArrayList<History> histories = c.histories;

                History h = histories.get(getPos(position));
                final String reason = (h.reason);
                new ActionSheetDialog(MainActivity.this).builder()
                        .setTitle(reason)
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
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
                                                                        for (int i = 0; i != c.members.length; i++) {
                                                                            if (names.contains(c.members[i])) {
                                                                                c.scores.set(i, c.scores.get(i) - change);
                                                                            }
                                                                        }
                                                                    }
                                                                })
                                                                .setNegativeButton("不撤销", new OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                    }
                                                                }).show();
                                                        histories.remove(histories.get(getPos(position)));
                                                        updateList();
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
                                        History h = histories.get(getPos(position));
                                        new AlertDialogios(MainActivity.this).builder()
                                                .setTitle("详细记录")
                                                .setMsg("理由: " + h.reason + "\n" +
                                                        "成员: " + h.names + "\n" +
                                                        "分数: " + h.getScore() + "\n" +
                                                        "时间: " + h.getDate())
                                                .setNegativeButton("返回", new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                    }
                                                }).show();

                                    }
                                })
                                //可添加多个SheetItem
                        .show();
                //return false;
            }
        });


        findViewById(R.id.pick).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.year)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.month)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.day)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.textView5)).setTextColor(Color.parseColor("#9b9b9b"));
                ((TextView) findViewById(R.id.textView7)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.droppp)).setImageResource(R.drawable.dropdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.year)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.month)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.day)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.textView5)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) findViewById(R.id.textView7)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.droppp)).setImageResource(R.drawable.drop);
                }
                return false;
            }
        });
        findViewById(R.id.pickclass).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.classnow)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.dropclass)).setImageResource(R.drawable.dropdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.classnow)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.dropclass)).setImageResource(R.drawable.drop);
                }
                return false;
            }
        });
        findViewById(R.id.pickpm).setOnTouchListener(new View.OnTouchListener() {
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
        findViewById(R.id.pickpm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ActionSheetDialog(MainActivity.this).builder()
                        .setTitle("筛选加/减分")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("加分", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        ((TextView) findViewById(R.id.pm)).setText("  + ");
                                        // ((TextView) findViewById(R.id.pm)).setTextSize(30);
                                        scoreFilter = 1;
                                    }
                                })
                        .addSheetItem("减分", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        ((TextView) findViewById(R.id.pm)).setText(" — ");
                                      //  ((TextView) findViewById(R.id.pm)).setTextSize(30);
                                        scoreFilter=0;
                                    }
                                })
                        .addSheetItem("不限", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        ((TextView) findViewById(R.id.pm)).setText("不限");
                                      //  ((TextView) findViewById(R.id.pm)).setTextSize(20);
                                        scoreFilter=-1;
                                    }
                                })
                                //可添加多个SheetItem
                        .show();
            }

        });
        findViewById(R.id.pickclass).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActionSheetDialog ASD = new ActionSheetDialog(MainActivity.this).builder()
                        .setTitle("选择班级")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true);
                for (final String key : classes.keySet()) {
                    final String name = classes.get(key).name;
                    ASD.addSheetItem(name, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    ((TextView) findViewById(R.id.classnow)).setText(name);
                                    classNow = key;
                                    updateList();
                                }
                            });
                }

                ASD.show();
            }

        });
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, add.class), 2);
            }
        });

        //筛选日期
        findViewById(R.id.pick).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                superflag =true;
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //((TextView) findViewById(R.id.numofitem)).setText(String.valueOf(histories.size()));
                        if (superflag) {
                            showyear = String.valueOf(year) + "年";
                            showmonth =((month + 1) < 10) ?
                                    "0" + (month + 1) :
                                    String.valueOf(month + 1);
                            showday =(day < 10) ? "0" + day : String.valueOf(day);
                            ((TextView) findViewById(R.id.year)).setText(showyear);
                            ((TextView) findViewById(R.id.month)).setText(showmonth);
                            ((TextView) findViewById(R.id.day)).setText(showday);
                            ((TextView) findViewById(R.id.textView5)).setText("月");
                            ((TextView) findViewById(R.id.textView7)).setText("日");
                          //  ((TextView) findViewById(R.id.month)).setTextSize(30);
                            d= String.valueOf(year) + '-' + (month + 1) + '-' + day;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            try {
                                date = simpleDateFormat.parse(d);
                            } catch (ParseException e) {
                                //e.printStackTrace();
                            }
                            timeStemp = date.getTime() / 1000;
                            updateList();
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
                        //((TextView) findViewById(R.id.month)).setTextSize(20);
                        superflag = false;
                        timeStemp = 0;
                        updateList();
                    }
                });
                dpd.show();
            }
        });

        //读取数据
        String rawClasses = spReader.getString("classes","");
        if(true||rawClasses.isEmpty()){
            classes=getClassInfo();
        }else{
            String[] classesinfo=rawClasses.split(",");
            for(int i=0;i<classesinfo.length;i+=2){
                //TODO: 完善读取
                //classes.put(classesinfo[i],classesinfo[i+1]);
            }
        }
    }

    public void onStop(){
        super.onStop();
        //保存数据
        String classesData="";
        for (final String key : classes.keySet()) {
            Classes c=classes.get(key);
            classesData += key + "," + c.name;

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(key + ".txt", Activity.MODE_PRIVATE);
                for (int i = 0; i != c.scores.size(); i++) {
                    outputStream.write((c.scores.get(i).toString() + (i==c.scores.size()-1?"":" ")).getBytes());
                }
                for(int i=0;i!=c.histories.size();i++){
                    outputStream.write((c.histories.get(i).score+"|").getBytes());
                    outputStream.write((c.histories.get(i).names +"|").getBytes());
                    outputStream.write((c.histories.get(i).reason +"|").getBytes());
                    outputStream.write((c.histories.get(i).date.toGMTString() + "|").getBytes());
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spEditor.putString("classes", classesData);
        spEditor.apply();
    }

    private TreeMap<String, Classes> getClassInfo(){
        //假装从网上获得了数据
        TreeMap<String, Classes> c=new TreeMap<String, Classes>();
        c.put("1", new Classes("1班", "A B C"));
        c.put("2", new Classes("2班", "甲 乙 丙"));
        return c;
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

    public void onResume(){
        super.onResume();
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        if(classNow.equals("-1")) return listItem;
        final ArrayList<History> histories = classes.get(classNow).histories;
        for (int i = histories.size() - 1; i >= 0; i--) {
            if (d.compareTo("不限") != 0 && (histories.get(i).date.getTime() / 1000 - 86400 > timeStemp || histories.get(i).date.getTime() / 1000 <= timeStemp))
                continue;
            if (scoreFilter == 0 && histories.get(i).score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && histories.get(i).score < 0) continue; //筛选加分但是是扣分记录，忽略
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", (histories.get(i).reason.length() > 6 ? histories.get(i).reason.substring(0, 6) + "…" : histories.get(i).reason));
            map.put("ItemText", (histories.get(i).names.length() > 10 ? histories.get(i).names.substring(0, 18) + "…" : histories.get(i).names));
            map.put("mark", (histories.get(i).score > 0 ? "+" : "") + (histories.get(i).score / 10.0));
            listItem.add(map);
        }
        return listItem;
    }

    private int getPos(int position) {
        if(classNow.equals("-1")) return -1;
        int count = 0, sum = -1;
        final ArrayList<History> histories = classes.get(classNow).histories;
        for (int i = histories.size() - 1; i >= 0; i--) {
            sum++;
            if (d.compareTo("不限") != 0 && (histories.get(i).date.getTime() / 1000 - 86400 > timeStemp || histories.get(i).date.getTime() / 1000 <= timeStemp))
                continue;

            if (scoreFilter == 0 && histories.get(i).score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && histories.get(i).score < 0) continue; //筛选加分但是是扣分记录，忽略

            if (count < position) {
                count++;
                continue;
            }
            return sum;
        }
        return -1;
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
                convertView = mInflater.inflate(R.layout.itemmoreinfo, parent);
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

                                                            for (int i = 0; i != MainActivity.names.length; i++) {
                                                                if (names.indexOf(MainActivity.names[i]) != -1) {
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
            if(Integer.parseInt((String) (getData().get(position).get("mark")))>0) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case 1:
                String result = data.getExtras().getString("res"); //得到新Activity关闭后返回的数据
                if (!result.matches("NULL")) {
                    String[] strArray = result.split("[|]");
                    filterName = strArray[1];
                    filterClass = strArray[0];
                    ((TextView) findViewById(R.id._name)).setText(filterName);
                    ((TextView) findViewById(R.id._class)).setText(filterClass);
                }
                break;
            case 2:
                String his = data.getExtras().getString("his");
                  //得到新Activity关闭后返回的数据
                if (!his.matches("NULL")) {
                    //更新记录
                }
                break;
        }

    }
}
