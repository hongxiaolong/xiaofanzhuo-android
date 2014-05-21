package com.qihoo.xiaofanzhuo.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.carrey.bitmapcacheapi.ImageHelper;
import com.carrey.bitmapcacheapi.ImageLruCacheApi;
import com.carrey.bitmapcachedemo.R;
import com.carrey.customview.customview.CustomView;
/**
 * LruCache and DiskLruCache
 * @author hongxiaolong
 *
 */
public class ForTestMainActivity extends Activity {
	
	private LayoutInflater inflater;	
	private ImageLruCacheApi lruCache=null;
	
	private GridView gridView;
	private GridAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		// 隐藏android系统的状态栏  
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		 // 隐藏应用程序的标题栏，即当前activity的标题栏 
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		lruCache = new ImageLruCacheApi("10.65.7.234");
		lruCache.LruCacheInit();
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new GridAdapter();
		gridView.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		lruCache.poolManager.stop();
		super.onDestroy();
	}
	
	private class GridAdapter extends BaseAdapter {
		private Bitmap mBackgroundBitmap;
		
		public GridAdapter() {
			mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_bg);
		}
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item, null);
				holder.customView = (CustomView) convertView.findViewById(R.id.customview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.customView.position = position;
			holder.customView.setBackgroundBitmap(mBackgroundBitmap);
			
			holder.customView.setTitleText("主菜");
			holder.customView.setSubTitleText("价格: " + position);
			
			String imageURL = ImageHelper.getImageUrl(lruCache.getWebServerAddress(), position);
			holder.customView.setUUID(imageURL);
			holder.customView.setImageBitmap(lruCache.getBitmap(imageURL, holder.customView.getBitmapCallback()));
			return convertView;
		}
		
	}
	
	static class ViewHolder {
		CustomView customView;
	}
}
