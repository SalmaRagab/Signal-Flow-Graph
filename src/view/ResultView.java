package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.MainController;
import graph.Path;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import signalFlowGraph.SignalFlowGraph;
import sun.misc.SignalHandler;

public class ResultView {
	
	private Stage stage;
	private Scene scene;
	private TabPane tabPane;
	private MainController mainController;
	private SignalFlowGraph signalFlowGraph;
	
	private final int WIDTH = 1250;
	private final int HEIGHT = 800;
	
	public ResultView(Stage stage, Scene scene, MainController mainController) {
		
		this.scene = scene;
		this.stage = stage;
		this.mainController = mainController;
		signalFlowGraph = new SignalFlowGraph(mainController);
		
		switchView();
		defineTabs();
		
	}


	private void switchView() {
		tabPane = new TabPane();
		scene.setRoot(tabPane);
		
		
		String style = getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().addAll(style);
		
	}


	private void defineTabs() {
		defineGraphTab();
		defineEquationsTab();
		defineTransferTab();
		
	}

 
	private void defineGraphTab() {
		Tab graphTab = new Tab("Graph");
		tabPane.getTabs().add(graphTab);
		drawGraphTab(graphTab);
		
	}


	private void defineEquationsTab() {
		Tab equationsTab = new Tab("Equations");
		tabPane.getTabs().add(equationsTab);
		drawEquationsTab(equationsTab);
		
	}


	private void defineTransferTab() {
		Tab transferTab = new Tab("Transfer Function");
		tabPane.getTabs().add(transferTab);
		drawTransferTab(transferTab);
		
	}


	private void drawGraphTab(Tab graphTab) {
				
		               
		BorderPane pane1 = new BorderPane();
        pane1.getStyleClass().add("pane1");
        pane1.setPrefWidth(WIDTH);
        pane1.setPrefHeight(HEIGHT);
        
        GridPane pane2 = new GridPane();
        pane2.getChildren().add(pane1);
		pane2.getStyleClass().add("pane");
		pane2.setAlignment(Pos.CENTER);

        graphTab.setContent(pane2);
       
        drawGraph(pane1);
        
        
        
		
	}


	private void drawEquationsTab(Tab equationsTab) {

		
        GridPane pane1 = new GridPane();
        pane1.getStyleClass().add("pane1");
        pane1.setPrefWidth(WIDTH);
        pane1.setPrefHeight(HEIGHT);
        pane1.setAlignment(Pos.CENTER);
		
        GridPane pane2 = new GridPane();
        pane2.getChildren().add(pane1);
		pane2.getStyleClass().add("pane");
		pane2.setAlignment(Pos.CENTER);

        equationsTab.setContent(pane2);
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        pane1.getChildren().add(vBox);
        
        drawForwardPaths(vBox);
        drawLoops(vBox);
	}


	private void drawLoops(VBox vBox) {
		Label label = new Label("Loops: ");
		label.setTextFill(Color.DARKGREEN);
		vBox.getChildren().add(label);
		
		VBox forwardVbox = new VBox();
		forwardVbox.setAlignment(Pos.TOP_LEFT);
		forwardVbox.setPrefWidth(800);
		forwardVbox.setPrefHeight(300);
		forwardVbox.setPadding(new Insets(10));
		
		ScrollPane scrollPane = new ScrollPane(forwardVbox);
		scrollPane.setPrefWidth(1000);
		scrollPane.setPrefHeight(450);
		
		ArrayList<ArrayList<Path>> loops = signalFlowGraph.getLoops();
		ArrayList<Path> oneKindLoops;

		for (int i = 0; i < loops.size(); i++) {
			oneKindLoops = loops.get(i);
			Label labelLoopTitle = new Label((i+1) + "-Non touching loops");
			labelLoopTitle.setTextFill(Color.DARKBLUE);
			forwardVbox.getChildren().add(labelLoopTitle);
			displayLoop(oneKindLoops, forwardVbox);
		}
		
		vBox.getChildren().add(scrollPane);

		
	}


