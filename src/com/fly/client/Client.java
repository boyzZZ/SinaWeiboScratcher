package com.fly.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.fly.function.ClassifyWeiBo;
import com.fly.function.GetCookie;
import com.fly.function.HTMLTool;
import com.fly.function.repair.CheckHTML;
import com.fly.model.OrgArticle;
import com.fly.model.TransArticle;
import com.fly.store.MySQLStorage;
import com.fly.thread.ProcessDownload;
import com.fly.thread.ProcessParse;

/**
 * 主程序运行起点，也是所有图形化界面的类，该类的编写过程由NetBeans来编写
 * @author fly
 *
 */
public class Client {
	
	public final static String HTML_PATH = "html\\";
	public final static String CATEGORY_DIR = "category";
	
	/**
	 * 系统初始化，各种文件夹的准备
	 */
	private void init(){
		
		File html_dir=new File("html");
		if(!html_dir.exists()){
			html_dir.mkdir();
		}else{
			html_dir.delete();  //无法删除，目录删除必须为空
		}
		File code_gif=new File("data/code.gif");
		if(code_gif.exists()){
			code_gif.delete();
		}else{
			try {
				code_gif.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//微博客户端，程序入口处
	public static void main(String[] args) {	
		new Client().init();
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
     	new LoginClient().setVisible(true);
   }
}


/**
 * 登录界面
 * @author fly
 *
 */

class LoginClient extends javax.swing.JFrame {

    
	private static final long serialVersionUID = 1L;

    public LoginClient() {
        initComponents();
    }

    //所有的组件                     
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JLabel Welcome;
    private javax.swing.JPanel WelcomePanel;
    private javax.swing.JLabel jCodeImage;
    private javax.swing.JLabel jCodeLabel;
    private javax.swing.JTextPane jCodeTextPane;
    private javax.swing.JButton jLoginBtn;
    private javax.swing.JLabel jLoginLabel;
    private javax.swing.JPanel jLoginPanel;
    private javax.swing.JTextPane jLoginTextPane;
    private javax.swing.JTextPane jPassTextPane;
    private javax.swing.JLabel jPasswordLabel;
    private javax.swing.JButton jResetBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
                               
    private void initComponents() {

    	//组件初始化
        WelcomePanel = new javax.swing.JPanel();
        Welcome = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        jResetBtn = new javax.swing.JButton();
        jLoginBtn = new javax.swing.JButton();
        jLoginPanel = new javax.swing.JPanel();
        jCodeLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jCodeTextPane = new javax.swing.JTextPane();
        jLoginLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLoginTextPane = new javax.swing.JTextPane();
        jPasswordLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPassTextPane = new javax.swing.JTextPane();
        jCodeImage = new javax.swing.JLabel();
        
        //准备工作，将验证码获取到
        try {
			GetCookie.getCode();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
        byte[] bytes=new byte[2048];
		try {
			new FileInputStream(new File("data\\code.gif")).read(bytes);
		} catch (Exception e){
			e.printStackTrace();
		}
		ImageIcon icon=new ImageIcon(bytes);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(400,200);

        //Welcome Panel begin ....
        Welcome.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        Welcome.setText("欢迎使用新浪下载分类工具，请先登录！");

        javax.swing.GroupLayout WelcomePanelLayout = new javax.swing.GroupLayout(WelcomePanel);
        WelcomePanel.setLayout(WelcomePanelLayout);
        WelcomePanelLayout.setHorizontalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(Welcome)
                .addGap(51, 51, 51))
        );
        WelcomePanelLayout.setVerticalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        //Login Panel begin ....
        jResetBtn.setText("重置");

        jLoginBtn.setText("登录");
        jLoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
           	 String username=jLoginTextPane.getText();
					String password=jPassTextPane.getText();
					String code=jCodeTextPane.getText();
			
					try {
						GetCookie.getCookies(username, password,code);
					} catch (Exception e1){
						e1.printStackTrace();
					}
					
					//Hide Current JFrame
				    setVisible(false);
				    //Display ShowClient
				    new TaskClient().setVisible(true);
            }
        });
        
        jResetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
           	  jLoginTextPane.setText("");
           	  jPassTextPane.setText("");
           	  jCodeTextPane.setText("");
            }
        });

        jCodeLabel.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jCodeLabel.setText("验证码：");

        jScrollPane3.setViewportView(jCodeTextPane);

        jLoginLabel.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jLoginLabel.setText("登录名：");

        jScrollPane1.setViewportView(jLoginTextPane);

        jPasswordLabel.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jPasswordLabel.setText("密 码：");

        jScrollPane2.setViewportView(jPassTextPane);
        
        jCodeImage.setIcon(icon);

        javax.swing.GroupLayout jLoginPanelLayout = new javax.swing.GroupLayout(jLoginPanel);
        jLoginPanel.setLayout(jLoginPanelLayout);
        jLoginPanelLayout.setHorizontalGroup(
            jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCodeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jLoginPanelLayout.createSequentialGroup()
                        .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCodeLabel)
                            .addComponent(jPasswordLabel)
                            .addComponent(jLoginLabel))
                        .addGap(31, 31, 31)
                        .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jLoginPanelLayout.setVerticalGroup(
            jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCodeImage, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(WelcomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(WelcomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
    }                       
                                         
                   
}

/**
*
* @author fly
*/
class TaskClient extends javax.swing.JFrame {

	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Variables declaration - do not modify                     
	   private javax.swing.JPanel MainPanel;
	   private javax.swing.JMenuBar MenuTool;
	   private javax.swing.JPanel StepPanel;
	   private javax.swing.JLabel jLabel1;
	   private javax.swing.JLabel jLabel2;
	   private javax.swing.JLabel jLabel3;
	   private javax.swing.JLabel jLabel4;
	   private javax.swing.JMenu jMenu5;
	   private javax.swing.JMenu jMenu6;
	   private javax.swing.JMenu jMenu7;
	   private javax.swing.JMenu jMenu8;
	   private javax.swing.JMenuItem jMenuItem1;
	   private javax.swing.JMenuItem jMenuItem2;
	   private javax.swing.JMenuItem jMenuItem3;
	   private javax.swing.JMenuItem jMenuItem4;
	   private javax.swing.JMenuItem jMenuItem5;
	   private javax.swing.JMenuItem jMenuItem6;
	   private javax.swing.JMenuItem jMenuItem7;
	   private javax.swing.JMenuItem jMenuItem8;
	   private javax.swing.JMenuItem jMenuItem9;
	   private javax.swing.JProgressBar jProgressBar1;
	   private javax.swing.JProgressBar jProgressBar2;
	   private javax.swing.JProgressBar jProgressBar3;
	   // End of variables declaration 
   /**
    * Creates new form SinaWeibo
    */
   public TaskClient() {
       initComponents();
   }

                         
   private void initComponents() {

       MainPanel = new javax.swing.JPanel();
       jLabel1 = new javax.swing.JLabel();
       StepPanel = new javax.swing.JPanel();
       jLabel2 = new javax.swing.JLabel();
       jLabel3 = new javax.swing.JLabel();
       jLabel4 = new javax.swing.JLabel();
       jProgressBar1 = new javax.swing.JProgressBar();
       jProgressBar2 = new javax.swing.JProgressBar();
       jProgressBar3 = new javax.swing.JProgressBar();
       MenuTool = new javax.swing.JMenuBar();
       jMenu7 = new javax.swing.JMenu();
       jMenuItem3 = new javax.swing.JMenuItem();
       jMenuItem4 = new javax.swing.JMenuItem();
       jMenuItem1 = new javax.swing.JMenuItem();
       jMenu5 = new javax.swing.JMenu();
       jMenuItem2 = new javax.swing.JMenuItem();
       jMenuItem5 = new javax.swing.JMenuItem();
       jMenu6 = new javax.swing.JMenu();
       jMenuItem6 = new javax.swing.JMenuItem();
       jMenuItem7 = new javax.swing.JMenuItem();
       jMenuItem8 = new javax.swing.JMenuItem();
       jMenu8 = new javax.swing.JMenu();
       jMenuItem9 = new javax.swing.JMenuItem();

       setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       setResizable(false);
       setLocation(400,200);

       MainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("数据获取"));

       jLabel1.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
       jLabel1.setText("请先按照以下步骤完成数据获取，过程会耗费一段时间，请耐心等待！");

       StepPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Step"));

       jLabel2.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
       jLabel2.setText("No.1 下载微博     下载-->开始下载-->修复文件");

       jLabel3.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
       jLabel3.setText("No.2 解析微博     解析-->开始解析");

       jLabel4.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
       jLabel4.setText("No.3 微博分类     分类-->设置训练集-->开始分类");

       javax.swing.GroupLayout StepPanelLayout = new javax.swing.GroupLayout(StepPanel);
       StepPanel.setLayout(StepPanelLayout);
       StepPanelLayout.setHorizontalGroup(
           StepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(StepPanelLayout.createSequentialGroup()
               .addGroup(StepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addGroup(StepPanelLayout.createSequentialGroup()
                       .addGap(31, 31, 31)
                       .addGroup(StepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                           .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                           .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                   .addGroup(StepPanelLayout.createSequentialGroup()
                       .addGap(76, 76, 76)
                       .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                   .addGroup(StepPanelLayout.createSequentialGroup()
                       .addGap(76, 76, 76)
                       .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                   .addGroup(StepPanelLayout.createSequentialGroup()
                       .addGap(75, 75, 75)
                       .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
               .addContainerGap(84, Short.MAX_VALUE))
       );
       StepPanelLayout.setVerticalGroup(
           StepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(StepPanelLayout.createSequentialGroup()
               .addContainerGap()
               .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
               .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGap(18, 18, 18)
               .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addContainerGap(38, Short.MAX_VALUE))
       );

       javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
       MainPanel.setLayout(MainPanelLayout);
       MainPanelLayout.setHorizontalGroup(
           MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(MainPanelLayout.createSequentialGroup()
               .addGap(47, 47, 47)
               .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addComponent(StepPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addContainerGap(63, Short.MAX_VALUE))
       );
       MainPanelLayout.setVerticalGroup(
           MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(MainPanelLayout.createSequentialGroup()
               .addContainerGap()
               .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGap(18, 18, 18)
               .addComponent(StepPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addContainerGap())
       );

       jMenu7.setText("下载");

       jMenuItem3.setText("开始下载");
       jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
        	   HTMLTool.downloadHTML(HTMLTool.getlist());
               new Thread(new ProcessDownload(jProgressBar1)).start();
           }
       });
       jMenu7.add(jMenuItem3);
   
       
       jMenuItem4.setText("修复文件");
       jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
        	   ArrayList<Integer> list=new ArrayList<Integer>();
        	   list=new CheckHTML().check(new File("html\\"));
        	   System.out.println(list.size());
        	   if(list.size()!=0){
               HTMLTool.repair(list);
               System.out.println("now is for recursively repair"+list.size());
        	   }
           }
        	   
           
       });
       jMenu7.add(jMenuItem4);

       jMenuItem1.setText("查看文件位置");
       jMenu7.add(jMenuItem1);

       MenuTool.add(jMenu7);

       jMenu5.setText("解析");

       jMenuItem2.setText("开始解析");
       jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
        	  try {
				HTMLTool.parseHTML();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	  try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  Thread t1=new Thread(new ProcessParse(jProgressBar2));
        	  t1.start();
           }
       });
       jMenu5.add(jMenuItem2);

       jMenuItem5.setText("解析结果显示");
       jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               jMenuItem5ActionPerformed(evt);
           }
       });
       jMenu5.add(jMenuItem5);

       MenuTool.add(jMenu5);

       jMenu6.setText("分类");

       jMenuItem6.setText("设置训练集");
       jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               
           }
       });
       jMenu6.add(jMenuItem6);
       
       jMenuItem7.setText("开始分类");
       jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               new ClassifyWeiBo().classify();
           }
       });
       jMenu6.add(jMenuItem7);

       jMenuItem8.setText("分类结果统计");
       jMenu6.add(jMenuItem8);

       MenuTool.add(jMenu6);

       jMenu8.setText("展示");
       jMenuItem9.setText("展示页面");
       jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
        	   setVisible(false);
               new ShowClient().setVisible(true);
           }
       });
       jMenu8.add(jMenuItem9);
       MenuTool.add(jMenu8);

       setJMenuBar(MenuTool);

       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
       getContentPane().setLayout(layout);
       layout.setHorizontalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGap(0, 629, Short.MAX_VALUE)
           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                   .addContainerGap(12, Short.MAX_VALUE)
                   .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addContainerGap(33, Short.MAX_VALUE)))
       );
       layout.setVerticalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGap(0, 404, Short.MAX_VALUE)
           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                   .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
       );

       pack();
   }// </editor-fold>                        

   private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {                                           
   }                                          

  
  

                  
}

