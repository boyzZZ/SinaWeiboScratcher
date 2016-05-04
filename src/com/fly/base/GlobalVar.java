package com.fly.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalVar {
 
	//将值作为类的静态属性来存储数据有风险，因为类一旦卸载，之前的值就不存在了，这涉及到如何保存临时数据和永久保存数据的问题
	
	private static int WEIBO_ID_COUNT=400000;
	
	/**
	 * 获取当前的年份
	 * @return  2006年
	 */
	public static String getYear(){
		SimpleDateFormat df1=new SimpleDateFormat("yyyy年");
		String nowyear=df1.format(new Date());
		return nowyear;
	}
	
	/**
	 * 获取当前的具体日期  
	 * @return 2016年03月31日
	 */
	public static String getDate(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
		String nowdate=df.format(new Date());
		return nowdate;
	}
	
	/**
	 * 获取微博的编号，编号类型ORG开头为原创不带图片，ORP开头为原创带图片
	 * TRN开头为转发不带图片，TRP为转发带图片
	 * @param type
	 * @return 编号  ORG1111112
	 */
	public synchronized static String getNumber(int type){
		if(type==1){
			return "ORG"+WEIBO_ID_COUNT++;}
		else
		if(type==2){
			WEIBO_ID_COUNT++;
			return "ORP"+WEIBO_ID_COUNT++;
		}else
		if(type==3){
			WEIBO_ID_COUNT++;
			return "TRN"+WEIBO_ID_COUNT;
		}else
		if(type==4){
			WEIBO_ID_COUNT++;
			return "TRP"+WEIBO_ID_COUNT;
		}else{
	    	return "获取编号出现错误";}
	}
	
}
