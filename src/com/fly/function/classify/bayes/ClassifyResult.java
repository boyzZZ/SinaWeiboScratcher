package com.fly.function.classify.bayes;

//������ʵ��
public class ClassifyResult{

	 String classification;  //���
	 float p;                //����Ӧ�ĸ���
	
	//Ĭ�Ϲ�����
	public ClassifyResult(String category,float p){
		this.classification=category;
		this.p=p;
	}
	public ClassifyResult(){};
	
	
	
}
