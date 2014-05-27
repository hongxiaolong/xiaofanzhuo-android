package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.xiaofanzhuo.datafromserver.HttpToServer;

public class ForTestMainActivity extends Activity {

	private static final String TAG = "ForTestMainActivity";
	
	private ImageFetcher mImageFetcher;
	private HttpToServer mHttpToServer;

	HorizontalListView hListView;
	HorizontalListViewAdapter hListViewAdapter;
	ImageView previewImg;
	View olderSelectView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fortest_horizontal_activity_main);
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		
		initUI();
		
		
		try {
			
			mHttpToServer = new HttpToServer(this, "GetShopInfoByLocation____大山子");
			LinkedList<String> imageUrlList = new LinkedList<String>();
			
			for(int i = 0; i < mHttpToServer.getBusinessDataList().size(); ++i)
				imageUrlList.add((mHttpToServer.getBusinessDataList().get(i).getShopImgUrl()));
			
//			for (int i = 0; i < ForTestUrlString.imageUrlString.length; ++i)
//				imageUrlList.add(ForTestUrlString.imageUrlString[i]);

//				hListViewAdapter.addItemLast(imageUrlList);
//				hListViewAdapter.notifyDataSetChanged();
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initUI() {
		
		
		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		previewImg = (ImageView) findViewById(R.id.image_preview);
		String[] titles = { "怀师", "南怀瑾军校", "闭关", "南怀瑾", "南公庄严照", "怀师法相" };
		final int[] ids = { R.drawable.nanhuaijin_miss,
				R.drawable.nanhuaijin_school, R.drawable.nanhuaijin_biguan,
				R.drawable.nanhuaijin, R.drawable.nanhuaijin_zhuangyan,
				R.drawable.nanhuaijin_faxiang };
		hListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), mImageFetcher);
		hListView.setAdapter(hListViewAdapter);
		
		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				previewImg.setImageResource(ids[position]);
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();
			}
		});

	}
}
