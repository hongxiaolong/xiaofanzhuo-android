package com.carrey.bitmapcacheapi;

import android.graphics.Bitmap;
import android.os.Process;
import android.util.Log;

/**
 * 任务单元，在内存缓存没有图片的情况下从sd卡或者网络中获得图片
 * 然后调用回调来进行下一步操作
 * @author carrey
 *
 */
public class MyThreadPoolTask extends ThreadPoolTask {

	private static final String TAG = "MyThreadPoolTask";
	
	private DiskLruCache mDiskLruCache;
	
	private BitmapCallback callback;
	private BitmapViewCallback viewback;
	
	public MyThreadPoolTask(String url, DiskLruCache mDiskLruCache, BitmapCallback callback) {
		super(url);
		this.mDiskLruCache = mDiskLruCache;
		this.callback = callback;
	}

	public MyThreadPoolTask(String url, DiskLruCache mDiskLruCache, BitmapViewCallback callback) {
		super(url);
		this.mDiskLruCache = mDiskLruCache;
		this.viewback = callback;
	}
	
	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
		Bitmap bitmap = null;
		
		synchronized (mDiskLruCache) {
			bitmap = mDiskLruCache.get(url);
		}
		
		if (bitmap == null) {
			Log.i(TAG, "bitmap from net, url = " + url);
			bitmap = ImageHelper.loadBitmapFromNet(url);
		} else {
			Log.i(TAG, "bitmap from SD, url = " + url);
		}
		
		if (callback != null) {
			callback.onReady(url, bitmap);
		}
		if (viewback != null) {
			viewback.onReady(url, bitmap);
		}
	}

}
