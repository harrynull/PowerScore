package com.github.hitgif.powerscore;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class reason_setting extends Activity {

    private ListView mListView;
    private List<String> dataSourceList;
    private ArrayAdapter mAdapter;
    private int screenWidth;
    private String[] reasonsArray;
    private boolean editting;
    interface MyListener{

        public void refreshActivity(String text);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reason_setting);
        mListView = (ListView) findViewById(R.id.lv_data);
        screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        final SharedPreferences sharedata = getSharedPreferences("data", 0);
        String reasons = sharedata.getString("reasons", "上课发言 打扫卫生 小组加分 上午迟到 中午迟到 上课讲话 晚修讲话 随意下位 没有值日 ");
        reasonsArray = reasons.split(" ");
        mAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_view_item,R.id.list_item,reasonsArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

                new ActionSheetDialog(reason_setting.this).builder()
                        .setTitle("选择操作")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        String newreasons = "";
                                        for (int i = 0; i < reasonsArray.length; i++) {
                                            if (i != arg2) {
                                                newreasons += reasonsArray[i] + " ";
                                            }
                                        }
                                        reasonsArray = newreasons.split(" ");
                                        mAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_view_item, R.id.list_item, reasonsArray);
                                        mListView.setAdapter(mAdapter);
                                    }
                                })
                        .addSheetItem("修改", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {


                                        //修改理由
                                        final EditText text = new EditText(reason_setting.this);
                                        text.setText(reasonsArray[arg2].toString());
                                        new AlertDialog.Builder(reason_setting.this)
                                                .setTitle("修改理由")
                                                .setView(text)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (text.getText().toString().isEmpty()) {
                                                            Toast.makeText(reason_setting.this, "修改理由失败:理由不能为空", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            SharedPreferences sharedata = getSharedPreferences("data", 0);
                                                            reasonsArray[arg2] = text.getText().toString();
                                                            mAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_view_item, R.id.list_item, reasonsArray);
                                                            mListView.setAdapter(mAdapter);
                                                            Toast.makeText(reason_setting.this, "修改理由成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();

                                    }
                                })
                        .show();


            }


        });

        ((ImageView)findViewById(R.id.addreason)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //添加理由
                final EditText textp = new EditText(reason_setting.this);
                new AlertDialog.Builder(reason_setting.this)
                        .setTitle("添加理由")
                        .setView(textp)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (textp.getText().toString().isEmpty()) {
                                    Toast.makeText(reason_setting.this, "添加理由失败:理由不能为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    String newreasonsp = "";
                                    for (int i = 0; i < reasonsArray.length; i++) {
                                        if (i != position) {
                                            newreasonsp += reasonsArray[i] + " ";
                                        }
                                    }
                                    newreasonsp += textp.getText().toString() + " ";
                                    reasonsArray = newreasonsp.split(" ");
                                    mAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_view_item, R.id.list_item, reasonsArray);
                                    mListView.setAdapter(mAdapter);
                                    Toast.makeText(reason_setting.this, "添加理由成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


            }
        });


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
                new AlertDialogios(reason_setting.this).builder()
                        .setTitle("警告")
                        .setMsg("直接返回将放弃所有未完成的修改 :(")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reason_setting.this.finish();
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

        ((Button) findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成按下，保存
                String newreasons = "";
                for (int i = 0; i < reasonsArray.length; i++) {
                    newreasons += reasonsArray[i] + " ";
                }
                SharedPreferences.Editor sharedata2 = sharedata.edit();
                sharedata2.putString("reasons", newreasons);
                sharedata2.apply();

                reason_setting.this.finish();
            }
        });
    }


    private int position=-1;

    private int maxFontSize=18;

}