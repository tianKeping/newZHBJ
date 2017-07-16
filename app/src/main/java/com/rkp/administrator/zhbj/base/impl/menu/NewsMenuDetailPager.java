package com.rkp.administrator.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rkp.administrator.zhbj.MainActivity;
import com.rkp.administrator.zhbj.R;
import com.rkp.administrator.zhbj.base.BaseMenuDetailPager;
import com.rkp.administrator.zhbj.base.impl.menu.TabDetailPager;
import com.rkp.administrator.zhbj.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.rkp.administrator.zhbj.global.GlobalConstants.ALL_URL;
import static com.rkp.administrator.zhbj.global.GlobalConstants.getUrl;


/**
 * 菜单详情页-新闻
 *
 * ViewPagerIndicator使用流程: 1.引入库 2.解决support-v4冲突(让两个版本一致) 3.从例子程序中拷贝布局文件
 * 4.从例子程序中拷贝相关代码(指示器和viewpager绑定; 重写getPageTitle返回标题) 5.在清单文件中增加样式 6.背景修改为白色
 * 7.修改样式-背景样式&文字样式
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;

	//private ArrayList<NewsMenu.NewsTabData> mTabData;// 页签网络数据
	private ArrayList<String> mTitleTop;
	private ArrayList<TabDetailPager> mPagers;// 页签页面集合
	private ArrayList<String> mTitle;

	public NewsMenuDetailPager(Activity activity) {
		super(activity);
		mTitle=new ArrayList<String>();
		mTitle.add("科技");
		mTitle.add("娱乐");
		mTitle.add("财经");
		mTitle.add("体育");
		mTitle.add("文化");

	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,
				null);
		x.view().inject(this, view);
		return view;
	}
	int number=0;
	@Override
	public void initData() {
		// 初始化页签
		mPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTitle.size(); i++) {
			String url= ALL_URL[i];
			//String s = getUrl(url, number);
			TabDetailPager pager = new TabDetailPager(mActivity,url);
			mPagers.add(pager);
		}

		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);// 将viewpager和指示器绑定在一起.注意:必须在viewpager设置完数据之后再绑定

		// 设置页面滑动监听
		mIndicator.setOnPageChangeListener(this);// 此处必须给指示器设置页面监听,不能设置给viewpager
	}

	class NewsMenuDetailAdapter extends PagerAdapter {

		// 指定指示器的标题
		@Override
		public CharSequence getPageTitle(int position) {
			String titleTop =mTitle.get(position);
			return titleTop;
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);
			View view = pager.mRootView;
			container.addView(view);
			pager.initData();
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
							   int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		System.out.println("当前位置:" + position);
		if (position == 0) {
			// 开启侧边栏
			setSlidingMenuEnable(true);
		} else {
			// 禁用侧边栏
			setSlidingMenuEnable(false);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	/**
	 * 开启或禁用侧边栏
	 *
	 * @param enable
	 */
	protected void setSlidingMenuEnable(boolean enable) {
		// 获取侧边栏对象
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

}
