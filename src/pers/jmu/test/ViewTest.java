package pers.jmu.test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ViewTest extends Application {
	private static final int BG = -1;
	private static final int BORDER = -2;

	private double width = 500;// 窗口宽
	private double height = 500;// 窗口高
	private int gameWidth = 4;// 卡片列数
	private int gameHeight = 4;// 卡片行数

	public static void main(String[] args) {
		launch(args);// 启动javafx窗口
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Simple2048Test");// 设置标题
		Group root = new Group();

		// 绘制游戏界面
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawShapes(gc);
		root.getChildren().add(canvas);

		root.getChildren().add(drawTexts());

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/view.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void drawShapes(GraphicsContext gc) {
		double cardWidth = 50;// 卡片宽
		double cardHeight = 50;// 卡片高
		double startX = 100;// 排列起始点x
		double startY = 100;// 排列起始点y
		double border = 0.2;// 间距 几倍卡片长宽
		double arc = 10;// 圆角
		Color color;
		// 背景
		color = getColor(BG);
		gc.setFill(color);
		gc.fillRoundRect(0, 0, width, height, 0, 0);
		// 边缘色
		color = getColor(BORDER);
		gc.setFill(color);
		double x = startX - border * cardWidth;
		double y = startY - border * cardHeight;
		double w = cardWidth * gameWidth + border * cardWidth * (gameWidth+1);
		double h = cardHeight * gameHeight + border * cardHeight * (gameHeight+1);
		gc.fillRoundRect(x, y, w, h, arc, arc);
		
		// 卡片
		color = getColor(0);
		for (int i = 0; i < gameHeight; i++) {
			for (int j = 0; j < gameWidth; j++) {
				gc.setFill(color);
				gc.fillRoundRect(startX + (1 + border) * cardWidth * j, startY + (1 + border) * cardHeight * i,
						cardWidth, cardHeight, arc, arc);
			}
		}

		return;
	}

	private Group drawTexts() {
		TextFlow textFlow = new TextFlow();
		textFlow.setLayoutX(20);
		textFlow.setLayoutY(10);
		// 文字
		// text.setFont(Font.loadFont("file:resources/fonts/isadoracyr.ttf",
		// 120));
		Text t = new Text("2048");
		t.setFill(new Color(112.0 / 255, 104.0 / 255, 96.0 / 255, 255.0 / 255));
		t.setFont(Font.font("微软雅黑", FontWeight.BOLD, 50));
		// t.setFontSmoothingType(FontSmoothingType.LCD);

		textFlow.getChildren().addAll(t);

		Group group = new Group(textFlow);

		return group;
	}

	/*
	 * 获取不同数字对应颜色
	 */
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
		}
		// TODO 获取颜色
		return color;
	}
}