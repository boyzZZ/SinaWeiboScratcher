package com.fly.thread;

import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JProgressBar;

import com.fly.function.HTMLTool;

public class ProcessDownload implements Runnable {

	private JProgressBar progressbar=new JProgressBar();
	public ProcessDownload(JProgressBar j){
		this.progressbar=j;
	}

	public void run() {
		Properties pros=new Properties();
		try {
			pros.load(new FileInputStream("data\\temp.properties"));
		} catch (Exception e){
			e.printStackTrace();
		}	
		int total_weibo_number=Integer.parseInt(pros.getProperty("LYF_PAGENUMBER"));
		int now_finished_number=0;
		System.out.println(now_finished_number);
		progressbar.setMinimum(0);
		progressbar.setMaximum(total_weibo_number);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		while(now_finished_number<=total_weibo_number){
			now_finished_number=HTMLTool.hasFinished();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progressbar.setValue(now_finished_number);
			if(now_finished_number==total_weibo_number){
				break;
			}
		}	
		
	}

}
