package pers.jmu.test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
 
public class ViewTest extends Application {

    private double width = 500;// 窗口宽
	private double height = 500;// 窗口高
	private int gamewidth = 4;// 卡片列数
	private int gameheight = 4;// 卡片行数
	
    public static void main(String[] args) {
        launch(args);// 启动javafx窗口
    }
 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy2048Test");// 设置标题
        Group root = new Group();

		Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
 
    private void drawShapes(GraphicsContext gc) {
        
        //gc.setStroke(Color.BLUE);
        /*
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        */
        double cardWidth = 50;// 卡片宽
        double cardHeight = 50;// 卡片高
        double startX = 100;// 排列起始点x
        double startY = 100;// 排列起始点y
        Color color = Color.GOLD;
        for (int i = 0; i < gamewidth; i++) {
			for (int j = 0; j < gameheight; j++) {
				color = color.darker();
				gc.setFill(color);
				gc.fillRoundRect(startX+1.1*cardWidth*j, startY+1.1*cardHeight*i, cardWidth, cardHeight, 10, 10);
				
			}
		}
        return;
    }
    
    /*
     * 获取不同数字对应颜色
     */
    private Color getColor(int value){
		
    	//TODO 获取颜色
    	return null;
    }
}