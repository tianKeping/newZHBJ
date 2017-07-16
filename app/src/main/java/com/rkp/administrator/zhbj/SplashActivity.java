package com.rkp.administrator.zhbj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.rkp.administrator.zhbj.utils.PrefUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rlroot;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        rlroot = (RelativeLayout) findViewById(R.id.activity_splash);
        //0到360度旋转
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);//时间
        rotateAnimation.setFillAfter(true);//保持动画结束状态
        //缩放动画
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //动画集合
        AnimationSet set=new AnimationSet(true);
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        //启动动画
        rlroot.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {



            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画跳转
                //第一次进入跳新手指导
                //否则跳主页面
                Intent intent;
                boolean is_first_enter = PrefUtils.getBoolean(mContext, "is_first_enter", true);
                if(is_first_enter){
                    //第一次进入跳新手指导
                    intent = new Intent(mContext,GuideActivity.class);
                }else {
                     intent=new Intent(mContext,MainActivity.class);
                }
                startActivity(intent);
                finish();//结束当前页面
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
