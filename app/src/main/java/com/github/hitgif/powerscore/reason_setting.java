package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class reason_setting extends Activity {

    private ListView mListView;
    private String[] reasonsArray;
    SharedPreferences spReader;
    SharedPreferences.Editor spEditor;

    private void updateList(){
        mListView.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.list_view_item, R.id.list_item, reasonsArray));
    }
    private void saveReasons(String newreasons) {
        spEditor.putString("reasons", newreasons);
        spEditor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reason_setting);
        mListView = (ListView) findViewById(R.id.lv_data);
        spReader= getSharedPreferences("data", Activity.MODE_PRIVATE);
        spEditor = spReader.edit();
        String reasons = spReader.getString("reasons", "上课发言 打扫卫生 小组加分 上午迟到 中午迟到 上课讲话 晚修讲话 随意下位 没有值日 ");
        reasonsArray = reasons.split(" ");
        mListView.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.list_view_item,R.id.list_item,reasonsArray));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int pos, long arg3) {

                new ActionSheetDialog(reason_setting.this).builder()
                        .setTitle("选择操作")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        String newReasons = "";
                                        for (int i = 0; i < reasonsArray.length; i++) {
                                            if (i == pos) continue;
                                            newReasons += reasonsArray[i] + " ";
                                        }
                                        reasonsArray = newReasons.split(" ");
                                        updateList();
                                        saveReasons(newReasons);
                                    }
                                })
                        .addSheetItem("修改", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //修改理由
                                        final EditText text = new EditText(reason_setting.this);
                                        text.setText(reasonsArray[pos]);
                                        new AlertDialog.Builder(reason_setting.this)
                                                .setTitle("修改理由")
                                                .setView(text)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (text.getText().toString().isEmpty()) {
                                                            Toast.makeText(reason_setting.this, "修改理由失败:理由不能为空", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            reasonsArray[pos] = text.getText().toString();
                                                            updateList();
                                                            String newReasons = "";
                                                            for (String aReasonsArray : reasonsArray) {
                                                                newReasons += aReasonsArray + " ";
                                                            }
                                                            saveReasons(newReasons);
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

        findViewById(R.id.addreason).setOnClickListener(new View.OnClickListener() {
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
                                    String newReasons = "";
                                    for (String aReasonsArray : reasonsArray) {
                                        newReasons += aReasonsArray + " ";
                                    }
                                    newReasons += textp.getText().toString() + " ";
                                    reasonsArray = newReasons.split(" ");
                                    updateList();
                                    saveReasons(newReasons);
                                    Toast.makeText(reason_setting.this, "添加理由成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


            }
        });
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
               reason_setting.this.finish();
           }
       });
    }
}