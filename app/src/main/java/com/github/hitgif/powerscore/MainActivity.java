package com.github.hitgif.powerscore;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.content.Context;
import android.content.DialogInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class MainActivity extends Activity implements AbsListView.OnScrollListener {

    public static TreeMap<String, Classes> classes = new TreeMap<String, Classes>();
    public static ArrayList<Group> groups=new ArrayList<Group>();
    SharedPreferences spReader;
    SharedPreferences.Editor spEditor;

    private long timeStamp =0;
    private boolean superFlag = true;
    private boolean isSync = false;
    private int lsi = 2333;

    private String classNow="-1";
    private String d="不限";
    private String showYear;
    private String showMonth;
    private String showDay;
    private String filterClass="";
    private String filterName="";
    private Boolean isGen = true;
    private int scoreFilter=-1;
    private int lastItem;
    private int countLimit=20;

    //布局
    private ListView lv;
    private int sbar = 0;
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
        RelativeLayout genLayout;
        RelativeLayout perLayout;
        ImageView add;
        //初始化
        spReader= getSharedPreferences("data", Activity.MODE_PRIVATE);
        spEditor = spReader.edit();

        //布局初始化
        setContentView(R.layout.activity_main);
        boolean splash = getSharedPreferences("data", 0).getBoolean("splash", true);
        if(splash){
            findViewById(R.id.onspl).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.onspl).setVisibility(View.GONE);
        }
        gen = (Button) findViewById(R.id.gen);
        per = (Button) findViewById(R.id.per);
        genLayout = (RelativeLayout) findViewById(R.id.genlayout);
        perLayout = (RelativeLayout) findViewById(R.id.perlayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = findViewById(R.id.ds).getLayoutParams();
            lp.width = 1;
            lp.height = sbar;
            findViewById(R.id.ds).setLayoutParams(lp);
            findViewById(R.id.ds2).setLayoutParams(lp);
        }
        add = (ImageView) findViewById(R.id.add);
        final  ImageView sync = (ImageView)findViewById(R.id.sync);
        final  Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        final  Animation in_per = AnimationUtils.loadAnimation(this, R.anim.personal_in);
        final  Animation out_gen = AnimationUtils.loadAnimation(this, R.anim.personal_out);
        per.setTextColor(Color.parseColor("#7fffffff"));
        in_per.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.lpd).setVisibility(View.GONE);
                findViewById(R.id.lpd2).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        genLayout.setVisibility(View.VISIBLE);
        perLayout.setVisibility(View.GONE);
        leftLayout=(RelativeLayout) findViewById(R.id.left);
        rightLayout=(RelativeLayout) findViewById(R.id.right);

        sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSync) {
                    //开始转
                    sync.startAnimation(operatingAnim);
                    isSync = true;
                } else {
                    //停止转
                    sync.clearAnimation();
                    isSync = false;
                }
            }
        });
        gen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isGen) {
                    findViewById(R.id.bar).startAnimation(out_gen);
                }
                gen.setTextColor(Color.parseColor("#ffffff"));
                per.setTextColor(Color.parseColor("#7fffffff"));
                findViewById(R.id.lpd).setVisibility(View.VISIBLE);
                findViewById(R.id.lpd2).setVisibility(View.GONE);
                findViewById(R.id.pnm).setVisibility(View.GONE);
                findViewById(R.id.pcr).setVisibility(View.VISIBLE);
                isGen = true;
                countLimit=20;
                updateList();
            }
        });


        ls = (Button) findViewById(R.id.ls);
        ls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classes.get(classNow).histories.add(new History(+10, "王安海", String.valueOf(lsi), new Date(), "test"));
                lsi++;
                updateList();
            }
        });

        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                per.setTextColor(Color.parseColor("#ffffff"));
                gen.setTextColor(Color.parseColor("#7fffffff"));
                ((TextView) findViewById(R.id.year)).setText("");
                ((TextView) findViewById(R.id.day)).setText("");
                ((TextView) findViewById(R.id.textView5)).setText("");
                ((TextView) findViewById(R.id.textView7)).setText("");
                findViewById(R.id.bar).startAnimation(in_per);
                findViewById(R.id.pcr).setVisibility(View.GONE);
                findViewById(R.id.pnm).setVisibility(View.VISIBLE);
                updateList();
                isGen = false;
            }
        });

        findViewById(R.id.personal).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((DrawerLayout) findViewById(R.id.drawerlayout)).openDrawer(leftLayout);
            }
        });

        findViewById(R.id.flit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerLayout) findViewById(R.id.drawerlayout)).openDrawer(rightLayout);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogios(MainActivity.this).builder()
                        .setTitle("退出登录")
                        .setMsg("确定要退出登录吗?")
                        .setPositiveButton("退出登录", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spEditor.putString("username", "");
                                spEditor.putString("password", "");
                                spEditor.apply();
                                startActivity(new Intent(getApplication(), login.class));
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();

            }
        });

        findViewById(R.id.reason).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, reason_setting.class));
            }

        });

        findViewById(R.id.setspl).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SharedPreferences.Editor sharedata2 = getSharedPreferences("data", 0).edit();
                Boolean Oncp;
                if (findViewById(R.id.onspl).getVisibility()==View.VISIBLE)
                {
                    Oncp = false;
                    findViewById(R.id.onspl).setVisibility(View.GONE);
                }else {
                    Oncp = true;
                    findViewById(R.id.onspl).setVisibility(View.VISIBLE);
                }
                sharedata2.putBoolean("splash", Oncp);
                sharedata2.apply();

            }

        });
        findViewById(R.id.linearLayout7).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, group_setting.class));
            }

        });
        findViewById(R.id.overView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OverView.class));
            }

        });
        lv = (ListView) findViewById(R.id.listView3);
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
        lv.setOnScrollListener(this);
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
                                                                                c.scores[i]-=change;
                                                                            }
                                                                        }
                                                                    }
                                                                })
                                                                .setNegativeButton("不撤销", null).show();
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
                                        String info_of_record = h.reason+"|"+c.name+"|"+h.names+"|"+h.getScore()+"|"+h.getDate(true)+"|"+h.oper;
                                        Intent i=new Intent();
                                        i.putExtra("record", info_of_record);
                                        i.setClass(MainActivity.this, moreinfo.class);
                                        startActivity(i);

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
        findViewById(R.id.pnmb).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id._name)).setTextColor(Color.parseColor("#9b9b9b"));
                ((ImageView) findViewById(R.id.nmdr)).setImageResource(R.drawable.dropdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id._name)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.nmdr)).setImageResource(R.drawable.drop);
                    startActivityForResult(new Intent(MainActivity.this, choosestudent.class), 1);
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
                                        updateList();
                                    }
                                })
                        .addSheetItem("减分", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        ((TextView) findViewById(R.id.pm)).setText(" — ");
                                        //  ((TextView) findViewById(R.id.pm)).setTextSize(30);
                                        scoreFilter = 0;
                                        updateList();
                                    }
                                })
                        .addSheetItem("不限", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        ((TextView) findViewById(R.id.pm)).setText("不限");
                                        //  ((TextView) findViewById(R.id.pm)).setTextSize(20);
                                        scoreFilter = -1;
                                        updateList();
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
                superFlag = true;
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        if (superFlag) {
                            showYear = String.valueOf(year) + "年";
                            showMonth = ((month + 1) < 10) ?
                                    "0" + (month + 1) :
                                    String.valueOf(month + 1);
                            showDay = (day < 10) ? "0" + day : String.valueOf(day);
                            ((TextView) findViewById(R.id.year)).setText(showYear);
                            ((TextView) findViewById(R.id.month)).setText(showMonth);
                            ((TextView) findViewById(R.id.day)).setText(showDay);
                            ((TextView) findViewById(R.id.textView5)).setText("月");
                            ((TextView) findViewById(R.id.textView7)).setText("日");
                            //  ((TextView) findViewById(R.id.month)).setTextSize(30);
                            d = String.valueOf(year) + '-' + (month + 1) + '-' + day;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            try {
                                date = simpleDateFormat.parse(d);
                            } catch (ParseException e) {
                                //e.printStackTrace();
                            }
                            timeStamp = date.getTime() / 1000;
                            updateList();
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
                        superFlag = false;
                        timeStamp = 0;
                        updateList();
                    }
                });
                dpd.show();
            }
        });

        //读取个人信息
        //读取组列表
        String content = spReader.getString("groups","");
        String[] result=content.split(",");
        groups.clear();
        for(int i=0;i<result.length-1;i+=2){
            groups.add(new Group(result[i],result[i+1]));
        }

        //读取数据
        String rawClasses = spReader.getString("classes","");
        if(rawClasses.isEmpty()){
            classes=getClassInfo();
        }else{
            String[] classesinfo=rawClasses.split(",");
            for(int i=0;i<classesinfo.length;i+=2) {
                Classes readNow = new Classes(classesinfo[i + 1]);

                //读取数据
                try {
                    FileInputStream inputStream = this.openFileInput(classesinfo[i] + ".dat");
                    byte[] bytes = new byte[inputStream.available()];
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    while (inputStream.read(bytes) != -1) {
                        arrayOutputStream.write(bytes, 0, bytes.length);
                    }
                    inputStream.close();
                    arrayOutputStream.close();
                    String buffer = new String(arrayOutputStream.toByteArray());

                    String[] strs = buffer.split("\n");
                    readNow.setMembers(strs[0]);

                    String[] strScores=strs[1].split(" ");
                    for (int j = 0; j < readNow.members.length; j++) {
                        readNow.scores[j]=Integer.valueOf(strScores[j]);
                    }

                    String[] histories=strs[2].split("\\|");
                    for(int j=0;j<histories.length-1;j+=5){
                        readNow.histories.add(new History(Integer.parseInt(histories[j]), histories[j + 1],
                                histories[j + 2], new Date(histories[j + 3]), histories[j + 4]));
                    }
                } catch (Exception ignored) {ignored.printStackTrace();}

                classes.put(classesinfo[i], readNow);
            }
        }

        //默认选择一个班
        if(classes.size()!=0) {
            Classes c = classes.get(classes.firstKey());
            ((TextView) findViewById(R.id.classnow)).setText(c.name);
            classNow = classes.firstKey();
            updateList();
        }
    }

    public void onStop(){
        super.onStop();
        //保存数据
        String classesData="";
        for (final String key : classes.keySet()) {
            Classes c=classes.get(key);
            classesData += key + "," + c.name + ",";

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(key + ".dat", Activity.MODE_PRIVATE);
                for (int i = 0; i != c.members.length; i++) {
                    outputStream.write((c.members[i] + (i == c.members.length - 1 ? "" : " ")).getBytes());
                }
                outputStream.write("\n".getBytes());
                for (int i = 0; i != c.scores.length; i++) {
                    outputStream.write((c.scores[i] + (i == c.scores.length - 1 ? "" : " ")).getBytes());
                }
                outputStream.write("\n".getBytes());
                for(int i=0;i!=c.histories.size();i++){
                    outputStream.write((c.histories.get(i).score+"|").getBytes());
                    outputStream.write((c.histories.get(i).names +"|").getBytes());
                    outputStream.write((c.histories.get(i).reason +"|").getBytes());
                    outputStream.write((c.histories.get(i).date.toGMTString() + "|").getBytes());
                    outputStream.write((c.histories.get(i).oper + "|").getBytes());
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spEditor.putString("classes", classesData);
        String groupsStr="";
        for (Group group : groups) {
            groupsStr+=group.groupName+","+group.groupMembers+",";
        }
        spEditor.putString("groups", groupsStr);
        spEditor.apply();
    }

    private TreeMap<String, Classes> getClassInfo(){
        //假装从网上获得了数据
        TreeMap<String, Classes> c=new TreeMap<String, Classes>();
        Classes c1=new Classes("1班");
        c1.setMembers("A B C");
        Classes c2=new Classes("2班");
        c2.setMembers("甲 乙 丙");
        c.put("1", c1);
        c.put("2", c2);
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


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        lastItem = firstVisibleItem + visibleItemCount;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
        if(lastItem == countLimit && scrollState == SCROLL_STATE_IDLE){
            mHandler.sendEmptyMessage(0);
        }

    }
    //声明Handler
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    loadMore();  //加载更多数据，这里可以使用异步加载
                    updateList();
                    break;
                default:
                    break;
            }
        }
    };
    private void loadMore(){
        countLimit = lv.getAdapter().getCount()+1;
        if(countLimit>classes.get(classNow).histories.size()) countLimit=classes.get(classNow).histories.size();
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        if(classNow.equals("-1")&&isGen) return listItem;
        if(filterClass.isEmpty()&&!isGen) return listItem;
        if(filterName.isEmpty()&&!isGen) return listItem;
        final ArrayList<History> histories;
        if(isGen)
            histories=classes.get(classNow).histories;
        else
            histories=classes.get(filterClass).histories;
        int vaildItem=0;
        for (int i = histories.size() - 1; i >= 0; i--) {
            if (vaildItem++>countLimit) break;
            History h=histories.get(i);
            if (d.compareTo("不限") != 0 && (h.date.getTime() / 1000 - 86400 > timeStamp || h.date.getTime() / 1000 <= timeStamp))
                continue;
            if (scoreFilter == 0 && h.score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && h.score < 0) continue; //筛选加分但是是扣分记录，忽略
            if (!h.names.contains(filterName)&&!isGen) continue;
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", h.shortReason);
            map.put("ItemText", h.shortNames);
            map.put("strmark", h.scoreWithSign);
            map.put("mark", h.score);
            map.put("date", h.getDate(false));
            listItem.add(map);
        }
        return listItem;
    }

    private int getPos(int position) {
        if(classNow.equals("-1")&&isGen) return -1;
        if(filterClass.isEmpty()&&!isGen) return -1;
        if(filterName.isEmpty()&&!isGen) return -1;
        int count = 0;
        final ArrayList<History> histories;
        if(isGen)
            histories=classes.get(classNow).histories;
        else
            histories=classes.get(filterClass).histories;
        for (int i = histories.size() - 1; i >= 0; i--) {
            if (histories.size()-i>countLimit) break;
            if (d.compareTo("不限") != 0 && (histories.get(i).date.getTime() / 1000 - 86400 > timeStamp || histories.get(i).date.getTime() / 1000 <= timeStamp))
                continue;
            if (scoreFilter == 0 && histories.get(i).score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && histories.get(i).score < 0) continue; //筛选加分但是是扣分记录，忽略
            if (!histories.get(i).names.contains(filterName)&&!isGen) continue;
            if (count < position) {
                count++;
                continue;
            }
            return i;
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
                convertView = mInflater.inflate(R.layout.itemmoreinfo, null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
                holder.mark = (TextView) convertView.findViewById(R.id.mark);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.positive = (ImageView) convertView.findViewById(R.id.positive);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText((String) (getData().get(position).get("ItemTitle")));
            String s = (String) (getData().get(position).get("ItemText"));
            holder.text.setText(s);
            holder.mark.setText((String) (getData().get(position).get("strmark")));
            holder.date.setText((String) (getData().get(position).get("date")));
            if((Integer) (getData().get(position).get("mark"))>0) {
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
            public TextView date;
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

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case 1:
                String result = data.getExtras().getString("result"); //得到新Activity关闭后返回的数据
                if (!result.matches("NULL")) {
                    String[] strArray = result.split("[|]");
                    filterClass = strArray[0];
                    filterName = strArray[1];
                    ((TextView) findViewById(R.id._name)).setText(filterName);
                    updateList();
                }
                break;
            case 2:
                String[] results = data.getExtras().getStringArray("data");
                  //接收add
                if (!results[0].equals("NULL")&& !results[2].isEmpty()) {
                    //更新记录
                    String[] namesByClasses=new String[classes.size()]; //分别储存每个班的人
                    String[] allNames=results[1].split("\\|");
                    for(int i=0;i!=namesByClasses.length;i++){
                        namesByClasses[i]="";
                    }
                    for(int i=0;i<allNames.length;i+=2) {
                        String[] members = classes.get(allNames[i]).members;
                        for (int j = 0; j != members.length; j++) {
                            if (allNames[i + 1].equals(members[j])) {
                                int cid = 0;
                                for (final String key : classes.keySet()) { //查找每个班
                                    if(key.equals(allNames[i])) break;
                                    cid++;
                                }
                                namesByClasses[cid] += allNames[i + 1] + ",";
                            }
                        }
                    }
                    int cid=0;
                    int score=(int)(Float.parseFloat(results[2])*10);
                    for (final String key : classes.keySet()) { //查找每个班
                        if(!namesByClasses[cid].isEmpty()) {
                            String names=namesByClasses[cid].substring(0, namesByClasses[cid].length()-1);
                            classes.get(key).histories.add(new History(score, names, results[0], new Date(), spReader.getString("Username","")));
                        }
                        cid++;
                    }
                    updateList();
                }
                break;
        }

    }
    public static void setMarginsd (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
