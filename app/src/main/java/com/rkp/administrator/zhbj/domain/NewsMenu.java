package com.rkp.administrator.zhbj.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class NewsMenu implements Serializable {
    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;

    // 侧边栏菜单对象
    public class NewsMenuData implements Serializable{
        public int id;
        public String title;
        public int type;

        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData [title=" + title + ", children=" + children
                    + "]";
        }
    }

    // 页签的对象
    public class NewsTabData implements Serializable{
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData [title=" + title + "]";
        }

    }

    @Override
    public String toString() {
        return "NewsMenu [data=" + data + "]";
    }
}
