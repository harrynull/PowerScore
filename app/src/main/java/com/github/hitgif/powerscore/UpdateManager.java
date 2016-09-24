package com.github.hitgif.powerscore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Null on 9/23/2016.
 */
public class UpdateManager {
    private final String TAG = this.getClass().getName();
    private final int Update_NONEED = 0;
    private final int Update_CLIENT = 1;
    private final int GET_UPDATAINFO_ERROR = 2;
    private final int DOWN_ERROR = 4;
    private UpdateInfo info;
    private String localVersion;

    private Context context;

    public UpdateManager(Context context){this.context=context;}

    /////检查更新
    public void check() throws PackageManager.NameNotFoundException {
        localVersion = getVersionName();
        new Thread(new CheckVersionTask()).start();
    }

    private static class UpdateInfo {
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

    private static class UpdateInfoParser {
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

    private String getVersionName() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Update_NONEED:
                    Toast.makeText(context, "已是最新版本 :)", Toast.LENGTH_SHORT).show();
                    break;
                case Update_CLIENT:
                    //对话框通知用户升级程序
                    showUpdateDialog();
                    break;
                case GET_UPDATAINFO_ERROR:
                    //服务器超时
                    //showToast("获取更新失败 :( 请检查网络");
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(context, "下载新版本失败 :(", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private class CheckVersionTask implements Runnable {
        InputStream is;

        public void run() {

            try {
                String path = context.getResources().getString(R.string.url_server);
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
                msg.what = GET_UPDATAINFO_ERROR;
                updateHandler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }


    private void showUpdateDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
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

    //从服务器中下载APK
    private void downLoadApk() {
        final ProgressDialog pd; //进度条对话框
        pd = new ProgressDialog(context);
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
    private void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
