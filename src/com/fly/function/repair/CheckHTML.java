package com.fly.function.repair;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/*
 * HTML文件检查类，用以检查下载HTML文件是否正常
 */
public class CheckHTML {

	private ArrayList<Integer> to_be_repair=new ArrayList<Integer>();;
	public ArrayList<Integer> check(File file){
		System.out.println(file.getName());
		File[] all_htmls=file.listFiles(new SizeFileFilter());
		System.out.println(all_htmls.length);
		for(File f:all_htmls){
			System.out.println(f.getName());
			int name=Integer.parseInt(f.getName().substring(0,f.getName().length()-5));
			to_be_repair.add(name);
		}
		return to_be_repair;
	}
	
	public static boolean checkSingle(File file){
		if(file.length()<10*1024){
			return true;
		}
		return false;
	}
	
	
	
}

class SizeFileFilter implements FileFilter{

	@Override
	public boolean accept(File file) {
		if(file.length()<10*1024){
			return true;
		}
	    return false;
	}
	
}