class ShowClient1 extends JFrame{
	/**
	 * 展示窗口
	 */
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private JProgressBar progressbar;
	private JTextArea statics;
	private JPanel menupl;
	private JPanel mainpl;
	private JPanel main;
	
	//构造函数
	public ShowClient1(){
	this.setTitle("Si1naWeibo Tool");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize(1200,800);
	this.setLayout(new BorderLayout());
	this.setLocation(100,40);
	
	
/* 将整个页面分为2个部分，第一个部分为菜单栏，第二个部分为对应的显示页面*/
	
	//No.1主面版 （欢迎语）
	mainpl=new JPanel();
	mainpl.setBackground(Color.gray);
	mainpl.setSize(1200,600);
	JLabel lb1=new JLabel("欢迎使用SinaWeibo自动下载分类工具",JLabel.CENTER);
	mainpl.add(lb1,BorderLayout.SOUTH);
	this.add(mainpl,BorderLayout.SOUTH);
	
	//No.3 主面板 
	main=new JPanel();
	main.setBackground(Color.pink);
	this.add(main,BorderLayout.CENTER);
	//No.2 菜单栏
	menupl=new JPanel();
	menupl.setBackground(Color.LIGHT_GRAY);
	JMenuBar menubar=new JMenuBar();
	JMenu menu1=new JMenu("下载");
	JMenu menu2=new JMenu("解析");
	JMenu menu3=new JMenu("分类");
	JMenu menu4=new JMenu("展示");
	menubar.add(menu1);
	menubar.add(menu2);
	menubar.add(menu3);
	menubar.add(menu4);
	JMenuItem item1=new JMenuItem("开始下载");
	JMenuItem item2=new JMenuItem("修复文件");
	JMenuItem item3=new JMenuItem("内容显示");
	JMenuItem item9=new JMenuItem("");
	menu1.add(item1);
	menu1.add(item2);
	menu1.add(item3);
	menu1.add(item9);
	JMenuItem item4=new JMenuItem("解析微博");
	JMenuItem item5=new JMenuItem("显示解析后文本");
	menu2.add(item4);
	menu2.add(item5);
	JMenuItem item6=new JMenuItem("设置训练集");
	JMenuItem item7=new JMenuItem("开始分类");
	JMenuItem item8=new JMenuItem("分类统计");
	menu3.add(item6);
	menu3.add(item7);
	menu3.add(item8);
	
	
	item1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			progressbar=new JProgressBar();
			progressbar.setBackground(Color.red);
		    progressbar.setMaximum(924);
		    progressbar.setMinimum(0);
		    progressbar.setValue(0);
		    progressbar.setStringPainted(true);
		    progressbar.setPreferredSize(new Dimension(800,40));
		    progressbar.setLocation(200,200);
			timer=new Timer(1000,new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					int finished=HTMLTool.hasFinished();
					progressbar.setValue(finished);
				}
				
			});
			main.removeAll();
			main.add(progressbar,BorderLayout.CENTER);
			add(main,BorderLayout.CENTER);
			main.repaint();
			validate();
			
