package view;


import com.sun.glass.events.MouseEvent;
import com.sun.prism.paint.Color;

import controller.MainController;
import controller.MainControllerIF;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BranchesView {
	
	GridPane gridPane;
	Stage stage;
	Scene scene;
	VBox vBox;
	VBox nodes;
	MainController mainController;
	
	final int WIDTH = 1300;
	final int HEIGHT = 900;
	
	public BranchesView(Scene scene, Stage stage, MainController mainController) {
		
		this.scene = scene;
		this.stage = stage;
		this.mainController = mainController;
		

		switchScene();
		drawLayout();
		
	}

	private void switchScene() {
		gridPane = new GridPane();
		scene.setRoot(gridPane);
		
		String style = getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().addAll(style);

		
	}

	private void drawLayout() {
		drawVBox();
		drawNoOfBranches();
				

		
	}

	private void drawVBox() {
		vBox = new VBox();

		gridPane.getChildren().add(vBox);
		gridPane.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		gridPane.getStyleClass().add("pane");


		
	}

	private void drawNoOfBranches() {
		HBox noOfBranches = new HBox();
		noOfBranches.setPadding(new Insets(10));
		Label label = new Label("No of Branches : ");
		
		
		TextField field = new TextField();
		noOfBranches.getChildren().addAll(label,field);
		
		Label error = new Label("Please Enter a value");
		error.setTextFill(javafx.scene.paint.Color.RED);
		error.setVisible(false);
		


		Button button = new Button("Next");
		
		defineNoOfBranchesAction(button, field, error);
		
		vBox.getChildren().addAll(noOfBranches, button, error);
		
	}
	
	private void defineNoOfBranchesAction(Button button, TextField field, Label error) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				checkNoOfBranches(button, field, error);
				
			}
		});
		
		field.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
		        if (event.getCode() == KeyCode.ENTER)  {
					checkNoOfBranches(button, field, error);
		        }
				
			}
			
			
		});
	    
		
	}

	private void checkNoOfBranches(Button button, TextField field, Label error) {

				String noOfNodes = field.getText();
				if (noOfNodes.trim().length() == 0) {
					error.setVisible(true);
					error.setText("Please Enter a value");
					field.setStyle("-fx-text-box-border: red;");
				} else {
					field.setStyle("");
					error.setVisible(false);
					int no = 0;
					try {
						no = Integer.parseInt(noOfNodes);
						if (no <=  0) {
							throw new RuntimeException();
						}
						button.setText("change");
						stage.setWidth(WIDTH);
						Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
					    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
					    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2); 
						
						DrawNodesLabels(no);
					} catch (Exception e) {
						error.setVisible(true);
						error.setText("no of branches must be +ve integer value!");
						field.setStyle("-fx-text-box-border: red;");
					}
					
					
				}
	}
		
		
		private void DrawNodesLabels(int no) {
			

			try {
				vBox.getChildren().remove(nodes);
			} catch (Exception e) {}

			nodes = new VBox();
			nodes.setPrefWidth(WIDTH - 100);
			nodes.setAlignment(Pos.CENTER);
			nodes.setSpacing(10);
			
			VBox tempBox = new VBox();
			tempBox.setSpacing(10);
			tempBox.setAlignment(Pos.CENTER);
			tempBox.setPadding(new Insets(20));
			
			ScrollPane scrollPane = new ScrollPane(tempBox);
			scrollPane.setPrefHeight(680);
			scrollPane.fitToWidthProperty();
			
//			scrollPane.getStyleClass().add("pane");

			
			nodes.getChildren().add(scrollPane);
			vBox.getChildren().add(nodes);
			
			
			TextField branchNode1[] = new TextField[no];
			TextField branchNode2[] = new TextField[no];
			TextField branchGain[] = new TextField[no];
			Label error = new Label("Please Enter a value");

			for (int i = 0; i < no; i++) {
				HBox hBox = new HBox();
				hBox.setAlignment(Pos.CENTER);
				hBox.setSpacing(5);
				Label label = new Label("Branch " + (i + 1) + ":     ");
				Label label1 = new Label("Node 1 : ");
				Label label2 = new Label("Node 2 : ");
				Label label3 = new Label("gain : ");
				label.getStyleClass().add("textAfter");
				label1.getStyleClass().add("textAfter");
				label2.getStyleClass().add("textAfter");
				label3.getStyleClass().add("textAfter");
				TextField textField1 = new TextField();
				TextField textField2 = new TextField();
				TextField textField3 = new TextField();
				branchNode1[i] = textField1; 
				branchNode2[i] = textField2; 
				branchGain[i] = textField3; 
				
				
				hBox.getChildren().addAll(label,label1, textField1, label2,textField2, label3, textField3);
				tempBox.getChildren().add(hBox);
			}
			
			defineTextFieldAction(branchNode1, branchNode2, branchGain, error);

			
			Button next = new Button("Next");
			defineNextButtonAction(next, branchNode1, branchNode2, branchGain, error);
			error.setTextFill(javafx.scene.paint.Color.RED);
			error.setVisible(false);
			nodes.getChildren().addAll(next, error);
						
		}
		
		private void defineNextButtonAction(Button button, TextField[] branchNode1, TextField[] branchNode2, TextField[] branchGain,
				Label error) {
			
			button.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					assignBranches(branchNode1, branchNode2, branchGain, error);
					
				}
			});
		}
				

		
		

	private void defineTextFieldAction(TextField[] branchNode1, TextField[] branchNode2, TextField[] branchGain,
		Label error) {
		
		for (int i = 0; i < branchNode1.length; i++) {
			branchNode1[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
				
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER)  {
						assignBranches(branchNode1, branchNode2, branchGain, error);
					}
					
				}
				
				
			});
			branchNode2[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
				
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER)  {
						assignBranches(branchNode1, branchNode2, branchGain, error);
					}
					
				}
				
				
			});
			branchGain[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
				
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER)  {
						assignBranches(branchNode1, branchNode2, branchGain, error);
					}
					
				}
				
				
			});
		}
			
			
		}
		
		private void assignBranches(TextField[] branchNode1, TextField[] branchNode2, TextField[] branchGain, Label error) {
					boolean cont = true;
					for (int j = 0; j < branchNode1.length; j++) {
						branchNode1[j].setStyle("");
						branchNode2[j].setStyle("");
						branchGain[j].setStyle("");
						error.setVisible(false);
					}
					for (int i = 0; i < branchNode1.length; i++) {
						if (branchNode1[i].getText().trim().length() == 0 ) {
							error.setVisible(true);
							error.setText("Please Enter a value");
							branchNode1[i].setStyle("-fx-text-box-border: red;");
							cont = false;
							break;
						}
						if (branchNode2[i].getText().trim().length() == 0 ) {
							error.setVisible(true);
							error.setText("Please Enter a value");
							branchNode2[i].setStyle("-fx-text-box-border: red;");
							cont = false;
							break;

						}
						if (branchGain[i].getText().trim().length() == 0 ) {
							error.setVisible(true);
							error.setText("Please Enter a value");
							branchGain[i].setStyle("-fx-text-box-border: red;");
							cont = false;
							break;

						}
						try {
							mainController.addBranch(branchNode1[i].getText(), branchNode2[i].getText(), Double.parseDouble(branchGain[i].getText()));
						} catch (Exception e) {
							error.setVisible(true);
//							error.setText("This value cannot be assigned");
							error.setText(e.getMessage());
							branchNode1[i].setStyle("-fx-text-box-border: red;");	
							branchNode2[i].setStyle("-fx-text-box-border: red;");	
							branchGain[i].setStyle("-fx-text-box-border: red;");	
							cont = false;
							break;

						}
						
					}
					
					if (cont) {
						new ResultView(stage, scene, mainController);
						
					}

			
		}


















}