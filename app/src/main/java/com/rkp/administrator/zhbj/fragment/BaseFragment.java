package com.rkp.administrator.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/2 0002.
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    //创建fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前fragment所依赖的activity
        mActivity = getActivity();
    }
    //初始化fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=initView();
        return view;
    }
    //fragment 所依赖的activity的onCreate方法执行结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
    }

    //初始化布局，由子类实现
    public abstract View initView() ;
    public abstract void initData();
}
