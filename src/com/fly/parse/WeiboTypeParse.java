package com.fly.parse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiboTypeParse {
	    /**
	     * ��Ϊ�漰��Ȩ�޵�ʱ�򣬻��кܶ���������ÿһ������Ӧ��һ������̫���ˣ��������ڽ�΢����Ȩ�޷���Ȩ�޺���Ȩ�ޣ������پ�������Ȩ��
	     * ����div�������з��ࣺ
	     *  div=1  ����  �� 1�� ԭ����ͼ+������ A 2�� ԭ����ͼ+������B
	     *  div=2  ����  : 1��ԭ����ͼ+������ C 2�� ԭ����ͼ+������ D3�� ת��ԭ����ͼ+������E 4�� ת��ԭ����ͼ+������F
	     *  div=3  ����   �� 1�� ת��ԭ����ͼ+������G 2��ת��ԭ����ͼ+������ H
	     */
		public static char justifyWeibo(Element e){
			int size=e.children().size();
			//div=1  ����
			if(size==1){
				Elements cmts=e.getElementsByClass("cmt");
				if(cmts.size()==0){
					return 'A';
				}
				Element first_cmt=e.getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("�ɼ�")){
					return 'B';
				}
			}
			
			//div=2  4��
			if(size==2){
				if(e.children().get(0).getElementsByClass("cmt").size()==0){
					return 'C';
				}
				int cmt_number=e.children().get(0).getElementsByClass("cmt").size();
				if(cmt_number==4){
					//ת��ԭ������ͼƬ+������
					return 'F';
				}
				if(cmt_number==3){
					//ת��ԭ������ͼƬ+������
					return 'E';
				}
				Element first_cmt=e.children().get(0).getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("�ɼ�")){
					//ԭ����ͼƬ+������
					return 'D';
				}
				
		
				//ʣ�඼��ԭ����ͼƬ+������
				return 'C';
			}
			if(size==3){
				Element first_cmt=e.children().get(0).getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("�ɼ�")){
					//ת��ԭ������ͼƬ+������
					return 'G';
				}
				return 'H';
			}
			return 'Z';	
		}
	}


