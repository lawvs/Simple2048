package pers.jmu.test;


import pers.jmu.model.Card;
import pers.jmu.model.Game;

/**
 * @version 创建时间：2016年6月26日21:12:53
 * 
 *          用于测试Game类
 */
public class GameTest {

	public static void main(String[] args) {
		Game game = new Game();
		Card[][] gameStatus = game.getCardStatus();
		//displayStatus(gameStatus);
		game.saveStatus();
		//game.addNewCard();
		game.setCardValue(0, 1, 8);
		game.setCardValue(0, 3, 8);
		displayStatus(gameStatus);
		//game.undoStatus();
		
		System.out.println("当前可回退次数:"+game.undoCount());
		game.slideLeft();
		displayStatus(game.getCardStatus());
		return;
	}

	/*
	 * @version 创建时间：2016年6月26日21:00:00
	 * 
	 * 显示状态
	 */
	private static void displayStatus(Card[][] gameStatus) {
		for (int x = 0; x < gameStatus.length; ++x) {
			for (int y = 0; y < gameStatus[x].length; ++y) {
				System.out.print(gameStatus[x][y].getValue() + " ");
			}
			System.out.println("");
		}
		System.out.println("");
		return;
	}

}
