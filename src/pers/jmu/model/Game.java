/**
 * @version 创建时间：2016年6月26日17:30:52
 * Simple2048逻辑类
 * 实现游戏逻辑
 *
 */
package pers.jmu.model;

import pers.jmu.controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 游戏逻辑类 使用数组存储2048游戏逻辑
 *
 * @version 创建时间：2016年6月26日16:30:00
 */
public class Game {
	// 动作状态变量
	private final int SWIPE_UP = 1;
	private final int SWIPE_DOWN = 2;
	private final int SWIPE_LEFT = 3;
	private final int SWIPE_RIGHT = 4;

	private GameController gameController;
	private int cardRow;// 行数
	private int cardColumn;// 列数
	private Card[][] cardStatus;// 卡片数组
	private List<Point> emptyCard;// 保存空卡片坐标
	private Stack<Card[][]> undoStack;// 储存游戏旧状态 在卡片状态改变前存储
	private int maxUndo;// 最大可undo次数
	private int maxScore;

	private int nowScore;
	private boolean isChange;// 该次移动行为是否有效
	private int step;// 行走步数

	/*
	 * 初始化游戏，新建控制器
	 */
	public Game() {
		gameController = new GameController(this);
		init();
		restart();
		return;
	}

	/**
	 * 初始化游戏 并设置控制器
	 * @param gameController 游戏控制器
	 */
	public Game(GameController gameController) {
		setGameController(gameController);
		init();
		restart();
		return;
	}

	/**
	 * @version 创建时间：2016年6月26日20:53:23 创建
	 *
	 *          获得game数组
	 */
	public Card[][] getCardStatus() {
		return cardStatus;
	}

	/*
	 * 初始化设置 从文件读取卡片行列数及其他设置
	 *
	 * 实例化变量
	 */
	private void init() {

		cardRow = Integer.valueOf(gameController.getValue("cardRow"));
		cardColumn = Integer.valueOf(gameController.getValue("cardColumn"));
		maxUndo = Integer.valueOf(gameController.getValue("maxUndo"));
		maxScore = Integer.valueOf(gameController.getValue("maxScore"));

		cardStatus = new Card[cardRow][cardColumn];
		emptyCard = new ArrayList<Point>();// 保存空卡片坐标
		undoStack = new Stack<Card[][]>();
		return;
	}

	/**
	 * 初始化 重新开始游戏
	 */
	public void restart() {
		nowScore = 0;
		step = 0;
		emptyCard.clear();
		undoStack.clear();
		// 初始化卡片
		for (int x = 0; x < cardStatus.length; ++x) {
			for (int y = 0; y < cardStatus[x].length; ++y) {
				cardStatus[x][y] = new Card();
				emptyCard.add(new Point(x, y));// 加入
			}
		}
		// 随机改变两张空卡片为2
		addNewCard();
		addNewCard();

		return;
	}

	/**
	 * @version 创建时间：2016年6月26日19:43:39
	 *
	 *          在数值为0的卡片中随机抽取一个，改变数值为2
	 */
	public boolean addNewCard() {

		if (emptyCard == null || emptyCard.isEmpty()) {
			// 添加卡片失败
			// Log.warn("卡片添加失败");
			return false;
		}
		// 随机添加卡片
		// 生成0-emptyCard.size的随机数
		int random = (int) (Math.random() * emptyCard.size());
		Point pos = emptyCard.remove(random);// 从空列表中移除卡片
		// 几率生成值为4的卡片 Math.random() > 0.1 ? 2 : 4
		setCardValue(pos.x, pos.y, 2);

		return true;
	}

	/*
	 * 遍历数组 查找空卡片
	 *
	 * @低效
	 *
	 * @return 包含所有空卡片的ArrayList<Card>
	 */
	private int calcEmptyCard() {
		emptyCard.clear();// 清空，重新计算
		// 遍历数组
		for (int x = 0; x < cardStatus.length; ++x) {
			for (int y = 0; y < cardStatus[x].length; ++y) {
				if (cardStatus[x][y].getValue() == 0) {
					emptyCard.add(new Point(x, y));
				}
			}
		}
		return emptyCard.size();
	}

	/**
	 * 设置x，y处的卡片值为value
	 *
	 * @param x
	 *            设置卡片的x坐标 取 0-width
	 * @param y
	 *            设置卡片的y坐标 取 0-height
	 * @param value
	 *            要设置卡片的值
	 * @return 成功返回true 失败返回false
	 */
	public boolean setCardValue(int x, int y, int value) {
		if (x < 0 || x >= cardRow || y < 0 || y >= cardColumn) {// 越界检查
			return false;
		}
		cardStatus[x][y].setValue(value);
		// if (value > 0){emptyCard.remove( );}
		return true;
	}

