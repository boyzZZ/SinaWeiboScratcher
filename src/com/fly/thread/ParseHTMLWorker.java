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
					System.out.println("���ǵ�"+(i+index*100)+"��ҳ��,��ҳ�湲��"+(all_weibos.size()-2)+"��΢����"+"���ڴ�ӡ���ǵ�"+a+"��΢����΢����������"+type);
				}
				System.out.println(type);
				switch(type){
				case 'A':  //ԭ��  ��ͼ ������
					new ParseOrgWeibo().parse(all_weibos.get(a));
				    break;
				case 'B':  //ԭ��  ��ͼ ������
					new ParseOrgWeiboLimited().parse(all_weibos.get(a));
					break;
				case 'C':  //ԭ��  ��ͼ ������
					new ParseOrgWeiboAndPic().parse(all_weibos.get(a));
					break;
				case 'D':  //ԭ��  ��ͼ ������
					new ParseOrgWeiboAndPicLimited().parse(all_weibos.get(a));
					break;
				case 'E':   //ת��  ��ͼ ������
					new ParseTransWeibo().parse(all_weibos.get(a));
					break;
				case 'F':   //ת��  ��ͼ  ������
					new ParseTransWeiboLimited().parse(all_weibos.get(a));
					break;
				case 'G':  //ת��  ��ͼ ������
					new ParseTransWeiboAndPicLimited().parse(all_weibos.get(a));
					break;
				case 'H':  //ת�� ��ͼ ������
					new ParseTransWeiboAndPic().parse(all_weibos.get(a));
					break;
				default :System.out.println("�޷��ж�΢������");
				}
				}
				catch(Exception e){
				e.printStackTrace();
				try {
					FileWriter fw=new FileWriter(new File("data\\error.txt"),true);
					fw.write(e.getMessage()+"��"+i+"��ҳ��ĵ�"+a+"��΢��δ�ɹ����룬���飡");
					System.out.println(e.getMessage()+"��"+i+"��ҳ��ĵ�"+a+"��΢��δ�ɹ����룬���飡");
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			}
			
		}
		
	}
	
}
