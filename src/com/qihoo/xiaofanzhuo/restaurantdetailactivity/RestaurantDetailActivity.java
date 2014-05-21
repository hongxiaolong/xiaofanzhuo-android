package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrey.bitmapcacheapi.ImageHelper;
import com.carrey.bitmapcacheapi.ImageLruCacheApi;
import com.carrey.bitmapcachedemo.R;
import com.carrey.customview.customview.CustomView;
import com.qihoo.orderdishes.gc.OrderDishesMainActivity;
import com.qihoo.xiaofanzhuo.mainactivity.Activity_res;
import com.qihoo.xiaofanzhuo.mainactivity.ForTestUrlString;
import com.qihoo.xiaofanzhuo.mainactivity.ButtonClickEffect;
import com.qihoo.xiaofanzhuo.mainactivity.MyGlobalClass;
import com.qihoo.xiaofanzhuo.mainactivity.ZoneShowActivity;

/**
 * RestaurantDetailActivity
 * 
 * @author hongxiaolong
 * 
 */
public class RestaurantDetailActivity extends Activity implements
		OnTouchListener, android.view.GestureDetector.OnGestureListener {

	private GestureDetector mGestureDetector;
	private int verticalMinistance = 20; // 水平最小识别距离
	private int minVelocity = 10; // 最小识别速度

	private LayoutInflater inflater;
	private ImageLruCacheApi lruCache = null;
	private TextView textView;
	private TextView textView01;
	private TextView textView02;
	private TextView textView03;
	private TextView textView04;
	// 店家展示信息
	private String textString[] = new String[5];
	// "大公鸡",
	// "人均：25",
	// "川菜",
	// "大山子北里301号",
	// "64736868"
	// };

	private ImageButton buttonQueue;
	private ImageButton buttonTakeOut;
	private ImageButton buttonPraise;
	private ImageButton buttonBusy;
	private ImageButton buttonBack;

	private GridView gridView;
	private DetailGridAdapter adapter;
	private MyGlobalClass appGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hong_detail_layout);

		// 手势监听
		mGestureDetector = new GestureDetector(RestaurantDetailActivity.this);// (OnGestureListener)
		// 获得当前接受操作的布局id
		LinearLayout mActivity = (LinearLayout) findViewById(R.id.detail_linerlayout);
		// 触屏监听
		mActivity.setOnTouchListener(this);
		mActivity.setLongClickable(true);

		Bundle extras = getIntent().getExtras();
		textString[0] = extras.getString("name");
		textString[1] = extras.getString("price");
		textString[2] = extras.getString("type");
		textString[3] = extras.getString("location");
		textString[4] = extras.getString("phone");

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		lruCache = new ImageLruCacheApi("10.65.7.234");
		lruCache.LruCacheInit();
		for (int i = 0; i < ForTestUrlString.imageUrlString.length; ++i)
			lruCache.getUrlList().add(ForTestUrlString.imageUrlString[i]);

		appGlobal = (MyGlobalClass) getApplication(); // 获取应用程序

		// 设置自定义字体
		textView = (TextView) this.findViewById(R.id.detail_title);
		textView01 = (TextView) this.findViewById(R.id.textview01);
		textView02 = (TextView) this.findViewById(R.id.textview02);
		textView03 = (TextView) this.findViewById(R.id.textview03);
		textView04 = (TextView) this.findViewById(R.id.textview04);
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/huakangwawa.ttf");
		textView.setTypeface(typeFace);
		textView01.setTypeface(typeFace);
		textView02.setTypeface(typeFace);
		textView03.setTypeface(typeFace);
		textView04.setTypeface(typeFace);
		// 设置文字信息并显示
		textView.setText(textString[0]);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		textView01.setText(textString[1]);
		textView02.setText(textString[2]);
		textView03.setText(textString[3]);
		textView04.setText(textString[4]);

		buttonBack = (ImageButton) findViewById(R.id.button_back);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		buttonQueue = (ImageButton) findViewById(R.id.btn_queue);
		buttonTakeOut = (ImageButton) findViewById(R.id.btn_take_out);
		buttonPraise = (ImageButton) findViewById(R.id.btn_praise);
		buttonBusy = (ImageButton) findViewById(R.id.btn_busy);

		if (appGlobal.getQueueStatus())
			buttonQueue
					.setBackgroundResource(R.drawable.hong_img_queue_pressed);
		else
			buttonQueue.setBackgroundResource(R.drawable.hong_img_queue);
		if (appGlobal.getBusyStatus())
			buttonBusy.setBackgroundResource(R.drawable.hong_busy);
		else
			buttonBusy.setBackgroundResource(R.drawable.hong_idle);
		if (appGlobal.getPraiseStatus())
			buttonPraise.setBackgroundResource(R.drawable.hong_praise);
		else
			buttonPraise.setBackgroundResource(R.drawable.hong_unpraise);

		buttonQueue.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					appGlobal.setQueueStatus(true);
					// 重新设置按下时的背景图片
					buttonQueue
							.setBackgroundResource(R.drawable.hong_img_queue_pressed);
					Intent intent = new Intent();
					intent.setClass(RestaurantDetailActivity.this,
							QueueNumActivity.class);
					startActivity(intent);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// 再修改为抬起时的正常图片
					if (!appGlobal.getQueueStatus())
						buttonQueue
								.setBackgroundResource(R.drawable.hong_img_queue);
				}
				return false;
			}
		});

		buttonPraise.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					appGlobal.setPraiseStatus(true);
					// 重新设置按下时的背景图片
					buttonPraise.setBackgroundResource(R.drawable.hong_praise);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// 再修改为抬起时的正常图片
					if (!appGlobal.getPraiseStatus())
						buttonPraise
								.setBackgroundResource(R.drawable.hong_unpraise);
				}
				return false;
			}
		});

		buttonBusy.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					appGlobal.setBusyStatus(true);
					// 重新设置按下时的背景图片
					buttonBusy.setBackgroundResource(R.drawable.hong_busy);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// 再修改为抬起时的正常图片
					if (!appGlobal.getBusyStatus())
						buttonBusy.setBackgroundResource(R.drawable.hong_idle);
				}
				return false;
			}
		});

		gridView = (GridView) findViewById(R.id.pict_gridview);
		adapter = new DetailGridAdapter();
		gridView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		lruCache.poolManager.stop();
		super.onDestroy();
	}

	private class DetailGridAdapter extends BaseAdapter {
		private Bitmap mBackgroundBitmap;

		public DetailGridAdapter() {
			mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.item_bg);
		}

		@Override
		public int getCount() {
			return 12;
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
				holder.customView = (CustomView) convertView
						.findViewById(R.id.customview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.customView.position = position;
			holder.customView.setBackgroundBitmap(mBackgroundBitmap);

			holder.customView.setTitleText("第" + position + "道菜" + " ￥1" + position);
			holder.customView.setSubTitleText("价格: " + position);

			String imageURL = ImageHelper.getImageUrlFromUrlList(
					lruCache.getUrlList(), position);
			if (0 == holder.customView.position) {
				holder.customView.setTitleText("推荐菜");
				imageURL = "http://m3.img.srcdd.com/farm4/d/2014/0519/16/463B5122527864E949ECFED82E85E33C_B1280_1280_540_524.png";
			}
			if (4 == holder.customView.position) {
				holder.customView.setTitleText("特价菜");
				imageURL = "http://m1.img.srcdd.com/farm4/d/2014/0519/16/DCE523105C7000D1793EB907827C5674_B1280_1280_540_524.png";
			}
			if (8 == holder.customView.position) {
				holder.customView.setTitleText("套餐");
				imageURL = "http://m3.img.srcdd.com/farm4/d/2014/0519/16/9E7EAF8A00C079CB86DD11AA5A8F164A_B1280_1280_540_524.png";
			}
			holder.customView.setUUID(imageURL);
			holder.customView.setImageBitmap(lruCache.getBitmap(imageURL,
					holder.customView.getBitmapCallback()));
			return convertView;
		}

	}

	static class ViewHolder {
		CustomView customView;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		// 大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法
		if (e1.getX() - e2.getX() > verticalMinistance
				&& Math.abs(velocityX) > minVelocity) {
			// 在此处实现跳转
			Intent intent = new Intent(RestaurantDetailActivity.this,
					OrderDishesMainActivity.class);
			startActivity(intent);

			Toast.makeText(RestaurantDetailActivity.this, "左滑菜单",
					Toast.LENGTH_SHORT).show();

		} else if (e2.getX() - e1.getX() > verticalMinistance
				&& Math.abs(velocityX) > minVelocity) {	
			// 在此处实现跳转
						Intent intent = new Intent(RestaurantDetailActivity.this,
								Activity_res.class);
						startActivity(intent);
			Toast.makeText(RestaurantDetailActivity.this, "右滑返回",
					Toast.LENGTH_SHORT).show();

		} /* else if (e1.getY() - e2.getY() > 20 && Math.abs(velocityY) > 10) {
			Toast.makeText(RestaurantDetailActivity.this, "turn up",
					Toast.LENGTH_SHORT).show();

		} else if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 10) {
			Toast.makeText(RestaurantDetailActivity.this, "turn down",
					Toast.LENGTH_SHORT).show();
		} */

		return false;
	}

	// 只要有触发就会调用次方法
	public boolean onDown(MotionEvent e) {
//		Toast.makeText(RestaurantDetailActivity.this, "onDown",
//				Toast.LENGTH_SHORT).show();
		return false;
	}

	public void onLongPress(MotionEvent e) {
//		Toast.makeText(RestaurantDetailActivity.this, "onLongPress",
//				Toast.LENGTH_SHORT).show();

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		// Toast.makeText(RestaurantDetailActivity.this, "onScroll",
		// Toast.LENGTH_SHORT).show();
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// Toast.makeText(RestaurantDetailActivity.this, "onShowPress",
		// Toast.LENGTH_SHORT).show();
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// Toast.makeText(RestaurantDetailActivity.this, "onsingleTapup",
		// Toast.LENGTH_SHORT).show();
		return false;
	}
}