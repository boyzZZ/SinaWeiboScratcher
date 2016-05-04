package com.fly.parse.type;

import java.sql.SQLException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fly.base.GlobalVar;
import com.fly.model.OrgArticle;
import com.fly.parse.DateParse;
import com.fly.store.MySQLStorage;
/**
 * 八大类别之一
 * 原创有图+无限制
 * @author Administrator
 *
 */

public class ParseOrgWeiboAndPic implements WeiboParseProcessor {

	public void parse(Element e) throws SQLException{
		/**
		 * 该微博元素应该包含两个DIV，
		 * 第一包含微博内容
		 * 其他的包含其他信息
		 */
		Elements two_div=e.children();
		//No.1 get Weibo_Content from first div
		String  weibo_content=two_div.get(0).text();
		String weibo_pic_address="";
		//No.2 get Weibo Other Infomation
		Elements all_pic=two_div.get(1).getElementsByTag("a").get(0).children();
		
		weibo_pic_address+=all_pic.get(0).getElementsByTag("img").get(0).attr("src");
		
		OrgArticle article=new OrgArticle();
		
		//No.3 set the value for instance article
		article.setPakageName(weibo_pic_address);
		article.setAid(GlobalVar.getNumber(2));
		article.setContent(weibo_content);
		article.setCtnPic("Y");
		
		Element trans_A=two_div.get(1).getElementsByTag("a").get(3);
		article.setTransamt(Integer.parseInt(trans_A.text().substring(3, trans_A.text().length()-1)));
		
		Element praise_A=two_div.get(1).getElementsByTag("a").get(2);
		article.setPraiseamt(Integer.parseInt(praise_A.text().substring(2,praise_A.text().length()-1)));
		
		Element comment_A=two_div.get(1).getElementsByTag("a").get(4);
		article.setCommentamt(Integer.parseInt(comment_A.text().substring(3,comment_A.text().length()-1)));
		
		Element pubtime_Span=two_div.get(1).getElementsByClass("ct").get(0);
		article.setPubtime(DateParse.parse(pubtime_Span));
		
		Element content_Span=two_div.get(0).getElementsByClass("ctt").get(0);
		//Get the content and analyze if contain URL or @
		
		Elements links=content_Span.getElementsByTag("a");
		if(links.size()==0){
			article.setCtnURL("N");
			article.setAtOthers("N");
		}
		String atOthers="";
		String URLs="";
		for(int j=0;j<links.size();j++){
			if(links.get(j).text().substring(0, 1).equals("@")){
				article.setAtOthers("Y");
				atOthers+=links.get(j).text();
			}else{
				URLs+=links.get(j).text();
				if(URLs.equals("")){
					article.setCtnURL("N");
				}
				else{
					article.setCtnURL("Y");
				}
			}
			article.setUrl(URLs);
			article.setAtPeoples(atOthers);
		}
		
		//Store into DataBase
			MySQLStorage.insertInitWeibo(article);
		
			
		    
	}
}
