package com.fly.function.classify.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DataClear {

	public static void clear(String directory) throws IOException{
		File classDir = new File(directory);
		String[] a1=classDir.list();
		for(String a:a1){
			String[] a2=new File(directory+"\\"+a).list();
			for(String b:a2){
				File file=new File(directory+"\\"+a+"\\"+b);
				InputStreamReader isReader = new InputStreamReader(new FileInputStream(file), "GBK");
				BufferedReader reader = new BufferedReader(isReader);
				String aline;
				StringBuilder sb = new StringBuilder();
				while ((aline = reader.readLine()) != null) {
					sb.append(aline + " ");
				}
				isReader.close();
				reader.close();
				String text=sb.toString();
				String text1=ChineseSpliter.split(text, " ");
				String[] ss=ChineseSpliter.filter(text1.split(" "));
				file.createNewFile();
				OutputStreamWriter osr=new OutputStreamWriter(new FileOutputStream(file),"GBK");
				for(String sss:ss){
					if(sss==null){
						continue;
					}
				osr.write(sss+" ");}
				osr.close();
				
			}
		}
		String[] ret = classDir.list();
		for(String s:ret){
		System.out.println(s);
		}
	}
	public static void main(String[] args) {
		try {
			clear("F:\\WeiBoCategories");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

