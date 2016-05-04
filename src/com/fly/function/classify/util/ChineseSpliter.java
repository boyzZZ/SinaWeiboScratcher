package com.fly.function.classify.util;

import java.io.IOException;

import jeasy.analysis.MMAnalyzer;

//���ķִ������򵥵ķ�װ��һ�¼������ķ۴����
public class ChineseSpliter {

	//Default Constructor
	public ChineseSpliter(){};
	
	public static String split(String text,String splitToken){
		MMAnalyzer analyzer=new MMAnalyzer();
		String result=null;
		try{
			result=analyzer.segment(text, splitToken);
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static String[] filter(String[] initarray){
		String[] aa=new String[initarray.length];
		int i=0;
		for(int j=0;j<initarray.length;j++){
			
			if(initarray[j].length()>1){
				aa[i]=initarray[j];
				i++;
			}
		}
		int actualnumber=0;
		for(int r=0;r<aa.length;r++){
			if(aa[r]==null){
				continue;
			}
			actualnumber++;
		}
		String[] X = new String[actualnumber];
		for(int g=0;g<actualnumber;g++){
			X[g]=aa[g];
		}
		return X;
	}
	public static void main(String[] args){
		String text="";
		System.out.println(split(text," "));
		System.out.println(MMAnalyzer.size());
	}
}
