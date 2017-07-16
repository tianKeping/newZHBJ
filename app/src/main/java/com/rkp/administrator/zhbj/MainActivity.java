package com.rkp.administrator.zhbj;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.rkp.administrator.zhbj.fragment.ContentFragment;
import com.rkp.administrator.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu=getSlidingMenu();
        //slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
       //slidingMenu.setBehindOffset(400);

        WindowManager wm=getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width*200/320);

        initFragment();
    }
    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        //拿到fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commit();
    }
    // 获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm
                .findFragmentByTag(TAG_LEFT_MENU);// 根据标记找到对应的fragment
        return fragment;
    }

    // 获取主页fragment对象
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm
                .findFragmentByTag(TAG_CONTENT);// 根据标记找到对应的fragment
        return fragment;
    }
}