	private void displayLoop(ArrayList<Path> oneKindLoops, VBox forwardVbox) {
		Path path;
		for (int j = 0; j < oneKindLoops.size(); j++) {
			path = oneKindLoops.get(j);
			Label label = new Label();
			String string = "";
			for (int i = 0; i < path.getPath().size(); i++) {
				if(path.getPath().get(i).trim().length() == 0) { // add and 
					string += "  and  ";
				} else {
					string += path.getPath().get(i); 
					if (!(i == path.getPath().size()-1) && !(path.getPath().get(i+1).trim().length() == 0)) {
							string += ", ";
					}
				}
			}
			string += " = " + path.getGain();
			label.setText(string);
			forwardVbox.getChildren().add(label);
		}
		

		
	}


	private void drawForwardPaths(VBox vBox) {
		Label label = new Label("Forward paths: ");
		label.setTextFill(Color.DARKGREEN);
		vBox.getChildren().add(label);
		
		VBox forwardVbox = new VBox();
		forwardVbox.setAlignment(Pos.TOP_LEFT);
		forwardVbox.setPrefWidth(800);
		forwardVbox.setPrefHeight(150);
		forwardVbox.setPadding(new Insets(10));
		
		ScrollPane scrollPane = new ScrollPane(forwardVbox);
		scrollPane.setPrefWidth(1000);
		scrollPane.setPrefHeight(200);
		
		ArrayList<Path> forwardPaths = signalFlowGraph.getForwardPaths();
		LinkedList<String> path;
		for (int i = 0; i < forwardPaths.size(); i++) {
			path =  forwardPaths.get(i).getPath();
			Label label2 = new Label();
			String labelString = "Path " + (i+1) + " = ";
			for (int j = 0; j < path.size(); j++) {
				labelString += path.get(j);
				if (j != path.size() - 1) {
					labelString +=", ";
				}
			}
			labelString += " = " + forwardPaths.get(i).getGain();
			label2.setText(labelString);
			forwardVbox.getChildren().add(label2);
		}
		
		
		vBox.getChildren().add(scrollPane);
		
		
	}


	private void drawTransferTab(Tab transferTab) {
       
        GridPane pane1 = new GridPane();
        pane1.getStyleClass().add("pane1");
        pane1.setPrefWidth(WIDTH);
        pane1.setPrefHeight(HEIGHT);
		pane1.setAlignment(Pos.CENTER);

		
		GridPane pane2 = new GridPane();
        pane2.getChildren().add(pane1);
		pane2.getStyleClass().add("pane");
		pane2.setAlignment(Pos.CENTER);

        transferTab.setContent(pane2);
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        pane1.getChildren().add(vbox);
        
        drawDeltas(vbox);
        drawDelta(vbox);
        drawTF(vbox);
		
	}




