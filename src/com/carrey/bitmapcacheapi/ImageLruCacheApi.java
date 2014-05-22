package com.carrey.bitmapcacheapi;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * LruCache and DiskLruCache
 * 
 * @author hongxiaolong
 * 
 */
public class ImageLruCacheApi {

	private static final String TAG = "ImageLruCacheApi";

	// 图片服务器ip地址
	private String webServerStr;

	// 图片url列表
	private List<String> imageUrlList;

	public ThreadPoolManager poolManager;

	// 下载任务队列 Map的key代表要下载的图片url，后面的List队列包含所有请求这张图片的回调
	public HashMap<String, ArrayList<SoftReference<BitmapCallback>>> mCallbacks = new HashMap<String, ArrayList<SoftReference<BitmapCallback>>>();
	public HashMap<String, ArrayList<SoftReference<BitmapViewCallback>>> mViewCallbacks = new HashMap<String, ArrayList<SoftReference<BitmapViewCallback>>>();
	// LruCache
	private static final int MEM_MAX_SIZE = 10 * 1024 * 1024;// MEM 4MB
	private LruCache<String, Bitmap> mMemoryCache = null;

	// DiskLruCache
	private static final int DISK_MAX_SIZE = 32 * 1024 * 1024;// SD 32MB
	private DiskLruCache mDiskCacke = null;

	// 构造函数
	public ImageLruCacheApi(String webServerStr) {
		this.webServerStr = webServerStr;
		this.imageUrlList = new ArrayList<String>();
	}

	public ImageLruCacheApi() {
		this.imageUrlList = new ArrayList<String>();
	}

	public String getWebServerAddress() {
		return webServerStr;
	}

	public List<String> getUrlList() {
		return this.imageUrlList;
	}

	// 获取url列表中对应位置的url
	public String getImageUrl(int position) {
		return this.imageUrlList.get(position);
	}

