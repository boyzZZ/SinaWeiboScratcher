package com.fly.parse.type;

import java.sql.SQLException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fly.base.GlobalVar;
import com.fly.model.TransArticle;
import com.fly.parse.DateParse;
import com.fly.store.StorageTool;

public class ParseTransWeiboAndPicLimited implements WeiboParseProcessor {

	@Override
	public void parse(Element e) throws Exception {
		/**
	     * Procedure:
	     * get the data form Elements and give it 
	     * to the instance of the OrgAritcle Class
	     */
	    TransArticle article=new TransArticle();
	    article.setAid(GlobalVar.getNumber(4));
	    
	    //先获取每个字段的数据
	    /*
	     * private String orgAuthor;
	     * private String orgContent;
	     * private int orgtransamt;
	     * private int orgpraiseamt;
	     * private int orgcommentamt;
	     * private String orpubtime;
	     * private boolean orgisCtnPic;
	     * ........
	     */
	    Element firstDiv=e.children().get(0);
	    String org_author=firstDiv.getElementsByClass("cmt").get(1).getElementsByTag("a").get(0).text();
	    String org_content=firstDiv.getElementsByClass("ctt").get(0).text();
	    String org_pic_package="";
	    if(firstDiv.children().size()==2){
	    	org_pic_package=e.children().get(1).getElementsByTag("img").get(0).attr("src");
	    }else{
	     org_pic_package=firstDiv.children().get(2).attr("href");
	    }
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
	    article.setOrgpakageName(org_pic_package);
	    
	    Element secondDiv=e.children().get(1);
	    int org_praiseAmt=Integer.parseInt(secondDiv.getElementsByClass("cmt").get(0).text().substring(2, secondDiv.getElementsByClass("cmt").get(0).text().length()-1));
	    int org_transAmt=Integer.parseInt(secondDiv.getElementsByClass("cmt").get(1).text().substring(5, secondDiv.getElementsByClass("cmt").get(1).text().length()-1));
	    int org_commentAmt=Integer.parseInt(secondDiv.getElementsByClass("cc").text().substring(5,secondDiv.getElementsByClass("cc").text().length()-1));
	    
	    article.setOrgpraiseamt(org_praiseAmt);
	    article.setOrgtransamt(org_transAmt);
	    article.setOrgcommentamt(org_commentAmt);
	    
	    Element thirdDiv=e.children().get(2);
	    String reason_trans=thirdDiv.ownText().substring(0,thirdDiv.ownText().length()-8);
	    Elements all_a=thirdDiv.getElementsByTag("a");
	    int praise_amt;
	    int trans_amt;
		int comment_amt;
	    
	    praise_amt=Integer.parseInt(all_a.get(all_a.size()-5).text().substring(2,all_a.get(all_a.size()-5).text().length()-1));
	    trans_amt=Integer.parseInt(thirdDiv.getElementsByClass("cmt").get(1).text().substring(3,thirdDiv.getElementsByClass("cmt").get(1).text().length()-1));
	    comment_amt=Integer.parseInt(all_a.get(all_a.size()-4).text().substring(3,all_a.get(all_a.size()-4).text().length()-1));
	    
	    String pubtime=DateParse.parse(thirdDiv.getElementsByClass("ct").get(0));
		article.setContent(reason_trans);
		article.setPraiseamt(praise_amt);
		article.setTransamt(trans_amt);
		article.setCommentamt(comment_amt);
		article.setPubtime(pubtime);
		
		article.setOrgisCtnPic("Y");
		
		
		
		//对转发理由进行分析
		Elements as=thirdDiv.getElementsByTag("a");
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
			new StorageTool().insertTransWeibo(article);  //将实例化的原创微博存入数据库
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		
	}

}
