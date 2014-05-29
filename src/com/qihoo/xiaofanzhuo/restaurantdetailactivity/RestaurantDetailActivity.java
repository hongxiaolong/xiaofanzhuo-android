package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrey.bitmapcachedemo.R;
import com.carrey.customview.customview.CustomView;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData.MenuData;
import com.qihoo.xiaofanzhuo.mainactivity.Activity_res;
import com.qihoo.xiaofanzhuo.mainactivity.BaseActivity;
import com.qihoo.xiaofanzhuo.mainactivity.MyApplication;
import com.qihoo.xiaofanzhuo.mainactivity.WindowsConstant;

/**
 * RestaurantDetailActivity
 * 
 * @author hongxiaolong
 * 
 */
public class RestaurantDetailActivity extends BaseActivity implements
		OnTouchListener, android.view.GestureDetector.OnGestureListener {

	private static final String TAG = "RestaurantDetailActivity";

	private ImageFetcher mImageFetcher;
	private String mExtraDatas;
	private BusinessData mDatas;
	private ArrayList<MenuData> mMenuDataList;

	private GestureDetector mGestureDetector;
	private int verticalMinistance = 20; // 水平最小识别距离
	private int minVelocity = 10; // 最小识别速度

	private LayoutInflater inflater;

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
	private ImageButton buttonOrder;

	private MyApplication appGlobal;
	private PreferencesService mPreference;

	private HorizontalListView hListView;
	private HorizontalListView hListView_Spec;
	private HorizontalListViewAdapter hListViewAdapter;
	private HorizontalListViewAdapter hListViewAdapter_Spec;

	private ImageView shopCart;// 购物车
	private ViewGroup anim_mask_layout;// 动画层
	private ImageView buyImg;// 这是在界面上跑的小图片
	private int buyNum = 0;// 购买数量
	private BadgeView buyNumView;// 显示购买数量的控件
	private ImageView basketImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hong_detail_layout);

		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		basketImg = (ImageView) findViewById (R.id.res_shopping_img_cart);

		// 手势监听
		mGestureDetector = new GestureDetector(RestaurantDetailActivity.this);// (OnGestureListener)
		// 获得当前接受操作的布局id
		RelativeLayout mActivity = (RelativeLayout) findViewById(R.id.detail_relativelayout);
		// 触屏监听
		mActivity.setOnTouchListener(this);
		mActivity.setLongClickable(true);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		WindowsConstant.displayWidth = displayMetrics.widthPixels;
		WindowsConstant.displayHeight = displayMetrics.heightPixels;

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		appGlobal = (MyApplication) getApplication(); // 获取应用程序

		Bundle extras = getIntent().getExtras();
		mExtraDatas = extras.getString("data");
		mDatas = new BusinessData(mExtraDatas);

		textString[0] = mDatas.getShopName();
		textString[1] = mDatas.getShopAverPrice();
		textString[2] = mDatas.getShopTag();
		textString[3] = mDatas.getShopSite();
		textString[4] = mDatas.getPhoneNum();

		mMenuDataList = new ArrayList<MenuData>();
		for (int i = 0; i < mDatas.getFoodList().size(); ++i)
			mMenuDataList.add(mDatas.getFoodList().get(i));

		textViewInit();
		buttonInit();
		horizontalListViewAdapterInit();

		hListViewAdapter.addItemLast(mMenuDataList);
		hListViewAdapter.notifyDataSetChanged();

		hListViewAdapter_Spec.addItemLast(mMenuDataList);
		hListViewAdapter_Spec.notifyDataSetChanged();

		shopCartInit();
		
		mPreference = new PreferencesService(RestaurantDetailActivity.this, "MenuOrder-" + mDatas.getShopName());
		if (!mPreference.isKeyContains("name"))
		{
			mPreference.saveToPerferences(mDatas.getShopName(), mDatas.getShopImgUrl(), "0");
			Log.i(TAG, "首次写入SharedPreferences---" + "MenuOrder-" + mDatas.getShopName() + "!!!!!!!!!!!!");
		}
		if (0 == buyNum)
		{
			mPreference.datasFromPreferencesService();
			Log.i(TAG, "MenuOrder-" + mDatas.getShopName() + ": " + mPreference.getPerferencesByKey("name"));
			int arraySize = mPreference.getPerferencesByKey("name").size();
			buyNum = arraySize;
            buyNumView.setText(buyNum + "");
			buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			buyNumView.show();
		}
		basketImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				creatBasketDialog();
			}
		});
	}

	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	      // return true;//返回真表示返回键被屏蔽掉
	      creatDialog();// 创建弹出的Dialog
	    }
	    return super.onKeyDown(keyCode, event);
	  }

	  /**
	   * 弹出提示退出对话框
	   */
	  private void creatDialog() {
	    new AlertDialog.Builder(this)
	        .setMessage("确定退出app?")
	        .setPositiveButton("YES",
	            new DialogInterface.OnClickListener() {

	              @Override
	              public void onClick(DialogInterface dialog,
	                  int which) {
	                MyApplication.getInstance().exit();
	              }
	            })
	        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

	          @Override
	          public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	          }
	        }).show();
	  }
	  
	  /**
	   * 弹出提示清空对话框
	   */
	  private void creatBasketDialog() {
	    new AlertDialog.Builder(this)
	        .setMessage("亲，您确定要清空购物篮么?")
	        .setPositiveButton("YES",
	            new DialogInterface.OnClickListener() {
	              @Override
	              public void onClick(DialogInterface dialog,
	                  int which) {
	            	  if (0 == buyNum)
	            		  Toast.makeText(getApplicationContext(), "亲，购物篮是空的哦，请先点餐!!",
	 	  					     Toast.LENGTH_SHORT).show();
	            	  else {
			            	PreferencesService temp= new PreferencesService(RestaurantDetailActivity.this, "MenuOrder-" + mDatas.getShopName());
			            	temp.saveToPerferences(mDatas.getShopName(), mDatas.getShopImgUrl(), "0");
			  				buyNum = 0;
			  	            buyNumView.setText(buyNum + "");
			  				buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			  				buyNumView.show();
			  				Toast.makeText(getApplicationContext(), "亲，购物篮已清空，您可以尽情点餐!!",
			  					     Toast.LENGTH_SHORT).show();
	            	  }
	              }
	            })
	        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

	          @Override
	          public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	          }
	        }).show();
	  }
	
	
	public void horizontalListViewAdapterInit() {

		hListView = (HorizontalListView) findViewById(R.id.horizon_listview_recommend);
		hListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), mImageFetcher);
		hListView.setAdapter(hListViewAdapter);

		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hListViewAdapter.setSelectIndex(position);
				//会闪烁
				hListViewAdapter.notifyDataSetChanged();
			}

		});

		hListView_Spec = (HorizontalListView) findViewById(R.id.horizon_listview_spec);
		hListViewAdapter_Spec = new HorizontalListViewAdapter(
				getApplicationContext(), mImageFetcher);
		hListView_Spec.setAdapter(hListViewAdapter_Spec);

		hListView_Spec.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hListViewAdapter_Spec.setSelectIndex(position);
				//会闪烁
				hListViewAdapter_Spec.notifyDataSetChanged();
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
		textView01.setText("均价：" + textString[1]);
		textView02.setText("种类：" + textString[2]);
		textView03.setText("地址：" + textString[3]);
		textView04.setText("电话：" + textString[4]);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 当otherActivity中返回数据的时候，会响应此方法
		// requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
		if (requestCode == 1) 
		{
			if (0 != buyNum)
			{
				PreferencesService temp= new PreferencesService(RestaurantDetailActivity.this, "MenuOrder-" + mDatas.getShopName());
				temp.datasFromPreferencesService();
				Log.i(TAG, "onActivityResult    ------  MenuOrder-" + mDatas.getShopName() + ": " + temp.getPerferencesByKey("name"));
				int arraySize = temp.getPerferencesByKey("name").size();
				buyNum = arraySize;
	            buyNumView.setText(buyNum + "");
				buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				buyNumView.show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void buttonInit() {
		buttonBack = (ImageButton) findViewById(R.id.button_back);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		buttonOrder = (ImageButton) findViewById(R.id.button_order);
		buttonOrder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(RestaurantDetailActivity.this,
						MenuMainActivity.class);
				intent.putExtra("data", mExtraDatas);
				startActivityForResult(intent, 1);
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
		super.onDestroy();
	}

	static class ViewHolder {
		CustomView customView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.hong_menu_main, menu);
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
					MenuMainActivity.class);
			intent.putExtra("data", this.mExtraDatas);
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

	private void shopCartInit() {
		shopCart = (ImageView) findViewById(R.id.res_shopping_img_cart);
		buyNumView = new BadgeView(RestaurantDetailActivity.this, shopCart);
		buyNumView.setTextColor(Color.WHITE);
		buyNumView.setBackgroundColor(Color.RED);
		buyNumView.setTextSize(12);
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void setAnim(final View v, int[] start_location) {
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		// shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
		end_location[0] = 60;
		end_location[1] = 1710;

		// 计算位移
		int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
		int endY = end_location[1] - start_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				buyNum++;// 让购买数量加1
				buyNumView.setText(buyNum + "");//
				buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				buyNumView.show();
			}
		});

	}

	/*
	 * HorizontalListViewAdapter by hongxiaolong
	 */

	public class HorizontalListViewAdapter extends BaseAdapter {

		private static final String TAG = "HorizontalListViewAdapter";

		private List<MenuData> mInfos;

		private ImageFetcher mImageFetcher;
		private Context mContext;
		private LayoutInflater mInflater;
		private Bitmap iconBitmap;
		private int selectIndex = -1;

		public HorizontalListViewAdapter(Context context, ImageFetcher f) {
			mInfos = new ArrayList<MenuData>();
			mImageFetcher = f;
			this.mContext = context;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mInfos.size();
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

			final String infoUrl = mInfos.get(position).getFoodImgUrl()
					.toString();
			final String infoName = mInfos.get(position).getFood().toString();
			final String infoPrice = mInfos.get(position).getFoodPrice()
					.toString();

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.hong_horizontal_list_item, null);
				convertView.setClickable(true);

				holder.mImage = (ImageView) convertView
						.findViewById(R.id.img_list_item);
				holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(
						(int) mContext.getResources().getDimensionPixelOffset(
								R.dimen.thumnail_default_width), (int) mContext
								.getResources().getDimensionPixelSize(
										R.dimen.thumnail_default_height)));

				holder.mTitle = (TextView) convertView
						.findViewById(R.id.text_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == selectIndex) {
				convertView.setSelected(true);
			} else {
				convertView.setSelected(false);
			}

			holder.mTitle.setText(infoName + " ￥ " + infoPrice);
			mImageFetcher.loadImage(infoUrl.toString(), holder.mImage);
			Log.i(TAG, infoUrl);
			// iconBitmap = getPropThumnail(imageURL);
			// holder.mImage.setImageBitmap(iconBitmap);
			// holder.mImage.setImageBitmap(iconBitmap);

			holder.mImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
					v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
					buyImg = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
					buyImg.setImageResource(R.drawable.hong_sign);// 设置buyImg的图片
					setAnim(buyImg, start_location);// 开始执行动画
					
					mPreference.addToPerferences(infoName, infoUrl, infoPrice);        
					Log.i(TAG, "写入后，MenuOrder-" + mDatas.getShopName() + ": " + mPreference.getPerferences().get("name"));
				}
			});

			return convertView;
		}

		private class ViewHolder {
			private TextView mTitle;
			private ImageView mImage;
		}

		// private Bitmap getPropThumnail( String url){
		// // Drawable d = mContext.getResources().getDrawable(id);
		// // Bitmap b = BitmapUtil.drawableToBitmap(d);
		// // Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		// int w =
		// mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
		// int h =
		// mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
		//
		// Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		//
		// return thumBitmap;
		// }
		public void setSelectIndex(int i) {
			selectIndex = i;
		}

		public void addItemLast(List<MenuData> datas) {
			for (int i = 0; i < datas.size(); ++i)
				mInfos.add(datas.get(i));
		}

		public void addItemTop(List<MenuData> datas) {
			for (int i = 0; i < datas.size(); ++i) {
				mInfos.add(datas.get(i));
			}
		}
	}
}