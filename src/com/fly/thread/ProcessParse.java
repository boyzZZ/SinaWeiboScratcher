package com.fly.thread;

import javax.swing.JProgressBar;

import com.fly.function.HTMLTool;

public class ProcessParse implements Runnable {
 
	private JProgressBar progressbar=new JProgressBar();
	public ProcessParse(JProgressBar j){
		this.progressbar=j;
	}
	public void run() {
		int all_weibos=HTMLTool.getWeibosNumber();
		int finish_weibos=HTMLTool.getFinishedNumber();
		progressbar.setMinimum(0);
		progressbar.setMaximum(all_weibos);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		while(true){
			finish_weibos=HTMLTool.getFinishedNumber();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressbar.setValue(finish_weibos);
			if(finish_weibos>=all_weibos){
				break;
			}
		}	
		
		
	}

}