	/**
	 * 将game向一个方向移动 上下左右
	 *
	 * @param direction
	 *            移动方向 SLIDE_UP SLIDE_DOWN ...
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月30日20:54:44
	 */
	public boolean swipeTo(int direction) {
		isChange = false;// 此次移动是否有效
		saveStatus();// 可能引发小错误 undo次数可能减少

		switch (direction) {
		case SWIPE_UP:
			slideUP();
			break;
		case SWIPE_DOWN:
			slideDown();
			break;
		case SWIPE_LEFT:
			slideLeft();
			break;
		case SWIPE_RIGHT:
			slideRight();
			break;
		default:
			return false;
		}

		// 重新计算空卡片位置
		calcEmptyCard();

		if (isChange) {// 此次移动有效
			step++;// 移动有效，步数加1
			addNewCard();
			return true;
		} else {// 移动无效
			undoStack.pop();// 删除保存的状态 可能引起undo次数减少
		}
		if (!canMerge()) {
			// 游戏结束
			gameover();
			return false;
		}
		return true;
	}

	/**
	 * 将game向上滑动
	 *
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月30日20:50:00
	 */
	private boolean slideUP() {
		// 移动卡片 slideUP
		for (int y = 0; y < cardStatus[0].length; y++) {
			int tx = 0;// 临时x
			int ty = 0;// 临时y
			int v = Card.ILLEGAL;// 保留前一个方格的值 初始化为非法值
			for (int x = 0; x < cardStatus.length; x++) {
				// 值为0
				if (cardStatus[x][y].getValue() == 0) {
					// 尝试找到非0值填充，或进行合并
					for (int i = x + 1; i < cardStatus.length; i++) {
						int temval = cardStatus[i][y].getValue();
						// 找到值和空格前格子值相同，进行合并
						if (temval == v) {
							// 合并操作
							merge(tx, ty, i, y);
							x--;// 合并后该格子变为0，需重新检查
							// 清空临时变量 达到不允许重复合目的，不允许如2 2 2 2直接变为4
							v = Card.ILLEGAL;
							tx = 0;
							ty = 0;
							break;
						} else if (temval > 0) {
							v = temval;
							tx = x;
							ty = y;
							move(x, y, i, y);
							break;
						} else if (temval == Card.BLOCK) {// 障碍卡片 清空变量
							v = Card.ILLEGAL;
							tx = 0;
							ty = 0;
							break;
						}
					} // end of for

				} else if (cardStatus[x][y].getValue() == v) {
					// 合并操作
					merge(tx, ty, x, y);
					x--;// 合并后格子变为0，需重新检查
					v = Card.ILLEGAL;
					tx = 0;
					ty = 0;

				} else {
					v = cardStatus[x][y].getValue();
					tx = x;
					ty = y;
				} // end of if
			}
		}

		return true;
	}

	/**
	 * 将game向下滑动
	 *
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月29日23:27:00
	 */
	private boolean slideDown() {
		// 移动卡片 slideDown
		for (int y = 0; y < cardStatus[0].length; y++) {
			int tx = 0;// 临时x
			int ty = 0;// 临时y
			int v = Card.ILLEGAL;// 保留前一个方格的值 初始化为非法值
			for (int x = cardStatus.length - 1; x >= 0; x--) {
				// 值为0
				if (cardStatus[x][y].getValue() == 0) {
					// 尝试找到非0值填充，或进行合并
					for (int i = x - 1; i >= 0; i--) {
						int temval = cardStatus[i][y].getValue();
						// 找到值和空格前格子值相同，进行合并
						if (temval == v) {
							// 合并操作
							merge(tx, ty, i, y);
							x++;// 合并后格子变为0，需重新检查
							// 清空临时变量
							v = Card.ILLEGAL;// 不允许重复合并，不允许如2 2 2 2直接变为4
							tx = 0;
							ty = 0;
							break;
						} else if (temval > 0) {
							v = temval;
							tx = x;
							ty = y;
							move(x, y, i, y);
							break;
						}else if (temval == Card.BLOCK) {// 障碍卡片 清空变量
							v = Card.ILLEGAL;
							tx = 0;
							ty = 0;
							break;
						}
					} // end of for

				} else if (cardStatus[x][y].getValue() == v) {
					// 合并操作
					merge(tx, ty, x, y);
					x++;// 合并后格子变为0，需重新检查
					v = Card.ILLEGAL;
					tx = 0;
					ty = 0;

				} else {
					v = cardStatus[x][y].getValue();
					tx = x;
					ty = y;
				} // end of if
			}
		}

		return true;
	}

