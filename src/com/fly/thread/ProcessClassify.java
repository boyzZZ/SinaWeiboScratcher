package com.fly.thread;

import java.sql.SQLException;

import javax.swing.JProgressBar;

import com.fly.function.HTMLTool;
import com.fly.store.StorageTool;

public class ProcessClassify implements Runnable {

	private JProgressBar progressbar=new JProgressBar();
	private StorageTool store=new StorageTool();
	public ProcessClassify(JProgressBar j){
		this.progressbar=j;
	}
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int all_weibos=HTMLTool.getWeibosNumber();
		int finish_weibos=0;
		try {
			finish_weibos = store.getFinishedClassifiedNumber();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		progressbar.setMinimum(0);
		progressbar.setMaximum(all_weibos);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		while(true){
			try {
				finish_weibos=store.getFinishedClassifiedNumber();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
