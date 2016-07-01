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

		widowWidth = Double.valueOf(Config.getValue("windowWidth"));
		windowHeight = Double.valueOf(Config.getValue("windowHeight"));
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
		// 新组
		Group root = new Group();

		// 绘制背景
		Node canvasBg = drawBG();
		root.getChildren().add(canvasBg);
		// 绘制游戏界面

		Node canvasGame = drawGame();
		root.getChildren().add(canvasGame);

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
			/*
			 * if (keyCode.equals(KeyCode.R)) { gameController.restart();
			 * return; }
			 *
			 * if (keyCode.equals(KeyCode.Q) || keyCode.equals(KeyCode.ESCAPE))
			 * { gameController.quitGame(); return; }
			 */

		});
	}

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
	private Node drawBG() {
		Canvas canvasBg = new Canvas(widowWidth, windowHeight);
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
	private Node drawGame() {

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
		canvasGame = new Canvas(startX + w, startY + h);

		GraphicsContext gc = canvasGame.getGraphicsContext2D();
		Color color;

		// 绘制边缘
		color = getColor(BORDER);
		gc.setFill(color);
		gc.fillRoundRect(x, y, w, h, arc, arc);

		// 绘制卡片

		for (int i = 0; i < cardColumn; i++) {
			for (int j = 0; j < cardRow; j++) {
				color = getColor(0);
				gc.setFill(color);
				double cardX = startX + (1 + border) * cardWidth * j;
				double cardY = startY + (1 + border) * cardHeight * i;
				gc.fillRoundRect(cardX, cardY, cardWidth, cardHeight, arc, arc);
				color = Color.WHITE;
				gc.setFill(color);
				double fontsize = 40;
				gc.setFont(Font.font("arial", FontWeight.LIGHT, fontsize));
				gc.fillText("2", cardX+cardWidth/2-0.6*fontsize/2, cardY+cardHeight/2+0.65*fontsize/2, cardWidth);
			}
		}

		return canvasGame;
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
			color = Color.WHITE;
			break;
		}
		// TODO 获取颜色
		return color;
	}

	/**
	 * @param gamecontroller
	 *            the gamecontroller to set
	 */
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	/**
	 * 游戏结束
	 */
	public void gameover() {
		// TODO 游戏结束

	}

	public void refrsh() {
		// TODO Auto-generated method stub

	}
}
