package com.qihoo.orderdishes.gc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.carrey.bitmapcachedemo.R;
import com.qihoo.orderdishes.gc.MyAdapter.ViewHolder;


public class OrderDishesMainActivity extends Activity implements OnClickListener {
	private DishInfos dishInfo;
	private GridView gridView0;
	private ViewPager mPager;//页卡内容
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private int screenHeigh;
    private Button titleButton;
    private Button bottomButton;
    private TextView titleText;
    private CheckBox checkBox;
    private MyAdapter myAdapter;
    private DishInfos infoContainers;
    private ArrayList<String> stringList = new ArrayList<String>(); 
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      	setContentView(R.layout.gc_order_dishes);
        //店铺页面点击点菜button，跳转到本页面，之前请求的时候把所有相关的信息已经获取到本地；此处从本地获取照片以及相关信息即可；
        titleButton = (Button)findViewById(R.id.orderdishes_title_button);
        bottomButton = (Button)findViewById(R.id.orderdishes_menu_button);
        titleText = (TextView)findViewById(R.id.orderdishes_title_text);
        
        gridView0 = (GridView)findViewById(R.id.orderdishes_gridview);            
        dishInfo = new DishInfos();
      	dishInfo.InitDishes();         
	    myAdapter = new MyAdapter(this,dishInfo, this);
	    gridView0.setAdapter(myAdapter);
	    
	    
	    bottomButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderDishesMainActivity.this, MenuActivity.class);	
			    infoContainers =  myAdapter.getSelectedDishes();
				ArrayList<HashMap<String,Object>> tmp = infoContainers.dishInfoMap;
				ArrayList<String> stringTmp = hashmapToString(tmp);
				if(stringTmp.size()>0){
					intent.putStringArrayListExtra("keys", stringTmp);
					startActivity(intent);
				}else{
					Toast.makeText(OrderDishesMainActivity.this, String.format("亲，要點菜啊！"),Toast.LENGTH_SHORT).show();
				}
			}	
	    });
	    
	    
	    titleButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//返回到上一個界面
				OrderDishesMainActivity.this.finish();//結束當前的activity，返回到前一個。
			}
	    	
	    	
	    });
	}	  
	

	ArrayList<String> hashmapToString(ArrayList<HashMap<String,Object>> tmp){
		stringList.clear();
		for(int i=0;i<tmp.size();++i){
			if(!tmp.get(i).isEmpty()&&tmp.get(i).get("image")!=null){
				String imagePath = tmp.get(i).get("image")+"";
				String text = (String) tmp.get(i).get("text");
				String strings = imagePath+":"+text;
				stringList.add(strings);
			}
		}
		return stringList;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.dish_checkbox){
			int pos = (Integer) v.getTag();
			myAdapter.getIsSelected().put(pos, !myAdapter.getIsSelected().get(pos));
		}
		
	}
}



