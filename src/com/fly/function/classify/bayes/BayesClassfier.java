package com.fly.function.classify.bayes;

import java.util.ArrayList;
import java.util.Comparator;

import com.fly.function.classify.util.ChineseSpliter;

//Bayes分类器，将所有操作都几种到该类本身。
public class BayesClassfier {

	//训练集
	private TrainingData tdm;
	
	public BayesClassfier(String filePath){
		tdm=new TrainingData(filePath);
	}
	
	//主体方法
	public String  classify(String plain_text){
		/**
		 * 按下步骤进行：
		 * 准备阶段：
		 * No.1  对plain_text分词
		 * No.2  对分完词后的split_text过滤
		 * No.3 过滤完的filter_text就是我们可以拿来分类的东西啦
		 * No.4 获取训练集中所有的分类，也就是已经定义的分类
		 * 
		 * 计算阶段：
		 * No.5 计算filter_text属于某一分类的概率，比较所有分类的概率，最大的就是filter_text的类别
		 * 
		 */
		//No.1 
		String split_text=ChineseSpliter.split(plain_text, " ");
		//No.2
		String[] filter_text=ChineseSpliter.filter(split_text.split(" "));
		//No.4
		String[] allClassifications=tdm.getTrainingClassifications();
		
		/*
		 * 因为对应着分类，所以会得到多个结果值，要比较这些值的大小就必须将这些值进行存储，然后比较
		 */
		ArrayList<ClassifyResult> list=new ArrayList<ClassifyResult>();
		
		for(int i=0;i<allClassifications.length;i++){
			float p=calProbility(filter_text,allClassifications[i]);
			ClassifyResult cr=new ClassifyResult(allClassifications[i],p);
			list.add(cr);
		}	
		/*
		 * 需要对list里的结果进行比较
		 */
		java.util.Collections.sort(list,new Comparator<Object>() {
			public int compare(Object o1,Object o2){
				ClassifyResult m1=(ClassifyResult)o1;
				ClassifyResult m2=(ClassifyResult)o2;
				if(m1.p>m2.p){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return list.get(0).classification;
		
	}
	
	
	public float calProbility(String[] X,String Ci){
		float result=0F;
		for(int i=0;i<X.length;i++){
			result+=(tdm.getCountContainKeyOfClassification(Ci, X[i])/tdm.getTrainingFileCountOfClassification(Ci));
		}
		result*=(tdm.getTrainingFileCountOfClassification(Ci)/tdm.getTrainingFileCount());
		return result;
	}
}