//			int pagenumber=HTMLTool.getlist();
//   		HTMLTool.downloadHTML(pagenumber);
   		timer.start();
		}
			
	});
   item2.addActionListener(new ActionListener(){
   	public void actionPerformed(ActionEvent e) {
   		
		}
		
	});
   item3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
		}
		
	});
   item4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			try {
				HTMLTool.parseHTML();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	});
   item5.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
		}
		
	});
   item6.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
		}
		
	});
   item7.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			new ClassifyWeiBo().classify();
		}
		
	});
   item8.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
		}
		
	});
   item9.addActionListener(new ActionListener(){

   	public void actionPerformed(ActionEvent e) {
			statics=new JTextArea();
			statics.setEditable(false);
			statics.setText("截至到目前，您所有的微博数为9999条，其中原创的微博有6723条，转发微博为2343条。\\r您的第一条微博始于2006年6月 说的是巴啦啦啦啦，根据您的要求，我们将微博分成了10大类别，准确度依赖与您的训练集的质量，具体展示如右图表格");
			statics.setSize(222,200);
			main.removeAll();
			main.add(statics,BorderLayout.CENTER);
			add(main,BorderLayout.CENTER);
			main.repaint();
			validate();
		}
		
		
	});
   menu4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			statics=new JTextArea();
			statics.setEditable(false);
			statics.setText("截至到目前，您所有的微博数为9999条，其中原创的微博有6723条，转发微博为2343条。\\r您的第一条微博始于2006年6月 说的是巴啦啦啦啦，根据您的要求，我们将微博分成了10大类别，准确度依赖与您的训练集的质量，具体展示如右图表格");
			statics.setSize(222,200);
			main.removeAll();
			main.add(statics,BorderLayout.CENTER);
			add(main,BorderLayout.CENTER);
			main.repaint();
			validate();
		}
   	
   });
   menupl.add(menubar);
   this.add(menupl,BorderLayout.NORTH);
   
   
	this.setVisible(true);
	}
	};



/**
 * 微博详细查询界面
 * @author fly
 *
 */
class ShowClient extends javax.swing.JFrame implements ItemListener{

	  // Variables declaration - do not modify                     
	  private javax.swing.JPanel Category_Panel;
	  private javax.swing.JMenuBar MenuTool;
	  private javax.swing.JPanel SearchPanel;
	  private javax.swing.JCheckBox jCheckBox1;
	  private javax.swing.JCheckBox jCheckBox11;
	  private javax.swing.JCheckBox jCheckBox2;
	  private javax.swing.JCheckBox jCheckBox3;
	  private javax.swing.JCheckBox jCheckBox4;
	  private javax.swing.JCheckBox jCheckBox5;
	  private javax.swing.JCheckBox jCheckBox6;
	  private javax.swing.JCheckBox jCheckBox7;
	  private javax.swing.JCheckBox jCheckBox8;
	  private javax.swing.JCheckBox jCheckBox9;
	  private javax.swing.JMenu jMenu5;
	  private javax.swing.JMenu jMenu6;
	  private javax.swing.JMenu jMenu7;
	  private javax.swing.JMenu jMenu8;
	  private javax.swing.JMenuItem jMenuItem1;
	  private javax.swing.JMenuItem jMenuItem2;
	  private javax.swing.JMenuItem jMenuItem3;
	  private javax.swing.JMenuItem jMenuItem4;
	  private javax.swing.JMenuItem jMenuItem5;
	  private javax.swing.JMenuItem jMenuItem6;
	  private javax.swing.JMenuItem jMenuItem7;
	  private javax.swing.JMenuItem jMenuItem8;
	  private javax.swing.JPanel jPanel1;
	  private javax.swing.JScrollPane jScrollPane1;
	  private javax.swing.JTable jTable1;
	  private javax.swing.JTextField jTextField1;
	  
	  private javax.swing.JButton search_ok;
	  private javax.swing.JLabel search_tips;
	  private javax.swing.JPanel showPanel;
	  private MySQLStorage store=new MySQLStorage();
	  // End of variables declaration 
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
   * Creates new form SinaWeibo
   */
  public ShowClient() {
  	this.setLocation(400,200);
      initComponents();
  }
                         
