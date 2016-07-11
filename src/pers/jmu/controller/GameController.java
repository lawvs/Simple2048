/**
 * 控制器 中介者
 * @version 创建时间：2016年7月2日01:21:07
 */
package pers.jmu.controller;

import pers.jmu.model.Card;
import pers.jmu.model.Game;
import pers.jmu.util.Config;
import pers.jmu.util.Log;
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
		
		game = new Game(this);
		return;
	}
	
	public GameController(Game game) {
		this.game = game;
		return;
	}

	/**
	 * @param gameView the gameView to set
	 */
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}
	
	public void setCardValue(int x, int y, int value) {
		boolean flag;
		flag = game.setCardValue(x, y, value);
		if (!flag) {
			Log.warn("坐标越界，设置卡片值失败");
		}
		return;
	}

	public Card[][] getCards() {
		Card[][] cards = game.getCardStatus();
		//cards[0][0].setValue(2048);
		return cards;
	}
	
	public void move(int direction) {
		boolean flag = false;
		flag = game.swipeTo(direction);
		if (flag) {
			gameView.refrshGame();
		}
		return;
	}

	public void gameover() {
		gameView.gameover();
		Log.info("游戏结束 分数：" + game.getNowScore());
		return;
	}

	public void restart() {
		game.restart();
		Log.info("开始游戏");
		return;
	}

	public void merge(int x1, int y1, int x2, int y2) {
		// TODO 动画接口
		
	}

	public void move(int toX, int toY, int fromX, int fromY) {
		// TODO 动画接口
		
	}

	public void undo() {
		game.undoStatus();
		Log.info("回退成功，当前可回退次数：" + game.undoCount());
		return;
	}

	public int getScore() {
		
		return game.getNowScore();
	}

	public int getMaxScore() {
		
		return game.getMaxScore();
	}

	public String getValue(String key) {
		return Config.getValue(key);
	}

	public void setValue(String key, String value) {
		Config.setValue(key, value);
		return;
	}

	public void saveValue(String string) {
		Config.saveValue(string);
		return;
	}

}
