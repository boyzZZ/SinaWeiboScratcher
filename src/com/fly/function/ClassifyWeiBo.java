package com.fly.function;

import java.util.ArrayList;

import com.fly.store.MySQLStorage;
import com.fly.thread.ClassifyWorker;
import com.fly.thread.ClassifyWorker1;
import com.fly.thread.LastClassifyWorker;
import com.fly.thread.LastClassifyWorker1;

//用于对微博进行分类的类

/*
 * 在这里要先弄清楚根据什么字段进行分类。（并不是根据字段来分，而是基于概率）
 * 微博分类的质量取决于训练集的质量
 */
public class ClassifyWeiBo {

	private  MySQLStorage st=null;
	public ClassifyWeiBo(){
		this.st=new MySQLStorage();
	}
	public  void classify(){
		//先将微博存为文件的形式
		
		
		ArrayList<String> id_list=new ArrayList<String>();//用来存放找到的Weibo_ID
		/**
		 * 处理原创微博
		 */
		try {
			id_list=st.getAlInitWeibos();
		} catch (Exception e){
			e.printStackTrace();
		}
	    int init_length=id_list.size();
	    int process_number_init=init_length/1000;
	    for(int i=0;i<process_number_init;i++){
	    	new Thread(new ClassifyWorker(id_list,i)).start();	
	    };
	    new Thread(new LastClassifyWorker(id_list,id_list.size()-(init_length/1000)*1000,process_number_init)).start();
	    
	    /**
	     * 处理转发微博
	     */
	    try{
	    	id_list=st.getAlTransWeibos();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    int trans_length=id_list.size();
	    int process_number_trans=trans_length/1000;
	    for(int i=0;i<process_number_trans;i++){
	    	new Thread(new ClassifyWorker1(id_list,i)).start();	
	    };
	    new Thread(new LastClassifyWorker1(id_list,id_list.size()-(trans_length/1000)*1000,process_number_trans)).start();
	}
	
	
}
