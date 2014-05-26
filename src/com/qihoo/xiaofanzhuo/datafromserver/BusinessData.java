package com.qihoo.xiaofanzhuo.datafromserver;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * ZoneListData：商家数据
 * by hongxiaolong
 */

public class BusinessData {

	private String ShopID = null;
	private String  ShopName = null;
	private String BusyState = null;
	private String ShopImgUrl = null;
	private String ShopInfo = null;
	private String ShopSite = null;
	private String PhoneNum = null;
	private String SendFoodOut = null;
	private String NumofPeopleWant2Eat = null;
	private String PraiseNum = null;
	private String ShopAverPrice = null;
	private String TasteScore = null;
	private String ServiceScore = null;
	private String EnvScore = null;
	private String ShopTag = null;
	private String Other1 = null;
	private String Other2 = null;
	private String Other3 = null;
	private String ShopMenu = null;
	
	//商家的菜单信息列表
	private ArrayList<MenuData> mFoodList = new ArrayList<MenuData>();
	
	private HashMap<String, String> bussinessMap = new HashMap<String, String>();
	public static final String[] keyArray= {
			"ShopID", "ShopName",  "BusyState",  "ShopImgUrl",  "ShopInfo",  "ShopSite",  "PhoneNum",  "SendFoodOut",  
			"NumofPeopleWant2Eat",  "PraiseNum",  "ShopAverPrice",  "TasteScore", "ServiceScore",  "EnvScore",  "ShopTag",  
			"Other1", "Other2",  "Other3",  
			"ShopMenu"
	};

	public final static String[] menuKeyArray= {
		"Food", "FoodPrice", "IsRecommend", "IsSpec", "FoodImgUrl"
	};
	
	/*
	 * 构造函数
	 * @pram:服务端传入的字符串，，以"____"分隔
	 */
	BusinessData(String originString)
	{
		String[] listArray = splitFromStringBySymbol(originString, "\t");
		for (int i = 0; i < listArray.length; ++i)
			bussinessMap.put(keyArray[i], listArray[i]);
		String[] foodList = splitFromStringBySymbol(getShopMenu(), "____");
		for (int i = 0; i < foodList.length; ++i)
			mFoodList.add(new MenuData(foodList[i]));
	}
	
	public static String[] splitFromStringBySymbol(String orginString, String symbol){
		return orginString.split(symbol);
	}
	
	public String getShopID()
	{
		return this.ShopID = bussinessMap.get("ShopID");
	}
	public String getShopName()
	{
		return this.ShopName = bussinessMap.get("ShopName");
	}
	public String getBusyState()
	{
		return this.BusyState = bussinessMap.get("BusyState");
	}
	public String getShopImgUrl()
	{
		return this.ShopImgUrl = getActualString(bussinessMap.get("ShopImgUrl"));
	}
	public String getShopInfo()
	{
		return this.ShopInfo = bussinessMap.get("ShopInfo");
	}
	public String getShopSite()
	{
		return this.ShopSite = bussinessMap.get("ShopSite");
	}
	public String getPhoneNum()
	{
		return this.PhoneNum = bussinessMap.get("PhoneNum");
	}
	public String getSendFoodOut()
	{
		return this.SendFoodOut = bussinessMap.get("SendFoodOut");
	}
	public String getNumofPeopleWant2Eat()
	{
		return this.NumofPeopleWant2Eat = bussinessMap.get("NumofPeopleWant2Eat");
	}
	public String getPraiseNum()
	{
		return this.PraiseNum = bussinessMap.get("PraiseNum");
	}
	public String getTasteScore()
	{
		return this.TasteScore = bussinessMap.get("TasteScore");
	}
	public String getShopAverPrice()
	{
		return this.ShopAverPrice = bussinessMap.get("ShopAverPrice");
	}
	public String getServiceScore()
	{
		return this.ServiceScore = bussinessMap.get("ServiceScore");
	}
	public String getEnvScore()
	{
		return this.EnvScore = bussinessMap.get("EnvScore");
	}
	public String getShopTag()
	{
		return this.ShopTag = bussinessMap.get("ShopTag");
	}
	public String getOther1()
	{
		return this.Other1 = bussinessMap.get("Other1");
	}
	public String getOther2()
	{
		return this.Other2 = bussinessMap.get("Other2");
	}
	public String getOther3()
	{
		return this.Other3 = bussinessMap.get("Other3");
	}
	public String getShopMenu()
	{
		return this.ShopMenu = bussinessMap.get("ShopMenu");
	}
	
	public ArrayList<MenuData> getFoodList()
	{
		return this.mFoodList;
	}
	
	public static String getActualString(String str)
	{
		//String str;
		if(str.indexOf("\'") == 0) 
			str = str.substring(1, str.length());   //去掉第一个 "
		if(str.lastIndexOf("\'") == (str.length()-1)) 
			str = str.substring(0, str.length()-1);  //去掉最后一个 " 
		return str;
	}
	
	private static class MenuData{
		
		private HashMap<String, String> MenuMap;
		
		//传入的是一个菜的信息
		MenuData(String datas){
			MenuMap = new HashMap<String, String>();
			String[] foodList = splitFromStringBySymbol(datas, "_");
			for (int i = 0; i < foodList.length; ++i){
					MenuMap.put(menuKeyArray[i], foodList[i]);
			}			
		}
		public String getFood(){
			return MenuMap.get("Food");
		}
		public String getFoodPrice(){
			return MenuMap.get("FoodPrice");
		}
		public String getDishProperty(){
			return MenuMap.get("Food");
		}
		public String getFoodImgUrl(){
			return getActualString(MenuMap.get("FoodImgUrl"));
		}
		public String isRecommend(){
			return MenuMap.get("IsRecommend");
		}
		public String isSpecial(){
			return MenuMap.get("IsSpec");
		}
		
	}
	
}
