package view;


import com.sun.prism.paint.Color;

import controller.MainController;
import controller.MainControllerIF;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;

public class NodesView {
	
	GridPane gridPane;
	Stage stage;
	Scene scene;
	VBox vBox;
	VBox nodes;
	
	public NodesView(Scene scene, Stage stage) {
		
		this.scene = scene;
		this.stage = stage;
		
		
		
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
		drawNoOfNodes();
	}

	private void drawVBox() {
		vBox = new VBox();

		gridPane.getChildren().add(vBox);
		gridPane.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		gridPane.getStyleClass().add("pane");


		
	}

	private void drawNoOfNodes() {
		HBox noOfNodes = new HBox();
		noOfNodes.setPadding(new Insets(10));
		Label label = new Label("No of nodes : ");
		
		
		TextField field = new TextField();
		noOfNodes.getChildren().addAll(label,field);
		
		Label error = new Label("Please Enter a value");
		error.setTextFill(javafx.scene.paint.Color.RED);
		error.setVisible(false);
		
		Button button = new Button("Next");
		
		defineNoOfNodesActions(button, field, error);

		vBox.getChildren().addAll(noOfNodes, button, error);
		
	}

	private void defineNoOfNodesActions(Button button, TextField field, Label error) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				checkNoOfNodes(button, field, error);
				
			}
		});
		
		field.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
		        if (event.getCode() == KeyCode.ENTER)  {
					checkNoOfNodes(button, field, error);
		        }
				
			}
			
			
		});
	    
		
	}

	private void checkNoOfNodes(Button button, TextField field, Label error) {
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
						if (no <= 0) {
							throw new RuntimeException();
						}
						button.setText("change");
						DrawNodesLabels(no);
					} catch (Exception e) {
						error.setVisible(true);
						error.setText("no of nodes must be +ve integer value!");
						field.setStyle("-fx-text-box-border: red;");
					}
				}
					
					
	}
		
		
		private void DrawNodesLabels(int no) {
			try {
				vBox.getChildren().remove(nodes);
			} catch (Exception e) {}

			nodes = new VBox();
			nodes.setAlignment(Pos.CENTER);
			nodes.setSpacing(10);
			
			VBox tempBox = new VBox();
			tempBox.setSpacing(10);
			tempBox.setAlignment(Pos.CENTER);
			tempBox.setPadding(new Insets(20));
			
			ScrollPane scrollPane = new ScrollPane(tempBox);
			scrollPane.setPrefHeight(680);
			
//			scrollPane.getStyleClass().add("pane");

			
			nodes.getChildren().add(scrollPane);
			vBox.getChildren().add(nodes);
			
			
			TextField nodesNames[] = new TextField[no];
			Label error = new Label("Please Enter a value");

			for (int i = 0; i < no; i++) {
				HBox hBox = new HBox();
				hBox.setAlignment(Pos.CENTER);
				Label label = new Label("Node " + (i + 1) + ":     ");
				label.getStyleClass().clear();
				label.getStyleClass().add("textAfter");
				TextField textField = new TextField();
				defineTextFieldAction(textField, nodesNames, error);
				nodesNames[i] = textField; 
				hBox.getChildren().addAll(label,textField);
				tempBox.getChildren().add(hBox);
			}
			
			Button next = new Button("Next");
			defineNextButtonAction(next, nodesNames, error); 
			error.setTextFill(javafx.scene.paint.Color.RED);
			error.setVisible(false);
			nodes.getChildren().addAll(next, error);
						
		}
		
		private void defineTextFieldAction(TextField textField, TextField[] nodesNames, Label error) {
			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
			        if (event.getCode() == KeyCode.ENTER)  {
						assignNodes(nodesNames, error);
			        }
					
				}
				
				
			});
			
		}
		
		private void defineNextButtonAction (Button button, TextField[] nodesNames, Label error) {
			button.setOnAction(new EventHandler<ActionEvent>() {
				
			@Override
			public void handle(ActionEvent event) {
				assignNodes(nodesNames, error);
			}
			
		});
		}

		
		private void assignNodes(TextField[] nodesNames, Label error) {
					boolean cont = true;
					MainController mainController = new MainController();
					for (TextField textField : nodesNames) {
						textField.setStyle("");
						error.setVisible(false);
					}
					for (TextField textField : nodesNames) {
						if (textField.getText().trim().length() == 0) {
							error.setVisible(true);
							error.setText("Please Enter a value");
							textField.setStyle("-fx-text-box-border: red;");
							cont = false;
							break;
							
						}
					     try {
								mainController.addNode(textField.getText());
							} catch (Exception e) {
								error.setVisible(true);
//								error.setText("This value cannot be assigned");
								error.setText(e.getMessage());
								textField.setStyle("-fx-text-box-border: red;");	
								cont = false;
								break;
							}
						}	
					
					if (cont) {
						new BranchesView(scene, stage, mainController);
					}

			
		}


















}