package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.origamilabs.library.views.StaggeredGridView;
import com.qihoo.orderdishes.gc.MenuActivity;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData.MenuData;

/*
 * MenuMainActivity
 * by hongxiaolong
 */
public class MenuMainActivity extends Activity {
	
	private static final String TAG = "MenuMainActivity";
	
	private ImageFetcher mImageFetcher;
	private String mExtraDatas;
	private BusinessData mDatas;
	private ArrayList<MenuData> mMenuDataList;
	
	private ImageButton buttonBack;
	private ImageButton buttonOrder;
	private TextView textView;
	private MenuAdapter mAdapter;
	
	private ImageView shopCart;//购物车
	private ViewGroup anim_mask_layout;//动画层
	private ImageView buyImg;// 这是在界面上跑的小图片
	private int buyNum = 0;//购买数量
	private BadgeView buyNumView;//显示购买数量的控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hong_menu_main);
		
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

		buttonInit();
		shopCartInit();
	}

	private void buttonInit()
	{
		textView = (TextView) this.findViewById(R.id.menu_detail_title);
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/huakangwawa.ttf");
		textView.setTypeface(typeFace);
		textView.setText(mDatas.getShopName());
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		
		buttonBack = (ImageButton) findViewById(R.id.menu_button_back);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		buttonOrder = (ImageButton) findViewById(R.id.menu_button_order);
		buttonOrder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuMainActivity.this,
						MenuActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void shopCartInit() {
		shopCart = (ImageView) findViewById(R.id.shopping_img_cart);
		buyNumView = new BadgeView(MenuMainActivity.this, shopCart);
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
		anim_mask_layout.addView(v);//把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
		Log.i(TAG, "end_location" + " X = " + end_location[0] + "Y = " + end_location[1]);

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
				buyNum++;//让购买数量加1
				buyNumView.setText(buyNum + "");//
				buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				buyNumView.show();
			}
		});

	}
	
	public class MenuAdapter extends BaseAdapter {

		private static final String TAG = "MenuAdapter";

		private Context mContext;
		private List<MenuData> mInfos;
		private ImageFetcher mImageFetcher;

		public MenuAdapter(Context context, ImageFetcher f) {
			mInfos = new ArrayList<MenuData>();
			mContext = context;
			mImageFetcher = f;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			final String infoUrl = mInfos.get(position).getFoodImgUrl().toString();
			final String infoName = mInfos.get(position).getFood().toString();
			final String infoPrice = mInfos.get(position).getFoodPrice().toString();

			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(parent
						.getContext());
				convertView = layoutInflator
						.inflate(R.layout.hong_infos_list, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.news_pic);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.pict_checkbox);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.news_title);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();

			// float iHeight = ((float) 200 / 183 * duitangInfo.getHeight());
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, (int) 150));

			holder.contentView.setText(infoName + " " + infoPrice);
			mImageFetcher.loadImage(infoUrl, holder.imageView);

			final CheckBox checkBox = holder.checkBox;
			holder.imageView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
					v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
					buyImg = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
					buyImg.setImageResource(R.drawable.hong_sign);// 设置buyImg的图片
					setAnim(buyImg, start_location);// 开始执行动画
					
					SharedPreferences mySharedPreferences = mContext.getSharedPreferences(
							"MenuOrder", 
							mContext.CONTEXT_IGNORE_SECURITY);
					SharedPreferences.Editor editor = mySharedPreferences.edit();
	                String url = mySharedPreferences.getString("Url", "");
	                String name = mySharedPreferences.getString("Name", "");
	                String price = mySharedPreferences.getString("Price", "");
	                
	                Log.i(TAG, "读出SharedPreferences:" + name + " " + price + " " + url);
	                
	                url = url + "_" + infoUrl;
	                name = name + "_" + infoName;
	                price = price + "_" + infoPrice;
	                
					editor.putString("Url", url);
					editor.putString("Name", name);
					editor.putString("Price", price);
					editor.commit();

					Log.i(TAG, "写入SharedPreferences:" + name + " " + price + " " + url);
				}

			});

			return convertView;
		}

		private class ViewHolder {
			ImageView imageView;
			CheckBox checkBox;
			TextView contentView;
			TextView timeView;
		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
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
