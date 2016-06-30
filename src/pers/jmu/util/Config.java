/**
 * 通用配置类
 * 
 * @version 添加时间：2016年6月30日22:23:26
 */
package pers.jmu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @version 创建时间：2016年5月12日22:30:25 
 * 
 * 单例模式  饿汉式单例
 * 读取设置 配置文件路径 resoure/config.properties
 */
public class Config {
	private String configPath = "resoure/config.properties";// 类目录下配置文件名
	private static String configName = "config.properties";
	private static Properties props;// 配置文件对象

	private static final Config conf = new Config();// 饿汉式实例化单例

	/**
	 * 初始化配置文件 装载props
	 */
	private Config() {
		// FileInputStream fis=new
		// FileInputStream(System.getProperty("user.dir")+File.separator+"config.properties");
		props = new Properties();
		loadProp();// 加载配置

	}// endof Config

	/**
	 * @return the conf
	 */
	public static Config getConf() {
		return conf;
	}

	/**
	 * @return props实例
	 */
	public static Properties getProp() {
		return props;
	}

	public void loadProp() {
		File file = new File(configName);
		if (file.exists()) {// 当前目录下文件存在，尝试读取当前目录下配置
			try (InputStream ins = new FileInputStream(file);) {
				props.load(ins);
				Log.info("加载当前目录下配置");
			} catch (IOException e) {// 读取失败，读取类目录下配置
				e.printStackTrace();
				Log.err("加载当前目录下配置发生异常 IOException", e);
			}
		} else {
			try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream(configPath);) {
				props.load(ins);
				Log.info("加载类目录下配置文件");
			} catch (IOException e1) {
				e1.printStackTrace();
				Log.err("加载类目录下配置文件时发生异常 IOException", e1);
			}
		}
		return;
	}

	/**
	 * Strings access
	 * 
	 * @param key
	 *            key值
	 * @return key 键对应的值
	 */
	public static String getValue(String key) {
		String s = props.getProperty(key);
		if (s != null) {
			return s;
			// return toChineseCode(s);
			// return s;
		}
		Log.warn("配置文件中不存在 " + key);
		return "!" + key + "!";
	}

	/*
	 * //编码转换 //ISO-8859-1 GB2312 GBK UTF-8 Unicode private static String
	 * toChineseCode(String oldStr){ String value=null; try { value =new
	 * String(oldStr.getBytes("ISO-8859-1"),"UTF-8"); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); } return value; }
	 */

	/**
	 * 设置某个key的值。
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setValue(String key, String value) {
		Log.info("尝试修改配置 " + key + " " + getValue(key));
		props.setProperty(key, value);// 修改键值
		Log.info("修改配置完成 " + key + " " + value);
		return;
	}// endof setValue

	/**
	 * 保存当前设置到配置文件 成功返回true 失败返回false 将字符串写在配置文件头
	 * 
	 * @param str
	 *            写在配置头的信息
	 */
	public static boolean saveValue(String str) {
		try (OutputStream fos = new FileOutputStream(configName)) {
			// 保存键值 将此 Properties表中的属性列表（键和元素对）写入输出流
			props.store(fos, str);
			Log.info("保存配置成功 " + str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.err("保存配置文件时发生异常 FileNotFoundException", e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.err("保存配置文件时发生异常 IOException", e);
			return false;
		}

		return true;
	}// endof setValue

	/**
	 * 读取properties的全部信息
	 * 
	 * @throws FileNotFoundException
	 *             配置文件没有找到
	 * @throws IOException
	 *             关闭资源文件，或者加载配置文件错误
	 *
	 */
	public Map<String, String> readAllProperties() throws FileNotFoundException, IOException {
		// 保存所有的键值
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String Property = props.getProperty(key);
			map.put(key, Property);
		}
		return map;
	}

}
