package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;

public class HorizontalListViewAdapter extends BaseAdapter{
	private LinkedList<String> mInfos;
	ImageFetcher mImageFetcher;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context, ImageFetcher f){
		mInfos = new LinkedList<String>();
		mImageFetcher = f;
		this.mContext = context;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
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

		String duitangInfo = mInfos.get(position);
		
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		holder.mTitle.setText("￥ " + position);
		mImageFetcher.loadImage(duitangInfo, holder.mImage);
//		iconBitmap = getPropThumnail(imageURL);
//		holder.mImage.setImageBitmap(iconBitmap);
//		holder.mImage.setImageBitmap(iconBitmap);

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
	}
	
//	private Bitmap getPropThumnail(	String url){
////		Drawable d = mContext.getResources().getDrawable(id);
////		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		Bitmap b = lruCache.getBitmap(url, lruCache.viewCallback);
////		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
//		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
//		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
//		
//		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
//		
//		return thumBitmap;
//	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
	
	public void addItemLast(LinkedList<String> datas) {
        mInfos.addAll(datas);
    }

    public void addItemTop(LinkedList<String> datas) {
        for (int i = 0; i < datas.size(); ++i) {
            mInfos.addFirst(datas.get(i));
        }
    }
}