  private void initComponents() {

      SearchPanel = new javax.swing.JPanel();
      search_tips = new javax.swing.JLabel();
      
      jTextField1 = new javax.swing.JTextField();
      search_ok = new javax.swing.JButton();
      Category_Panel = new javax.swing.JPanel();
      jPanel1 = new javax.swing.JPanel();
      jCheckBox11 = new javax.swing.JCheckBox();
      jCheckBox1 = new javax.swing.JCheckBox();
      jCheckBox9 = new javax.swing.JCheckBox();
      jCheckBox5 = new javax.swing.JCheckBox();
      jCheckBox4 = new javax.swing.JCheckBox();
      jCheckBox2 = new javax.swing.JCheckBox();
      jCheckBox7 = new javax.swing.JCheckBox();
      jCheckBox8 = new javax.swing.JCheckBox();
      jCheckBox6 = new javax.swing.JCheckBox();
      jCheckBox3 = new javax.swing.JCheckBox();
      showPanel = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      jTable1 = new javax.swing.JTable();
      MenuTool = new javax.swing.JMenuBar();
      jMenu7 = new javax.swing.JMenu();
      jMenuItem3 = new javax.swing.JMenuItem();
      jMenuItem4 = new javax.swing.JMenuItem();
      jMenuItem1 = new javax.swing.JMenuItem();
      jMenu5 = new javax.swing.JMenu();
      jMenuItem2 = new javax.swing.JMenuItem();
      jMenuItem5 = new javax.swing.JMenuItem();
      jMenu6 = new javax.swing.JMenu();
      jMenuItem6 = new javax.swing.JMenuItem();
      jMenuItem7 = new javax.swing.JMenuItem();
      jMenuItem8 = new javax.swing.JMenuItem();
      jMenu8 = new javax.swing.JMenu();
      @SuppressWarnings("serial")
	DefaultTableModel defaulttablemodel = new DefaultTableModel(new Object [][] {},new String [] {"微博ID","微博分类", "类型","微博内容", "微博日期"}) {@SuppressWarnings("rawtypes")
	Class[] types = new Class [] {java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                  };

    
				public Class<?> getColumnClass(int columnIndex) {
                      return types [columnIndex];
                  }
              };

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setResizable(false);

      SearchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "查询", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 0, 12), java.awt.Color.gray)); // NOI18N

      search_tips.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
      search_tips.setText("请输入关键字进行微博的查询：");

      
      jTextField1.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
      jTextField1.setText("微博类别/微博内容/微博日期");

      search_ok.setText("查询");
      search_ok.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
        	  String key=jTextField1.getText();
              System.out.println(key);
              ArrayList<String[]> show_weibos=new ArrayList<String[]>();
              ArrayList<OrgArticle> search_org=new MySQLStorage().searchOrgWeibos(key);
              for(OrgArticle article:search_org){
            	  String[] s={article.getAid(),article.getCategory(),"原创",article.getContent(),article.getPubtime()};
            	  show_weibos.add(s);
              }
              ArrayList<TransArticle> search_trans=new MySQLStorage().searchTransWeibos(key);
              for(TransArticle article:search_trans){
            	  String[] s={article.getAid(),article.getCategory(),"转发",article.getContent(),article.getPubtime()};
            	  show_weibos.add(s);
              }
              System.out.println(show_weibos.size());
              Object[][] ob=new Object[show_weibos.size()][5];
              
              for(int i=0;i<show_weibos.size();i++){
            	  ob[i]=show_weibos.get(i);
              }
              @SuppressWarnings("serial")
			DefaultTableModel defaulttablemodel=new DefaultTableModel(ob  ,new String [] {"微博ID","微博分类", "类型","微博内容", "微博日期"}) {
            		Class<?>[] types = new Class [] {
                          java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                      };

                      public Class<?> getColumnClass(int columnIndex) {
                          return types [columnIndex];
                      }
                  
          };
              jTable1.setModel(defaulttablemodel);
              jTable1.setColumnSelectionAllowed(true);
              jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
              if (jTable1.getColumnModel().getColumnCount() > 0) {
            	  jTable1.getColumnModel().getColumn(0).setMinWidth(15);
                  jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
                  jTable1.getColumnModel().getColumn(0).setMaxWidth(80);
                  jTable1.getColumnModel().getColumn(1).setMinWidth(15);
                  jTable1.getColumnModel().getColumn(1).setPreferredWidth(60);
                  jTable1.getColumnModel().getColumn(1).setMaxWidth(80);
                  jTable1.getColumnModel().getColumn(2).setMinWidth(15);
                  jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);
                  jTable1.getColumnModel().getColumn(2).setMaxWidth(80);
                  jTable1.getColumnModel().getColumn(4).setMinWidth(15);
                  jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
                  jTable1.getColumnModel().getColumn(4).setMaxWidth(160);}
              ;}
          
      });

      javax.swing.GroupLayout SearchPanelLayout = new javax.swing.GroupLayout(SearchPanel);
      SearchPanel.setLayout(SearchPanelLayout);
      SearchPanelLayout.setHorizontalGroup(
          SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(SearchPanelLayout.createSequentialGroup()
              .addContainerGap()
              .addComponent(search_tips, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
              .addComponent(search_ok)
              .addGap(30, 30, 30))
      );
      SearchPanelLayout.setVerticalGroup(
          SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(search_tips, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
              .addComponent(search_ok, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      Category_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "分类查询", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 10))); // NOI18N

      jCheckBox11.setText("历史");
      jCheckBox11.setAlignmentX(0.5F);
      jCheckBox11.addItemListener(this);

      jCheckBox1.setText("传统");
      jCheckBox1.setAlignmentX(0.5F);
      jCheckBox1.addItemListener(this);

      jCheckBox9.setText("科学");
      jCheckBox9.setAlignmentX(0.5F);
      jCheckBox9.addItemListener(this);

      jCheckBox5.setText("西学");
      jCheckBox5.setAlignmentX(0.5F);
      jCheckBox5.addItemListener(this);

      jCheckBox4.setText("儒学");
      jCheckBox4.setAlignmentX(0.5F);
      jCheckBox4.addItemListener(this);

      jCheckBox2.setText("文化");
      jCheckBox2.setAlignmentX(0.5F);
      jCheckBox2.addItemListener(this);

      jCheckBox7.setText("建筑");
      jCheckBox7.setAlignmentX(0.5F);
      jCheckBox7.addItemListener(this);

      jCheckBox8.setText("推荐");
      jCheckBox8.setAlignmentX(0.5F);
      jCheckBox8.addItemListener(this);

      jCheckBox6.setText("反思");
      jCheckBox6.setAlignmentX(0.5F);
      jCheckBox6.addItemListener(this);

      jCheckBox3.setText("时评");
      jCheckBox3.setAlignmentX(0.5F);
      jCheckBox3.addItemListener(this);

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
              .addContainerGap()
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jCheckBox1)
                  .addComponent(jCheckBox5)
                  .addComponent(jCheckBox4)
                  .addComponent(jCheckBox11)
                  .addComponent(jCheckBox2)
                  .addComponent(jCheckBox3)
                  .addComponent(jCheckBox6)
                  .addComponent(jCheckBox7)
                  .addComponent(jCheckBox8)
                  .addComponent(jCheckBox9))
              .addContainerGap())
      );

      jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCheckBox1, jCheckBox2, jCheckBox3, jCheckBox4, jCheckBox5, jCheckBox6, jCheckBox7, jCheckBox8, jCheckBox9});

      jPanel1Layout.setVerticalGroup(
          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
              .addContainerGap()
              .addComponent(jCheckBox1)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox2)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox3)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox6)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox7)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox5)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox4)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox8)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox9)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jCheckBox11)
              .addContainerGap())
      );

      jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCheckBox1, jCheckBox2, jCheckBox3, jCheckBox4, jCheckBox5, jCheckBox6, jCheckBox7, jCheckBox8, jCheckBox9});

      javax.swing.GroupLayout Category_PanelLayout = new javax.swing.GroupLayout(Category_Panel);
      Category_Panel.setLayout(Category_PanelLayout);
      Category_PanelLayout.setHorizontalGroup(
          Category_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(Category_PanelLayout.createSequentialGroup()
              .addContainerGap()
              .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      Category_PanelLayout.setVerticalGroup(
          Category_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(Category_PanelLayout.createSequentialGroup()
              .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGap(0, 64, Short.MAX_VALUE))
      );

      showPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "查询结果", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 10))); // NOI18N

      jTable1.setBackground(new java.awt.Color(153, 204, 255));
      jTable1.setModel(defaulttablemodel);
      jTable1.setColumnSelectionAllowed(true);
      jTable1.addMouseListener(new MouseAdapter(){
     	 public void mouseClicked(MouseEvent e){
     		 if(jTable1.getSelectedColumn()==0){
     			 if(jTable1.isCellSelected(jTable1.getSelectedRow(), jTable1.getSelectedColumn())){
         			String id=(String)jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn());	
         			
         		    if(id.substring(0,2).equals("OR")){
         		    	OrgArticle article;
						try {
							article = store.getOrgArticleById(id);
							new DetailClient(article).setVisible(true);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
         		    	
         		    }
         		   if(id.substring(0,2).equals("TR")){
         			   try {
						TransArticle article=store.getTransArticleById(id);
						new DetailClient2(article).setVisible(true);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

         		   }
        			System.out.println(id);
     			 } 
     		 }
     		 if(jTable1.getSelectedColumn()==3){
     			if(jTable1.isCellSelected(jTable1.getSelectedRow(), jTable1.getSelectedColumn())){ 
     				JOptionPane.showMessageDialog(null, jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()),"Detail",JOptionPane.PLAIN_MESSAGE);
     			}
     		 }
     		 
     		 
     	 }
      });
      jTable1.getModel().addTableModelListener(new TableModelListener(){

		@Override
		public void tableChanged(TableModelEvent e) {
			int row=jTable1.getEditingRow();
			int column=jTable1.getEditingColumn();
			System.out.println(row+" "+ column);
			System.out.println(jTable1.getValueAt(row, column));
			
		}
    	  
      });
      jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      jScrollPane1.setViewportView(jTable1);
     
          
      
      jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

      javax.swing.GroupLayout showPanelLayout = new javax.swing.GroupLayout(showPanel);
      showPanel.setLayout(showPanelLayout);
      showPanelLayout.setHorizontalGroup(
          showPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
      );
      showPanelLayout.setVerticalGroup(
          showPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(showPanelLayout.createSequentialGroup()
              .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGap(0, 18, Short.MAX_VALUE))
      );

      jMenu7.setText("下载");

      jMenuItem3.setText("开始下载");
      
      jMenu7.add(jMenuItem3);

      jMenuItem4.setText("修复文件");
      jMenu7.add(jMenuItem4);

      jMenuItem1.setText("查看文件位置");
      jMenu7.add(jMenuItem1);

      MenuTool.add(jMenu7);

      jMenu5.setText("解析");

      jMenuItem2.setText("开始解析");
      jMenu5.add(jMenuItem2);

      jMenuItem5.setText("解析结果显示");
      jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
             
          }
      });
      jMenu5.add(jMenuItem5);

      MenuTool.add(jMenu5);

      jMenu6.setText("分类");

      jMenuItem6.setText("设置训练集");
      jMenu6.add(jMenuItem6);

      jMenuItem7.setText("开始分类");
      jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              
          }
      });
      jMenu6.add(jMenuItem7);

      jMenuItem8.setText("分类结果统计");
      jMenu6.add(jMenuItem8);

      MenuTool.add(jMenu6);

      jMenu8.setText("展示");
      MenuTool.add(jMenu8);

      setJMenuBar(MenuTool);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
              .addContainerGap()
              .addComponent(Category_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addContainerGap(572, Short.MAX_VALUE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(SearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addGap(101, 101, 101)
                  .addComponent(showPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addContainerGap()))
      );
      layout.setVerticalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
              .addContainerGap(77, Short.MAX_VALUE)
              .addComponent(Category_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addContainerGap())
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(SearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addContainerGap(362, Short.MAX_VALUE)))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addContainerGap(77, Short.MAX_VALUE)
                  .addComponent(showPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addContainerGap()))
      );

      pack();
  }// </editor-fold>                                                                 


