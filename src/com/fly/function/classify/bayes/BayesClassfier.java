package com.fly.function.classify.bayes;

import java.util.ArrayList;
import java.util.Comparator;

import com.fly.function.classify.util.ChineseSpliter;

//Bayes�������������в��������ֵ����౾��
public class BayesClassfier {

	//ѵ����
	private TrainingData tdm;
	
	public BayesClassfier(String filePath){
		tdm=new TrainingData(filePath);
	}
	
	//���巽��
	public String  classify(String plain_text){
		/**
		 * ���²�����У�
		 * ׼���׶Σ�
		 * No.1  ��plain_text�ִ�
		 * No.2  �Է���ʺ��split_text����
		 * No.3 �������filter_text�������ǿ�����������Ķ�����
		 * No.4 ��ȡѵ���������еķ��࣬Ҳ�����Ѿ�����ķ���
		 * 
		 * ����׶Σ�
		 * No.5 ����filter_text����ĳһ����ĸ��ʣ��Ƚ����з���ĸ��ʣ����ľ���filter_text�����
		 * 
		 */
		//No.1 
		String split_text=ChineseSpliter.split(plain_text, " ");
		//No.2
		String[] filter_text=ChineseSpliter.filter(split_text.split(" "));
		//No.4
		String[] allClassifications=tdm.getTrainingClassifications();
		
		/*
		 * ��Ϊ��Ӧ�ŷ��࣬���Ի�õ�������ֵ��Ҫ�Ƚ���Щֵ�Ĵ�С�ͱ��뽫��Щֵ���д洢��Ȼ��Ƚ�
		 */
		ArrayList<ClassifyResult> list=new ArrayList<ClassifyResult>();
		
		for(int i=0;i<allClassifications.length;i++){
			float p=calProbility(filter_text,allClassifications[i]);
			ClassifyResult cr=new ClassifyResult(allClassifications[i],p);
			list.add(cr);
		}	
		/*
		 * ��Ҫ��list��Ľ�����бȽ�
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