	public void LruCacheInit() {
		poolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_LIFO, 5);
		// 内存缓存
		mMemoryCache = new LruCache<String, Bitmap>(MEM_MAX_SIZE) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// return value.getRowBytes() * value.getHeight();
				return value.getByteCount();
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// 不要在这里强制回收oldValue，因为从LruCache清掉的对象可能在屏幕上显示着，
				// 这样就会出现空白现象
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};

		// SD卡缓存
		File cacheDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "cacheDir");
		mDiskCacke = DiskLruCache.openCache(cacheDir, DISK_MAX_SIZE);

	}

	/*
	 * 自定义控件显示图片：Customer
	 */
	public Bitmap getBitmap(String url, BitmapCallback callback) {
		if (url == null) {
			return null;
		}

		synchronized (mMemoryCache) {
			Bitmap bitmap = mMemoryCache.get(url);
			if (bitmap != null && !bitmap.isRecycled()) {
				Log.i(TAG, "get bitmap from mem: url = " + url);
				return bitmap;
			}
		}

		// 内存中没有，异步回调
		if (callback != null) {
			ArrayList<SoftReference<BitmapCallback>> callbacks = null;
			synchronized (mCallbacks) {
				if ((callbacks = mCallbacks.get(url)) != null) {
					if (!callbacks.contains(callback)) {
						callbacks.add(new SoftReference<BitmapCallback>(
								callback));
					}
					return null;
				} else {
					callbacks = new ArrayList<SoftReference<BitmapCallback>>();
					callbacks.add(new SoftReference<BitmapCallback>(callback));
					mCallbacks.put(url, callbacks);
				}
			}

			poolManager.start();
			poolManager.addAsyncTask(new MyThreadPoolTask(url, mDiskCacke,
					mTaskCallback));
		}
		return null;
	}

	/*
	 * 2014-05-21
	 */
	private String uuid = null;

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getUUID() {
		return this.uuid;
	}

	// ListView、GridView重载图片的回调函数
	public BitmapViewCallback viewCallback = new BitmapViewCallback() {

		@Override
		public void onReady(String key, Bitmap bitmap) {
			// TODO Auto-generated method stub
			if (bitmap != null && key != null
					&& ImageLruCacheApi.this.uuid != null) {
				if (key.equals(ImageLruCacheApi.this.uuid)) {
					// postInvalidate();
					Log.i(TAG, "BitmapViewCallback");
				}
			}
		}

	};

	/*
	 * ListView、GridView显示图片 调用时需要在Adapter里分别调用setUUID()，getBitmap(arg1,
	 * ImageLruCacheApiClass.viewCallback)
	 */
	public Bitmap getBitmap(String url, BitmapViewCallback callback) {
		if (url == null) {
			return null;
		}

		synchronized (mMemoryCache) {
			Bitmap bitmap = mMemoryCache.get(url);
			if (bitmap != null && !bitmap.isRecycled()) {
				Log.i(TAG, "get bitmap from mem: url = " + url);
				return bitmap;
			}
		}

		// 内存中没有，异步回调
		if (callback != null) {
			ArrayList<SoftReference<BitmapViewCallback>> callbacks = null;
			synchronized (mViewCallbacks) {
				if ((callbacks = mViewCallbacks.get(url)) != null) {
					if (!callbacks.contains(callback)) {
						callbacks.add(new SoftReference<BitmapViewCallback>(
								callback));
					}
					return null;
				} else {
					callbacks = new ArrayList<SoftReference<BitmapViewCallback>>();
					callbacks.add(new SoftReference<BitmapViewCallback>(
							callback));
					mViewCallbacks.put(url, callbacks);
				}
			}

			poolManager.start();
			poolManager.addAsyncTask(new MyThreadPoolTask(url, mDiskCacke,
					mViewTaskCallback));
		}
		return null;
	}

	private BitmapCallback mTaskCallback = new BitmapCallback() {

		@Override
		public void onReady(String key, Bitmap bitmap) {
			Log.i(TAG, "task done callback url = " + key);

			ArrayList<SoftReference<BitmapCallback>> callbacks = null;
			synchronized (mCallbacks) {
				if ((callbacks = mCallbacks.get(key)) != null) {
					mCallbacks.remove(key);
				}
			}

			if (bitmap != null) {
				synchronized (mDiskCacke) {
					if (!mDiskCacke.containsKey(key)) {
						Log.i(TAG, "put bitmap to SD url = " + key);
						mDiskCacke.put(key, bitmap);
					}
				}
				synchronized (mMemoryCache) {
					Bitmap bmp = mMemoryCache.get(key);
					if (bmp == null || bmp.isRecycled()) {
						mMemoryCache.put(key, bitmap);
					}
				}
			}

			// 调用请求这张图片的回调
			if (callbacks != null) {
				for (int i = 0; i < callbacks.size(); i++) {
					SoftReference<BitmapCallback> ref = callbacks.get(i);
					BitmapCallback cal = ref.get();
					if (cal != null) {
						cal.onReady(key, bitmap);
					}
				}
			}
		}
	};

	private BitmapViewCallback mViewTaskCallback = new BitmapViewCallback() {

		@Override
		public void onReady(String key, Bitmap bitmap) {
			Log.i(TAG, "task done callback url = " + key);

			ArrayList<SoftReference<BitmapViewCallback>> callbacks = null;
			synchronized (mViewCallbacks) {
				if ((callbacks = mViewCallbacks.get(key)) != null) {
					mViewCallbacks.remove(key);
				}
			}

			if (bitmap != null) {
				synchronized (mDiskCacke) {
					if (!mDiskCacke.containsKey(key)) {
						Log.i(TAG, "put bitmap to SD url = " + key);
						mDiskCacke.put(key, bitmap);
					}
				}
				synchronized (mMemoryCache) {
					Bitmap bmp = mMemoryCache.get(key);
					if (bmp == null || bmp.isRecycled()) {
						mMemoryCache.put(key, bitmap);
					}
				}
			}

			// 调用请求这张图片的回调
			if (callbacks != null) {
				for (int i = 0; i < callbacks.size(); i++) {
					SoftReference<BitmapViewCallback> ref = callbacks.get(i);
					BitmapViewCallback cal = ref.get();
					if (cal != null) {
						cal.onReady(key, bitmap);
					}
				}
			}
		}
	};
}
