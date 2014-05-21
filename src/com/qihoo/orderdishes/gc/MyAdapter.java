package com.qihoo.orderdishes.gc;

import java.util.ArrayList;
import java.util.HashMap;





import com.carrey.bitmapcachedemo.R;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements SectionIndexer {
	private DishInfos dishes = null;
	private DishInfos selectedDishes;
	private Context mContext;
	private static HashMap<Integer, Boolean> isSelected;
	private ViewHolder viewHolder = null;
	private OnClickListener listener;
	public MyAdapter(Context mContext, final DishInfos dishes, OnClickListener listener2) {
		this.mContext = mContext;
		this.dishes = dishes;
		listener = listener2;
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < dishes.dishInfoMap.size(); i++) {
			getIsSelected().put(i, false);
		}
	
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	
	public DishInfos getSelectedDishes(){
		this.selectedDishes = new DishInfos();
		for(int i=0;i<this.dishes.dishInfoMap.size();i++){
			if(this.getIsSelected().get(i)){
				this.selectedDishes.dishInfoMap.add(this.dishes.dishInfoMap.get(i));
			}
		}
		return this.selectedDishes;
	}
	
	final static class ViewHolder{
		ImageView imageView;
		TextView textView;
		CheckBox checkBox;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.dishes.dishInfoMap.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dishes.dishInfoMap.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		final HashMap<String,Object> mContent = dishes.dishInfoMap.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.gc_dish_item, null);
			viewHolder.textView = (TextView)view.findViewById(R.id.dish_info_name_price);
			viewHolder.imageView = (ImageView)view.findViewById(R.id.dish_image);
			viewHolder.checkBox = (CheckBox)view.findViewById(R.id.dish_checkbox);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
			
		ArrayList<HashMap<String,Object>> dishInfoMap0 = this.dishes.dishInfoMap;
		Object imagePathId = (Object) dishInfoMap0.get(position).get("image");
		String nameAndPrice = (String) dishInfoMap0.get(position).get("text");
		ImageView image = (ImageView)view.findViewById(R.id.dish_image);
		image.setImageResource((Integer) imagePathId);
		TextView text = (TextView)view.findViewById(R.id.dish_info_name_price);
		text.setText(nameAndPrice);
		viewHolder.checkBox.setChecked(getIsSelected().get(position));
		viewHolder.checkBox.setOnClickListener(listener);
		viewHolder.checkBox.setTag(position);

		return view;
		
	}




	@Override
	public int getPositionForSection(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}


}
