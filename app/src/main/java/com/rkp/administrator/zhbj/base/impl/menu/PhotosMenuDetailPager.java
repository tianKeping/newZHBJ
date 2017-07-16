package com.rkp.administrator.zhbj.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rkp.administrator.zhbj.NewsDetailActivity;
import com.rkp.administrator.zhbj.R;
import com.rkp.administrator.zhbj.base.BaseMenuDetailPager;
import com.rkp.administrator.zhbj.domain.NewsBean;
import com.rkp.administrator.zhbj.domain.PhotosBean;
import com.rkp.administrator.zhbj.global.GlobalConstants;
import com.rkp.administrator.zhbj.utils.CacheUtils;
import com.rkp.administrator.zhbj.utils.JsonUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import static com.rkp.administrator.zhbj.global.GlobalConstants.ALL_URL;
import static com.rkp.administrator.zhbj.global.GlobalConstants.KEJI_URL;

/**
 * 菜单详情页-组图
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {
	@ViewInject(R.id.lv_photo)
	private ListView lvPhoto;
	@ViewInject(R.id.gv_photo)
	private GridView gvPhoto;
	private ArrayList<NewsBean> mNewsList;

	private ImageButton btnPhoto;
	private ImageOptions options;
	public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		btnPhoto.setOnClickListener(this);// 组图切换按钮设置点击事件
		this.btnPhoto=btnPhoto;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		x.view().inject(this,view);
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(KEJI_URL, mActivity);
		if(!TextUtils.isEmpty(cache)){
			processData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		RequestParams params = new RequestParams(KEJI_URL);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				processData(result);
				CacheUtils.setCache(ALL_URL[0],result,mActivity);
				System.out.println("请求的结果："+result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				ex.printStackTrace();
				Toast.makeText(mActivity, isOnCallback+"请求失败", Toast.LENGTH_SHORT).show();
				System.out.println(isOnCallback+"请求失败");
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});
	}

	private void processData(String result) {
//		Gson gson=new Gson();
//		PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
//		mNewsList = photosBean.data.news;
		mNewsList = JsonUtils.getAllNewsForNetWork(result);

		lvPhoto.setAdapter(new PhotoAdapter());
		gvPhoto.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致,
												// 所以可以共用一个adapter
		gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intentNext(position);
			}
		});
		lvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intentNext(position);
			}
		});
	}

	private void intentNext(int position) {
		NewsBean news = mNewsList.get(position);
		// 跳到新闻详情页面
		Intent intent = new Intent(mActivity, NewsDetailActivity.class);
		intent.putExtra("url", news.news_url);
		mActivity.startActivity(intent);
	}


	class PhotoAdapter extends BaseAdapter{

		public PhotoAdapter(){
			options= new ImageOptions.Builder()
					.setLoadingDrawableId(R.drawable.pic_item_list_default)
					.setFailureDrawableId(R.drawable.pic_item_list_default)
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
			if(convertView==null){
				convertView=View.inflate(mActivity,R.layout.list_item_photos,null);
				holder=new ViewHolder();
				holder.ivPic= (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else{
				holder= (ViewHolder) convertView.getTag();
			}
			NewsBean item = getItem(position);

			holder.tvTitle.setText(item.title);

			x.image().bind(holder.ivPic,item.icon_url);
			return convertView;
		}

	}
	static class ViewHolder{
		public ImageView ivPic;
		public TextView tvTitle;
	}
	private boolean isListView = true;// 标记当前是否是listview展示

	@Override
	public void onClick(View v) {
		if (isListView) {
			// 切成gridview
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

			isListView = false;
		} else {
			// 切成listview
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);

			isListView = true;
		}
	}
}
