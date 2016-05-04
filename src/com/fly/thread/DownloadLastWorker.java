package com.fly.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fly.client.Client;
import com.fly.function.HTMLTool;

@SuppressWarnings("deprecation")
public class DownloadLastWorker implements Runnable{

	private int remain_pages=HTMLTool.getlist()%100;
	private int remain_100=(HTMLTool.getlist()/100)*100;
	private Properties pros=new Properties();
	
	public DownloadLastWorker(){
		try {
			this.pros.load(new FileInputStream("data\\temp.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
		for(int i=1;i<=remain_pages;i++){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				HttpClient httpClient=new DefaultHttpClient();
				HttpGet request=new HttpGet(pros.getProperty("LYF_HOMEPAGE")+(remain_100+i));
				request.addHeader("Cookie",pros.getProperty("LYF_COOKIES"));
				request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
				HttpResponse response=httpClient.execute(request);
				HttpEntity entity=response.getEntity();
//				String content=EntityUtils.toString(entity);
				File file=new File(Client.HTML_PATH+(remain_100+i)+".html");
				file.createNewFile();
				FileWriter fo=new FileWriter(file);
				fo.write(EntityUtils.toString(entity));
				fo.flush();
				fo.close();
				httpClient.getConnectionManager().shutdown();
				
//				Thread.sleep(5000);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
