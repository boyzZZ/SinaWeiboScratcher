package com.fly.function.repair;

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

/**
 * 修复类 将不合格的文件重新获取
 * 
 * @author fly
 * @return true 修复成功 false修复失败 （需重复修复）
 */
@SuppressWarnings("deprecation")
public class Repair {

	public boolean repair(int number) throws FileNotFoundException, IOException{
		File file=new File("html\\"+number+".html");
	    Properties pros=new Properties();
	    pros.load(new FileInputStream("data\\temp.properties"))	;
		HttpClient httpClient=new DefaultHttpClient();
		HttpGet request=new HttpGet(pros.getProperty("LYF_HOMEPAGE")+(number));
		request.addHeader("Cookie",pros.getProperty("LYF_COOKIES"));
		request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		HttpResponse response=httpClient.execute(request);
		HttpEntity entity=response.getEntity();
		String content=EntityUtils.toString(entity);
		file.createNewFile();
		FileWriter fo=new FileWriter(file);
		fo.write(content);
		fo.flush();
		fo.close();
		httpClient.getConnectionManager().shutdown();
			
			
		if(CheckHTML.checkSingle(file)){
			return true;
		}else{
			return false;	
		}
		
		
	}
}
