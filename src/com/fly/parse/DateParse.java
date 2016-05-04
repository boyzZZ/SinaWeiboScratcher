package com.fly.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Element;

import com.fly.base.GlobalVar;

public class DateParse {

	public static String parse(Element e){
		String all=
				e.text();
		if(all.substring(0,2).equals("今天")){
			return GlobalVar.getDate()+all.substring(3,8);
			
		}
		
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df1=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date pubtime=null;
		try {
			pubtime = df.parse(all.substring(0,19));
			return df.format(pubtime);
		} catch (Exception e1) {
			
			try {
				pubtime=df1.parse(GlobalVar.getYear()+all.substring(0,12)+":00");
				return df.format(pubtime);
			} catch (ParseException e2) {
			
			}
			
			
		}
		return "1993-06-15 23:59:59";
	}
	

}
