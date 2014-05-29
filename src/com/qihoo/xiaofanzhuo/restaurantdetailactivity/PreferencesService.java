package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {

	private Context mContext;
	private String mPreferences;
	private List<String> mPreferencesNames;
	private List<String> mPreferencesUrls;
	private List<String> mPreferencesPrices;
	Map<String, List<String>> mDuplicateMap;

	private static String mTag = "_--_";

	public PreferencesService(Context context, String preferences) {
		this.mContext = context;
		this.mPreferences = preferences;
		this.mPreferencesNames = new ArrayList<String>();
		this.mPreferencesUrls = new ArrayList<String>();
		this.mPreferencesPrices = new ArrayList<String>();
		this.mDuplicateMap = new HashMap<String, List<String>>();
	}

	/**
	 * 保存参数
	 * 
	 * @param name
	 *            菜品名称
	 * @param url
	 *            菜品图片
	 * @param price
	 *            菜品价格
	 */
	public void saveToPerferences(String name, String url, String price) {
		// 获得SharedPreferences对象
		SharedPreferences preferences = mContext.getSharedPreferences(
				mPreferences, mContext.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putString("url", url);
		editor.putString("price", price);
		editor.commit();
	}

	/**
	 * 检测参数是否存在
	 * 
	 * @param key
	 * @return Returns true if the preference exists in the preferences,
	 *         otherwise false.
	 */
	public boolean isKeyContains(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mPreferences, mContext.MODE_PRIVATE);
		return preferences.contains(key);

	}

	/**
	 * 追加参数，在函数中已经执行了getPerferences()，直接在后面追加即可
	 * 
	 * @param name
	 *            菜品名称
	 * @param url
	 *            菜品图片
	 * @param price
	 *            菜品价格
	 */
	public void addToPerferences(String name, String url, String price) {
		// 获得SharedPreferences对象
		SharedPreferences preferences = mContext.getSharedPreferences(
				mPreferences, mContext.MODE_PRIVATE);
		Map<String, String> params = getPerferences();
		Editor editor = preferences.edit();
		editor.putString("name", params.get("name") + mTag + name);
		editor.putString("url", params.get("url") + mTag + url);
		editor.putString("price", params.get("price") + mTag + price);
		editor.commit();
	}

	/**
	 * 获取各项参数
	 * 
	 * @return 参数存在Map<String, String>中
	 */
	public Map<String, String> getPerferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = mContext.getSharedPreferences(
				mPreferences, Context.MODE_PRIVATE);
		params.put("name", preferences.getString("name", ""));
		params.put("url", preferences.getString("url", ""));
		params.put("price", preferences.getString("price", ""));
		return params;
	}

	/**
	 * @param 存放参数的Map
	 *            <String, String>，需先调用getPerferences()
	 * @return void,
	 *         将Map中对应数据切割后存放于this.mPreferencesNames,this.mPreferencesUrls,
	 *         this.mPreferencesPrices
	 */
	public void getPerferencesDatas(Map<String, String> datas) {

		String params = datas.get("name");
		String[] ret = splitFromStringBySymbol(params, mTag);
		for (int i = 1; i < ret.length; ++i)
			this.mPreferencesNames.add(ret[i]);
		params = datas.get("url");
		ret = splitFromStringBySymbol(params, mTag);
		for (int i = 1; i < ret.length; ++i)
			this.mPreferencesUrls.add(ret[i]);
		params = datas.get("price");
		ret = splitFromStringBySymbol(params, mTag);
		for (int i = 1; i < ret.length; ++i)
			this.mPreferencesPrices.add(ret[i]);

	}

	public static String[] splitFromStringBySymbol(String orginString,
			String symbol) {
		return orginString.split(symbol);
	}

	/**
	 * @param 需要的key类型
	 * @return List<String>, 返回结果是最新的数据
	 */
	public List<String> getPerferencesByKey(String key) {
		if (key == "name")
			return this.mPreferencesNames;
		if (key == "url")
			return this.mPreferencesUrls;
		if (key == "price")
			return this.mPreferencesPrices;
		return null;
	}

	/**
	 * @描 述：去掉重复元素方法
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public static List removeDuplicateWithOrder(List list) {
		// 定义Set :元素不重复
		Set set = new HashSet();
		// 定义List 存放不重复的元素
		List newList = new ArrayList();
		// 得到list的迭代器
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			// 获得每一个元素
			Object element = iter.next();
			// 如果 set 中尚未存在指定的元素，则添加此元素,返回true
			// 如果此 set 已经包含该元素，则该调用不改变此 set 返回 false。
			if (set.add(element))
				// 添加元素
				newList.add(element);

		}
		return newList;

	}
	
	/**
	 * @描 述：去掉重复信息后，获取最终的数据集
	 * @return Map<String, List<String>>
	 */
	@SuppressWarnings("unchecked")
	private void getDuplicateGatherWithOrder(Map<String, List<String>> datas)
	{
		List<String> gatherName = new ArrayList<String>();
		List<String> gatherUrl = new ArrayList<String>();
		List<String> gatherPrice = new ArrayList<String>();
		List<String> gatherCount = new ArrayList<String>();
		
		List<String> removeDuplicateList = removeDuplicateWithOrder(datas.get("url"));
		 //遍历去掉重复元素的List 计算每个元素的个数
		for (int i = 0; i < removeDuplicateList.size(); i++) {		
			String value = (String) removeDuplicateList.get(i);
			int count = 0;
			String name = "";
			String price = " ";
			for (int j = 0; j < datas.get("url").size(); j++) {
				//如果元素值有和原来List中的值相等 count++
				if (value.equals(datas.get("url").get(j))) {
					//更新name、price、count
					count++;
					name = datas.get("name").get(j);
					price = datas.get("price").get(j);
				}
			}
			gatherUrl.add(value);
			gatherName.add(name);
			gatherPrice.add(price);
			gatherCount.add(String.valueOf(count));
		}
		mDuplicateMap.put("url", gatherUrl);
		mDuplicateMap.put("name", gatherName);
		mDuplicateMap.put("price", gatherPrice);
		mDuplicateMap.put("count", gatherCount);
		
		return;
	}
	
	/**
	 * @param 需要的key类型
	 * @return List<String>, 返回结果是订单去重后数据
	 */
	public List<String> getOrderByKey(String key) {
		if (key == "name")
			return this.mDuplicateMap.get("name");
		if (key == "url")
			return this.mDuplicateMap.get("url");
		if (key == "price")
			return this.mDuplicateMap.get("price");
		if (key == "count")
			return this.mDuplicateMap.get("count");
		return null;
	}
	
	/**
	 * @描 述：下单时加减按钮的作用
	 * @param true = "+", false = "-"
	 * @return 
	 */
	public void addOrSubtractFromOrder(String url, boolean operation)
	{
		List<String> names = this.mPreferencesNames;
		List<String> urls = this.mPreferencesUrls;
		List<String> prices = this.mPreferencesPrices;
		for (int i = urls.size()-1; i >= 0; i--)
		{
			if (urls.get(i) == url)
			{
				if (operation){
					urls.add(urls.get(i));
					names.add(names.get(i));
					prices.add(prices.get(i));
				} else {
					urls.remove(i);
					names.remove(i);
					prices.remove(i);
				}
				break;
			}
		}
		saveToPerferences("name", "url", "price");
		for (int i = 0 ; i < urls.size(); ++i) 
			addToPerferences(names.get(i), urls.get(i), prices.get(i));
		return;
	}
	
	/**
	 * @描 述：返回下单页的总价
	 * @return 
	 */
	public String getOrderTotalPrice()
	{
		int total = 0;
		for (int i = 0; i < this.mDuplicateMap.get("price").size(); ++i)
			total += Integer.valueOf(this.mDuplicateMap.get("price").get(i)).intValue() * Integer.valueOf(this.mDuplicateMap.get("count").get(i)).intValue();
		return Integer.toString(total);
	}
	
	/**
	 * @描 述：要得到数据，必须实例PreferencesService后执行该函数一次
	 * @return 
	 */
	public void datasFromPreferencesService(){
		//必要的方法
		getPerferencesDatas(getPerferences());
		Map<String, List<String>> gatherMap= new HashMap<String, List<String>>();
		gatherMap.put("url", this.mPreferencesUrls);
		gatherMap.put("name", this.mPreferencesNames);
		gatherMap.put("price", this.mPreferencesPrices);
		getDuplicateGatherWithOrder(gatherMap);
	}

}