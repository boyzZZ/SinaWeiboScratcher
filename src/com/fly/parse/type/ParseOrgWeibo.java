package com.fly.parse.type;

import java.sql.SQLException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fly.base.GlobalVar;
import com.fly.model.OrgArticle;
import com.fly.parse.DateParse;
import com.fly.store.OracleStorage;
/**
 * 八大类别之一   
 * 原创无图+无限制
 * @author fly
 *
 */

public class ParseOrgWeibo implements WeiboParseProcessor{

	/**
	 * static function for Parsing 
	 * @param  Weibo Element (A DOM node)
	 * @throws SQLException 
	 */
	public  void parse(Element e) throws SQLException{
		    /**
		     * Procedure:
		     * get the data form Elements and give it 
		     * to the instance of the OrgAritcle Class
		     */
		    Elements all=e.children().get(0).children();
		    OrgArticle article=new OrgArticle();

		    if(all.size()==8){
			article.setContent(all.get(0).text());
			article.setAid(GlobalVar.getNumber(1));
			article.setPraiseamt(Integer.parseInt(all.get(1).text().substring(2,all.get(1).text().length()-1)));
			article.setTransamt(Integer.parseInt((all.get(2).text()).substring(3,(all.get(2).text().length())-1)));
			article.setCommentamt(Integer.parseInt(all.get(3).text().substring(3,all.get(3).text().length()-1)));
			article.setPubtime(DateParse.parse(all.get(7)));
			article.setCtnPic("N");
			
			//取得关于@的数据
			Elements links=all.get(0).getElementsByTag("a");
			String atothers="";
			String urls="";
			if(links.size()==0){
				article.setCtnURL("N");
				article.setAtOthers("N");
			}
			for(int j=0;j<links.size();j++){
				if((links.get(j).text().substring(0,1)).equals("@")){
					article.setAtOthers("Y");
					atothers+=links.get(j).text();
				}else{
					urls=urls+links.get(j).text();
					if(urls==""){
						article.setCtnURL("N");
					}
					else{
						article.setCtnURL("Y");
						article.setUrl(urls);
					}
				}
				if(atothers==null){
					atothers="";
				}
				article.setAtPeoples(atothers);
			}
		    }   //适应地图
		    else if(all.size()==9){
		    	article.setContent(all.get(0).text());
				article.setAid(GlobalVar.getNumber(1));
				article.setPraiseamt(Integer.parseInt(all.get(2).text().substring(2,all.get(2).text().length()-1)));
				article.setTransamt(Integer.parseInt((all.get(3).text()).substring(3,(all.get(3).text().length())-1)));
				article.setCommentamt(Integer.parseInt(all.get(4).text().substring(3,all.get(4).text().length()-1)));
				article.setPubtime(DateParse.parse(all.get(8)));
				article.setCtnPic("N");
				
				//取得关于@的数据
				Elements links=all.get(0).getElementsByTag("a");
				String atothers="";
				String urls="";
				if(links.size()==0){
					article.setCtnURL("N");
					article.setAtOthers("N");
				}
				for(int j=0;j<links.size();j++){
					if((links.get(j).text().substring(0,1)).equals("@")){
						article.setAtOthers("Y");
						atothers+=links.get(j).text();
					}else{
						urls=urls+links.get(j).text();
						if(urls==""){
							article.setCtnURL("N");
						}
						else{
							article.setCtnURL("Y");
							article.setUrl(urls);
						}
					}
					if(atothers==null){
						atothers="";
					}
					article.setAtPeoples(atothers);
				}
		    }
				OracleStorage.insertInitWeibo(article);  //将实例化的原创微博存入数据库
					
	}
}
