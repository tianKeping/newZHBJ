package com.rkp.administrator.zhbj.application;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;
import com.mob.MobApplication;

import org.xutils.x;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class MyApplication extends MobApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtils.init(this);
        x.Ext.init(this);
    }
}