	/**
	 * 将game向左滑动
	 *
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月28日22:00:00
	 */
	private boolean slideLeft() {
		// 移动卡片 slideLeft
		for (int x = 0; x < cardStatus.length; x++) {
			int tx = 0;// 临时x
			int ty = 0;// 临时y
			int v = Card.ILLEGAL;// 保留前一个方格的值 初始化为非法值
			for (int y = 0; y < cardStatus[x].length; y++) {
				// 值为0
				if (cardStatus[x][y].getValue() == 0) {
					// 尝试找到非0值填充，或进行合并
					for (int i = y + 1; i < cardStatus[x].length; i++) {
						// 找到值和空格前格子值相同，进行合并
						int temval = cardStatus[x][i].getValue();
						if (temval == v) {
							// 合并操作
							merge(tx, ty, x, i);
							// 清空临时变量
							v = Card.ILLEGAL;// 不允许重复合并，不允许如2 2 2 2直接变为4
							tx = 0;
							ty = 0;
							y--;// 合并后格子变为0，需重新检查
							break;
						} else if (temval > 0) {
							v = temval;
							tx = x;
							ty = y;
							move(x, y, x, i);
							break;
						}else if (temval == Card.BLOCK) {// 障碍卡片 清空变量
							v = Card.ILLEGAL;
							tx = 0;
							ty = 0;
							break;
						}
					} // end of for

				} else if (cardStatus[x][y].getValue() == v) {
					// 合并操作
					merge(tx, ty, x, y);
					v = Card.ILLEGAL;
					tx = 0;
					ty = 0;
					y--;
				} else {
					v = cardStatus[x][y].getValue();
					tx = x;
					ty = y;
				} // end of if
			}
		} // 移动卡片结束

		return true;
	}

	/**
	 * 将game向右滑动
	 *
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月30日21:09:43
	 * @return
	 */
	private boolean slideRight() {
		// 移动卡片 slideRight
		for (int x = 0; x < cardStatus.length; x++) {
			int tx = 0;// 临时x
			int ty = 0;// 临时y
			int v = Card.ILLEGAL;// 保留前一个方格的值 初始化为非法值
			for (int y = cardStatus[x].length - 1; y >= 0; y--) {
				// 值为0
				if (cardStatus[x][y].getValue() == 0) {
					// 尝试找到非0值填充，或进行合并
					for (int i = y - 1; i >= 0; i--) {
						int temval = cardStatus[x][i].getValue();
						// 找到值和空格前格子值相同，进行合并
						if (temval == v) {
							// 合并操作
							merge(tx, ty, x, i);
							// 清空临时变量
							v = Card.ILLEGAL;// 不允许重复合并，不允许如2 2 2 2直接变为4
							tx = 0;
							ty = 0;
							y++;// 合并后格子变为0，需重新检查
							break;
						} else if (temval > 0) {
							v = temval;
							tx = x;
							ty = y;
							move(x, y, x, i);
							break;
						}else if (temval == Card.BLOCK) {// 障碍卡片 清空变量
							v = Card.ILLEGAL;
							tx = 0;
							ty = 0;
							break;
						}
					} // end of for

				} else if (cardStatus[x][y].getValue() == v) {
					// 合并操作
					merge(tx, ty, x, y);
					v = Card.ILLEGAL;
					tx = 0;
					ty = 0;
					y++;
				} else {
					v = cardStatus[x][y].getValue();
					tx = x;
					ty = y;
				} // end of if
			}
		} // 移动卡片结束

		return true;
	}

	// 合并x2y2位置卡片到x1y1卡片，值为x1y1的值*2
	private boolean merge(int x1, int y1, int x2, int y2) {
		cardStatus[x1][y1].setValue(2 * cardStatus[x1][y1].getValue());
		cardStatus[x2][y2].setValue(0);
		nowScore += cardStatus[x1][y1].getValue();// 计算分数
		isChange = true;// 布局发生改变
		gameController.merge(x1, y1, x2, y2);
		return true;
	}

	// 将fromX fromY的卡片移动到toX toY
	private boolean move(int toX, int toY, int fromX, int fromY) {
		cardStatus[toX][toY].setValue(cardStatus[fromX][fromY].getValue());
		cardStatus[fromX][fromY].setValue(0);
		isChange = true;// 状态发生改变
		gameController.move(toX, toY, fromX, fromY);
		return true;
	}

