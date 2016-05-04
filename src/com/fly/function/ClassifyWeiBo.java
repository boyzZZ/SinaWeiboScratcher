package com.fly.function;

import java.util.ArrayList;

import com.fly.store.MySQLStorage;
import com.fly.thread.ClassifyWorker;
import com.fly.thread.ClassifyWorker1;
import com.fly.thread.LastClassifyWorker;
import com.fly.thread.LastClassifyWorker1;

//���ڶ�΢�����з������

/*
 * ������Ҫ��Ū�������ʲô�ֶν��з��ࡣ�������Ǹ����ֶ����֣����ǻ��ڸ��ʣ�
 * ΢�����������ȡ����ѵ����������
 */
public class ClassifyWeiBo {

	private  MySQLStorage st=null;
	public ClassifyWeiBo(){
		this.st=new MySQLStorage();
	}
	public  void classify(){
		//�Ƚ�΢����Ϊ�ļ�����ʽ
		
		
		ArrayList<String> id_list=new ArrayList<String>();//��������ҵ���Weibo_ID
		/**
		 * ����ԭ��΢��
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
	     * ����ת��΢��
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
