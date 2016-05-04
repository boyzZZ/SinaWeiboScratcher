package com.fly.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@SuppressWarnings("deprecation")
public class GetCookie {

	
	/**
	 * ����Ҫ��˳������ĸ���˳���ó���ģ�������ʱ����û��Cookie�ģ�����˳�����£�
	 * 1������http://login.weibo.cn/login (����Ҫ�κ�Cookie������������һ��Cookie)
	 * ��������ַ�����¼ҳ�棬������Ϣ�ᵼ�·��ص�½ҳ���ڣ��˵�¼ҳ����ڵ�¼�ǳ���Ҫ���ܶ������Ҫ�Ӹ�ҳ����ȡ��
	 * 2���ڸ�ҳ�������û��������롢��֤�뼴�ɵ�¼
	 * ��������Ϣ����ȷ�󣬲�������������������Զ�����location����ת�����������Ҫ�������Cookie����Ҫģ�����������һ����ҳ�����ת��
	 * �������������ֻ��Ҫweibo.cn�µ�����Cookie���У���ʵҲ����������Ҫ��Cookie��SUB��gsid_CTandWM
	 * ���������ǲ�����ת��ô��ҳ�棬ֻ�õ�½�ɹ��󣬻����Ӧͷ����Set-Cookie��ֵ����ȡ���ɡ� �������תҳ������� ������ login.txt
	 * */
	
    /**
     * ��ȡ��֤�벢���б��ش洢��data\code.gif��
     **/ 
	public static void getCode() throws ClientProtocolException, IOException {
		// No.1��ȡ��¼ҳ������� ,ֻ�������ַ�����½�������˳����¼
		String address = "http://login.weibo.cn/login/?ns=1&revalid=2&backURL=" +
				"http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=4";
		
		// No.2 ģ������+������¼ҳ�棬׼�����ύʱ��Ҫ�Ĳ���
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(address);
		HttpResponse response = null;
		response = httpClient.execute(request);

		// No.3 ��������
		HttpEntity entity = response.getEntity();
		Header[] headers = response.getAllHeaders();
		String content = EntityUtils.toString(entity);
		File file = new File("data\\login.html");
		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.flush();
		
		// ��ȡ��Ӧͷ�����õ�Cookie
		String cookie_T_WM = "";
		for (Header h : headers) {
			if (h.getName().equals("Set-Cookie")) {
				cookie_T_WM = h.getValue().split(";")[0];
			}
		}
		FileWriter fw1 = new FileWriter(new File("data\\cookies.properties"));
		fw1.write(cookie_T_WM+"\n" );
		fw1.flush();
		fw1.close();
		File gif = new File("data\\code.gif");
		Document doc = Jsoup.parse(content);
		String imageaddress = doc.getElementsByTag("img").attr("src");
		
		// ��ȡgifͼƬ
		request = new HttpGet(imageaddress);
		response = httpClient.execute(request);
		InputStream gifinput = (response.getEntity().getContent());
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileoutput = new FileOutputStream(gif);
		while ((len = gifinput.read(data)) != -1) {
			fileoutput.write(data, 0, len);
		}
		fileoutput.close();
		gifinput.close();
	}
	
	/**
	 * @param username,password,code
	 * �ύ���ݣ���ȡCookies,����data\cookies.properties��
	 */
	public static void getCookies(String username, String password, String code)
			throws ClientProtocolException, IOException {
		Document doc = Jsoup.parse(new File("data\\login.html"), "UTF-8");
		// ��ȡһЩ�ύʱ����Ҫ�Ĳ���
		String post_address = "http://login.weibo.cn/login/"
				+ doc.getElementsByTag("form").get(0).attr("action");
		String password_xxxx = doc
				.getElementsByAttributeValue("type", "password").get(0)
				.attr("name");
		Elements hiddens = doc.getElementsByAttributeValue("type", "hidden");
		String backURL = hiddens.get(0).attr("value");
		String backTitle = hiddens.get(1).attr("value");
		String tryCount = hiddens.get(2).attr("value");
		String vk = hiddens.get(3).attr("value");
		String capId = hiddens.get(4).attr("value");
		FileReader fr=new FileReader("data\\cookies.properties");
		BufferedReader br=new BufferedReader(fr);
		String cookie_T_WM=br.readLine();
		fr.close();
		br.close();
		// ��������ģ���¼�������������Щ������
		HttpPost post = new HttpPost(post_address);
		post.addHeader("Host", "login.weibo.cn");
		post.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");
		post.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Cookie", cookie_T_WM);
		post.addHeader("Referer", "http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3" +
				"A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=4");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", username));
		nvps.add(new BasicNameValuePair(password_xxxx, password));
		nvps.add(new BasicNameValuePair("code", code));
		nvps.add(new BasicNameValuePair("remember", "on"));
		nvps.add(new BasicNameValuePair("backURL", backURL));
		nvps.add(new BasicNameValuePair("backTitle", backTitle));
		nvps.add(new BasicNameValuePair("tryCount", tryCount));
		nvps.add(new BasicNameValuePair("vk", vk));
		nvps.add(new BasicNameValuePair("capId", capId));
		nvps.add(new BasicNameValuePair("submit", "�ύ"));
		// ��������һ�������٣���Ȼ�͵�¼���ɹ�

		post.setEntity(new UrlEncodedFormEntity(nvps));
		String postSti = post.toString();
		System.out.println(postSti);
		HttpResponse response = new DefaultHttpClient().execute(post);
		Header[] hds = response.getAllHeaders();
		FileWriter f = new FileWriter(new File("data" + File.separator
				+ "cookies.properties"),true);
		Properties pros1=new Properties();
		Properties pros2=new Properties();
		pros1.load(new FileInputStream("data\\temp.properties"));
		pros2.load(new FileInputStream("data\\cookies.properties"));
		String old_cookie="_T_WM="+pros2.getProperty("_T_WM");
		String to_write = "";
		String to_write2="";
		for (Header h : hds) {
			if (h.getName().equals("Set-Cookie")) {
				to_write += h.getValue().split(";")[0] + ";";
				to_write2+= h.getValue().split(";")[0] + "\n";
			}
		}
		System.out.println(to_write);
		pros1.setProperty("LYF_COOKIES", old_cookie+";"+to_write);
		
		FileOutputStream fos=new FileOutputStream("data\\temp.properties");
		pros1.store(fos, "");
		fos.close();
		f.write(to_write2);
		
		f.flush();
		f.close();
	}

}
