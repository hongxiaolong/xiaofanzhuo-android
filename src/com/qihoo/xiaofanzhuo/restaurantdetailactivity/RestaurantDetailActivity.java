package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrey.bitmapcacheapi.ImageLruCacheApi;
import com.carrey.bitmapcachedemo.R;
import com.carrey.customview.customview.CustomView;
import com.qihoo.orderdishes.gc.OrderDishesMainActivity;
import com.qihoo.xiaofanzhuo.mainactivity.Activity_res;
import com.qihoo.xiaofanzhuo.mainactivity.ForTestUrlString;
import com.qihoo.xiaofanzhuo.mainactivity.MyGlobalClass;

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

	private ImageButton buttonTakeOut;
	private ImageButton buttonPraise;
	private ImageButton buttonBusy;
	private ImageButton buttonBack;

	private MyGlobalClass appGlobal;

	private HorizontalListView hListView;
	private HorizontalListViewAdapter hListViewAdapter;

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
		appGlobal = (MyGlobalClass) getApplication(); // 获取应用程序

		lruCache = new ImageLruCacheApi("10.65.7.234");
		lruCache.LruCacheInit();
		for (int i = 0; i < ForTestUrlString.imageUrlString.length; ++i)
			lruCache.getUrlList().add(ForTestUrlString.imageUrlString[i]);

		textViewInit();
		buttonInit();
		activityInit();

	}

	public void activityInit() {
		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
//		hListViewAdapter = new HorizontalListViewAdapter(
//				getApplicationContext(), );
		hListView.setAdapter(hListViewAdapter);
		
		hListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();
			}
		});
	}

	public void textViewInit() {
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
	}

	public void buttonInit() {
		buttonBack = (ImageButton) findViewById(R.id.button_back);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		buttonTakeOut = (ImageButton) findViewById(R.id.btn_take_out);
		buttonPraise = (ImageButton) findViewById(R.id.btn_praise);
		buttonBusy = (ImageButton) findViewById(R.id.btn_busy);

		if (appGlobal.getBusyStatus())
			buttonBusy.setBackgroundResource(R.drawable.hong_busy);
		else
			buttonBusy.setBackgroundResource(R.drawable.hong_idle);
		if (appGlobal.getPraiseStatus())
			buttonPraise.setBackgroundResource(R.drawable.hong_praise);
		else
			buttonPraise.setBackgroundResource(R.drawable.hong_unpraise);

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
	}

	@Override
	protected void onDestroy() {
		lruCache.poolManager.stop();
		super.onDestroy();
	}

	static class ViewHolder {
		CustomView customView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.hong_menu_main, menu);
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

		} /*
		 * else if (e1.getY() - e2.getY() > 20 && Math.abs(velocityY) > 10) {
		 * Toast.makeText(RestaurantDetailActivity.this, "turn up",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * } else if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 10) {
		 * Toast.makeText(RestaurantDetailActivity.this, "turn down",
		 * Toast.LENGTH_SHORT).show(); }
		 */

		return false;
	}

	// 只要有触发就会调用次方法
	public boolean onDown(MotionEvent e) {
		// Toast.makeText(RestaurantDetailActivity.this, "onDown",
		// Toast.LENGTH_SHORT).show();
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// Toast.makeText(RestaurantDetailActivity.this, "onLongPress",
		// Toast.LENGTH_SHORT).show();

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