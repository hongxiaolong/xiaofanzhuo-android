package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import com.carrey.bitmapcachedemo.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class QueueNumActivity extends Activity {

	private TextView textViewQueue;
	private TextView textViewQueueNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hong_queue_num);
		// 设置自定义字体
		textViewQueue = (TextView) this.findViewById(R.id.textView_queue);
		textViewQueueNum=(TextView) this.findViewById(R.id.textView_queue_num);
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/huakangwawa.ttf");
		textViewQueue.setTypeface(typeFace);
		textViewQueueNum.setTypeface(typeFace);
	}

}