@Override
public void itemStateChanged(ItemEvent e) {

	Object source=e.getItemSelectable();
	String category="";
	String operation="";
	if(source==jCheckBox1){
		 category="传统";
	}
	if(source==jCheckBox2){
		category="文化";
	}
	if(source==jCheckBox3){
		category="时评";
	}
	if(source==jCheckBox4){
		category="儒学";
	}
	if(source==jCheckBox5){
		category="西学";
	}
	if(source==jCheckBox6){
		category="反思";
	}
	if(source==jCheckBox7){
		category="建筑";
	}
	if(source==jCheckBox8){
		category="推荐";
	}
	if(source==jCheckBox9){
		category="科学";
	}
	if(source==jCheckBox11){
		category="历史";
	}
	if(e.getStateChange() == ItemEvent.DESELECTED)
		operation="-";
	if(e.getStateChange()==ItemEvent.SELECTED)
		operation="+";
	 ArrayList<String[]> show_weibos=new ArrayList<String[]>();
     ArrayList<OrgArticle> search_org=new ArrayList<OrgArticle>();
	try {
		search_org = new MySQLStorage().searchOrgByCategory(category);
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
     for(OrgArticle article:search_org){
   	  String[] s={article.getAid(),article.getCategory(),"原创",article.getContent(),article.getPubtime()};
   	  show_weibos.add(s);
     }
     ArrayList<TransArticle> search_trans=new ArrayList<TransArticle>();
	try {
		search_trans = new MySQLStorage().searchTransByCategory(category);
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
     for(TransArticle article:search_trans){
   	  String[] s={article.getAid(),article.getCategory(),"转发",article.getContent(),article.getPubtime()};
   	  show_weibos.add(s);
     }
     System.out.println(show_weibos.size());
     Object[][] ob=new Object[show_weibos.size()][5];
     
     for(int i=0;i<show_weibos.size();i++){
   	  ob[i]=show_weibos.get(i);
     }
     @SuppressWarnings("serial")
	DefaultTableModel defaulttablemodel=new DefaultTableModel(ob  ,new String [] {"微博ID","微博分类", "类型","微博内容", "微博日期"}) {
   		Class<?>[] types = new Class [] {
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
             };

             public Class<?> getColumnClass(int columnIndex) {
                 return types [columnIndex];
             }
         
 };
    if(operation.equals("+")){
     jTable1.setModel(defaulttablemodel);}
     jTable1.setColumnSelectionAllowed(true);
     jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    
     if (jTable1.getColumnModel().getColumnCount() > 0) {
   	  jTable1.getColumnModel().getColumn(0).setMinWidth(15);
         jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
         jTable1.getColumnModel().getColumn(0).setMaxWidth(80);
         jTable1.getColumnModel().getColumn(1).setMinWidth(15);
         jTable1.getColumnModel().getColumn(1).setPreferredWidth(60);
         jTable1.getColumnModel().getColumn(1).setMaxWidth(80);
         jTable1.getColumnModel().getColumn(2).setMinWidth(15);
         jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);
         jTable1.getColumnModel().getColumn(2).setMaxWidth(80);
         jTable1.getColumnModel().getColumn(4).setMinWidth(15);
         jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
         jTable1.getColumnModel().getColumn(4).setMaxWidth(160);}
     ;
          
}	


  
class DetailClient extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrgArticle org=new OrgArticle();
	/**
     * Creates new form SinaWeibo
     */
    public DetailClient(OrgArticle org) {
    	this.org=org;
        initComponents();
    }

    //所有的组件                     
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel WelcomePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
                               
    private void initComponents() {

    	//组件初始化
    	                    
        setLocation(400,200);
     
        WelcomePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        
        //数据准备 ImageIcon
        ImageIcon icon = null;
        if(org.getPakageName()==null||org.getPakageName().equals("null")){
       	try {
    		InputStream  input=new FileInputStream(new File("data\\default.jpg"));
    		byte[] buffer=new byte[1024];
    		ByteArrayOutputStream outstream=new ByteArrayOutputStream();
    		int len=0;
    		while((len=input.read(buffer))!=-1){
    			outstream.write(buffer, 0, len);
    		}
    		input.close();
    		byte[] data=outstream.toByteArray();
    		icon=new ImageIcon(data);
    	} catch (Exception e){
    	}
    		 
    	 }else{
        
    		try{
    		URL url = new URL(org.getPakageName());  
       
           HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
    
           conn.setRequestMethod("GET");  
     
           conn.setConnectTimeout(5 * 1000);  
    
           InputStream inStream = conn.getInputStream();  
     
           ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        
           byte[] buffer = new byte[1024];  
         
           int len = 0;  
       
           while( (len=inStream.read(buffer)) != -1 ){  
          
               outStream.write(buffer, 0, len);  
           }  
    
           inStream.close();  
    
           
           byte[] data = outStream.toByteArray(); 

          icon=new ImageIcon(data);
           outStream.close();  
    		}catch(Exception e){
    			e.printStackTrace();
    	}
    		
    	 }

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("日期："+org.getPubtime());

        jLabel2.setText("编号："+org.getAid());

        jLabel3.setText("分类:"+org.getCategory());

        jLabel4.setText("赞："+org.getPraiseamt());

        jLabel5.setText("评论："+org.getCommentamt());

        jLabel6.setText("转发："+org.getTransamt());
        
        jLabel7.setIcon(icon);
        jTextArea1.setText(org.getContent());
        jTextArea1.setLineWrap(true);

        javax.swing.GroupLayout WelcomePanelLayout = new javax.swing.GroupLayout(WelcomePanel);
        WelcomePanel.setLayout(WelcomePanelLayout);
        WelcomePanelLayout.setHorizontalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        WelcomePanelLayout.setVerticalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WelcomePanelLayout.createSequentialGroup()
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1))
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)))
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4))
                    .addComponent(jLabel3))
                .addGap(20, 20, 20))
        );