	private void drawDeltas(VBox vbox) {
		
		
		HBox hbox = new HBox();
		VBox vbox1 = new VBox();
		vbox1.setAlignment(Pos.CENTER);
		vbox1.setSpacing(10);
		VBox vbox2 = new VBox();
		vbox2.setAlignment(Pos.CENTER);
		vbox2.setSpacing(10);
		VBox vbox3 = new VBox();
		vbox3.setAlignment(Pos.CENTER);
		vbox3.setSpacing(10);

		
		hbox.getChildren().addAll(vbox1, vbox2, vbox3);
		hbox.setAlignment(Pos.CENTER);

		hbox.setSpacing(200);		
		hbox.setPrefWidth(950);
		hbox.setPrefHeight(250);
		
		ScrollPane scrollPane = new ScrollPane(hbox);
		scrollPane.setPrefWidth(1000);
		scrollPane.setPrefHeight(300);

		Label label = new Label("Deltas : ");
		label.setTextFill(Color.DARKGREEN);

		ArrayList<Double> deltas = signalFlowGraph.getDeltas();
		for (int i = 0; i < deltas.size(); i += 3) {
			try {
				Label label1 = new Label("\u0394"+generateSubscript(i+1) + " = "+ deltas.get(i).toString());
				vbox1.getChildren().add(label1);
				label1.setAlignment(Pos.CENTER);
				vbox1.getChildren().add(label1);
			} catch (Exception e){}
			try {
				Label label1 = new Label("\u0394"+generateSubscript(i+2) + " = "+ deltas.get(i+1).toString());
				vbox1.getChildren().add(label1);
				label1.setAlignment(Pos.CENTER);
				vbox2.getChildren().add(label1);
			} catch (Exception e){
				Label label1 = new Label("__" );
				vbox2.getChildren().add(label1);
			}
			try {
				Label label1 = new Label("\u0394"+generateSubscript(i+3) + " = "+ deltas.get(i+2).toString());
				vbox1.getChildren().add(label1);
				label1.setAlignment(Pos.CENTER);
				vbox3.getChildren().add(label1);
			} catch (Exception e){
				Label label1 = new Label("__");
				vbox3.getChildren().add(label1);
			}
			
			
		}
		vbox.getChildren().addAll(label, scrollPane);
		
	}
	
	private String generateSubscript(int i) {
	    StringBuilder sb = new StringBuilder();
	    for (char ch : String.valueOf(i).toCharArray()) {
	        sb.append((char) ('\u2080' + (ch - '0')));
	    }
	    return sb.toString();
	}


	private void drawDelta(VBox vbox) {
		Label label1 = new Label("Total Delta : ");
		label1.setTextFill(Color.DARKGREEN);
		Label label2 = new Label();
		String delta = "\u0394 =  " + signalFlowGraph.getDelta();
		label2.setText(delta);
		
		vbox.getChildren().addAll(label1, label2);
		
	}


	private void drawTF(VBox vbox) {
		Label label1 = new Label("Transfer function : ");
		label1.setTextFill(Color.DARKGREEN);
		Label label2 = new Label();
		String delta = "T.F =  " + signalFlowGraph.getTransferFunction();
		label2.setText(delta);
		
		vbox.getChildren().addAll(label1, label2);
		
	}


	private void drawGraph(BorderPane pane) {
		
		Map<String, LinkedHashMap<String, Double>> nodes = mainController.getNodes();
		drawNodes(pane, nodes);
	}


