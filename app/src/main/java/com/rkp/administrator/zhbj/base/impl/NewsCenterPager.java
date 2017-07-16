package com.rkp.administrator.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.rkp.administrator.zhbj.MainActivity;
import com.rkp.administrator.zhbj.base.BaseMenuDetailPager;
import com.rkp.administrator.zhbj.base.BasePager;
import com.rkp.administrator.zhbj.base.impl.menu.InteractMenuDetailPager;
import com.rkp.administrator.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.rkp.administrator.zhbj.base.impl.menu.PhotosMenuDetailPager;
import com.rkp.administrator.zhbj.base.impl.menu.TopicMenuDetailPager;
import com.rkp.administrator.zhbj.domain.NewsMenu;
import com.rkp.administrator.zhbj.fragment.LeftMenuFragment;
import com.rkp.administrator.zhbj.global.GlobalConstants;
import com.rkp.administrator.zhbj.utils.CacheUtils;


import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;



/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 菜单详情页集合
    private ArrayList<String> mTitleLeft;
    public NewsCenterPager(Activity activity) {
        super(activity);
        mTitleLeft= new ArrayList<String>();
        mTitleLeft.add("新闻");
        mTitleLeft.add("专题");
        mTitleLeft.add("组图");
        mTitleLeft.add("互动");
    }

    @Override
    public void initData() {
        System.out.println("新闻初始化啦...");
        //给帧布局填充对象
        processData();
    }
    private void processData() {

        //获取侧边栏对象
        MainActivity mainUI= (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        // 给侧边栏设置数据
        leftMenuFragment.setMenuData(mTitleLeft);

        // 初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        // 将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }
    // 设置菜单详情页
    public void setCurrentDetailPager(int position) {
        // 重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);// 获取当前应该显示的页面
        View view = pager.mRootView;// 当前页面的布局

        // 清除之前旧的布局
        flContent.removeAllViews();

        flContent.addView(view);// 给帧布局添加布局

        // 初始化页面数据
        pager.initData();

        // 更新标题
        tvTitle.setText(mTitleLeft.get(position));

        //如果是组图页面，需要显示切换按钮
        if(pager instanceof PhotosMenuDetailPager){
            btnPhoto.setVisibility(View.VISIBLE);
        }else {
            btnPhoto.setVisibility(View.GONE);
        }
    }
}
