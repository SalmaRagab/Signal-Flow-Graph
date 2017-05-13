package view;

import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainGui extends Application{
	
	
	Stage stage;
	Scene scene;
	GridPane gridPane;
	
	final int WIDTH = 500;

	final int HEIGHT = 900;
	final String NAME = "Signal Flow Graph";
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = new Stage();
		primaryStage = stage;
		gridPane = new GridPane();
		
		scene = new Scene(gridPane, WIDTH, HEIGHT);
		
		stage.setScene(scene);
		stage.setTitle(NAME);
		stage.setResizable(false);
		
		stage.show();
		
		new NodesView(scene, stage);
		
	}
	
	

}
