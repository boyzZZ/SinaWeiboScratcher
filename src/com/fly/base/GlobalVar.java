package com.fly.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalVar {
 
	//��ֵ��Ϊ��ľ�̬�������洢�����з��գ���Ϊ��һ��ж�أ�֮ǰ��ֵ�Ͳ������ˣ����漰����α�����ʱ���ݺ����ñ������ݵ�����
	
	private static int WEIBO_ID_COUNT=400000;
	
	/**
	 * ��ȡ��ǰ�����
	 * @return  2006��
	 */
	public static String getYear(){
		SimpleDateFormat df1=new SimpleDateFormat("yyyy��");
		String nowyear=df1.format(new Date());
		return nowyear;
	}
	
	/**
	 * ��ȡ��ǰ�ľ�������  
	 * @return 2016��03��31��
	 */
	public static String getDate(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy��MM��dd��");
		String nowdate=df.format(new Date());
		return nowdate;
	}
	
	/**
	 * ��ȡ΢���ı�ţ��������ORG��ͷΪԭ������ͼƬ��ORP��ͷΪԭ����ͼƬ
	 * TRN��ͷΪת������ͼƬ��TRPΪת����ͼƬ
	 * @param type
	 * @return ���  ORG1111112
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
	    	return "��ȡ��ų��ִ���";}
	}
	
}
