package com.carrey.bitmapcacheapi;
import android.graphics.Bitmap;

public interface BitmapViewCallback {
	/*
	 * by hongxiaolong
	 * onReady：图片缓存时若内存中没有，异步回调  
	 * */
	public void onReady(String key, Bitmap bitmap);
	
}
