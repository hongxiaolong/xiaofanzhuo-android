package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.origamilabs.library.views.StaggeredGridView;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData.MenuData;

/** This will not work so great since the heights of the imageViews are
 * calculated on the iamgeLoader callback ruining the offsets. To fix this try
 * to get the (intrinsic) image width and height and set the views height
 * manually. I will look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz */
public class MenuMainActivity extends Activity {
	
	private ImageFetcher mImageFetcher;
	private String mExtraDatas;
	private BusinessData mDatas;
	private ArrayList<MenuData> mMenuDataList;
	
	private MenuAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hong_activity_main);
		
		Bundle extras = getIntent().getExtras();
		mExtraDatas = extras.getString("data");
		mDatas = new BusinessData(mExtraDatas);	
		mMenuDataList = new ArrayList<MenuData>();
		for (int i = 0; i < mDatas.getFoodList().size(); ++i)
			mMenuDataList.add(mDatas.getFoodList().get(i));
		
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setFastScrollEnabled(true);

		mAdapter = new MenuAdapter(MenuMainActivity.this, mImageFetcher);
		gridView.setAdapter(mAdapter);
		
		mAdapter.addItemLast(mMenuDataList);
		mAdapter.notifyDataSetChanged();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
