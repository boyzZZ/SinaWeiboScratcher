package com.fly.parse.type;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fly.base.GlobalVar;
import com.fly.model.OrgArticle;
import com.fly.parse.DateParse;
import com.fly.store.StorageTool;

/**
 * 八大类别之一
 * 原创无图 +有限制
 * @author fly
 *
 */
public class ParseOrgWeiboLimited implements WeiboParseProcessor{

	@Override
	public void parse(Element e) throws Exception {
		
		Elements all=e.children().get(0).children();
	    OrgArticle article=new OrgArticle();
	    
	    
		article.setContent(all.get(1).text());
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
			article.setAtPeoples(atothers);
		}
		StorageTool.insertInitWeibo(article);  //将实例化的原创微博存入数据库
		
	}

}
