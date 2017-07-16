package com.rkp.administrator.zhbj.utils;

import com.rkp.administrator.zhbj.domain.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class JsonUtils {
    public static ArrayList<NewsBean> getAllNewsForNetWork(String result) {
        ArrayList<NewsBean> arrayList = new ArrayList<NewsBean>();
        if(result!=null){
            try{
                //2.解析获取的新闻数据到List集合中。
                JSONObject root_json = new JSONObject(result);//将一个字符串封装成一个json对象。
                JSONObject jsonData = root_json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("items");//获取root_json中的newss作为jsonArray对象
                for (int i = 0 ;i < jsonArray.length();i++){//循环遍历jsonArray
                    JSONObject news_json = jsonArray.getJSONObject(i);//获取一条新闻的json

                    NewsBean newsBean = new NewsBean();
                    newsBean. title = news_json.getString("title");
                    newsBean. news_url = news_json.getString("url");
                    newsBean. read_amount = news_json.getInt("read_amount");//评论数

                    String cover_images=news_json.getString("cover_images");
                    JSONArray jsonArray1 = new JSONArray(cover_images);
//                        for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject pictrue=	jsonArray1.getJSONObject(0);
                    newsBean. icon_url = pictrue.getString("src");
//						}
                    newsBean.publish_at=news_json.getString("publish_at");
                    newsBean.id=news_json.getString("id");
                    newsBean.writer_name=news_json.getString("writer_name");
                    arrayList.add(newsBean);
                }

                //3.清楚数据库中旧的数据，将新的数据缓存到数据库中
                //  new NewsDaoUtils(context).delete();
                // new NewsDaoUtils(context).saveNews(arrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arrayList;
        }
       return null;
    }
}
