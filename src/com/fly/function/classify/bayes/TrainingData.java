package com.fly.function.classify.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

//训练库管理器
public class TrainingData {

	private String[] AllClassifications;
	private File trainingDirecotry;

	// default constructor
	public TrainingData(String filePath) {
		try {
			trainingDirecotry = new File(filePath);

			if (!trainingDirecotry.isDirectory()) {

				throw new IllegalArgumentException("训练语料库搜索失败！ ["
						+ trainingDirecotry + "]");

			}
			this.AllClassifications=trainingDirecotry.list();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	};

	/**
	 * 查询某一个分类中,包含Key属性的数量
	 * 
	 * @param classification
	 *            给定的分类
	 * @param key
	 *            给定的关键字／词
	 * @return 给定分类中包含关键字／词的训练文本的数目
	 */

	public float getCountContainKeyOfClassification(String classification,
			String key) {
		float number = 0.01F;
		try {
			String[] filePath = getFilesPath(classification);

			for (int j = 0; j < filePath.length; j++) {

				String text = getText(filePath[j]);
				int count = 0;
				int start = 0;

				while (text.indexOf(key, start) >= 0 && start < text.length()) {
					count++;
					start = text.indexOf(key, start) + key.length();
				}
				number+=count;
			
			}

		} catch (Exception e){
			e.printStackTrace();
		}

		return number;

	}

	/**
	 * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
	 * 
	 * @param classification
	 *            给定的类别
	 * @return 给定分类下所有的文件路径
	 */
	public String[] getFilesPath(String classification) {
		File classDir = new File(trainingDirecotry.getPath() + File.separator+ classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = trainingDirecotry.getPath() + File.separator
					+ classification + File.separator + ret[i];
		}//加上前缀成为完整路径，classDir.list()返回的只是文件名
		return ret;
	}

	/**
	 * 
	 * 返回给定路径的文本文件内容
	 * 
	 * @param filePath
	 *            给定的文本文件路径
	 * @return 文本内容
	 * @throws java.io.FileNotFoundException
	 * 
	 * 
	 * @throws java.io.IOException
	 */

	public static String getText(String filePath) throws FileNotFoundException,
			IOException {
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "GBK");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();
		while ((aline = reader.readLine()) != null) {
			sb.append(aline + " ");
		}
		isReader.close();
		reader.close();
		return sb.toString();

	}

	/**
	 * 
	 * 返回训练文本集中所有的文本数目
	 * 
	 * @return 训练文本集中所有的文本数目
	 */

	public float getTrainingFileCount() {

		float number = 0F;

		for (int i = 0; i < AllClassifications.length; i++) {

			number=number +getTrainingFileCountOfClassification(AllClassifications[i]);

		}

		return number;

	}

	/**
	 * 
	 * 返回训练文本集中在给定分类下的训练文本数目
	 * 
	 * @param classification
	 *            给定的分类
	 * 
	 * 
	 * @return 训练文本集中在给定分类下的训练文本数目
	 */

	public float getTrainingFileCountOfClassification(String classification) {

		File classDir = new File(trainingDirecotry.getPath() +File.separator +classification);

		return classDir.list().length;

	}

	/**
	 * 返回训练文本类别，这个类别就是目录名
	 * 分类好的文件按子目录文件进行存储
	 * @return 训练文本类别
	 */
	public String[] getTrainingClassifications() {
		return this.AllClassifications;
	};

}
