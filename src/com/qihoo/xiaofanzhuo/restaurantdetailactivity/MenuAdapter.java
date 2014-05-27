package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData.MenuData;

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
				
				SharedPreferences mySharedPreferences = mContext.getSharedPreferences(
						"MenuOrder", 
						mContext.CONTEXT_IGNORE_SECURITY);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
                String url = mySharedPreferences.getString("Url", "");
                String name = mySharedPreferences.getString("Name", "");
                String price = mySharedPreferences.getString("Price", "");
                String amount = mySharedPreferences.getString("amount", "");
				editor.putString("Url", url + "_" + infoUrl);
				editor.putString("Name", name + "_" + infoName);
				editor.putString("Price", price + "_" + infoPrice);
				editor.putString("amount", amount + "_" + "1");
				editor.commit();
				
				// 使用toast信息提示框提示成功写入数据
				Log.i(TAG, "写入SharedPreferences:" + infoName + " " + infoPrice + " " + infoUrl);
			}

		});

		return convertView;
	}

	class ViewHolder {
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
