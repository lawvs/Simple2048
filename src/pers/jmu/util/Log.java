/**
 * 通用日志模块
 * @version 创建时间：2016年6月30日22:20:09
 */
package pers.jmu.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

public class Log {
	private static String name = "Log";
	private static Logger log = Logger.getLogger(name);

	// private static Vector<List> lis = new Vector<List>();
	OutputStream output;

	public Log() {
		super();

	}

	public void initLog() throws SecurityException, IOException {
		String logPath = "Log/Log.log";// 这个是你指定的log文件的路径
		FileHandler fileHandler = new FileHandler(logPath);
		fileHandler.setFormatter(new SimpleFormatter());
		// fileHandler.limit = 50000;

		log.setUseParentHandlers(false);// 指定不使用父类的handler
		log.setLevel(Level.INFO); // 日志输出级
		log.addHandler(fileHandler);
	}

	/*
	 * public static boolean add(JTextArea textArea) { lis.add(textArea); return
	 * true; } public static boolean remove(JTextArea textArea) {
	 * lis.remove(textArea); return true; }
	 */

	private static void printLog(String str) {
		/*
		for (Logable lo : lis) {
			if (lo == null) {
				continue;
			}
			lo.log(str);
		}
		*/
		System.out.print(str);
	}

	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long nowTime = System.currentTimeMillis();
		return sdf.format(nowTime);
	}

	private static void headLog(String str) {
		printLog(getTime() + " " + str + " ");
	}

	private static void endLog() {
		printLog("\n");
	}

	public static void info(String str) {
		/*
		 * if (str == null) { return; }
		 */
		headLog("info");
		printLog(str);
		endLog();
		return;
	}

	public static void warn(String str) {
		headLog("warn");
		printLog(str);
		endLog();
		return;
	}

	public static void err(String str) {
		headLog("erro");
		printLog(str);
		endLog();
		JOptionPane.showMessageDialog(null, str, "发生错误", JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void err(String str, Exception e) {
		headLog("erro");
		printLog(str);
		endLog();
		JOptionPane.showMessageDialog(null, e.toString(), str, JOptionPane.ERROR_MESSAGE);
		return;
	}

}
