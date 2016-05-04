package com.fly.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fly.client.Client;
import com.fly.function.repair.Repair;
import com.fly.store.MySQLStorage;
import com.fly.thread.DownloadLastWorker;
import com.fly.thread.DownloadWorker;
import com.fly.thread.ParseHTMLWorker;
import com.fly.thread.ParseLastHTMLWorker;

@SuppressWarnings("deprecation")
public class HTMLTool {

	private static Properties properties=new Properties();;
	
	//下载微博对应的方法，1、获得用户的微博列表的地址，2、获取用户的微博的页数，3、下载
	/**
	 * 下载微博的时候，要明确下载的个数
	 * 通过 getlist() 获取需要下载的页面数
	 * @return 总的页面数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
    public static int getlist(){
	try {
		properties.load(new FileInputStream("data\\temp.properties"));
	} catch (Exception e){
		e.printStackTrace();
	}
	HttpClient httpClient=new DefaultHttpClient();
	HttpGet request=new HttpGet(properties.getProperty("HOMEPAGE"));
	int pagenumber = 0;
    request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
	request.addHeader("Cookie",properties.getProperty("LYF_COOKIES"));
 
    HttpResponse response;
    String real_homepage="http://weibo.cn";
    String content="";
	try {
		response = httpClient.execute(request);
		HttpEntity entity=response.getEntity();
		content=EntityUtils.toString(entity);
	} catch (Exception e){
		e.printStackTrace();
	}
	try {
		FileWriter fw=new FileWriter("data\\homepage.html");
		fw.write(content);
		fw.flush();
		fw.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	Document doc=Jsoup.parse(content);
	
	real_homepage+=doc.getElementsByClass("tip2").get(0).children().get(0).attr("href");
	
	properties.setProperty("LYF_HOMEPAGE", real_homepage+"&page=");
	 
	
	//二次请求
	System.out.println(real_homepage+"&page=");
	request=new HttpGet(real_homepage);
	request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
	request.addHeader("Cookie",properties.getProperty("LYF_COOKIES"));
	String content1="";
	try {
		response=httpClient.execute(request);
		HttpEntity entity1=response.getEntity();
		content1=EntityUtils.toString(entity1);
	} catch (Exception e){
		e.printStackTrace();
	}
	
	Document doc1=Jsoup.parse(content1);
	if(doc1.getElementById("pagelist")==null){
		pagenumber=1;
	}else{
	pagenumber=Integer.parseInt(doc1.getElementById("pagelist").getElementsByTag("input").val());
	}
	properties.setProperty("LYF_PAGENUMBER", new Integer(pagenumber).toString());
	FileOutputStream fos;
	try {
		fos = new FileOutputStream("data\\temp.properties");
		properties.store(fos, "");
		fos.close();
	} catch (Exception e){
		e.printStackTrace();
	}
	
	return pagenumber;
}
	/**
	 * DownloadHTML类的静态方法，用于下载某个用户的所有微博
	 * HTML页面的形式
	 * 以HTML文件存放在SinaWeibo目录下
	 * 具体的下载任务分配给进程DownloadWorker
	 * 每个进程负责100个页面的下载
	 */
	public static void downloadHTML(int pagenumber){
		if(pagenumber<=100){
			new Thread(new DownloadWorker(0,pagenumber)).start();
		}else{
		for(int j=0;j<(pagenumber/100);j++){
			new Thread(new DownloadWorker(j,100)).start();
		}		
		new Thread(new DownloadLastWorker()).start();
		}
	}
	/*在这里有几个问题第一，单线程获取没有问题
	 * 如果是多线程，会出现如下问题：
	 * 1、新浪监测到获取次数过多，会不再显示数据页面而是显示登录页面
	 * 2、获取的网页微博其他部分正常，网页正文部分为空，就是没有微博
	 * 
	 * 
	 * 现阶段为了获取数据做解析HTML的工作，我们首先用单线程获取，在请求之间加一点间隔时间
	 * 这样可以稳定的获取数据。
	 */
	/**
	 * 修复没有成功下载的微博
	 */
	public static void repair(ArrayList<Integer> list){
		
		
		for(int i=0;i<list.size();i++){ 
			
			try {
				new Repair().repair(list.get(i));
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * To parse the SinaWeibo Directory's files
	 * To enhance capability 
	 * Use Thread to parse file ,the same principle to download file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void parseHTML() throws FileNotFoundException, IOException{
		properties.load(new FileInputStream("data\\temp.properties"));
		for(int j=0;j<(Integer.parseInt(properties.getProperty("LYF_PAGENUMBER"))/100);j++){
			new Thread(new ParseHTMLWorker(j)).start();
		}		
		new Thread(new ParseLastHTMLWorker()).start();
	}
	
	/**
	 * 以下所有方法供GUI页面组件调用，实时查询
	 */
	public static int hasFinished(){
		File file=new File(Client.HTML_PATH);
		return file.listFiles().length;
	}
	
	public static int getWeibosNumber(){
		Document doc=null;
		try {
			doc = Jsoup.parse(new File("data\\homepage.html"), "GBK");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String weibo=doc.getElementsByClass("tip2").get(0).children().get(0).text();
		int weibos_number=Integer.parseInt(weibo.substring(3, weibo.length()-1));
		return weibos_number;
	}
	
    public static int getFinishedNumber(){
    	MySQLStorage st=new MySQLStorage();
    	int x=0,y=0;
    	try{
    	x=st.getInitWeibos();
    	y=st.getTransWeibos();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return x+y;
    }
    public static void main(String[] args) {
		try {
			parseHTML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
