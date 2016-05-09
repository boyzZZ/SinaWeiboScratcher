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

@SuppressWarnings("deprecation")
public class DownloadWorker implements Runnable {

	private Properties pros=new Properties();
	private int ThreadIndex;
	private int can_read=100;
	
	public DownloadWorker(int Threadindex,int pagenumber){
		this.ThreadIndex=Threadindex;
		this.can_read=pagenumber;
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
		
		for(int i=1;i<=can_read;i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				HttpClient httpClient=new DefaultHttpClient();
				HttpGet request=new HttpGet(pros.getProperty("LYF_HOMEPAGE")+(i+100*ThreadIndex));
				request.addHeader("Cookie",pros.getProperty("LYF_COOKIES"));
				request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
				HttpResponse response=httpClient.execute(request);
				HttpEntity entity=response.getEntity();
				File file=new File(Client.HTML_PATH+(i+100*ThreadIndex)+".html");
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

	public static void main(String[] args){
		
	}
	
}
