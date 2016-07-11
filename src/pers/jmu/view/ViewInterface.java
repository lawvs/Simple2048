package pers.jmu.view;

import pers.jmu.controller.GameController;

public interface ViewInterface {

	/**
	 * 设置控制器
	 *
	 * @param gamecontroller
	 *            the gamecontroller to set
	 */
	public void setGameController(GameController gameController);

	/**
	 * 游戏开始
	 */
	public void gameStart();

	/**
	 * 游戏结束
	 */
	public void gameover();

	/**
	 * 刷新游戏
	 */
	public void refrshGame();

}