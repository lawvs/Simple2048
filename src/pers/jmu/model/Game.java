/**
 * @version 创建时间：2016年6月26日17:30:52
 * Easy2048逻辑类
 * 实现游戏逻辑
 *
 */
package pers.jmu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 *
 * 游戏逻辑类 使用数组存储2048游戏逻辑
 *
 * @version 创建时间：2016年6月26日16:30:00
 */
public class Game {

	private int height;// 列数
	private int width;// 行数
	private Card[][] cardStatus;// 卡片数组
	private List<Point> emptyCard;// 保存空卡片坐标
	private Stack<Card[][]> undoStack;// 储存游戏旧状态 在卡片状态改变前存储
	private int maxUndo;// 最大可undo次数

	/**
	 * 初始化游戏
	 */
	public Game() {
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

		// TODO Config从配置文件读取width、height
		width = 4;
		height = 4;
		maxUndo = 100;

		/*
		 * width = Integer.valueOf(Config.getValue("width")); height =
		 * Integer.valueOf(Config.getValue("height")); maxUndo =
		 * Integer.valueOf(Config.getValue("maxUndo"));
		 */
		cardStatus = new Card[width][height];
		emptyCard = new ArrayList<Point>();// 保存空卡片坐标
		undoStack = new Stack<Card[][]>();
		return;
	}

	/**
	 * 初始化 重新开始游戏
	 */
	public void restart() {
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
		if (x < 0 || x >= width || y < 0 || y >= height) {// 越界检查
			// TODO Log.warn("坐标越界，设置卡片值失败");
			return false;
		}
		cardStatus[x][y].setValue(value);
		// if (value > 0){emptyCard.remove( );}
		return true;
	}

	/**
	 * 将game向左滑动
	 *
	 * @return 移动是否成功
	 * @version 创建时间：2016年6月28日22:00:00
	 */
	public boolean slideLeft() {
		boolean isChange = false;// 此次移动是否有效
		saveStatus();// 可能引发小错误
		// 移动卡片
		// slideLeft
		for (int x = 0; x < cardStatus.length; x++) {
			int tx = 0;// 临时x
			int ty = 0;// 临时y
			int v = -1;// 保留前一个方格的值
			for (int y = 0; y < cardStatus[x].length; y++) {
				// 值为0
				if (cardStatus[x][y].getValue() == 0) {
					// 找到非0值填充
					for (int i = y + 1; i < cardStatus[x].length; i++) {
						if (cardStatus[x][i].getValue() == v) {
							// 合并操作
							cardStatus[tx][ty].setValue(2 * v);
							// System.out.println(x+" " +y+ " "+2*v);
							cardStatus[x][i].setValue(0);
							isChange = true;// 格子合并
							v = -1;
							tx = 0;
							ty = 0;
							y--;
							break;
						} else if (cardStatus[x][i].getValue() != 0) {
							v = cardStatus[x][i].getValue();
							tx = x;
							ty = y;
							cardStatus[x][y].setValue(v);
							cardStatus[x][i].setValue(0);

							isChange = true;// 格子移动
							// System.out.println(x+" "+y);
							break;
						}
					}

				} else if (cardStatus[x][y].getValue() == v) {
					// 合并操作
					cardStatus[tx][ty].setValue(2 * v);
					// System.out.println(x+" " +y+ " "+2*v);
					cardStatus[x][y].setValue(0);
					isChange = true;// 格子合并
					v = -1;
					tx = 0;
					ty = 0;
					y--;
				} else {
					v = cardStatus[x][y].getValue();
					tx = x;
					ty = y;

				} // end of if
			}
		}

		/*
		 * test for (y = 0; y < cardStatus[0].length; y++) { for (x = 0; x <
		 * cardStatus.length; x++) { cardStatus[x][y].getValue();
		 * System.out.println(x + " " + y); } } //test end
		 */
		// 重新计算空卡片位置
		calcEmptyCard();
		if (emptyCard.isEmpty()) {
			// 游戏结束
			// gameover();
			return false;
		}
		if (isChange) {// 此次移动有效
			addNewCard();
		} else {// 移动无效
			undoStack.pop();// 删除保存的状态
		}
		return true;
	}

	// 合并x2y2位置卡片到x1y1卡片，值为x1y1的值*2
	private boolean merge(int x1, int y1, int x2, int y2) {
		// TODO 合并x2y2到x1y1，值为x1y1的值*2
		return true;
	}

	// 将x1y1卡片移动到x2y2
	private boolean move(int x1, int y1, int x2, int y2) {
		// TODO 将x1y1移动到x2y2
		return true;
	}

	/**
	 * 保存当前游戏状态到undoStack
	 *
	 * @version 2016年6月27日22:55:13
	 */
	public void saveStatus() {
		Card[][] temp = new Card[width][height];// 卡片数组
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
