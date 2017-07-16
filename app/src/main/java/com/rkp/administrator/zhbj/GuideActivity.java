package com.rkp.administrator.zhbj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rkp.administrator.zhbj.utils.DensityUtils;
import com.rkp.administrator.zhbj.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    Context mContext;
    private ArrayList<ImageView> mImageViewArrayList;//ImageView集合
    private ImageView ivRedPoint;// 小红点
    //引导页图片id
    private int[] mImageIds=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private LinearLayout llContainer;
    private int mPointDis;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        //找到控件
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint= (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setVisibility(View.INVISIBLE);

        //设置数据
        initData();
        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 计算小红点当前的左边距
                int leftMargin= (int) ((mPointDis*positionOffset)+position*mPointDis);
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                // 修改左边距
                params.leftMargin=leftMargin;
                // 重新设置布局参数
                ivRedPoint.setLayoutParams(params);
                // 更新小红点距离
            }

            @Override
            public void onPageSelected(int position) {
                if(position==mImageViewArrayList.size()-1){
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除监听，只计算一次
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
                System.out.println("圆点距离:" + mPointDis);
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp
                PrefUtils.setBoolean(mContext,"is_first_enter",false);
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    //初始化数据
    private void initData(){
        mImageViewArrayList=new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewArrayList.add(imageView);
            //初始化小圆点
          ImageView point= new ImageView(mContext);
            //设置原点图片shape
            point.setImageResource(R.drawable.shape_point_gray);
            //初始化布局参数LinearLayout.LayoutParams父控件是谁就设置谁
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i>0){
                params.leftMargin= DensityUtils.dip2px(10,this);
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }
    }
    class  GuideAdapter extends PagerAdapter{
        //item的个数
        @Override
        public int getCount() {
            return mImageViewArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        //初始化iten布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewArrayList.get(position);
            container.addView(view);
            return view;
        }
        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
