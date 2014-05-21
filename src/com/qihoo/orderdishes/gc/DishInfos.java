package com.qihoo.orderdishes.gc;

import java.util.ArrayList;
import java.util.HashMap;

import com.carrey.bitmapcachedemo.R;



public class DishInfos  {
	//主要获取得到所有菜品的图片以及价格等信息，无论是后期从网络获取还是从本地获取；
	ArrayList<HashMap<String,Object>> dishInfoMap = new ArrayList<HashMap<String,Object>>();
	//图，图对应的菜名，菜对应的价格

	private String[] arrText ;
	private int[] arrImage;
	/*--------------------mock-----------------------*/
	public DishInfos(){
	}
	
	void InitDishes(){//mock,后期数据考虑获取的扩展性，真实代码的时候，请求数据库，相应的获取图片以及信息
		//这里需要一次性吧所有的图片以及信息多线程的拉下来！所以这里需要写多线程的代码去拉数据，等这个界面写完了我再写；
		arrText = new String[] {
				"鱼香肉丝 ￥15元","红烧牛肉 ￥12元","干炒河粉 ￥2元","炒青菜 ￥10元","红烧猪肉 ￥12元","巫山烤鱼 ￥17元","香酥鸡 ￥7元","外婆烤肉 ￥6元","烤全鸭 ￥22元","大葱卷大饼 ￥1元"
		};//到时在处理了
		arrImage = new int[] {
				R.drawable.mock_gc_1,R.drawable.mock_gc_2,R.drawable.mock_gc_3,R.drawable.mock_gc_4,R.drawable.mock_gc_5,R.drawable.mock_gc_6,R.drawable.mock_gc_7,R.drawable.mock_gc_8,R.drawable.mock_gc_9,R.drawable.mock_gc_10
		};
		for(int i=0;i<10;++i){
			HashMap<String,Object> hashMap = new HashMap<String,Object>();
			hashMap.put("image", arrImage[i]);
			hashMap.put("text", arrText[i]);
			dishInfoMap.add(hashMap);
		}
	}

	
}
