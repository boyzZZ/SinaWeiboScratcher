package com.fly.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Test1 implements ActionListener, ChangeListener {
	JFrame jf=null;
	JProgressBar progressbar;
	JLabel label;
	Timer timer;
	JButton button;
	public Test1(){
		jf=new JFrame("������ʾ��");
		Container contentPane=jf.getContentPane();
		label=new JLabel("",JLabel.CENTER);
		progressbar=new JProgressBar();
		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		progressbar.addChangeListener(this);
		progressbar.setPreferredSize(new Dimension(400,40));
		JPanel panel=new JPanel();
		button=new JButton("Start");
		button.addActionListener(this);
		panel.add(button);
		timer=new Timer(50,this);
		contentPane.add(panel,BorderLayout.NORTH);
		contentPane.add(progressbar,BorderLayout.CENTER);
		contentPane.add(label,BorderLayout.SOUTH);
		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
	}
public static void main(String[] args){
	new Test1();
}
@Override
public void stateChanged(ChangeEvent e) {
	int value=progressbar.getValue();
	//���������˶�ʱ�����������ʾ�ڱ�ǩ��
	if(e.getSource()==progressbar){
		label.setText("Ŀǰ����ɽ��ȣ�"+Integer.toString(value)+"%");
	}
	
}
@Override
public void actionPerformed(ActionEvent e) {
	//��������ťʱ����ʱ��ʼ
	if(e.getSource()==button){
		timer.start();
	}
	//�������¼����ʱ����������ʼ�仯
	if(e.getSource()==timer){
		int value=progressbar.getValue();
		if(value<100){
			value++;
			progressbar.setValue(value);
		}
		else{
			timer.stop();
			progressbar.setValue(0);
		}
	}
	
}

}
