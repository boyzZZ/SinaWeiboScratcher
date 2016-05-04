package com.fly.function.classify.bayes;

//分类结果实体
public class ClassifyResult{

	 String classification;  //类别
	 float p;                //类别对应的概率
	
	//默认构造器
	public ClassifyResult(String category,float p){
		this.classification=category;
		this.p=p;
	}
	public ClassifyResult(){};
	
	
	
}
