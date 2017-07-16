package com.rkp.administrator.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rkp.administrator.zhbj.NewsDetailActivity;
import com.rkp.administrator.zhbj.R;
import com.rkp.administrator.zhbj.base.BaseMenuDetailPager;
import com.rkp.administrator.zhbj.domain.NewsBean;
import com.rkp.administrator.zhbj.domain.NewsMenu;
import com.rkp.administrator.zhbj.domain.NewsTabBean;
import com.rkp.administrator.zhbj.global.GlobalConstants;
import com.rkp.administrator.zhbj.utils.CacheUtils;
import com.rkp.administrator.zhbj.utils.JsonUtils;
import com.rkp.administrator.zhbj.utils.PrefUtils;
import com.rkp.administrator.zhbj.view.PullToRefreshListView;
import com.rkp.administrator.zhbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.rkp.administrator.zhbj.global.GlobalConstants.getUrl;

/**
 * 页签页面对象
 *
 */
public class TabDetailPager extends BaseMenuDetailPager {


	@ViewInject(R.id.lv_list)
	private PullToRefreshListView  lvList;

	private String mUrl;
	private String mMoreUrl;
	private String first_url;
	private int number=0;
	//private ArrayList<NewsTabBean.TopNews> mTopNews;
	private ArrayList<NewsBean> mNewsList;
	//private String mMoreUrl;// 下一页数据链接
	private NewsAdapter mNewsAdapter;

	private Handler mHandler;

	public TabDetailPager(Activity activity,String url) {
		super(activity);

		//mUrl = getUrl(url, number);
		first_url=url;

	}

	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		x.view().inject(this, view);

		//前端界面设置回调
		lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				//刷新数据
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				// 判断是否有下一页数据
				if (number != 112) {
					// 有下一页
					number+=16;
					getMoreDataFromServer(number);

				} else {
					// 没有下一页
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT)
							.show();
					System.out.println("没有更多数据了");
					// 没有数据时也要收起控件
					lvList.onRefreshComplete(true);
				}
			}
		});
		lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int headerViewsCount = lvList.getHeaderViewsCount();// 获取头布局数量
				position = position - headerViewsCount;// 需要减去头布局的占位
				System.out.println("第" + position + "个被点击了");

				NewsBean news = mNewsList.get(position);

				// read_ids: 1101,1102,1105,1203,
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");

				if (!readIds.contains(news.id + "")) {// 只有不包含当前id,才追加,
					// 避免重复添加同一个id
					readIds = readIds + news.id + ",";// 1101,1102,
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}

				// 要将被点击的item的文字颜色改为灰色, 局部刷新, view对象就是当前被点击的对象
				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);
				// mNewsAdapter.notifyDataSetChanged();//全局刷新, 浪费性能

				// 跳到新闻详情页面
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", news.news_url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void initData() {
		mUrl=getUrl(first_url, number);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache,false);
		}else {
			getDataFromServer();
		}

	}

	private void getDataFromServer() {
		// 请求参数
		RequestParams params = new RequestParams(mUrl);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable error, boolean arg1) {
				error.printStackTrace();
				Toast.makeText(mActivity, arg1+"", Toast.LENGTH_SHORT).show();
				lvList.onRefreshComplete(arg1);
			}
			@Override
			public void onFinished() {
			}
			@Override
			public void onSuccess(String result) {
				processData(result,false);
				CacheUtils.setCache(mUrl, result, mActivity);
				//收起下拉刷新控件
				lvList.onRefreshComplete(true);
			}
		});
	}
	/**
	 * 加载下一页数据
	 */
	protected void getMoreDataFromServer(int number) {
		// 请求参数

		mMoreUrl=getUrl(first_url,number);
		RequestParams params = new RequestParams(mMoreUrl);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println("更多数据"+result);
				processData(result,true);

				// 收起下拉刷新控件
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				// 请求失败
				ex.printStackTrace();
				Toast.makeText(mActivity, "加载失败,请检查网络！", Toast.LENGTH_LONG).show();

				// 收起下拉刷新控件
				lvList.onRefreshComplete(false);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});
	}

	protected void processData(String result, boolean isMore) {
		if(!isMore){
			// 列表新闻
			mNewsList = JsonUtils.getAllNewsForNetWork(result);
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
		}else {
			//加载更多数据
			ArrayList<NewsBean> moreNews = JsonUtils.getAllNewsForNetWork(result);
			mNewsList.addAll(moreNews);// 将数据追加在原来的集合中
			//刷新listview
			mNewsAdapter.notifyDataSetChanged();
		}
	}


	class NewsAdapter extends BaseAdapter {

		private ImageOptions options;

		public NewsAdapter() {
			// 设置加载图片的参数
			options = new ImageOptions.Builder()
					// 下载中显示的图片
					.setLoadingDrawableId(R.drawable.news_pic_default)
					// 下载失败显示的图片
					.setFailureDrawableId(R.drawable.news_pic_default)
					// 得到ImageOptions对象
					.build();
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsBean getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				holder.tvWriter= (TextView) convertView.findViewById(R.id.item_writer_name);
				holder.tvReadNumber= (TextView) convertView.findViewById(R.id.item_read_amount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsBean news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.publish_at);
			holder.tvWriter.setText(news.writer_name);
			holder.tvReadNumber.setText("阅读量("+news.read_amount+")");
			// 根据本地记录来标记已读未读
			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(news.id + "")) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			//mBitmapUtils.display(holder.ivIcon, news.listimage);
			x.image().bind(holder.ivIcon, news.icon_url,options);

			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
		public TextView tvWriter;
		public TextView tvReadNumber;
	}

}
