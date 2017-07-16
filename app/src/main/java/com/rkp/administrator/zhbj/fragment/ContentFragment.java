package com.rkp.administrator.zhbj.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rkp.administrator.zhbj.MainActivity;
import com.rkp.administrator.zhbj.R;
import com.rkp.administrator.zhbj.base.BasePager;
import com.rkp.administrator.zhbj.base.impl.GovAffairsPager;
import com.rkp.administrator.zhbj.base.impl.HomePager;
import com.rkp.administrator.zhbj.base.impl.NewsCenterPager;
import com.rkp.administrator.zhbj.base.impl.SettingPager;
import com.rkp.administrator.zhbj.base.impl.SmartServicePager;
import com.rkp.administrator.zhbj.view.NoScrollViewPager;

import java.util.ArrayList;

public class ContentFragment extends BaseFragment {

    private NoScrollViewPager mViewPager;

    private ArrayList<BasePager> mPagers;
    private RadioGroup mRadioGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
            mPagers=new ArrayList<BasePager>();
            //添加5个标签页
            //mPagers.add(new HomePager(mActivity));
            mPagers.add(new NewsCenterPager(mActivity));
            mPagers.add(new SmartServicePager(mActivity));
            //mPagers.add(new GovAffairsPager(mActivity));
            mPagers.add(new SettingPager(mActivity));
            mViewPager.setAdapter(new ContenAdapter());
        //给mRadioGroup添加事件监听
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_serverce:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(2,false);
                        break;
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
                if(position==1||position==mPagers.size()-1){
                    // 首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                }else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动添加第一页数据
        mPagers.get(0).initData();
        setSlidingMenuEnable(true);
    }

    private void setSlidingMenuEnable(boolean b) {
           MainActivity mainActivity= (MainActivity) mActivity;
        SlidingMenu menu = mainActivity.getSlidingMenu();
        if(b){
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    class ContenAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;
            //pager.initData();();// 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
        }
    }
    // 获取新闻中心页面
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(0);
        return pager;
    }


}
