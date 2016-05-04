package com.fly.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * 继承JPanel中间类
 */
public class Test2 extends JPanel{
	private static final long serialVersionUID = 1L;
	static final int WIDTH=300;
	static final int HEIGHT=200;
	JFrame loginframe;
	
	/**
	 * @param 将插入到容器中的组件，限制，以及位置参数
	 * x为控件位于第几列，
	 * y为控件位于第几行
	 * w为控件需要占几列
	 * h为控件需要占几行
	 */
	public void add(Component c,GridBagConstraints constraints,int x,int y,int w,int h){
	    constraints.gridx=x;
	    constraints.gridy=y;
	    constraints.gridheight=w;
	    constraints.gridwidth=h;
	    add(c,constraints);
	}
	
	/**
	 * 构造方法
	 * setDefaultCloseOperation使关闭按钮有小的类库方法
	 * lay是网格组布局管理器的对象
	 * nameinput是用来输入用户名的文本域
	 * passwordinput是用来输入密码的文本域
	 * title是用来显示标题的标签
	 * name是用来显示“姓名”的标签
	 * password是用来显示“密码”的标签
	 * OK是一个按钮，用于进入系统
	 * cancel是一个按钮，适用于推出界面和系统
	 * ok.addActionListener是一个进入系统动作时间的监听方法
	 * cancel.addActionListener是一个推出系统和界面动作时间的监听方法
	 */
	public Test2(){
		loginframe=new JFrame("welcome to Java World!");
		loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout lay=new GridBagLayout();
		setLayout(lay);
		loginframe.add(this,BorderLayout.WEST);
		loginframe.setSize(WIDTH, HEIGHT);
		Toolkit kit=Toolkit.getDefaultToolkit();
		Dimension screenSize=kit.getScreenSize();
		int width=screenSize.width;
		int height=screenSize.height;
		int x=(width-WIDTH)/2;
		int y=(height-HEIGHT)/2;
		loginframe.setLocation(x,y);
		JButton ok=new JButton("登录");
		JButton cancel=new JButton("放弃");
		JLabel title=new JLabel("welcome to Java World!");
		JLabel name=new JLabel("username");
		JLabel password=new JLabel("password");
		final JTextField nameinput=new JTextField(15);
		final JTextField passwordinput=new JTextField(15);
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.fill=GridBagConstraints.NONE;
		constraints.anchor=GridBagConstraints.EAST;
		constraints.weightx=3;
		constraints.weighty=4;
		add(title,constraints,0,0,4,1);
		add(name,constraints,0,1,1,1);
		add(password,constraints,0,2,1,1);
		add(nameinput,constraints,2,1,1,1);
		add(passwordinput,constraints,2,2,1,1);
		add(ok,constraints,0,3,1,1);
		add(cancel,constraints,2,3,1,1);
		loginframe.setResizable(true);
		loginframe.setVisible(true);
		
		
	}

	public static void main(String[] args) {
	new Test2();
    }
}
