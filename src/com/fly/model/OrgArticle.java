package com.fly.model;
/*
 * 原始微博对应的Model
 */
public class OrgArticle {

	//对应微博属性
	private String aid;
	private String content;
	private int transamt;
	private int praiseamt;
	private int commentamt;
	private String pubtime;
	private boolean isCtnPic;
	private boolean isCtnURL;
	private boolean isAtOthers;
	private String pakageName;
	private String url;
	private String AtPeoples;
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	private String Category;
	//getter和setters方法
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content.replace("'", "''");
	}
	public int getTransamt() {
		return transamt;
	}
	public void setTransamt(int transamt) {
		this.transamt = transamt;
	}
	public int getPraiseamt() {
		return praiseamt;
	}
	public void setPraiseamt(int praiseamt) {
		this.praiseamt = praiseamt;
	}
	public int getCommentamt() {
		return commentamt;
	}
	public void setCommentamt(int commentamt) {
		this.commentamt = commentamt;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public char isCtnPic() {
		if(isCtnPic)
			return 'Y';
		else{
			return 'N';
		}
	}
	public void setCtnPic(String isCtnPic) {
		if(isCtnPic.equals("Y")){
		this.isCtnPic = true;}
		else{
			this.isCtnPic=false;
		}
	}
	public char isCtnURL() {
		if(isCtnURL)
			return 'Y';
		else{
			return 'N';
		}
	}
	public void setCtnURL(String isCtnURL) {
		if(isCtnURL.equals("Y")){
			this.isCtnURL = true;}
			else{
				this.isCtnURL=false;
			}
	}
	public char isAtOthers() {
		if(isAtOthers)
			return 'Y';
		else{
			return 'N';
		}
	}
	public void setAtOthers(String isAtOthers) {
		if(isAtOthers==null){
			isAtOthers="";
		}
		if(isAtOthers.equals("Y")){
			this.isAtOthers = true;}
			else{
				this.isAtOthers=false;
			}
	}
	public String getPakageName() {
		return pakageName;
	}
	public void setPakageName(String pakageName) {
		this.pakageName = pakageName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAtPeoples() {
		return AtPeoples;
	}
	public void setAtPeoples(String atPeoples) {
		AtPeoples = atPeoples;
	}
	public OrgArticle(){
		
	}
	public OrgArticle(String aid, String content, int transamt,
			int praiseamt, int commentamt, String pubtime, boolean isCtnPic,
			boolean isCtnURL, boolean isAtOthers, String pakageName,
			String url, String atPeoples) {
		super();
		this.aid = aid;
		this.content = content;
		this.transamt = transamt;
		this.praiseamt = praiseamt;
		this.commentamt = commentamt;
		this.pubtime = pubtime;
		this.isCtnPic = isCtnPic;
		this.isCtnURL = isCtnURL;
		this.isAtOthers = isAtOthers;
		this.pakageName = pakageName;
		this.url = url;
		AtPeoples = atPeoples;
	}
	
	
}
