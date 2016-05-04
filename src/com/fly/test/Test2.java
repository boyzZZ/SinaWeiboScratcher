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
 * �̳�JPanel�м���
 */
public class Test2 extends JPanel{
	private static final long serialVersionUID = 1L;
	static final int WIDTH=300;
	static final int HEIGHT=200;
	JFrame loginframe;
	
	/**
	 * @param �����뵽�����е���������ƣ��Լ�λ�ò���
	 * xΪ�ؼ�λ�ڵڼ��У�
	 * yΪ�ؼ�λ�ڵڼ���
	 * wΪ�ؼ���Ҫռ����
	 * hΪ�ؼ���Ҫռ����
	 */
	public void add(Component c,GridBagConstraints constraints,int x,int y,int w,int h){
	    constraints.gridx=x;
	    constraints.gridy=y;
	    constraints.gridheight=w;
	    constraints.gridwidth=h;
	    add(c,constraints);
	}
	
	/**
	 * ���췽��
	 * setDefaultCloseOperationʹ�رհ�ť��С����ⷽ��
	 * lay�������鲼�ֹ������Ķ���
	 * nameinput�����������û������ı���
	 * passwordinput����������������ı���
	 * title��������ʾ����ı�ǩ
	 * name��������ʾ���������ı�ǩ
	 * password��������ʾ�����롱�ı�ǩ
	 * OK��һ����ť�����ڽ���ϵͳ
	 * cancel��һ����ť���������Ƴ������ϵͳ
	 * ok.addActionListener��һ������ϵͳ����ʱ��ļ�������
	 * cancel.addActionListener��һ���Ƴ�ϵͳ�ͽ��涯��ʱ��ļ�������
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
		JButton ok=new JButton("��¼");
		JButton cancel=new JButton("����");
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
