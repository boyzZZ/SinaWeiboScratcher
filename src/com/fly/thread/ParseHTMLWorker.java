package com.fly.thread;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fly.client.Client;
import com.fly.parse.WeiboTypeParse;
import com.fly.parse.type.ParseOrgWeibo;
import com.fly.parse.type.ParseOrgWeiboAndPic;
import com.fly.parse.type.ParseOrgWeiboAndPicLimited;
import com.fly.parse.type.ParseOrgWeiboLimited;
import com.fly.parse.type.ParseTransWeibo;
import com.fly.parse.type.ParseTransWeiboAndPic;
import com.fly.parse.type.ParseTransWeiboAndPicLimited;
import com.fly.parse.type.ParseTransWeiboLimited;

public class ParseHTMLWorker implements Runnable {

	private int index;
	public ParseHTMLWorker(int x){
		this.index=x;
	}
	@Override
	public void run() {
		for(int i=1;i<=100;i++){
			
			
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			File file=new File(Client.HTML_PATH+(i+index*100)+".html");
			Document doc = null;
			try {
				doc = Jsoup.parse(file,"GBK");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements all_weibos=doc.getElementsByClass("c");
			for(int a=0;a<all_weibos.size()-2;a++){
				
				try{
				char type=WeiboTypeParse.justifyWeibo(all_weibos.get(a));
				if(all_weibos.size()!=12){
					System.out.println("这是第"+(i+index*100)+"个页面,本页面共有"+(all_weibos.size()-2)+"条微博，"+"正在打印的是第"+a+"条微博，微博的类型是"+type);
				}
				System.out.println(type);
				switch(type){
				case 'A':  //原创  无图 无限制
					new ParseOrgWeibo().parse(all_weibos.get(a));
				    break;
				case 'B':  //原创  无图 有限制
					new ParseOrgWeiboLimited().parse(all_weibos.get(a));
					break;
				case 'C':  //原创  有图 无限制
					new ParseOrgWeiboAndPic().parse(all_weibos.get(a));
					break;
				case 'D':  //原创  有图 有限制
					new ParseOrgWeiboAndPicLimited().parse(all_weibos.get(a));
					break;
				case 'E':   //转发  无图 无限制
					new ParseTransWeibo().parse(all_weibos.get(a));
					break;
				case 'F':   //转发  无图  有限制
					new ParseTransWeiboLimited().parse(all_weibos.get(a));
					break;
				case 'G':  //转发  有图 有限制
					new ParseTransWeiboAndPicLimited().parse(all_weibos.get(a));
					break;
				case 'H':  //转发 有图 无限制
					new ParseTransWeiboAndPic().parse(all_weibos.get(a));
					break;
				default :System.out.println("无法判断微博类型");
				}
				}
				catch(Exception e){
				e.printStackTrace();
				try {
					FileWriter fw=new FileWriter(new File("data\\error.txt"),true);
					fw.write(e.getMessage()+"第"+i+"个页面的第"+a+"条微博未成功插入，请检查！");
					System.out.println(e.getMessage()+"第"+i+"个页面的第"+a+"条微博未成功插入，请检查！");
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			}
			
		}
		
	}
	
}
