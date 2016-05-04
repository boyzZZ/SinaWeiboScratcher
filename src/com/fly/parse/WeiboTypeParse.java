package com.fly.parse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiboTypeParse {
	    /**
	     * 因为涉及到权限的时候，会有很多种类别，如果每一种类别对应做一个处理，太多了，所以现在将微博按权限分有权限和无权限，而不再具体区分权限
	     * 根据div个数进行分类：
	     *  div=1  两种  ： 1、 原创无图+无限制 A 2、 原创无图+有限制B
	     *  div=2  四种  : 1、原创有图+无限制 C 2、 原创有图+有限制 D3、 转发原创无图+无限制E 4、 转发原创无图+有限制F
	     *  div=3  两种   ： 1、 转发原创有图+有限制G 2、转发原创有图+无限制 H
	     */
		public static char justifyWeibo(Element e){
			int size=e.children().size();
			//div=1  两种
			if(size==1){
				Elements cmts=e.getElementsByClass("cmt");
				if(cmts.size()==0){
					return 'A';
				}
				Element first_cmt=e.getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("可见")){
					return 'B';
				}
			}
			
			//div=2  4种
			if(size==2){
				if(e.children().get(0).getElementsByClass("cmt").size()==0){
					return 'C';
				}
				int cmt_number=e.children().get(0).getElementsByClass("cmt").size();
				if(cmt_number==4){
					//转发原创不带图片+有限制
					return 'F';
				}
				if(cmt_number==3){
					//转发原创不带图片+无限制
					return 'E';
				}
				Element first_cmt=e.children().get(0).getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("可见")){
					//原创带图片+有限制
					return 'D';
				}
				
		
				//剩余都是原创带图片+无限制
				return 'C';
			}
			if(size==3){
				Element first_cmt=e.children().get(0).getElementsByClass("cmt").get(0);
				String kejian=first_cmt.text().substring(first_cmt.text().length()-3,first_cmt.text().length()-1);
				if(kejian.equals("可见")){
					//转发原创不带图片+有限制
					return 'G';
				}
				return 'H';
			}
			return 'Z';	
		}
	}


