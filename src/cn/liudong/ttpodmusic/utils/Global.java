package cn.liudong.ttpodmusic.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * ��Ŀ�����ࣺ��װһЩ���룬����ֱ�ӵ���
 * 
 * @author ����
 *
 */
public class Global {

	// ��ʽ�ַ���
	// intΪ����ֵ
	// ��Ϊ��̬�������������
	public static String setTime(int time) {

		// ��ȡ��ʽ�ַ�������
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		// �Ѻ���ֵת��ΪDate����
		Date date = new Date(time);
		String str = sdf.format(date);

		return str;
	}
}