	private void drawNodes(BorderPane pane, Map<String, LinkedHashMap<String, Double>> nodes) {
		
		int no = nodes.size();
		double radius  = WIDTH / no /5;
		final double Y_POSITION = HEIGHT/2;
		final double FONT_SIZE = WIDTH / no  / 7 ;

		double space = (WIDTH - radius*no)/(no+1);
		double point = 0;
		double xPosition;	
		
		LinkedHashMap<String, Point> nodesPositions = new LinkedHashMap<>();

		
		for(Map.Entry<String, LinkedHashMap<String, Double>> entry : nodes.entrySet()){
			xPosition = point + space + radius/2;
			
			StackPane stackPane = new StackPane();
			stackPane.setLayoutX(xPosition);
			stackPane.setLayoutY(Y_POSITION);
			pane.getChildren().add(stackPane);
			
			Circle circle = new Circle(xPosition, Y_POSITION, radius);
			circle.setFill(Color.TRANSPARENT);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(2);
			

			
			Text text = new Text(entry.getKey());
			text.setFont(new Font(FONT_SIZE));
			stackPane.getChildren().add(text);
			
			
			stackPane.getChildren().add(circle);
			point = xPosition + radius/2;	
			
			nodesPositions.put(entry.getKey(), new Point((int) xPosition,(int) Y_POSITION));
			
			}
		drawCurves(pane, nodesPositions, nodes, (int) radius);

		
	}
	
	
	private void drawCurves(BorderPane pane, LinkedHashMap<String, Point> positions,
							Map<String, LinkedHashMap<String, Double>> nodes, int radius) {
		
		List<String> nodesNames = new LinkedList<String>(nodes.keySet());	
		
		for(int i = 0; i < nodesNames.size(); i++) {
			try {
				List<String> adjacentNames = new LinkedList<String>(nodes.get(nodesNames.get(i)).keySet());
				for(int j = 0; j < adjacentNames.size(); j++){	
					int nodeX = positions.get(nodesNames.get(i)).x; // source node
					int nodeY;
					int order = nodesNames.indexOf(adjacentNames.get(j)); // the order of the node
					int adjacentx = positions.get(adjacentNames.get(j)).x; // destination node
					int adjacenty;
					int midy;
					int midx = (nodeX + adjacentx)/2;
					boolean direction;
					int arrowy;
					int arrowx;



					if (i < order) { // upward
						nodeY = positions.get(nodesNames.get(i)).y - radius;
						adjacenty = positions.get(adjacentNames.get(j)).y - radius;
						midy = nodeY  - (Math.abs(i-order))*70;
						direction = true;
						arrowy = nodeY  - (Math.abs(i-order))*35;

					} else { // downward
						nodeY = positions.get(nodesNames.get(i)).y + radius;
						adjacenty = positions.get(adjacentNames.get(j)).y + radius;
						midy = nodeY  + (Math.abs(i-order))*70;
						direction = false;
						arrowy = nodeY  + (Math.abs(i-order))*35;
					}
					if (Math.abs(i - order) == 1) { // st line
//						check if there is loop to make it 2 curves not st line
						if ((nodes.get(adjacentNames.get(j)) == null) || (!nodes.get(adjacentNames.get(j)).containsKey(nodesNames.get(i)))) {
							adjacenty = positions.get(nodesNames.get(i)).y;
							nodeY = positions.get(nodesNames.get(i)).y;
							midy = nodeY;
							nodeX += radius;
							adjacentx -= radius;
							arrowy = nodeY;
							if (!direction) { // from right to left
								nodeX -= 2* radius;
								adjacentx += 2 * radius;
							}	
						}


					}
					arrowx = midx;
					
					if ((nodesNames.get(i).equals(adjacentNames.get(j)))) {
						int no = nodes.size();
						adjacentx = nodeX + radius;						
						midx = nodeX;
						nodeX -= radius;
						
						nodeY = nodeY - radius;
						adjacenty = nodeY;
						midy = nodeY - 3*radius;
						
						arrowy = (int) (nodeY - 1.5*radius);
						arrowx = midx ;
						direction = true;
						
					}
					
					QuadCurve curve = new QuadCurve(nodeX, nodeY, midx, midy, adjacentx, adjacenty);
				    curve.setStrokeWidth(2);
				    curve.setStroke(Color.BLACK);
				    curve.setFill(Color.TRANSPARENT);
				    
					pane.getChildren().add(curve);
					
					Double gain = (nodes.get(nodesNames.get(i)).get(adjacentNames.get(j)));
					
					drawArrow(arrowx, arrowy, direction, pane, gain.toString());

					
				}
				
			} catch (Exception e){}


		
		}
	
	}
	
	private void drawArrow(int x, int y, boolean direction, BorderPane borderPane, String gain1) {
		Line line1;
		Line line2;
		Text gain = new Text(gain1);
		gain.setX(x);

		if (direction) { // 0 -> right 
			line1 = new Line(x, y, x-5, y-5);
			line2 = new Line(x, y, x-5, y+5);
			gain.setY(y-10);
		} else {
			line1 = new Line(x, y, x+5, y-5);
			line2 = new Line(x, y, x+5, y+5);
			gain.setY(y+23);

		}
		
		line1.setStrokeWidth(2);
		line2.setStrokeWidth(2);
		borderPane.getChildren().addAll(line1, line2, gain);

	}


	

}
