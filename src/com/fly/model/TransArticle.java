package com.fly.model;

public class TransArticle {
	private String orgAuthor;
	private String orgContent;
	private int orgtransamt;
	private int orgpraiseamt;
	private int orgcommentamt;
	private String orpubtime;
	private boolean orgisCtnPic;
	private boolean orgisCtnURL;
	private boolean orgisAtOthers;
	private String orgpakageName;
	private String orgurl;
	private String orgAtPeoples;
	private String aid;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	private String content;
	//转发微博的Content 就是转发理由，所以2选1，去掉content
//	private String transreason;
	private int transamt;
	private int praiseamt;
	private int commentamt;
	private String pubtime;
	private boolean isAtOthers;
	private String AtPeoples;
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}

	private String Category;
	public TransArticle(){
		
	}
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
//	public String getTransreason() {
//		return transreason;
//	}
//	public void setTransreason(String transreason) {
//		this.transreason = transreason.replace("'", "''");
//	}
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
	public char isAtOthers() {
		if(isAtOthers){
			return 'Y';
		}
		return 'N';
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
	public String getAtPeoples() {
		return AtPeoples;
	}
	public void setAtPeoples(String atPeoples) {
		AtPeoples = atPeoples;
	}
	public String getOrgAuthor() {
		return orgAuthor;
	}
	public void setOrgAuthor(String orgAuthor) {
		this.orgAuthor = orgAuthor;
	}
	public String getOrgContent() {
		return orgContent.replace("'", "''");
	}
	public void setOrgContent(String orgContent) {
		
		this.orgContent = orgContent.replace("'", "''");
	}
	public int getOrgtransamt() {
		return orgtransamt;
	}
	public void setOrgtransamt(int orgtransamt) {
		this.orgtransamt = orgtransamt;
	}
	public int getOrgpraiseamt() {
		return orgpraiseamt;
	}
	public void setOrgpraiseamt(int orgpraiseamt) {
		this.orgpraiseamt = orgpraiseamt;
	}
	public int getOrgcommentamt() {
		return orgcommentamt;
	}
	public void setOrgcommentamt(int orgcommentamt) {
		this.orgcommentamt = orgcommentamt;
	}
	public String getOrpubtime() {
		return orpubtime;
	}
	public void setOrpubtime(String orpubtime) {
		this.orpubtime = orpubtime;
	}
	public char isOrgisCtnPic() {
		if(orgisCtnPic){
			return 'Y';
		}
		return 'N';
	}
	public void setOrgisCtnPic(String orgisCtnPic) {
		if(orgisCtnPic.equals("Y")){
			this.orgisCtnPic = true;}
		else{
				this.orgisCtnPic=false;
			}
	}
	public char isOrgisCtnURL() {
		if(orgisCtnURL){
			return 'Y';
		}
		return 'N';
	}
	public void setOrgisCtnURL(String orgisCtnURL) {
		if(orgisCtnURL.equals("Y")){
			this.orgisCtnURL = true;}
		else{
				this.orgisCtnURL=false;
			}
	}
	public char isOrgisAtOthers() {
		if(orgisAtOthers){
			return 'Y';
		}
		return 'N';	
	}
	public void setOrgisAtOthers(String orgisAtOthers) {
		if(orgisAtOthers.equals("Y")){
			this.orgisAtOthers = true;}
		else{
				this.orgisAtOthers=false;
			}
	}
	public String getOrgpakageName() {
		return orgpakageName;
	}
	public void setOrgpakageName(String orgpakageName) {
		this.orgpakageName = orgpakageName;
	}
	public String getOrgurl() {
		return orgurl;
	}
	public void setOrgurl(String orgurl) {
		this.orgurl = orgurl;
	}
	public String getOrgAtPeoples() {
		return orgAtPeoples;
	}
	public void setOrgAtPeoples(String orgAtPeoples) {
		this.orgAtPeoples = orgAtPeoples;
	}
	
	public String cleanS(String content){
		return content.replace("'", "''");
	}
}
