package com.fly.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.fly.function.HTMLTool;
import com.fly.model.OrgArticle;
import com.fly.model.TransArticle;

public class MySQLStorage {

	/*
	 * 数据库初始化
	 */
	public static Connection getConnection(){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1/sinaweibo","root","woaininannan");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	/*
	 * 插入操作，插入原创微博，将数据导入Excel表格中
	 */
	public static void insertInitWeiboInExcel(OrgArticle article,WritableWorkbook workbook,int j) throws IOException, RowsExceededException, WriteException{
        WritableSheet sheet = workbook.createSheet("First Sheet",0);
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        Label xuexiao = new Label(0,0,"微博编号");
        sheet.addCell(xuexiao);
        Label zhuanye = new Label(1,0,"微博内容");
        sheet.addCell(zhuanye);
        Label jingzhengli = new Label(2,0,"微博评论数");
        sheet.addCell(jingzhengli);
        Label praise_amt = new Label(3,0,"微博赞数");
        sheet.addCell(praise_amt);
        Label trans_amt = new Label(4,0,"微博转发数");
        sheet.addCell(trans_amt);
        Label pub_time = new Label(5,0,"微博发布时间");
        sheet.addCell(pub_time);
        
        
        Label qinghua = new Label(0,j,article.getAid());
        sheet.addCell(qinghua);
        Label jisuanji = new Label(1,j,article.getContent());
        sheet.addCell(jisuanji);
        Label gao = new Label(2,j,new Integer(article.getCommentamt()).toString());
        sheet.addCell(gao);
        Label praise = new Label(3,j,new Integer(article.getPraiseamt()).toString());
        sheet.addCell(praise);
        Label trans = new Label(4,j,new Integer(article.getTransamt()).toString());
        sheet.addCell(trans);
        Label pub = new Label(5,j,article.getPubtime());
        sheet.addCell(pub);
        
        
        
        
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        
	}
	/*
	 * 插入操作，插入原创微博数据
	 */
	public static void insertInitWeibo(OrgArticle article) throws SQLException{
		if(article.getAid()==null){
			System.out.println("这个Article有问题，ID为空！");
		}
		String SQL="INSERT INTO  WEIBO_INIT VALUES('"+article.getAid()+"','"+article.getContent()+"',"+0+","+article.getTransamt()+","
	   +article.getCommentamt()+","+article.getPraiseamt()+",'"+article.getPubtime()+"','"+article.isCtnPic()+"','"+article.isCtnURL()+"','"
	   +article.isAtOthers()+"','"+article.getPakageName()+"','"+article.getUrl()+"','"+article.getAtPeoples()+"','')";
		System.out.println(article.getPubtime());
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		stmt.execute(SQL);
		stmt.close();
		conn.close();
		
	}
	/*
	 * 插入操作，插入转发微博数据
	 */
	public void insertTransWeibo(TransArticle article) throws SQLException{
		String sql="Insert into Weibo_trans values('"+article.getAid()+"','"+article.getContent()+"',0,"+article.getTransamt()+","
				+article.getCommentamt()+","+article.getPraiseamt()+",'"+article.getPubtime()+"','"+article.getOrgAuthor()+"','"+article.getOrgContent()
				+"',0,"+article.getOrgtransamt()+","+article.getOrgcommentamt()+","+article.getOrgpraiseamt()+",'"+article.getOrpubtime()+"','"+
				article.isOrgisCtnPic()+"','"+article.isOrgisCtnURL()+"','"+article.isOrgisAtOthers()+"','"+article.getOrgpakageName()+"','"+article.getOrgurl()
				+"','"+article.getOrgAtPeoples()+"','"+article.getContent()+"','"+article.isAtOthers()+"','"+article.getAtPeoples()+"','')";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		stmt.execute(sql);
		stmt.close();
		
		if(conn!=null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * 查询并将所有的微博以文件的形式存储在本地
	 * 获取所有的微博（原创）
	 */
	public ArrayList<String> getAllInitWeibos() throws SQLException, IOException{
		ArrayList<String> list=new ArrayList<String>();
		String sql="select weibo_content from weibo_init";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		list.add(rs.getString(1));	
		}
		rs.close();
		stmt.close();
		conn.close();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			File file=new File("F:\\Weibos\\"+i+".txt");
	        file.createNewFile();
			OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(file),"GBK");
			osw.write(list.get(i));
			osw.flush();
			osw.close();
			System.out.println();
		}
		return list;
	}
	/*
	 * 查询并将所有微博以文件的形式存在本地
	 * 获取所有的微博（转发）
	 */
	public ArrayList<String> getAllTransWeibos() throws SQLException, IOException{
		ArrayList<String> list=new ArrayList<String>();
		String sql="select weibo_content,init_weibo_content from weibo_trans";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		list.add(rs.getString(1)+rs.getString(2));	
		}
		rs.close();
		stmt.close();
		conn.close();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			File file=new File("F:\\Weibos\\trans\\"+i+".txt");
	        file.createNewFile();
			OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(file),"GBK");
			osw.write(list.get(i));
			osw.flush();
			osw.close();
			System.out.println();
		}
		return list;
	}
	
	/*
	 * 查询操作
	 * 获取所有的微博（原创）
	 */
	public ArrayList<String> getAlInitWeibos() throws SQLException, IOException{
		ArrayList<String> id_list=new ArrayList<String>();
		String sql="select weibo_ID from weibo_init";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		id_list.add(rs.getString(1));	
		}
		rs.close();
		stmt.close();
		conn.close();
		return id_list;
	}
	
	/*
	 * 查询操作
	 * 获取所有的微博（转发）
	 */
	public ArrayList<String> getAlTransWeibos() throws SQLException, IOException{
		ArrayList<String> id_list=new ArrayList<String>();
		String sql="select weibo_ID from weibo_trans";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		id_list.add(rs.getString(1));	
		}
		rs.close();
		stmt.close();
		conn.close();
		return id_list;
	}
	
	/**
	 * 查询操作
	 * @param id
	 * @return content
	 * @throws SQLException 
	 */
	public String getContentById(String id) throws SQLException{
		String content="";
		String sql="select weibo_content from weibo_init where weibo_id='"+id+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		content=rs.getString(1);	
		}
		rs.close();
		stmt.close();
		conn.close();
		return content;
	}
	/**
	 * 查询操作
	 * @param id
	 * @return content
	 * @throws SQLException 
	 */
	public String getContentById1(String id) throws SQLException{
		String content="";
		String sql="select weibo_content from weibo_trans where weibo_id='"+id+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		content=rs.getString(1);	
		}
		rs.close();
		stmt.close();
		conn.close();
		return content;
	}
	
	//为原创微博添加分类属性
	public void InitWeiboAddCate(String id,String category){
		try{
		String SQL="Update WEIBO_INIT set weibo_category='"+category+"' where weibo_ID='"+id+"'";
					Connection conn=getConnection();
					Statement stmt=conn.createStatement();
					stmt.execute(SQL);
					conn.commit();
					stmt.close();
					conn.close();
	    }catch(Exception e){
		e.printStackTrace();
	     }};
		
	//为转发微博添加分类属性
	public void TransWeiboAddCate(String id,String category){
			try{
			String SQL="Update WEIBO_TRANS set weibo_category='"+category+"' where weibo_ID='"+id+"'";
						Connection conn=getConnection();
						Statement stmt=conn.createStatement();
						stmt.execute(SQL);
						conn.commit();
						stmt.close();
						conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
			}

	//查询数据库中原创微博的数量
	public int getInitWeibos() throws SQLException {
		int number=0;
		String sql="select count(*) from weibo_init";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
			number=rs.getInt(1);
		}
		rs.close();
		stmt.close();
		conn.close();
		return number;
	}
		
	//查询数据库中转发微博的数量
	public int getTransWeibos() throws SQLException {
		int number=0;
		String sql="select count(*) from weibo_trans";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
			number=rs.getInt(1);
		}
		rs.close();
		stmt.close();
		conn.close();
		return number;
	}
		
		
	
	
	
	/*以下方法均为在数据库中筛选微博的方法*/
	
	//根据日期筛选  Org
	public ArrayList<OrgArticle> searchOrgByDate(String date) throws SQLException{
		ArrayList<OrgArticle> article_list=new ArrayList<OrgArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,isctm_pic,isCtm_anchor,isat_others,pic_package,http_address," +
				"at_others,weibo_category from weibo_init where to_char(pub_date,'yyyymmdd')='"+date+"'";
		System.out.println(sql);
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			OrgArticle article=new OrgArticle();
			article.setAid(rs.getString(1));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			article.setCtnPic(rs.getString(7));
			article.setCtnURL(rs.getString(8));
			article.setAtOthers(rs.getString(9));
			article.setPakageName(rs.getString(10));
			article.setUrl(rs.getString(11));
			article.setAtOthers(rs.getString(12));
			article.setCategory(rs.getString(13));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	
	//根据日期筛选  Org (区间)
		public ArrayList<OrgArticle> searchOrgByDate(String datebegin,String dateend) throws SQLException{
			ArrayList<OrgArticle> article_list=new ArrayList<OrgArticle>();
			
			String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
					"pub_date,isctm_pic,isCtm_anchor,isat_others,pic_package,http_address," +
					"at_others,weibo_category from weibo_init where pub_date>=to_date('"+datebegin+"','yyyymmdd') " +
					"and pub_date<=to_date('"+dateend+"','yyyymmdd')";
			System.out.println(sql);
			Connection conn=getConnection();
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			while(rs.next()){
				OrgArticle article=new OrgArticle();
				article.setAid(rs.getString(1));
				String time=sdf.format(rs.getDate(6));
				article.setPubtime(time);
				article.setContent(rs.getString(2));
				article.setTransamt(rs.getInt(3));
				article.setCommentamt(rs.getInt(4));
				article.setPraiseamt(rs.getInt(5));
				article.setCtnPic(rs.getString(7));
				article.setCtnURL(rs.getString(8));
				article.setAtOthers(rs.getString(9));
				article.setPakageName(rs.getString(10));
				article.setUrl(rs.getString(11));
				article.setAtOthers(rs.getString(12));
				article.setCategory(rs.getString(13));
				article_list.add(article);
			}
			rs.close();
			stmt.close();
			conn.close();
			return article_list;
		}
	//根据内容中是否有关键字 Org
	public ArrayList<OrgArticle> searchOrgByContentKey(String key) throws SQLException{
		ArrayList<OrgArticle> article_list=new ArrayList<OrgArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,isctm_pic,isCtm_anchor,isat_others,pic_package,http_address," +
				"at_others,weibo_category from weibo_init where weibo_content like '%"+key+"%'";
		System.out.println(sql);
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			OrgArticle article=new OrgArticle();
			article.setAid(rs.getString(1));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			article.setCtnPic(rs.getString(7));
			article.setCtnURL(rs.getString(8));
			if(rs.getString(9)==null){
				article.setAtOthers("");
			}else{
			article.setAtOthers(rs.getString(9));
			}
			article.setPakageName(rs.getString(10));
			article.setUrl(rs.getString(11));
			if(rs.getString(12)==null){
				article.setAtOthers("N");
			}else{
				article.setAtOthers(rs.getString(12));
			}
			
			article.setCategory(rs.getString(13));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	//根据微博类别查找  Org
	public ArrayList<OrgArticle> searchOrgByCategory(String category) throws SQLException{
		ArrayList<OrgArticle> article_list=new ArrayList<OrgArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,isctm_pic,isCtm_anchor,isat_others,pic_package,http_address," +
				"at_others,weibo_category from weibo_init where weibo_category='"+category+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			OrgArticle article=new OrgArticle();
			article.setAid(rs.getString(1));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			article.setCtnPic(rs.getString(7));
			article.setCtnURL(rs.getString(8));
			article.setAtOthers(rs.getString(9));
			article.setPakageName(rs.getString(10));
			article.setUrl(rs.getString(11));
			article.setAtOthers(rs.getString(12));
			article.setCategory(rs.getString(13));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	
	//根据日期筛选  Trans
	public ArrayList<TransArticle> searchTransByDate(String date) throws SQLException{

		ArrayList<TransArticle> article_list=new ArrayList<TransArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,init_weibo_author,init_weibo_content,init_trans_amt,init_comment_amt," +
				"init_praise_amt,init_pub_date,init_isctm_pic,init_isCtm_anchor,init_isat_others," +
				"init_pic_package,init_http_address,init_at_others,weibo_trans_reason,isAt_others," +
				"at_others, weibo_category from weibo_trans where to_char(pub_date,'yyyymmdd')='"+date+"'";
		System.out.println(sql);
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			TransArticle article=new TransArticle();
			article.setAid(rs.getString(1));
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setOrgAuthor(rs.getString(7));
			article.setOrgContent(rs.getString(8));
			article.setOrgtransamt(rs.getInt(9));
			article.setOrgcommentamt(rs.getInt(10));
			article.setOrgpraiseamt(rs.getInt(11));
			article.setOrpubtime(rs.getString(12));
			article.setOrgisCtnPic(rs.getString(13));
			article.setOrgisCtnURL(rs.getString(14));
			article.setOrgisAtOthers(rs.getString(15));
			article.setOrgpakageName(rs.getString(16));
			article.setOrgurl(rs.getString(17));
			article.setOrgAtPeoples(rs.getString(18));
			article.setAtOthers(rs.getString(20));
			article.setAtOthers(rs.getString(21));
			article.setCategory(rs.getString(22));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	//根据日期筛选  Trans (区间)
	public ArrayList<TransArticle> searchTransByDate(String begin,String end) throws SQLException{
		ArrayList<TransArticle> article_list=new ArrayList<TransArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,init_weibo_author,init_weibo_content,init_trans_amt,init_comment_amt," +
				"init_praise_amt,init_pub_date,init_isctm_pic,init_isCtm_anchor,init_isat_others," +
				"init_pic_package,init_http_address,init_at_others,weibo_trans_reason,isAt_others," +
				"at_others, weibo_category from weibo_trans where pub_date>=to_date('"+begin+"','yyyymmdd') " +
					"and pub_date<=to_date('"+end+"','yyyymmdd')";
		System.out.println(sql);
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			TransArticle article=new TransArticle();
			article.setAid(rs.getString(1));
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setOrgAuthor(rs.getString(7));
			article.setOrgContent(rs.getString(8));
			article.setOrgtransamt(rs.getInt(9));
			article.setOrgcommentamt(rs.getInt(10));
			article.setOrgpraiseamt(rs.getInt(11));
			article.setOrpubtime(rs.getString(12));
			article.setOrgisCtnPic(rs.getString(13));
			article.setOrgisCtnURL(rs.getString(14));
			article.setOrgisAtOthers(rs.getString(15));
			article.setOrgpakageName(rs.getString(16));
			article.setOrgurl(rs.getString(17));
			article.setOrgAtPeoples(rs.getString(18));
			article.setAtOthers(rs.getString(20));
			article.setAtOthers(rs.getString(21));
			article.setCategory(rs.getString(22));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	//根据内容中是否有关键字 Trans
	public ArrayList<TransArticle> searchTransByContentKey(String key) throws SQLException{
		ArrayList<TransArticle> article_list=new ArrayList<TransArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,init_weibo_author,init_weibo_content,init_trans_amt,init_comment_amt," +
				"init_praise_amt,init_pub_date,init_isctm_pic,init_isCtm_anchor,init_isat_others," +
				"init_pic_package,init_http_address,init_at_others,weibo_trans_reason,isAt_others," +
				"at_others, weibo_category from weibo_trans where weibo_content like '%"+key+"%'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			TransArticle article=new TransArticle();
			article.setAid(rs.getString(1));
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setOrgAuthor(rs.getString(7));
			article.setOrgContent(rs.getString(8));
			article.setOrgtransamt(rs.getInt(9));
			article.setOrgcommentamt(rs.getInt(10));
			article.setOrgpraiseamt(rs.getInt(11));
			article.setOrpubtime(rs.getString(12));
			article.setOrgisCtnPic(rs.getString(13));
			article.setOrgisCtnURL(rs.getString(14));
			article.setOrgisAtOthers(rs.getString(15));
			article.setOrgpakageName(rs.getString(16));
			article.setOrgurl(rs.getString(17));
			article.setOrgAtPeoples(rs.getString(18));
			article.setAtOthers(rs.getString(20));
			article.setAtOthers(rs.getString(21));
			article.setCategory(rs.getString(22));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
	//根据微博类别查找  Trans
	public ArrayList<TransArticle> searchTransByCategory(String category) throws SQLException{
		ArrayList<TransArticle> article_list=new ArrayList<TransArticle>();
		
		String sql="select weibo_ID,Weibo_Content,trans_amt,comment_amt,praise_amt," +
				"pub_date,init_weibo_author,init_weibo_content,init_trans_amt,init_comment_amt," +
				"init_praise_amt,init_pub_date,init_isctm_pic,init_isCtm_anchor,init_isat_others," +
				"init_pic_package,init_http_address,init_at_others,weibo_trans_reason,isAt_others," +
				"at_others, weibo_category from weibo_trans where weibo_category='"+category+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(rs.next()){
			TransArticle article=new TransArticle();
			article.setAid(rs.getString(1));
			article.setContent(rs.getString(2));
			article.setTransamt(rs.getInt(3));
			article.setCommentamt(rs.getInt(4));
			article.setPraiseamt(rs.getInt(5));
			String time=sdf.format(rs.getDate(6));
			article.setPubtime(time);
			article.setOrgAuthor(rs.getString(7));
			article.setOrgContent(rs.getString(8));
			article.setOrgtransamt(rs.getInt(9));
			article.setOrgcommentamt(rs.getInt(10));
			article.setOrgpraiseamt(rs.getInt(11));
			article.setOrpubtime(rs.getString(12));
			article.setOrgisCtnPic(rs.getString(13));
			article.setOrgisCtnURL(rs.getString(14));
			article.setOrgisAtOthers(rs.getString(15));
			article.setOrgpakageName(rs.getString(16));
			article.setOrgurl(rs.getString(17));
			article.setOrgAtPeoples(rs.getString(18));
			article.setAtOthers(rs.getString(20));
			article.setAtOthers(rs.getString(21));
			article.setCategory(rs.getString(22));
			article_list.add(article);
		}
		rs.close();
		stmt.close();
		conn.close();
		return article_list;
	}
    //用户随意输入一个关键字，我们要进行判断，调用对应的方法
	public ArrayList<OrgArticle> searchOrgWeibos(String key){
		ArrayList<OrgArticle> article_list=new ArrayList<OrgArticle>();
		if(key.length()==8&&Integer.parseInt(key)>20000101){
			try {
				article_list=searchOrgByDate(key);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return article_list;
		}
		if(key.length()==17){
			String[] dates=key.split(",");
			try {
				article_list=searchOrgByDate(dates[0],dates[1]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return article_list;
		}
		try {
			article_list=searchOrgByContentKey(key);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return article_list;
	}
	public ArrayList<TransArticle> searchTransWeibos(String key){
		ArrayList<TransArticle> article_list=new ArrayList<TransArticle>();
		if(key.length()==8&&Integer.parseInt(key)>20000101){
			try {
				article_list=searchTransByDate(key);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return article_list;
		}
		if(key.length()==17){
			String[] dates=key.split(",");
			
			try {
				article_list=searchTransByDate(dates[0],dates[1]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return article_list;
		}
		try {
			article_list=searchTransByContentKey(key);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return article_list;
	}
	
	/*
	 * 以下是在GUI显示中，需要调用的方法
	 */
	public int getFinishedClassifiedNumber() throws SQLException{
		int number1=0;
		int number2=0;
		String sql="select count(*) from weibo_init where weibo_category is not null";
		String sql1="select count(*) from weibo_trans where weibo_category is not null";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs1=stmt.executeQuery(sql);
		while(rs1.next()){
			number1=rs1.getInt(1);
		}
		ResultSet rs2=stmt.executeQuery(sql1);
		while(rs2.next()){
			number2=rs2.getInt(1);
		}
		
		return number1+number2;
	}
	
	//根据id找到对应的原创详细微博信息
	public OrgArticle getOrgArticleById(String id) throws SQLException{
		OrgArticle article=new OrgArticle();
		
		String sql="select * from weibo_init where weibo_id='"+id+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		article.setAid(rs.getString(1));
		article.setContent(rs.getString(2));
		article.setPraiseamt(rs.getInt(6));
		article.setCommentamt(rs.getInt(5));
		article.setTransamt(rs.getInt(4));
		article.setPubtime(rs.getDate(7).toString());
		article.setAtOthers(rs.getString(13));
		article.setPakageName(rs.getString(11));
		article.setCategory(rs.getString(14));
		}
		rs.close();
		stmt.close();
		conn.close();
	
		return article;	
	}
	//根据id找到转发微博对应的详细信息
	public TransArticle getTransArticleById(String id) throws SQLException{
		TransArticle article=new TransArticle();
		
		String sql="select * from weibo_trans where weibo_id='"+id+"'";
		Connection conn=getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){
		article.setAid(rs.getString(1));
		article.setContent(rs.getString(2));
		article.setPraiseamt(rs.getInt(6));
		article.setCommentamt(rs.getInt(5));
		article.setTransamt(rs.getInt(4));
		article.setPubtime(rs.getDate(7).toString());
		
		article.setOrgAuthor(rs.getString(8));
		article.setOrgContent(rs.getString(9));
		article.setOrgtransamt(rs.getInt(11));
		article.setOrgcommentamt(rs.getInt(12));
		article.setOrgpraiseamt(rs.getInt(13));
		article.setOrpubtime(rs.getString(14));
		article.setOrgpakageName(rs.getString(18));
		article.setOrgAtPeoples(rs.getString(20));
		article.setAtOthers(rs.getString(23));
		article.setCategory(rs.getString(24));
		}
		rs.close();
		stmt.close();
		conn.close();
	
		return article;	
	}
	public static void main(String[] args) throws IOException {
		
			HTMLTool.parseHTML();
		
			
		}
}
