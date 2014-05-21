package com.qihoo.xiaofanzhuo.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.carrey.bitmapcachedemo.R;
import com.qihoo.xiaofanzhuo.restaurantdetailactivity.RestaurantDetailActivity;

/**
 * ZoneShowActivity
 * 
 * @author hongxiaolong
 * 
 */

public class ZoneShowActivity extends Activity {

	ImageButton imageButton1, imageButton2, imageButton3, imageButton4;
	private MyGlobalClass appGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hong_zone_show);

		appGlobal = (MyGlobalClass) getApplication();// 获得我们的应用程序MyApplication

		imageButton1 = (ImageButton) findViewById(R.id.img_dashanzi);
		imageButton2 = (ImageButton) findViewById(R.id.img_head);
		imageButton3 = (ImageButton) findViewById(R.id.img_798);
		imageButton4 = (ImageButton) findViewById(R.id.img_wangjing);

		ButtonClickEffect.setButtonFocusChanged(imageButton1);
		ButtonClickEffect.setButtonFocusChanged(imageButton2);
		ButtonClickEffect.setButtonFocusChanged(imageButton3);
		ButtonClickEffect.setButtonFocusChanged(imageButton4);

		imageButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZoneShowActivity.this,
						Activity_res.class);
				startActivity(intent);
				// finish();
			}
		});

		// personal information, login
		imageButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZoneShowActivity.this, LoginActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		imageButton3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZoneShowActivity.this,
						Activity_res.class);
				startActivity(intent);
				// finish();
			}
		});

		imageButton4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZoneShowActivity.this,
						Activity_res.class);
				startActivity(intent);
				// finish();
			}
		});
	}

}
