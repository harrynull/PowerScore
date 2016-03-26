package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

public class MainActivity extends Activity implements AbsListView.OnScrollListener {
    public static MainActivity MainActivityPointer;
    public static TreeMap<String, Classes> classes = new TreeMap<String, Classes>();
    public static ArrayList<Group> groups = new ArrayList<Group>();
    SharedPreferences spReader;
    SharedPreferences.Editor spEditor;

    private long timeStamp = 0;
    private boolean superFlag = true;
    private boolean isSync = false;

    private String classNow = "-1";
    private String d = "不限";
    private String showYear;
    private String showMonth;
    private String showDay;
    private String filterClass = "";
    private String filterName = "";
    private Boolean isGen = true;
    private int scoreFilter = -1;
    private int countLimit = 20;
    private boolean isLastRow;

    //布局
    private ListView lv;
    private int sbar = 0;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;
    private ImageView sync;

    private Animation in_per;
    private final String TAG = this.getClass().getName();
    private final int Update_NONEED = 0;
    private final int Update_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int DOWN_ERROR = 4;
    private UpdateInfo info;
    private String localVersion;
    Button gen;
    Button per;

    private void doSync() {
        //findViewById(R.id.add).setEnabled(false);
        getClassInfo();
        for (final String key : classes.keySet()) {
            final Classes c = classes.get(key);
            //分班级同步
            //生成diff
            String diff = "";
            for (int i = 0; i != c.unsyncHistories.size(); i++) {
                diff += c.unsyncHistories.get(i).score + "|";
                diff += c.unsyncHistories.get(i).names + "|";
                diff += c.unsyncHistories.get(i).reason + "|";
                diff += c.unsyncHistories.get(i).getDate(true) + "|";
                diff += c.unsyncHistories.get(i).oper + "|";
            }
            //将生成的diff上传到服务器上
            String username = spReader.getString("username", "");
            String password = spReader.getString("password", "");

            new Thread(new AccessNetwork("POST",
                    "http://scoremanagement.applinzi.com/sync.php",
                    "username=" + username + "&password=" + password + "&cid=" + key + "&diff=" + diff, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            showToast("登陆失败，用户名或密码错误");
                            break;
                        case 1:
                            showToast("网络异常，请检查网络连接");
                            break;
                        case 2:
                            readData(msg.obj.toString(), c);
                            break;
                    }
                }
            }, 0)).start();

            //同步结束
            c.unsyncHistories.clear();
        }
        sync.clearAnimation();
        isSync = false;
    }

    class AccessNetwork implements Runnable {
        private String op;
        private String url;
        private String params;
        private Handler h;
        private int tag;

        public AccessNetwork(String op, String url, String params, Handler h, int tag) {
            super();
            this.op = op;
            this.url = url;
            this.params = params;
            this.h = h;
            this.tag = tag;
        }

        @Override
        public void run() {
            Message m = new Message();
            m.what = tag;
            if (op.compareTo("POST") == 0)
                m.obj = GetPostUtil.sendPost(url, params);
            else
                m.obj = GetPostUtil.sendGet(url, params);

            if (m.obj.toString().equals("F")) {
                m.what = 0;
            } else if (m.obj.toString().isEmpty()) {
                m.what = 1;
            } else m.what = 2;
            h.sendMessage(m);
        }
    }

    private void updateList() {
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
    }

    public void jumpToStudent(String classID, String studentName) {
        filterClass = classID;
        filterName = studentName;
        ((TextView) findViewById(R.id._name)).setText(filterName);
        per.setTextColor(Color.parseColor("#ffffff"));
        gen.setTextColor(Color.parseColor("#7fffffff"));
        ((TextView) findViewById(R.id.year)).setText("");
        ((TextView) findViewById(R.id.day)).setText("");
        ((TextView) findViewById(R.id.textView5)).setText("");
        ((TextView) findViewById(R.id.textView7)).setText("");
        findViewById(R.id.bar).startAnimation(in_per);
        findViewById(R.id.pcr).setVisibility(View.GONE);
        findViewById(R.id.pnm).setVisibility(View.VISIBLE);
        isGen = false;
        ((DrawerLayout) findViewById(R.id.drawerlayout)).closeDrawer(leftLayout);
        updateList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityPointer = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            sbar = getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height")
                    .get(c.newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Util.setTranslucent(this);
        RelativeLayout genLayout;
        RelativeLayout perLayout;
        ImageView add;
        //初始化
        spReader = getSharedPreferences("data", Activity.MODE_PRIVATE);
        spEditor = spReader.edit();
        try {
            localVersion = getVersionName();
            CheckVersionTask cv = new CheckVersionTask();

            new Thread(cv).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //布局初始化
        setContentView(R.layout.activity_main);
        boolean splash = getSharedPreferences("data", 0).getBoolean("splash", true);
        if (splash) {
            findViewById(R.id.onspl).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.onspl).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.realname)).setText(spReader.getString("username", "未登录"));
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
        sync = (ImageView) findViewById(R.id.sync);
        final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        final Animation out_gen = AnimationUtils.loadAnimation(this, R.anim.personal_out);
        in_per = AnimationUtils.loadAnimation(this, R.anim.personal_in);
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
        leftLayout = (RelativeLayout) findViewById(R.id.left);
        rightLayout = (RelativeLayout) findViewById(R.id.right);
        ((DrawerLayout) findViewById(R.id.drawerlayout)).setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        ((DrawerLayout) findViewById(R.id.drawerlayout)).setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ((DrawerLayout) findViewById(R.id.drawerlayout)).setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                ((DrawerLayout) findViewById(R.id.drawerlayout)).setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSync) {
                    //开始转
                    sync.startAnimation(operatingAnim);
                    isSync = true;
                    doSync();
                }
            }
        });
        gen.setOnClickListener(new View.OnClickListener()
        {
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
                countLimit = 20;
                updateList();
            }
        });

        per.setOnClickListener(new View.OnClickListener(){
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
               if (findViewById(R.id.onspl).getVisibility() == View.VISIBLE) {
                   Oncp = false;
                   findViewById(R.id.onspl).setVisibility(View.GONE);
               } else {
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
            public void onItemClick(AdapterView<?> arg0, View arg1,final int position, long id) {
                final Classes c = classes.get(classNow);
                final ArrayList<History> histories = c.histories;
                final ArrayList<History> usHistories = c.unsyncHistories;

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
                                            .setMsg("确认删除记录“" + reason + "”吗?\n该条记录所修改的分数将被撤销")
                                            .setPositiveButton("删除", new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    History h = histories.get(getPos(position));
                                                    final int change = h.score;
                                                    final String names = h.names;
                                                    for (int i = 0; i != c.members.length; i++) {
                                                        if (names.contains(c.members[i])) {
                                                            c.scores[i] -= change;
                                                        }
                                                    }
                                                    histories.remove(h);
                                                    usHistories.add(new History(h.date));
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
                                    String info_of_record = h.reason + "|" + c.name + "|" + h.names + "|" + h.getScore() + "|" + h.getDate(true) + "|" + h.oper;
                                    Intent i = new Intent();
                                    i.putExtra("record", info_of_record);
                                    i.setClass(MainActivity.this, moreinfo.class);
                                    startActivity(i);
                                }
                            })
                            //可添加多个SheetItem
                    .show();
                }
            }
        );


        findViewById(R.id.pick).setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
             ((TextView) findViewById(R.id.year)).setTextColor(Color.parseColor("#7fffffff"));
             ((TextView) findViewById(R.id.month)).setTextColor(Color.parseColor("#7fffffff"));
             ((TextView) findViewById(R.id.day)).setTextColor(Color.parseColor("#7fffffff"));
             ((TextView) findViewById(R.id.textView5)).setTextColor(Color.parseColor("#7fffffff"));
             ((TextView) findViewById(R.id.textView7)).setTextColor(Color.parseColor("#7fffffff"));
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
                  ((TextView) findViewById(R.id.classnow)).setTextColor(Color.parseColor("#7fffffff"));
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
             ((TextView) findViewById(R.id._name)).setTextColor(Color.parseColor("#7fffffff"));
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
               ((TextView) findViewById(R.id.pm)).setTextColor(Color.parseColor("#7fffffff"));
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
                         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
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
        String content = spReader.getString("groups", "");
        String[] result = content.split(",");
        groups.clear();
        for (
                int i = 0;
                i < result.length - 1; i += 2)

        {
            groups.add(new Group(result[i], result[i + 1]));
        }

        //读取数据
        String rawClasses = spReader.getString("classes", "");
        getClassInfo();
        String[] classesinfo = rawClasses.split(",");
        for (int i = 0; i < classesinfo.length; i += 2) {
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
                readData(new String(arrayOutputStream.toByteArray()), readNow);

            } catch (Exception ignored) {}
            classes.put(classesinfo[i], readNow);
        }


        //默认选择一个班
        if (classes.size() != 0) {
            Classes c = classes.get(classes.firstKey());
            ((TextView) findViewById(R.id.classnow)).setText(c.name);
            classNow = classes.firstKey();
            updateList();
        }
    }

    public void readData(String data, Classes readNow) {
        try {
            String[] strs = data.split("\n");
            readNow.setMembers(strs[0]);

            String[] strScores = strs[1].split(" ");
            for (int j = 0; j < readNow.members.length; j++) {
                readNow.scores[j] = Integer.valueOf(strScores[j]);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.CHINA);

            if (strs.length < 3) return;
            String[] histories = strs[2].split("\\|");
            for (int j = 0; j < histories.length; j += 5) {
                readNow.histories.add(new History(Integer.parseInt(histories[j]), histories[j + 1],
                        histories[j + 2], sdf.parse(histories[j + 3]), histories[j + 4]));
            }
            if (strs.length < 4) return;
            String[] usHistories = strs[3].split("\\|");
            for (int j = 0; j < usHistories.length; j += 5) {
                readNow.unsyncHistories.add(new History(Integer.parseInt(histories[j]), histories[j + 1],
                        histories[j + 2], sdf.parse(histories[j + 3]), histories[j + 4]));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        super.onStop();
        //保存数据
        String classesData = "";
        for (final String key : classes.keySet()) {
            Classes c = classes.get(key);
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
                for (int i = 0; i != c.histories.size(); i++) {
                    outputStream.write((c.histories.get(i).score + "|").getBytes());
                    outputStream.write((c.histories.get(i).names + "|").getBytes());
                    outputStream.write((c.histories.get(i).reason + "|").getBytes());
                    outputStream.write((c.histories.get(i).getDate(true) + "|").getBytes());
                    outputStream.write((c.histories.get(i).oper + "|").getBytes());
                }
                outputStream.write("\n".getBytes());
                for (int i = 0; i != c.unsyncHistories.size(); i++) {
                    outputStream.write((c.unsyncHistories.get(i).score + "|").getBytes());
                    outputStream.write((c.unsyncHistories.get(i).names + "|").getBytes());
                    outputStream.write((c.unsyncHistories.get(i).reason + "|").getBytes());
                    outputStream.write((c.unsyncHistories.get(i).getDate(true) + "|").getBytes());
                    outputStream.write((c.unsyncHistories.get(i).oper + "|").getBytes());
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spEditor.putString("classes", classesData);
        String groupsStr = "";
        for (Group group : groups) {
            groupsStr += group.groupName + "," + group.groupMembers + ",";
        }
        spEditor.putString("groups", groupsStr);
        spEditor.apply();
    }
    private void getClassInfo() {
        //从网上获得数据
        final String username = spReader.getString("username", "");
        final String password = spReader.getString("password", "");
        new Thread(new AccessNetwork("POST",
                "http://scoremanagement.applinzi.com/getclasses.php",
                "username=" + username + "&password=" + password, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        showToast("无法获取班级数据：用户名或密码错误");
                        break;
                    case 1:
                        showToast("无法获取班级数据：无法连接到网络");
                        break;
                    case 2:
                        String[] ids = msg.obj.toString().split(",");
                        for (int i = 0; i < ids.length - 1; i += 2) {
                            if(classes.containsKey(ids[i])) continue;
                            final Classes c = new Classes(ids[i + 1]);
                            new Thread(new AccessNetwork("POST",
                                    "http://scoremanagement.applinzi.com/sync.php",
                                    "username=" + username + "&password=" + password + "&cid=" + ids[i] + "&diff=", new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case 0:
                                            showToast("无法获取班级数据：未知错误");
                                            break;
                                        case 1:
                                            showToast("无法获取班级数据：网络连接不稳定");
                                            break;
                                        case 2:
                                            readData(msg.obj.toString().substring(1), c);
                                            break;
                                    }
                                }
                            }, 0)).start();
                            classes.put(ids[i], c);
                        }
                        break;
                }
            }
        }, 0)).start();
    }

    public void onResume() {
        super.onResume();
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
        if (isLastRow && scrollState == SCROLL_STATE_IDLE) {
             loadMore();
             updateList();
        }

    }

    private void loadMore() {
        countLimit = lv.getAdapter().getCount() + 20;
        if (countLimit > classes.get(classNow).histories.size())
            countLimit = classes.get(classNow).histories.size();
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        if (classNow.equals("-1") && isGen) return listItem;
        if (filterClass.isEmpty() && !isGen) return listItem;
        if (filterName.isEmpty() && !isGen) return listItem;
        final ArrayList<History> histories;
        if (isGen)
            histories = classes.get(classNow).histories;
        else
            histories = classes.get(filterClass).histories;
        int vaildItem = 0;
        for (int i = histories.size() - 1; i >= 0; i--) {
            if (vaildItem++ > countLimit) break;
            History h = histories.get(i);
            if (d.compareTo("不限") != 0 && (h.date.getTime() / 1000 - 86400 > timeStamp || h.date.getTime() / 1000 <= timeStamp))
                continue;
            if (scoreFilter == 0 && h.score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && h.score < 0) continue; //筛选加分但是是扣分记录，忽略
            if (!h.names.contains(filterName) && !isGen) continue;
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
        if (classNow.equals("-1") && isGen) return -1;
        if (filterClass.isEmpty() && !isGen) return -1;
        if (filterName.isEmpty() && !isGen) return -1;
        int count = 0;
        final ArrayList<History> histories;
        if (isGen)
            histories = classes.get(classNow).histories;
        else
            histories = classes.get(filterClass).histories;
        for (int i = histories.size() - 1; i >= 0; i--) {
            if (histories.size() - i > countLimit) break;
            if (d.compareTo("不限") != 0 && (histories.get(i).date.getTime() / 1000 - 86400 > timeStamp || histories.get(i).date.getTime() / 1000 <= timeStamp))
                continue;
            if (scoreFilter == 0 && histories.get(i).score > 0) continue; //筛选扣分但是是加分记录，忽略
            if (scoreFilter == 1 && histories.get(i).score < 0) continue; //筛选加分但是是扣分记录，忽略
            if (!histories.get(i).names.contains(filterName) && !isGen) continue;
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
            if ((Integer) (getData().get(position).get("mark")) > 0) {
                holder.positive.setImageResource(R.drawable.green);
            } else {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
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
                if (!results[0].equals("NULL") && !results[2].isEmpty()) {
                    //更新记录
                    String[] namesByClasses = new String[classes.size()]; //分别储存每个班的人
                    String[] allNames = results[1].split("\\|");
                    for (int i = 0; i != namesByClasses.length; i++) {
                        namesByClasses[i] = "";
                    }
                    for (int i = 0; i < allNames.length; i += 2) {
                        String[] members = classes.get(allNames[i]).members;
                        for (int j = 0; j != members.length; j++) {
                            if (allNames[i + 1].equals(members[j])) {
                                int cid = 0;
                                for (final String key : classes.keySet()) { //查找每个班
                                    if (key.equals(allNames[i])) break;
                                    cid++;
                                }
                                namesByClasses[cid] += allNames[i + 1] + ",";
                            }
                        }
                    }
                    int cid = 0;
                    int score = (int) (Float.parseFloat(results[2]) * 10);
                    for (final String key : classes.keySet()) { //查找每个班
                        if (!namesByClasses[cid].isEmpty()) {
                            String names = namesByClasses[cid].substring(0, namesByClasses[cid].length() - 1);
                            String oper = spReader.getString("username", "未登录用户");
                            Date d = new Date();
                            classes.get(key).histories.add(new History(score, names, results[0], d, oper));
                            classes.get(key).unsyncHistories.add(new History(score, names, results[0], d, oper));
                            for (String name : names.split(",")) {
                                for (int i = 0; i < classes.get(key).members.length; i++) {
                                    if (classes.get(key).members[i].equals(name))
                                        classes.get(key).scores[i] += score;
                                }
                            }
                        }
                        cid++;
                    }
                    updateList();
                }
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /////检查更新
    public static class UpdateInfo {
        private String version;
        private String url;
        private String description;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class UpdateInfoParser {
        public static UpdateInfo getUpdateInfo(InputStream is) throws Exception {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int type = parser.getEventType();
            UpdateInfo info = new UpdateInfo();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("version".equals(parser.getName())) {
                            info.setVersion(parser.nextText());
                        } else if ("url".equals(parser.getName())) {
                            info.setUrl(parser.nextText());
                        } else if ("description".equals(parser.getName())) {
                            info.setDescription(parser.nextText());
                        }
                        break;
                }
                type = parser.next();
            }
            return info;
        }
    }


    private String getVersionName() throws Exception {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Update_NONEED:
                    showToast("已是最新版本 :)");
                    break;
                case Update_CLIENT:
                    //对话框通知用户升级程序
                    showUpdateDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    showToast("获取更新失败 :( 请检查网络");
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    showToast("下载新版本失败 :(");
                    break;
            }
        }
    };

    public class CheckVersionTask implements Runnable {
        InputStream is;

        public void run() {

            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdateInfoParser.getUpdateInfo(is);
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号相同");
                    Message msg = new Message();
                    msg.what = Update_NONEED;
                    updateHandler.sendMessage(msg);
                    // LoginMain();
                } else {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = Update_CLIENT;
                    updateHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                updateHandler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }


    protected void showUpdateDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        builer.setNegativeButton("取消", null);
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /*
    * 从服务器中下载APK
    */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    updateHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
