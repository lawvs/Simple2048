/**
 * 控制器 中介者
 * @version 创建时间：2016年7月2日01:21:07
 */
package pers.jmu.controller;

import pers.jmu.model.Card;
import pers.jmu.model.Game;
import pers.jmu.view.GameView;

public class GameController {
	// 动作状态变量
	public final static int SWIPE_UP = 1;
	public final static int SWIPE_DOWN = 2;
	public final static int SWIPE_LEFT = 3;
	public final static int SWIPE_RIGHT = 4;
	GameView gameView;
	Game game;
	
	
	public GameController() {
		// 初始化 注册
		//gameView = new GameView();
		//gameView.setGameController(this);
		
		game = new Game();
		game.setGameController(this);
	}
	
	public Card[][] getCard() {
		return game.getCardStatus();
	}
	
	public void move(int direction) {
		if(game.swipeTo(direction) == true ){
			gameView.refrsh();
		}
		return;
	}

	public void gameover() {
		gameView.gameover();
		return;
	}

	public void merge(int x1, int y1, int x2, int y2) {
		// TODO 动画接口
		
	}

	public void move(int toX, int toY, int fromX, int fromY) {
		// TODO 动画接口
		
	}

}
