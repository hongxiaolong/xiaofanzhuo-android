package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.io.IOException;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.xiaofanzhuo.mainactivity.ForTestUrlString;

public class ForTestMainActivity extends Activity {

	private ImageFetcher mImageFetcher;
	private ContentTask task = new ContentTask(this, 2);

	HorizontalListView hListView;
	HorizontalListViewAdapter hListViewAdapter;
	ImageView previewImg;
	View olderSelectView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fortest_horizontal_activity_main);
		initUI();
		AddItemToContainer(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initUI() {
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
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

	private void AddItemToContainer(int type) {
		if (task.getStatus() != Status.RUNNING) {
			String url = "hongxiaolong";
			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(this, type);
			task.execute(new LinkedList<String>());

		}
	}

	private class ContentTask extends
			AsyncTask<LinkedList<String>, Integer, LinkedList<String>> {

		private Context mContext;
		private int mType = 1;

		public ContentTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected LinkedList<String> doInBackground(
				LinkedList<String>... params) {

			try {
				return addNewUrlToContainer(params[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(LinkedList<String> result) {
			if (mType == 1) {

				hListViewAdapter.addItemTop(result);
				hListViewAdapter.notifyDataSetChanged();

			} else if (mType == 2) {
				hListViewAdapter.addItemLast(result);
				hListViewAdapter.notifyDataSetChanged();

			}

		}

		@Override
		protected void onPreExecute() {
		}

		public LinkedList<String> addNewUrlToContainer(LinkedList<String> url)
				throws IOException {
			LinkedList<String> duitangs = new LinkedList<String>();
			for (int i = 0; i < ForTestUrlString.imageUrlString.length; ++i)
				duitangs.add(ForTestUrlString.imageUrlString[i]);
			return duitangs;
		}
	}

}
