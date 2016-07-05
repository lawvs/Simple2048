/**
 * Simple2048
 * This software is supplied under the terms of a license agreement or nondisclosure
 * agreement
 * Copyright(c) 2016. All Rights Reserved.
 */
package pers.jmu.util;

import javafx.application.Application;
import pers.jmu.view.GameView;

/**
 * @since jdk1.8
 * @see javafx.application.Application
 * @version 创建时间：2016年7月5日20:55:38
 *
 *          Simple2048主函数
 */
public class Main {

	/**
	 * 启动GameView的javafx窗口
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(GameView.class, args);// 启动GameView的javafx窗口

	}

}
