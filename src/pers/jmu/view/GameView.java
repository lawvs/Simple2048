/**
 * @version 创建时间：2016年7月1日22:44:19
 *
 * 游戏的主要外观类
 */
package pers.jmu.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import pers.jmu.controller.GameController;
import pers.jmu.model.Card;
import pers.jmu.util.Config;
import pers.jmu.util.Messages;

public class GameView extends Application {
	private static final int BG = -1;
	private static final int BORDER = -2;
	private static final int TEXT = -3;

	private GameController gameController;
	private Canvas canvasGame;
	private Canvas canvasBg;
	private Canvas canvasScore;
	private Card[][] cards;
	private double widowWidth;
	private double windowHeight;

	public static void main(String[] args) {
		launch(args);// 启动javafx窗口
	}

	@Override
	public void init() throws Exception {
		super.init();
		gameController = new GameController();// 初始化
		gameController.setGameView(this);
		cards = gameController.getCards();
		widowWidth = Double.valueOf(Config.getValue("windowWidth"));
		windowHeight = Double.valueOf(Config.getValue("windowHeight"));
		canvasBg = new Canvas(widowWidth, windowHeight);
		canvasScore = new Canvas();
		canvasGame = new Canvas();
	}

	/*
	 * 绘制游戏画面
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		String title = Messages.getString("title");
		primaryStage.setTitle(title);// 设置标题
		// 设置图标
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/resources/icon.png")));
		primaryStage.setWidth(widowWidth);
		primaryStage.setHeight(windowHeight);
		primaryStage.setResizable(false);// 不可调整大小

		// 新组
		Group root = new Group();

		// 绘制背景
		drawBG();
		root.getChildren().add(canvasBg);
		// 绘制游戏界面
		drawGame();
		root.getChildren().add(canvasGame);
		// 绘制分数
		drawScore();
		root.getChildren().add(canvasScore);
		// 绘制文字
		Node canvasText = drawTexts();
		root.getChildren().add(canvasText);

		// 新建场景
		Scene scene = new Scene(root);
		addSwipeHandlers(scene);// 添加事件处理
		addKeyHandler(scene);

		// 获取css文件
		scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/view.css").toExternalForm());
		primaryStage.setScene(scene);// 设置场景
		primaryStage.show();// 显示窗口

	}

	/*
	 * 键盘事件
	 */
	private void addKeyHandler(Scene scene) {
		scene.setOnKeyPressed(ke -> {
			KeyCode keyCode = ke.getCode();
			if (keyCode.equals(KeyCode.UP) || keyCode.equals(KeyCode.W)) {
				gameController.move(GameController.SWIPE_UP);
				return;
			}
			if (keyCode.equals(KeyCode.DOWN) || keyCode.equals(KeyCode.S)) {
				gameController.move(GameController.SWIPE_DOWN);
				return;
			}
			if (keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.A)) {
				gameController.move(GameController.SWIPE_LEFT);
				return;
			}
			if (keyCode.equals(KeyCode.RIGHT) || keyCode.equals(KeyCode.D)) {
				gameController.move(GameController.SWIPE_RIGHT);
				return;
			}

			// 重新开始
			if (keyCode.equals(KeyCode.R)) {
				restart();
				return;
			}
			// UnDo
			if (keyCode.equals(KeyCode.U)) {
				gameController.undo();
				refrshGame();
				return;
			}

		});
	}

	/*
	 * 滑动事件
	 */
	private void addSwipeHandlers(Scene scene) {
		scene.setOnSwipeUp(e -> move(GameController.SWIPE_UP));
		scene.setOnSwipeRight(e -> move(GameController.SWIPE_RIGHT));
		scene.setOnSwipeLeft(e -> move(GameController.SWIPE_LEFT));
		scene.setOnSwipeDown(e -> move(GameController.SWIPE_DOWN));
	}

	private void move(int direction) {
		gameController.move(direction);
	}

	/*
	 * 绘制背景
	 */
	private Canvas drawBG() {

		GraphicsContext gc = canvasBg.getGraphicsContext2D();
		Color color;
		color = getColor(BG);
		gc.setFill(color);
		gc.fillRoundRect(0, 0, widowWidth, windowHeight, 0, 0);
		return canvasBg;
	}

	/*
	 * 绘制2048文字
	 */
	private Node drawTexts() {
		TextFlow textFlow = new TextFlow();
		textFlow.setLayoutX(20);
		textFlow.setLayoutY(10);
		// 文字
		// text.setFont(Font.loadFont("file:resources/fonts/isadoracyr.ttf",
		// 120));
		Text t = new Text("2048");
		t.setFill(getColor(TEXT));

		t.setFont(Font.font("微软雅黑", FontWeight.BOLD, 50));
		// t.setFontSmoothingType(FontSmoothingType.LCD);

		textFlow.getChildren().addAll(t);

		Group group = new Group(textFlow);
		return group;
	}

	/*
	 * 绘制游戏界面
	 */
	private void drawGame() {

		double cardWidth = Double.valueOf(Config.getValue("cardWidth"));// 卡片宽
		double cardHeight = Double.valueOf(Config.getValue("cardHeight"));// 卡片高
		double border = 0.2;// 间距 几倍卡片长宽
		double arc = 10;// 圆角
		// 行列
		double cardRow = Double.valueOf(Config.getValue("cardRow"));
		double cardColumn = Double.valueOf(Config.getValue("cardColumn"));

		double w = cardWidth * cardRow + border * cardWidth * (cardRow + 1);
		double h = cardHeight * cardColumn + border * cardHeight * (cardColumn + 1);
		double startX = widowWidth / 2 - w / 2;// 排列起始点x
		double startY = windowHeight / 2 - h / 2;// 排列起始点y
		double x = startX - border * cardWidth;
		double y = startY - border * cardHeight;

		// 画布大小
		canvasGame.setWidth(startX + w);
		canvasGame.setHeight(startY + h);
		GraphicsContext gc = canvasGame.getGraphicsContext2D();
		Color color;

		// 绘制边缘
		color = getColor(BORDER);
		gc.setFill(color);
		gc.fillRoundRect(x, y, w, h, arc, arc);

		// 绘制卡片
		cards = gameController.getCards();
		for (int i = 0; i < cardColumn; i++) {
			for (int j = 0; j < cardRow; j++) {
				int value;
				value = cards[i][j].getValue();
				color = getColor(value);
				gc.setFill(color);
				double cardX = startX + (1 + border) * cardWidth * j;
				double cardY = startY + (1 + border) * cardHeight * i;
				gc.fillRoundRect(cardX, cardY, cardWidth, cardHeight, arc, arc);
				color = Color.WHITE;// 字体颜色
				gc.setFill(color);

				String strValue = value != 0 ? String.valueOf(value) : "";
				int fontLlen = strValue.length();
				double fontsize = 40;
				// 文本过长由cardWidth自动修正 偏移量为cardX + cardWidth / 2 - 字体大小修正系数* fontsize / 2 - 字体长度修正*(fontLlen-1) * fontsize / 2
				double fontX = Math.max(cardX, cardX + cardWidth / 2 - 0.6 * fontsize / 2 - 0.46*(fontLlen-1) * fontsize / 2);
				double fontY = cardY + cardHeight / 2 + 0.7 * fontsize / 2;
				gc.setFont(Font.font("arial", FontWeight.LIGHT, fontsize));
				gc.fillText(strValue, fontX, fontY, cardWidth);
			}
		}

		return;
	}

	private void drawScore() {
		// TODO 绘制分数
		return;
	}

	public void refrshGame() {
		drawGame();
		drawScore();
		return;
	}

	/*
	 * 获取不同数字对应颜色
	 */
	// TODO 从文件读取
	private Color getColor(int value) {
		Color color = null;
		switch (value) {
		case 0:
			// 空白卡颜色
			color = new Color(189.0 / 255, 178.0 / 255, 166.0 / 255, 255.0 / 255);
			break;
		case 2:
			color = new Color(220.0 / 255, 211.0 / 255, 201.0 / 255, 255.0 / 255);
			break;
		case 4:
			color = getColor(2).darker();
			break;
		case 8:
			color = getColor(4).darker();
			break;
		case 16:
			color = getColor(8).darker();
			break;
		case 32:
			color = getColor(16).darker();
			break;
		case 64:
			color = getColor(32).darker();
			break;
		case BG:
			// 背景色
			color = new Color(250.0 / 255, 247.0 / 255, 238.0 / 255, 255.0 / 255);
			break;
		case BORDER:
			// 边缘色
			color = new Color(174.0 / 255, 161.0 / 255, 148.0 / 255, 255.0 / 255);
			break;
		case TEXT:
			// 文字颜色
			color = new Color(112.0 / 255, 104.0 / 255, 96.0 / 255, 0.9);
			break;
		default:
			color = Color.BLACK;
			break;
		}

		return color;
	}

	/**
	 * 设置控制器
	 *
	 * @param gamecontroller
	 *            the gamecontroller to set
	 */
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	/**
	 * 游戏结束 背景变红
	 */
	public void gameover() {
		GraphicsContext gc = canvasBg.getGraphicsContext2D();
		Color color;
		color = Color.RED;
		gc.setFill(color);
		gc.fillRoundRect(0, 0, widowWidth, windowHeight, 0, 0);
		return;
	}

	/*
	 * 重新开始游戏
	 */
	private void restart() {
		gameController.restart();
		drawBG();
		refrshGame();
		return;
	}
}
