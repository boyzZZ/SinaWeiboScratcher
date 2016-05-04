package com.fly.thread;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fly.client.Client;
import com.fly.function.classify.bayes.BayesClassfier;
import com.fly.store.MySQLStorage;

public class LastClassifyWorker implements Runnable{

	private ArrayList<String> weibo_ids=new ArrayList<String>();
	private MySQLStorage store;
	private int left;
	private int zheng;
	public LastClassifyWorker(ArrayList<String> ids,int left,int zheng){
		System.out.println(left);
		System.out.println(zheng);
		System.out.println(ids);
		this.weibo_ids=ids;
		store=new MySQLStorage();
		this.left=left;
		this.zheng=zheng;
	}
	public void run() {
		System.out.println(left);
		for(int j=0;j<left;j++){
		String weibo_id=weibo_ids.get(j+zheng*1000);
		String weibo_content="";
		try {
			weibo_content = store.getContentById(weibo_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	String category=new BayesClassfier(Client.CATEGORY_DIR).classify(weibo_content);
    	store.InitWeiboAddCate(weibo_id,category);
		
	}

}

}
