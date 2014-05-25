package com.qihoo.flipview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.UI;
import com.carrey.bitmapcacheapi.ImageHelper;
import com.carrey.bitmapcacheapi.ImageLruCacheApi;
import com.carrey.bitmapcachedemo.R;
import com.qihoo.xiaofanzhuo.mainactivity.ForTestUrlString;

public class MenuAdapter extends BaseAdapter {

	private static final String TAG = "MenuAdapter";
	
	private ImageLruCacheApi lruCache;

	private LayoutInflater inflater;

	private int repeatCount = 1;

	private List<Travels.Data> travelData;

	public MenuAdapter(Context context) {
		lruCache = new ImageLruCacheApi();
		lruCache.LruCacheInit();
		for (int i = 0; i < ForTestUrlString.imageUrlString.length; ++i)
			lruCache.getUrlList().add(ForTestUrlString.imageUrlString[i]);
		inflater = LayoutInflater.from(context);
		travelData = new ArrayList<Travels.Data>(Travels.IMG_DESCRIPTIONS);
	}

	@Override
	public int getCount() {
		int count = lruCache.getUrlList().size() * repeatCount / 2;
		Log.i(TAG, "getCount = " + count);
		return count;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if (convertView == null) {
			layout = inflater.inflate(R.layout.complex1, null);
			AphidLog.d("created new view from adapter: %d", position);
		}
		
		String imageURL = ImageHelper.getImageUrlFromUrlList(
				lruCache.getUrlList(), position);
		Log.i(TAG, "postion = " + position + ", imageURL = " + imageURL);
		ImageView imageView = (ImageView) layout.findViewById(R.id.photo);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(
				(int) (WindowsConstant.displayWidth * 0.3f),
				(int) (WindowsConstant.displayHeight * 0.3f)));
		UI.<ImageView> findViewById(layout, R.id.photo)
				.setImageBitmap(
						lruCache.getBitmap(imageURL, lruCache.viewCallback));
		
		String imageURL1 = ImageHelper.getImageUrlFromUrlList(
				lruCache.getUrlList(), ++position);
		Log.i(TAG, "postion = " + position + ", imageURL = " + imageURL1);
		imageView = (ImageView) layout.findViewById(R.id.photo1);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(
				(int) (WindowsConstant.displayWidth * 0.5f + 0.5f),
				(int) (WindowsConstant.displayHeight * 0.5f + 0.5f)));
		UI.<ImageView> findViewById(layout, R.id.photo1)
		.setImageBitmap(
				lruCache.getBitmap(imageURL1, lruCache.viewCallback));

		// UI.<TextView> findViewById(layout, R.id.title).setText(
		// AphidLog.format("%d. %s", position, data.title));

//		int realPosition = position * 4;
//		if (realPosition < lruCache.getUrlList().size()) {

//			// 第一张图片：宽度100%，高度40%
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.WRAP_CONTENT,
//					(int) (WindowsConstant.displayHeight * 0.4f + 0.5f));
//
//			String imageURL = ImageHelper.getImageUrlFromUrlList(
//					lruCache.getUrlList(), realPosition);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict1)
//					.setLayoutParams(params);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict1)
//					.setImageBitmap(
//							lruCache.getBitmap(imageURL, lruCache.viewCallback));
//
//		}
//		if (++realPosition < lruCache.getUrlList().size()) {

//			//第二张图片： 宽度50%，高度60%
//			@SuppressWarnings("deprecation")
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.FILL_PARENT,
//					(int) (WindowsConstant.displayWidth * 0.5f + 0.25f),
//					(int) (WindowsConstant.displayHeight * 0.6f + 0.5f));
//
//			String imageURL = ImageHelper.getImageUrlFromUrlList(
//					lruCache.getUrlList(), realPosition);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict2)
//					.setLayoutParams(params);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict2)
//					.setImageBitmap(
//							lruCache.getBitmap(imageURL, lruCache.viewCallback));
//
//		}
//		if (++realPosition < lruCache.getUrlList().size()) {
//
//			// 第三张图片：宽度50%，高度60%
//			@SuppressWarnings("deprecation")
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.FILL_PARENT,
//					(int) (WindowsConstant.displayWidth * 0.5f + 0.25f),
//					(int) (WindowsConstant.displayHeight * 0.6f + 0.5f));
//
//			String imageURL = ImageHelper.getImageUrlFromUrlList(
//					lruCache.getUrlList(), realPosition);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict3)
//					.setLayoutParams(params);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict3)
//					.setImageBitmap(
//							lruCache.getBitmap(imageURL, lruCache.viewCallback));
//
//		}
//		if (++realPosition < lruCache.getUrlList().size()) {
//
//			// 第四张图片：宽度50%，高度60%
//			@SuppressWarnings("deprecation")
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.FILL_PARENT,
//					(int) (WindowsConstant.displayWidth * 0.5f + 0.25f),
//					(int) (WindowsConstant.displayHeight * 0.6f + 0.5f));
//
//			String imageURL = ImageHelper.getImageUrlFromUrlList(
//					lruCache.getUrlList(), realPosition);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict4)
//					.setLayoutParams(params);
//
//			UI.<ImageView> findViewById(layout, R.id.menu_pict4)
//					.setImageBitmap(
//							lruCache.getBitmap(imageURL, lruCache.viewCallback));

//		}

		return layout;
	}

	public void removeData(int index) {
		if (travelData.size() > 1) {
			travelData.remove(index);
		}
	}
}
