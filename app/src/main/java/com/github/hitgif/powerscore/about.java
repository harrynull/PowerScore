package com.github.hitgif.powerscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 123 on 03/27/2016.
 */
public class about extends Activity{
    private String localVersion;
    private final String TAG = this.getClass().getName();
    private final int Update_NONEED = 0;
    private final int Update_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int DOWN_ERROR = 4;
    private UpdateInfo info;
    private ToastCommom toastCommom;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Util.setTranslucent(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        toastCommom = ToastCommom.createToastConfig();
        try {
            localVersion = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView)findViewById(R.id.version)).setText("v"+localVersion);
        ((RelativeLayout)findViewById(R.id.check_version)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    findViewById(R.id.checking).setVisibility(View.VISIBLE);
                    localVersion = getVersionName();
                    CheckVersionTask cv = new CheckVersionTask();
                    new Thread(cv).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.backabout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.this.finish();
                overridePendingTransition(R.anim.slide_in_froml, R.anim.slide_out_fromr);
            }
        });
    }
    private void showToast(String msg) {
        toastCommom.ToastShow(about.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
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
                    findViewById(R.id.checking).setVisibility(View.GONE);
                    break;
                case Update_CLIENT:
                    //对话框通知用户升级程序
                    showUpdateDialog();
                    findViewById(R.id.checking).setVisibility(View.GONE);
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    showToast("获取更新失败 :( 请检查网络");
                    findViewById(R.id.checking).setVisibility(View.GONE);
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