//        jLabel7.setText("jLabel7");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(WelcomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LoginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(WelcomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }
        
}


class DetailClient2 extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TransArticle trans=new TransArticle();
	/**
     * Creates new form SinaWeibo
     */
    public DetailClient2(TransArticle trans) {
    	this.trans=trans;
        initComponents();
    }

    //所有的组件                     
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel WelcomePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
                               
    private void initComponents() {

    	//组件初始化
    	                    
        
    	setLocation(400,200);
        WelcomePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        
        //数据准备 ImageIcon
        ImageIcon icon = null;
        if(trans.getOrgpakageName()==null||trans.getOrgpakageName().equals("null")){
       	 byte[] bytes=new byte[20000];
    		try {
    			new FileInputStream(new File("data\\default.jpg")).read(bytes);
    		} catch (Exception e){
    			e.printStackTrace();
    		}
    		 icon=new ImageIcon(bytes);
    	 }else{
        
    		try{
    		URL url = new URL(trans.getOrgpakageName());  
       
           HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
    
           conn.setRequestMethod("GET");  
     
           conn.setConnectTimeout(5 * 1000);  
    
           InputStream inStream = conn.getInputStream();  
     
           ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        
           byte[] buffer = new byte[1024];  
         
           int len = 0;  
       
           while( (len=inStream.read(buffer)) != -1 ){  
          
               outStream.write(buffer, 0, len);  
           }  
    
           inStream.close();  
    
           
           byte[] data = outStream.toByteArray(); 

          icon=new ImageIcon(data);
           outStream.close();  
    		}catch(Exception e){
    			e.printStackTrace();
    	}
    		
    	 }

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("日期："+trans.getPubtime());

        jLabel2.setText("编号："+trans.getAid());

        jLabel3.setText("分类:"+trans.getCategory());

        jLabel4.setText("赞："+trans.getPraiseamt());

        jLabel5.setText("评论："+trans.getCommentamt());

        jLabel6.setText("转发："+trans.getTransamt());
        
        jLabel7.setIcon(icon);
        jTextArea1.setText(trans.getContent());
        jTextArea1.setLineWrap(true);

        javax.swing.GroupLayout WelcomePanelLayout = new javax.swing.GroupLayout(WelcomePanel);
        WelcomePanel.setLayout(WelcomePanelLayout);
        WelcomePanelLayout.setHorizontalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, WelcomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        WelcomePanelLayout.setVerticalGroup(
            WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WelcomePanelLayout.createSequentialGroup()
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1))
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)))
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WelcomePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(WelcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WelcomePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4))
                    .addComponent(jLabel3))
                .addGap(20, 20, 20))
        );

//        jLabel7.setText("jLabel7");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(WelcomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LoginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(WelcomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }
        
}

}                                          

                  










