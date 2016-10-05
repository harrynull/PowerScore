package com.github.hitgif.powerscore;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Null on 9/21/2016.
 */

public class AccessNetwork implements Runnable{
    private boolean isPost;
    private String url;
    private String params;
    private Handler h;

    public AccessNetwork(boolean isPost, String url, String params, Handler h) {
        this.isPost = isPost;
        this.url = url;
        this.params = params;
        this.h = h;
    }

    @Override
    public void run() {
        Message m = new Message();
        m.obj = isPost?GetPostUtil.sendPost(url, params):GetPostUtil.sendGet(url, params);
        h.sendMessage(m);
    }
}
