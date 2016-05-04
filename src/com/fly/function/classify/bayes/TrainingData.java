package com.fly.function.classify.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

//ѵ���������
public class TrainingData {

	private String[] AllClassifications;
	private File trainingDirecotry;

	// default constructor
	public TrainingData(String filePath) {
		try {
			trainingDirecotry = new File(filePath);

			if (!trainingDirecotry.isDirectory()) {

				throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� ["
						+ trainingDirecotry + "]");

			}
			this.AllClassifications=trainingDirecotry.list();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	};

	/**
	 * ��ѯĳһ��������,����Key���Ե�����
	 * 
	 * @param classification
	 *            �����ķ���
	 * @param key
	 *            �����Ĺؼ��֣���
	 * @return ���������а����ؼ��֣��ʵ�ѵ���ı�����Ŀ
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
	 * ����ѵ���ı���𷵻��������µ�����ѵ���ı�·����full path��
	 * 
	 * @param classification
	 *            ���������
	 * @return �������������е��ļ�·��
	 */
	public String[] getFilesPath(String classification) {
		File classDir = new File(trainingDirecotry.getPath() + File.separator+ classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = trainingDirecotry.getPath() + File.separator
					+ classification + File.separator + ret[i];
		}//����ǰ׺��Ϊ����·����classDir.list()���ص�ֻ���ļ���
		return ret;
	}

	/**
	 * 
	 * ���ظ���·�����ı��ļ�����
	 * 
	 * @param filePath
	 *            �������ı��ļ�·��
	 * @return �ı�����
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
	 * ����ѵ���ı��������е��ı���Ŀ
	 * 
	 * @return ѵ���ı��������е��ı���Ŀ
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
	 * ����ѵ���ı������ڸ��������µ�ѵ���ı���Ŀ
	 * 
	 * @param classification
	 *            �����ķ���
	 * 
	 * 
	 * @return ѵ���ı������ڸ��������µ�ѵ���ı���Ŀ
	 */

	public float getTrainingFileCountOfClassification(String classification) {

		File classDir = new File(trainingDirecotry.getPath() +File.separator +classification);

		return classDir.list().length;

	}

	/**
	 * ����ѵ���ı�������������Ŀ¼��
	 * ����õ��ļ�����Ŀ¼�ļ����д洢
	 * @return ѵ���ı����
	 */
	public String[] getTrainingClassifications() {
		return this.AllClassifications;
	};

}
