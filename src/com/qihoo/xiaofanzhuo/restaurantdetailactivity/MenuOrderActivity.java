package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carrey.bitmapcachedemo.R;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.qihoo.orderdishes.gc.MenuActivity;
import com.qihoo.xiaofanzhuo.datafromserver.BusinessData;
import com.qihoo.xiaofanzhuo.mainactivity.BaseActivity;
import com.qihoo.xiaofanzhuo.mainactivity.MyApplication;

public class MenuOrderActivity extends BaseActivity{

	private static final String TAG = "MenuOrderActivity";
	
	private ImageFetcher mImageFetcher;
	private BusinessData mDatas;
	private String mExtraDatas;
	private ListView mListView;
	private Map<String, List<String>> mGroupDatas;
	private MyAdapter mListAdapter;
	private ImageButton buttonBack;
	private ImageButton buttonOrder;
	private TextView textView;
	
	private PreferencesService mPreference;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hong_menu_order_main);
        mListView=(ListView)findViewById(R.id.menu_order_listview);
        
        mImageFetcher = new ImageFetcher(this, 75);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		
		Bundle extras = getIntent().getExtras();
		mExtraDatas = extras.getString("data");
		mDatas = new BusinessData(mExtraDatas);	
		
		mGroupDatas = new HashMap<String, List<String>>();
		mPreference = new PreferencesService(MenuOrderActivity.this, "MenuOrder-" + mDatas.getShopName());
		mGroupDatas.put("name", mPreference.getPerferencesByKey("name"));
		mGroupDatas.put("url", mPreference.getPerferencesByKey("url"));
		mGroupDatas.put("price", mPreference.getPerferencesByKey("price"));
		
		mListAdapter = new MyAdapter(this, mImageFetcher);
		mListAdapter.addItemLast(mGroupDatas);
		mListView.setAdapter(mListAdapter);
		mListAdapter.notifyDataSetChanged();
		
		buttonInit();

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

	  private void buttonInit()
		{
			textView = (TextView) this.findViewById(R.id.order_detail_title);
			Typeface typeFace = Typeface.createFromAsset(getAssets(),
					"fonts/huakangwawa.ttf");
			textView.setTypeface(typeFace);
			textView.setText(mDatas.getShopName());
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			
			buttonBack = (ImageButton) findViewById(R.id.order_button_back);
			buttonBack.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					MenuOrderActivity.this.setResult(1, intent); 
					MenuOrderActivity.this.finish();
				}
			});
			
			buttonOrder = (ImageButton) findViewById(R.id.order_button_order);
			buttonOrder.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(MenuOrderActivity.this,
							MenuActivity.class);
					startActivity(intent);
				}
			});
		}
	  
	  public class MyAdapter extends BaseAdapter {
			private Map<String, List<String>> mAdapterDatas;
			private Context mContext;
			private LayoutInflater inflater = null;//用于导入文件布局
			private ImageFetcher mImageFetcher;

			public MyAdapter(Context context, ImageFetcher f) {//构造器
				mContext = context;
				mImageFetcher = f;
				mAdapterDatas = new HashMap<String, List<String>>();
				inflater = LayoutInflater.from(this.mContext);			
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mAdapterDatas.get("url").size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mAdapterDatas.get("url").get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup patent) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();//获取ViewHolder对象
					convertView = inflater.inflate(R.layout.hong_menu_order_item, null);//导入布局，赋值给convertView
					holder.imageView = (ImageView)convertView.findViewById(R.id.order_image_view);		
					holder.textTitle = (TextView) convertView.findViewById(R.id.menu_name);
					holder.textPrice = (TextView) convertView.findViewById(R.id.menu_price);
					holder.buttonAdd = (Button) convertView.findViewById(R.id.button_add);
					holder.buttonAdd = (Button) convertView.findViewById(R.id.button_delete);
					
					convertView.setTag(holder);//为view设置标签
				} else {
					holder = (ViewHolder) convertView.getTag();//取出holder
				}
				
				Typeface typeFace = Typeface.createFromAsset(getAssets(),
						"fonts/huakangwawa.ttf");
				holder.textTitle.setTypeface(typeFace);
				holder.textPrice.setTypeface(typeFace);
				holder.textAmount.setTypeface(typeFace);
				
				Log.i(TAG, mAdapterDatas.get("url").get(position));
				Log.i(TAG, mAdapterDatas.get("name").get(position));
				Log.i(TAG, mAdapterDatas.get("price").get(position));
				mImageFetcher.loadImage(mAdapterDatas.get("url").get(position), holder.imageView);
				holder.textTitle.setText(mAdapterDatas.get("name").get(position));
				holder.textPrice.setText(mAdapterDatas.get("price").get(position));
				
				final int amount = 5;
				holder.textAmount.setText(String.valueOf(amount));
				
				holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Log.i(TAG, "buttonAdd!!!!!!");
					}
				});
				holder.buttonDel.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Log.i(TAG, "buttonDel!!!!!!");
					}
				});
				
				return convertView;
			}

			class ViewHolder {
				ImageView imageView;
				TextView textTitle;
				TextView textPrice;
				Button buttonAdd;
				Button buttonDel;
				TextView textAmount;
			}
			
			public void addItemLast(Map<String, List<String>> datas) {
				mAdapterDatas.put("name", datas.get("name"));
				mAdapterDatas.put("url", datas.get("url"));
				mAdapterDatas.put("price", datas.get("price"));
			}
		}
	 
	  
}