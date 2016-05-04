package com.fly.thread;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fly.client.Client;
import com.fly.function.classify.bayes.BayesClassfier;
import com.fly.store.StorageTool;

public class LastClassifyWorker1 implements Runnable {


	private ArrayList<String> weibo_ids=new ArrayList<String>();
	private StorageTool store;
	private int left;
	private int zheng;
	public LastClassifyWorker1(ArrayList<String> ids,int left,int zheng){
		System.out.println(left);
		this.weibo_ids=ids;
		store=new StorageTool();
		this.left=left;
		this.zheng=zheng;
	}
	public void run() {
		System.out.println(left);
		for(int j=0;j<left;j++){
			
		String weibo_id=weibo_ids.get(j+zheng*1000);
		String weibo_content="";
		try {
			weibo_content = store.getContentById1(weibo_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String category="";
		try{
    	category=new BayesClassfier(Client.CATEGORY_DIR).classify(weibo_content);
		}catch(Exception e){
			System.out.println(weibo_content+"   "+weibo_id);
		}
    	store.TransWeiboAddCate(weibo_id,category);
		
	}

}

}
