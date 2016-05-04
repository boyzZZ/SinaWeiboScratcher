package com.fly.test;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TEST3 {
	public static void main(String[] args) {
		new TEST3();
	}
      public TEST3(){
	  JFrame jf=new JFrame("practice 1");
	  jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  jf.setSize(350, 500);
	  GridBagLayout gridlay=new GridBagLayout();
	  GridBagConstraints constraints=new GridBagConstraints();
	  constraints.fill=GridBagConstraints.NONE;
	  constraints.weightx=10;
	  constraints.weighty=10;
	  jf.setLayout(gridlay);
	  JPanel panel=(JPanel)jf.getContentPane();
	  JButton j1=new JButton("按键1");
	  JButton j2=new JButton("按键2");
	  JButton j3=new JButton("按键3");
	  JButton j4=new JButton("按键4");
	  JLabel label=new JLabel("标签");
	  add(panel,j1,constraints,0,0,1,1);
	  add(panel,j2,constraints,1,0,1,1);
	  add(panel,j3,constraints,2,0,1,1);
	  add(panel,j4,constraints,0,1,1,1);
	  add(panel,label,constraints,1,1,1,1);
	  jf.setVisible(true);
	  
     }
     public void  add(JPanel panel,Component c,GridBagConstraints constraints,int x,int y,int w,int h){
 	    constraints.gridx=x;
 	    constraints.gridy=y;
 	    constraints.gridheight=w;
 	    constraints.gridwidth=h;
 	    panel.add(c,constraints);
 	}
}
