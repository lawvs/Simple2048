package pers.jmu.test;

import java.util.Scanner;

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
		game.setCardValue(3, 0, 8);
		game.setCardValue(3, 1, 8);
		displayStatus(game.getCardStatus());

		while (true) {
			Scanner sc = new Scanner(System.in);
			String s;
			s = sc.next();
			char c;
			c = s.charAt(0);
			switch (c) {
			case 'w':
				game.slideTo(Game.SLIDE_UP);
				break;
			case 's':
				game.slideTo(Game.SLIDE_DOWN);
				break;
			case 'a':
				game.slideTo(Game.SLIDE_LEFT);
				break;
			case 'd':
				game.slideTo(Game.SLIDE_RIGHT);
				break;
			case 'r':
				game.restart();
				System.out.println("重新开始");
				break;
			case 'm':
				System.out.println("最高分：" + game.getMaxScore());
			case 'e':
				System.out.println("当前分数:" + game.getNowScore());
				break;
			case 'u':
				game.undoStatus();
				System.out.println("当前可回退次数:" + game.undoCount());
				break;
			case 'q':
				sc.close();
				System.out.println("离开游戏");
				System.exit(0);
				break;
			default:
				System.out.println(c);
				break;
			}
			displayStatus(game.getCardStatus());
		}

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
