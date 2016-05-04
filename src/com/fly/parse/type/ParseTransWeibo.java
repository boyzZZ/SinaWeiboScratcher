package com.fly.parse.type;

import java.sql.SQLException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fly.base.GlobalVar;
import com.fly.model.TransArticle;
import com.fly.parse.DateParse;
import com.fly.store.MySQLStorage;
/**
 * 八大类别之一
 * 转发微博原创无图+无限制
 * @author fly
 *
 */
public class ParseTransWeibo {
	
  public void parse(Element e){
	 /**
	     * Procedure:
	     * get the data form Elements and give it 
	     * to the instance of the OrgAritcle Class
	     */
	    TransArticle article=new TransArticle();
	    article.setAid(GlobalVar.getNumber(3));
	    Element firstDiv=e.children().get(0);
	    if(firstDiv.getElementsByClass("cmt").get(0).getElementsByTag("a").size()==0){
	    	String org_author="未知";
	    	String org_content="原文已删";
	    	article.setOrgAuthor(org_author);
	    	article.setOrgContent(org_content);
	    	article.setOrgAtPeoples("");
	    	article.setOrgcommentamt(0);
	    	article.setOrgisAtOthers("N");
	    	article.setOrgisCtnURL("N");
	    	article.setOrgpakageName("");
	    	article.setOrgisCtnPic("N");
	    	article.setOrgpraiseamt(0);
	    	article.setOrgpraiseamt(0);
	    	article.setOrgurl("");
	    	article.setOrgtransamt(0);
	    	article.setOrpubtime("");
	    	
	    }else{
	  
	    String org_author=firstDiv.getElementsByClass("cmt").get(0).getElementsByTag("a").get(0).text();
	    String org_content=firstDiv.getElementsByClass("ctt").get(0).text();
	    
	    //处理原微博中是否包含URL和@ 其他人
	    Elements links=firstDiv.getElementsByClass("ctt").get(0).getElementsByTag("a");
		String atothers="";
		String urls="";
		if(links.size()==0){
			article.setOrgisCtnURL("N");
			article.setOrgisAtOthers("N");
		}
		for(int j=0;j<links.size();j++){
			if((links.get(j).text().substring(0,1)).equals("@")){
				article.setOrgisAtOthers("Y");
				atothers+=links.get(j).text();
			}else{
				urls=urls+links.get(j).text();
				if(urls==""){
					article.setOrgisCtnURL("N");
				}
				else{
					article.setOrgisCtnURL("Y");
					article.setOrgurl(urls);
				}
			}
			article.setOrgAtPeoples(atothers);
		}
		article.setOrgAuthor(org_author);
	    article.setOrgContent(org_content);
	    
	    int org_praiseAmt=Integer.parseInt(firstDiv.getElementsByClass("cmt").get(1).text().substring(2, firstDiv.getElementsByClass("cmt").get(1).text().length()-1));
	    int org_transAmt=Integer.parseInt(firstDiv.getElementsByClass("cmt").get(2).text().substring(5, firstDiv.getElementsByClass("cmt").get(2).text().length()-1));
	    int org_commentAmt=Integer.parseInt(firstDiv.getElementsByClass("cc").text().substring(5,firstDiv.getElementsByClass("cc").text().length()-1));
	    
	    article.setOrgpraiseamt(org_praiseAmt);
	    article.setOrgtransamt(org_transAmt);
	    article.setOrgcommentamt(org_commentAmt);
	    }
	    
	    Element secondDiv=e.children().get(1);
	    String reason_trans=secondDiv.ownText().substring(0,secondDiv.ownText().length()-8);
	    Elements all_a=secondDiv.getElementsByTag("a");
	    int praise_amt=0;
	    int trans_amt=0;
	    int comment_amt=0;
	    if(all_a.size()==8&&all_a.get(7).text().equals("投票")){
	    	//处理投票类型的微博
	    	praise_amt=Integer.parseInt(all_a.get(all_a.size()-7).text().substring(2,all_a.get(all_a.size()-7).text().length()-1));
	    	trans_amt=Integer.parseInt(all_a.get(all_a.size()-6).text().substring(3,all_a.get(all_a.size()-6).text().length()-1));
	    	comment_amt=Integer.parseInt(all_a.get(all_a.size()-5).text().substring(3,all_a.get(all_a.size()-5).text().length()-1));	
	    }else if(all_a.size()==5){
	    	//对于特殊类型的转发微博的处理
	    	praise_amt=Integer.parseInt(all_a.get(0).text().substring(2,all_a.get(0).text().length()-1));
		    trans_amt=Integer.parseInt(secondDiv.getElementsByClass("cmt").get(1).text().substring(3,secondDiv.getElementsByClass("cmt").get(1).text().length()-1));
			comment_amt=Integer.parseInt(all_a.get(1).text().substring(3,all_a.get(1).text().length()-1));
		    
	    }
	    else{
	    praise_amt=Integer.parseInt(all_a.get(all_a.size()-6).text().substring(2,all_a.get(all_a.size()-6).text().length()-1));
	    trans_amt=Integer.parseInt(all_a.get(all_a.size()-5).text().substring(3,all_a.get(all_a.size()-5).text().length()-1));
		comment_amt=Integer.parseInt(all_a.get(all_a.size()-4).text().substring(3,all_a.get(all_a.size()-4).text().length()-1));
	    }
		String pubtime=DateParse.parse(secondDiv.getElementsByClass("ct").get(0));
		article.setContent(reason_trans);
		article.setPraiseamt(praise_amt);
		article.setTransamt(trans_amt);
		article.setCommentamt(comment_amt);
		article.setPubtime(pubtime);
		
		article.setOrgisCtnPic("N");
		
		//对转发理由进行分析
		Elements as=secondDiv.getElementsByTag("a");
		String Atothers="";
		if(as.size()==0){
			article.setAtOthers("N");
		}
		for(int j=0;j<as.size();j++){
			if((as.get(j).text().substring(0,1)).equals("@")){
				article.setAtOthers("Y");
				Atothers+=as.get(j).text();
			}
			article.setAtPeoples(Atothers);
		}
	    try {
			new MySQLStorage().insertTransWeibo(article);  //将实例化的原创微博存入数据库
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		
	    
 }
}
