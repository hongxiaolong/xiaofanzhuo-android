package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.List;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData.MenuData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuAdapter  extends BaseAdapter {
	
	private static final String TAG = "MenuAdapter";
	
	private Context mContext;
    private List<MenuData> mInfos;
    private ImageFetcher mImageFetcher;

    public MenuAdapter(Context context, ImageFetcher f) {
        mInfos = new ArrayList<MenuData>();
        mImageFetcher = f;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        String infoUrl= mInfos.get(position).getFoodImgUrl().toString();
		String infoName = mInfos.get(position).getFood().toString();
		String infoPrice = mInfos.get(position).getFoodPrice().toString();

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.hong_infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.    getTag();

        
//        float iHeight = ((float) 200 / 183 * duitangInfo.getHeight());
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		(int) 150));

        holder.contentView.setText(infoName + " " + infoPrice);
        mImageFetcher.loadImage(infoUrl, holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
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
