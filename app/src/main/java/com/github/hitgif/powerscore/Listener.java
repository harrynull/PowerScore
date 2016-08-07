package com.github.hitgif.powerscore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.github.hitgif.powerscore.speech.setting.IatSettings;
import com.github.hitgif.powerscore.speech.util.FucUtil;
import com.github.hitgif.powerscore.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;

public class Listener extends Activity implements View.OnClickListener {
    private static String TAG = Listener.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    public static ArrayList<Record> records = new ArrayList<Record>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String userwords;

    private ToastCommom toastCommom;

    private ListView lv;
    private String all_result;
    private String result_names;
    private String result_reason;
    private String result_mark;
    private boolean inputflag;
    private void updateList(){((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();}
    // 语记安装助手类
 //   ApkInstaller mInstaller;


    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listener);
        Bundle bundle = this.getIntent().getExtras();
        userwords = bundle.getString("key");
        toastCommom = ToastCommom.createToastConfig();
        initLayout();
        setListView();
        records.clear();
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(Listener.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(Listener.this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mResultText = ((EditText) findViewById(R.id.iat_text));

        boolean speakHelp = getSharedPreferences("data", 0).getBoolean("speakHelp", true);

        if(speakHelp)
            showHelp();

        findViewById(R.id.help_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleHelp();
            }

            });
        ((RelativeLayout) findViewById(R.id.backListen)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView10)).setTextColor(Color.parseColor("#7fffffff"));
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView10)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });
        findViewById(R.id.backListen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.this.finish();
                overridePendingTransition(R.anim.slide_in_froml, R.anim.slide_out_fromr);
            }
        });
      //  mInstaller = new ApkInstaller(Listener.this);
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.iat_recognize).setOnClickListener(Listener.this);
       // findViewById(R.id.iat_recognize_stream).setOnClickListener(Listener.this);
       // findViewById(R.id.iat_upload_contacts).setOnClickListener(Listener.this);
       // findViewById(R.id.iat_upload_userwords).setOnClickListener(Listener.this);
       // findViewById(R.id.iat_stop).setOnClickListener(Listener.this);
       // findViewById(R.id.iat_cancel).setOnClickListener(Listener.this);
       // findViewById(R.id.image_iat_set).setOnClickListener(Listener.this);
        // 选择云端or本地
       /* RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.iatRadioCloud:
                        mEngineType = SpeechConstant.TYPE_CLOUD;
                        findViewById(R.id.iat_upload_contacts).setEnabled(true);
                        findViewById(R.id.iat_upload_userwords).setEnabled(true);
                        break;
                    case R.id.iatRadioLocal:

                        break;
                    case R.id.iatRadioMix:

                        break;
                    default:
                        break;
                }
            }
        });*/
    }

    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognize:
                inputflag = true;
                // 上传用户词表
               // showTip(getString(R.string.text_upload_userwords));
                //String contents = FucUtil.readFile(Listener.this, "userwords","utf-8");
                mResultText.setText(userwords);
                // 指定引擎类型
                mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
                ret = mIat.updateLexicon("userword", userwords, mLexiconListener);
                if (ret != ErrorCode.SUCCESS)
                    //showTip("上传热词失败,错误码：" + ret);

                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(Listener.this, "iat_recognize");

               // mResultText.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));
                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败,错误码：" + ret);
                    } else {
                        showTip(getString(R.string.text_begin));
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                //showTip(error.toString());
            } else {
               // showTip(getString(R.string.text_upload_success));
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        all_result = resultBuffer.toString();
        DecimalFormat df = new DecimalFormat("###.0");
        if(inputflag) {
            if (all_result.contains("因为")) {
                result_names = all_result.substring(0, all_result.indexOf("因为"));

                if (all_result.contains("加")) {
                    result_reason = all_result.substring(all_result.indexOf("因为") + 2, all_result.lastIndexOf("加"));
                    result_mark = all_result.substring(all_result.lastIndexOf("加") + 1, all_result.lastIndexOf("分"));
                    if (changeToNum(result_mark) != 0) {
                        records.add(new Record(result_names, result_reason, Double.valueOf(df.format(changeToNum(result_mark))).doubleValue()));
                        updateList();
                    } else {
                        showTip("录入失败, 分数错误");
                    }
                } else if (all_result.contains("减")) {
                    result_reason = all_result.substring(all_result.indexOf("因为") + 2, all_result.lastIndexOf("减"));
                    result_mark = all_result.substring(all_result.lastIndexOf("减") + 1, all_result.lastIndexOf("分"));
                    if (changeToNum(result_mark) != 0) {
                        records.add(new Record(result_names, result_reason, Double.valueOf(df.format(-changeToNum(result_mark))).doubleValue()));
                        updateList();
                    } else {
                        showTip("录入失败, 分数错误");
                    }
                } else if (all_result.contains("扣")) {
                    result_reason = all_result.substring(all_result.indexOf("因为") + 2, all_result.lastIndexOf("扣"));
                    result_mark = all_result.substring(all_result.lastIndexOf("扣") + 1, all_result.lastIndexOf("分"));
                    if (changeToNum(result_mark) != 0) {
                        records.add(new Record(result_names, result_reason, Double.valueOf(df.format(-changeToNum(result_mark))).doubleValue()));
                        updateList();
                    } else {
                        showTip("录入失败, 分数错误");
                    }
                } else {
                    showTip("录入失败");
                }
            } else {
                showTip("录入失败");
            }
            inputflag = false;
        }



        //mResultText.setText(resultBuffer.toString());
        mResultText.setSelection(mResultText.length());
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    /**
     * 获取联系人监听器。
     */
    private ContactManager.ContactListener mContactListener = new ContactManager.ContactListener() {

        @Override
        public void onContactQueryFinish(final String contactInfos, boolean changeFlag) {
            // 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
            // 每当联系人发生变化，该接口都将会被回调，可通过ContactManager.destroy()销毁对象，解除回调。
            // if(changeFlag) {
            // 指定引擎类型
            runOnUiThread(new Runnable() {
                public void run() {
                    mResultText.setText(contactInfos);
                }
            });

            mIat.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
            mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            ret = mIat.updateLexicon("contact", contactInfos, mLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("上传联系人失败：" + ret);
            }
        }
    };

    private void showTip(final String msg) {
        toastCommom.ToastShow(Listener.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
    }

    private void showHelp()
    {
        final SharedPreferences.Editor sharedata2 = getSharedPreferences("data", 0).edit();
        final Boolean OnSpeakHelp = false;
        new AlertDialogios(Listener.this).builder()
                .setTitle("指南")
                .setMsg("轻按下方麦克风图标开始说话，录入完成后点按上方列表可修改相应记录项目，可录入多条记录后一并提交\n\n语音录入时请【务必】遵循以下句法：\n成员(如有多个成员请以【和】字隔开) + 【因为】理由 + 【加/减/扣】分数\n例如：\n张三和李四和王五 因为上课发言 加一分")
                .setPositiveButton("好的，不再提醒", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedata2.putBoolean("speakHelp", OnSpeakHelp);
                        sharedata2.apply();
                    }
                })
                .setNegativeButton("下次提醒", null).show();

    }
    private void showSingleHelp()
    {
        new AlertDialogios(Listener.this).builder()
                .setTitle("指南")
                .setMsg("轻按下方麦克风图标开始说话，录入完成后点按上方列表可修改相应记录项目，可录入多条记录后一并提交\n\n语音录入时请【务必】遵循以下句法：\n成员(如有多个成员请以【和】字隔开) + 【因为】理由 + 【加/减/扣】分数\n例如：\n张三和李四和王五 因为上课发言 加一分")
                .setNegativeButton("好的", null)
                .show();
    }
    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    private double changeToNum(String str)
    {
        str = str.replace("一","1");
        str = str.replace("二","2");
        str = str.replace("两","2");
        str = str.replace("三","3");
        str = str.replace("四","4");
        str = str.replace("五","5");
        str = str.replace("六","6");
        str = str.replace("七","7");
        str = str.replace("八","8");
        str = str.replace("九","9");
        str = str.replace("十","10");
        double res = 0;
        try {
            res  = Double.valueOf(str).doubleValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(Listener.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(Listener.this);
        super.onPause();
    }

    protected void setListView() {

        lv = (ListView) findViewById(R.id.listView_listener);

        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv = (ListView) findViewById(R.id.listView_listener);
        lv.setAdapter(new MyAdapter(this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int pos, long arg3) {

            }
        });
    }

    private ArrayList<HashMap<String, Object>> getData(){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i!=records.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemReason", records.get(i).Reason.replace("，", "").replace("？", "").replace("。", "").replace("！", ""));

            if (records.get(i).Mark > 0)
                map.put("ItemMark", "+" + String.valueOf(records.get(i).Mark));
            else
                map.put("ItemMark", String.valueOf(records.get(i).Mark));

            String s = records.get(i).Names;
            s = s.replace("，", "");
            s = s.replace("？", "");
            s = s.replace("。", "");
            s = s.replace("！", "");
            String[] members = s.split("和");
            String strMember="";
            for (String member : members) {
                strMember+=member+",";
            }
            map.put("ItemNames", strMember.substring(0,strMember.length()-1));
            //map.put("ItemNames", records.get(i).Names);
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
                convertView = mInflater.inflate(R.layout.item_for_listener, null);
                holder = new ViewHolder();
                holder.reason = (TextView) convertView.findViewById(R.id.reason);
                holder.names = (TextView) convertView.findViewById(R.id.names);
                holder.mark = (TextView) convertView.findViewById(R.id.mark);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            String r=(String)(getData().get(position).get("ItemReason"));
            if(r.length()>11) r=r.substring(0,11)+"…";
            holder.reason.setText(r);

            String s=(String)(getData().get(position).get("ItemNames"));
            if(s.length()>35) s=s.substring(0,35)+"…";
            holder.names.setText(s);

            holder.mark.setText((String)(getData().get(position).get("ItemMark")));
            return convertView;
        }



    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView reason;
        public TextView names;
        public TextView mark;
    }
}
