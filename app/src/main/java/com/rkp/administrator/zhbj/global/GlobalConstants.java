package com.rkp.administrator.zhbj.global;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class GlobalConstants  {
    public static final String KEJI_URL="https://baijia.baidu.com/listarticle?ajax=json&_limit=15&_skip=15&quality=1&_desc=top_st%2Cupdated_at";
    public static final String[] ALL_URL={"https://baijia.baidu.com/listarticle?ajax=json&cat=1&_limit=15&_skip=%d",
                                             "https://baijia.baidu.com/listarticle?ajax=json&cat=2&_limit=15&_skip=%d",
                                             "https://baijia.baidu.com/listarticle?ajax=json&cat=3&_limit=15&_skip=%d",
                                             "https://baijia.baidu.com/listarticle?ajax=json&cat=4&_limit=15&_skip=%d",
                                             "https://baijia.baidu.com/listarticle?ajax=json&cat=5&_limit=15&_skip=%d"};
    public static String getUrl(String url,int num){
            return  String.format(url,num);
    }
}