	/*
	 * 判断当前局势能否进一步行动 判断游戏是否结束
	 *
	 * @return 能进一步行动返回true 否则返回false
	 */
	private boolean canMerge() {
		// 不为空 能移动
		if (!emptyCard.isEmpty()) {
			return true;
		}

		// 横向遍历
		for (int x = 0; x < cardStatus.length; x++) {
			int v = Integer.MIN_VALUE;// 保留前一个方格的值 初始化为非法值
			for (int y = 0; y < cardStatus[x].length; y++) {
				// 值为0
				if (cardStatus[x][y].getValue() == v) {
					// 值和前一个格子相同，可以合并
					return true;
				} else if (cardStatus[x][y].getValue() != 0) {
					// 等于0也能正常运行
					v = cardStatus[x][y].getValue();
				}
			}
		}
		// 纵向遍历
		for (int y = 0; y < cardStatus[0].length; y++) {
			int v = Integer.MIN_VALUE;// 保留前一个方格的值 初始化为非法值
			for (int x = 0; x < cardStatus.length; x++) {
				// 值为0
				if (cardStatus[x][y].getValue() == v) {
					// 值和前一个格子相同，可以合并
					return true;
				} else if (cardStatus[x][y].getValue() != 0) {
					// 等于0也能正常运行
					v = cardStatus[x][y].getValue();
				}
			}
		}

		return false;
	}

	private void gameover() {
		if (nowScore > maxScore) {
			maxScore = nowScore;
			saveMaxScore();// 保存最高分
		}

		gameController.gameover();

		return;
	}

	/*
	 * 保存成绩
	 */
	private void saveMaxScore() {
		gameController.setValue("maxScore", String.valueOf(maxScore));
		gameController.saveValue("保存最高分:" + maxScore);
		return;
	}

	/**
	 * 保存当前游戏状态到undoStack
	 *
	 * @version 2016年6月27日22:55:13
	 */
	public void saveStatus() {
		Card[][] temp = new Card[cardRow][cardColumn];// 卡片数组
		// 初始化卡片
		for (int x = 0; x < temp.length; ++x) {
			for (int y = 0; y < temp[x].length; ++y) {
				temp[x][y] = new Card();
				temp[x][y].setValue(cardStatus[x][y].getValue());
			}
		}

		// 判断堆栈是否满
		if (undoStack.size() < maxUndo) {
			undoStack.push(temp);
		} else if (maxUndo > 0) {// 移除最远状态，加入新状态
			undoStack.remove(undoStack.firstElement());
			undoStack.push(temp);
		}

		return;
	}

	/**
	 * 保存传递的状态
	 *
	 * @version 创建时间：2016年6月29日23:22:02
	 * @param 状态数组
	 */
	public void saveStatus(Card[][] temp) {
		// 判断堆栈是否满
		if (undoStack.size() < maxUndo) {
			undoStack.push(temp);
		} else if (maxUndo > 0) {// 移除最远状态，加入新状态
			undoStack.remove(undoStack.firstElement());
			undoStack.push(temp);
		}

		return;
	}

	/**
	 * 撤销函数 回退到上一步状态
	 *
	 * @return 撤销成功返回true
	 * @version 2016年6月27日22:44:44
	 */

	public boolean undoStatus() {
		if (undoStack == null || undoStack.isEmpty()) {
			return false;
		}
		cardStatus = undoStack.pop();
		step--;
		// undo之后当前分数不会恢复为旧分数
		nowScore /= 2;// undo之后分数减半，以暂时修补无法回复的缺陷
		calcEmptyCard();// 重新计算空卡片
		return true;
	}

	/**
	 * @version 创建时间：2016年6月28日21:18:01
	 * @return 当前可回退次数，即回退堆栈大小
	 */
	public int undoCount() {
		return undoStack.size();
	}

	/**
	 * @return the maxScore
	 * @version 创建时间：2016年6月30日21:18:11
	 */
	public int getMaxScore() {
		return maxScore;
	}

	/**
	 * @return 现在得分nowScore
	 */
	public int getNowScore() {
		return nowScore;
	}

	/**
	 * @return 当前已行动的步数step
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @param gamecontroller
	 *            the gamecontroller to set
	 */
	public void setGameController(GameController gameController) {
		this.gameController = gameController;

	}

}

/**
 * @version 创建时间：2016年6月26日21:39:57
 *
 *          Point类 用于定位Card位置
 */
class Point {
	int x;
	int y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
