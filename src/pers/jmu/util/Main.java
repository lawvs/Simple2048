/**
 * Simple2048
 * This software is supplied under the terms of a license agreement or nondisclosure
 * agreement
 * Copyright(c) 2016. All Rights Reserved.
 */
package pers.jmu.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pers.jmu.view.ViewInterface;

/**
 * @since jdk1.8
 * @see javafx.application.Application
 * @version 创建时间：2016年7月5日20:55:38
 *
 *          Simple2048主函数
 */
public class Main {

	/**
	 * 使用反射启动游戏
	 *
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// 从配置文件中查找view外观类
		Class<? extends ViewInterface> viewClass = null;
		Method gameStart = null;
		String className = Config.getValue("viewClass");
		try {
			viewClass = (Class<? extends ViewInterface>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			Log.err("ClassNotFoundException " + className, e);
			e.printStackTrace();
			return;
		}

		// 查找gamestart方法
		try {
			gameStart = viewClass.getDeclaredMethod("gameStart");
		} catch (NoSuchMethodException e) {
			Log.err("NoSuchMethodException", e);
			e.printStackTrace();
		} catch (SecurityException e) {
			Log.err("SecurityException", e);
			e.printStackTrace();
		}

		// 实例化ViewInterface viewInst
		ViewInterface viewInst = null;
		try {
			viewInst = viewClass.newInstance();
		} catch (InstantiationException e) {
			Log.err("InstantiationException", e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.err("IllegalAccessException", e);
			e.printStackTrace();
		}

		// 调用gamestart方法
		try {
			gameStart.invoke(viewInst);
		} catch (IllegalAccessException e) {
			Log.err("IllegalAccessException", e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.err("IllegalArgumentException", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			Log.err("InvocationTargetException", e);
			e.printStackTrace();
		}
		return;

	}

}
