/**
 * 
 */
package pers.jmu.model;

/**
 * @version 创建时间：2016年6月26日17:36:34
 * 
 * 2048游戏卡片类
 */
public class Card {

	private int value;// 卡片的值

	public Card() {
		value = 0;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
