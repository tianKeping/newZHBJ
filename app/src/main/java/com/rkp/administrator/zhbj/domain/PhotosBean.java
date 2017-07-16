package com.rkp.administrator.zhbj.domain;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 组图对象
 */
public class PhotosBean implements Serializable{

	public PhotosData data;

	public class PhotosData {
		public ArrayList<PhotoNews> news;
	}

	public class PhotoNews {
		public int id;
		public String listimage;
		public String title;
	}
}
