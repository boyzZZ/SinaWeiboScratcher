package com.fly.thread;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fly.client.Client;
import com.fly.function.classify.bayes.BayesClassfier;
import com.fly.store.MySQLStorage;

public class ClassifyWorker1 implements Runnable {

	private ArrayList<String> weibo_ids=new ArrayList<String>();
	private int ThreadIndex=0;
	private MySQLStorage store;
	public ClassifyWorker1(ArrayList<String> ids,int i){
		this.weibo_ids=ids;
		this.ThreadIndex=i;
		store=new MySQLStorage();
	}
	public void run() {
		for(int j=0;j<1000;j++){
		String weibo_id=weibo_ids.get(j+ThreadIndex*1000);
		String weibo_content="";
		try {
			weibo_content = store.getContentById1(weibo_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	String category=new BayesClassfier(Client.CATEGORY_DIR).classify(weibo_content);
    	store.TransWeiboAddCate(weibo_id, category);
		
	}

}

}
