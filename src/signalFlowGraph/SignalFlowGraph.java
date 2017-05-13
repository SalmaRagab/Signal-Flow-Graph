package signalFlowGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import controller.MainController;
import graph.Graph;
import graph.Path;
import sun.print.PathGraphics;

public class SignalFlowGraph {

	private Graph graph;
	
	private LinkedHashMap<String, LinkedHashMap<String, Double>> nodes;
	private ArrayList<Path> forwardPaths;
	private ArrayList<ArrayList<Path>> loops ;
	private ArrayList<Double> deltas;
	private double delta;
	private double transferFunction;
	private MainController mainController;
	
	private ArrayList<LinkedList<String>> paths;
	
	
	public SignalFlowGraph(MainController mainController) {
		graph = new Graph();
		
		this.mainController = mainController;
		nodes = mainController.getNodes();
		forwardPaths = generateForwardPaths();
		loops = generateLoops();
		deltas = generateDeltas();
		delta = generateDelta();
		transferFunction = generateTransfereFunction();
		
		paths = new ArrayList<>();
		
	}

	private ArrayList<Path> generateForwardPaths() { // ma3 kol path tgebeh e7seby el gain bel mara
		
		forwardPaths = new ArrayList<>();
		
		
		paths = graph.findPaths(mainController.getC(), mainController.getR(), nodes);
		LinkedHashMap<String, Double> adjacencies;

		for (LinkedList<String> i: paths) {
			int index = 0;
			Double gain = 1.0;
			Path path = new Path();
			while (index < (i.size() - 1)) {
				adjacencies = mainController.getNodes().get(i.get(index)); //get the values corresponding to the key;
				gain = gain * adjacencies.get(i.get(index + 1));
				index++;
			}
			path.setPath(i);
			path.setGain(gain);
			
			forwardPaths.add(path);
		}
		
		for (int i = 0; i < forwardPaths.size(); i++) {
			System.out.println("Path '" + i + "': " + forwardPaths.get(i).getPath());
			System.out.println("Path '" + i + "' gain: " + forwardPaths.get(i).getGain());
			System.out.println("-------------------------------------------------------");

		}
		return forwardPaths;
		
	}


	private ArrayList<ArrayList<Path>> generateLoops() {
		loops = new ArrayList<>();
		
		ArrayList<Path> allLoops = new ArrayList<>();
		String[] keys = nodes.keySet().toArray(new String[nodes.size()]);
		ArrayList<LinkedList<String>> loop;
		paths = new ArrayList<>();
		
		for (String s: keys) {
			loop = graph.findPaths(s, s, nodes);
			int j = 0;
			if (loop == null) {
				continue;
			}
			while (j < loop.size()) {
				paths.add(loop.get(j));
				j++;
			}
		}
		
		LinkedHashMap<String, Double> adjacencies; //for getting the gains

		for (LinkedList<String> i: paths) {
			int index = 0;
			Double gain = 1.0;
			Path path = new Path();
			while (index < (i.size() - 1)) {
				adjacencies = mainController.getNodes().get(i.get(index)); //get the gains corresponding to the key
				gain = gain * adjacencies.get(i.get(index + 1));
				index++;
			}
			path.setPath(i);
			path.setGain(gain);
			
			if (notRepeated(path, allLoops)) {
				allLoops.add(path);
			}
			
			
		}
		

		loops.add(allLoops);
		
		//check for non-touching loops
		getNonTouchingLoops(allLoops);
		
//		for (int i = 0; i < loops.size(); i++) {
//			for (int j = 0; j < loops.size(); j++) {
//				System.out.println("Loop '" + i + "': " + loops.get(i).get(j).getPath());
//				System.out.println("Loop '" + i + "' gain: " + loops.get(i).get(j).getGain());
//				System.out.println("-------------------------------------------------------");
//			}
//
//		}
		
		return loops;
	}


	private boolean notRepeated(Path path, ArrayList<Path> allLoops) {
		LinkedList<String> lPath;
		for (int i = 0; i < allLoops.size(); i++) { // compare in all prev loops
			if (path.getPath().size() != allLoops.get(i).getPath().size()) {
				continue;
			}
			lPath = allLoops.get(i).getPath();
			for (int j = 0; j < lPath.size(); j++) { // compare 1 path
				if (path.getPath().equals(lPath)){
					return false;
				}
				lPath.removeFirst();
				lPath.addLast(lPath.getFirst());
				
			}
			
		}
		return true;
	}

	private void getNonTouchingLoops(ArrayList<Path> allLoops) {
		boolean finished = false;
		boolean found = false;
		int numberOfNonTouchingLoops = 2;
		ArrayList<Path> nonTouchingLoops = new ArrayList<>(); 
		
		for (int i = 0; (i < allLoops.size() - 1) && (!finished); i++) {
			for (int j = i + 1; (j < allLoops.size()); j++) {
				if (!isTouching(allLoops.get(i), allLoops.get(j))) {
					Double gain = 0.0;
					Path path = new Path();
					LinkedList<String> newPath = new LinkedList<>();

					for (int h = 0; h < allLoops.get(i).getPath().size(); h++) {
						newPath.add(allLoops.get(i).getPath().get(h));
					}
					newPath.add(" ");
					newPath.addAll(allLoops.get(j).getPath());
					gain = (allLoops.get(i).getGain() * allLoops.get(j).getGain());
					path.setPath(newPath);
					path.setGain(gain);
					
					nonTouchingLoops.add(path);
					
					found = true;
				}
			}
			
		}
		
		loops.add(nonTouchingLoops);
		
		if (found) { //momkn yb2a fi 3 non touching and so on
			while (true) {
				ArrayList<Path> temp = findNonTouchingLoops(allLoops, numberOfNonTouchingLoops + 1);
				if (temp.size() == 0) {
					break;
				}
				loops.add(temp);
				numberOfNonTouchingLoops++;
			}
		}
	}
	
	private ArrayList<Path> findNonTouchingLoops(ArrayList<Path> allLoops, int number) {
		ArrayList<Path> nonTouchingLoops = new ArrayList<>();
		Path[] elNonTouchingTest = new Path[number];
		int k = 0;
		Path pathReturned;
		while (k < allLoops.size()) {
			if ((allLoops.size() - number) < 0) {
				break;
			}
			for (int i = 0; i < number; i++) {
				if (k >= allLoops.size()) {
					break;
				}
				elNonTouchingTest[i] = allLoops.get(k);
				k++;
			}
			pathReturned = test(elNonTouchingTest);
			if (pathReturned != null) {
				nonTouchingLoops.add(pathReturned);
			}
		}
		return nonTouchingLoops;		
		
	}
	
	private Path test(Path[] elNonTouchingTest) {
		int maxNumber = 0;
		Path toReturn = null;
		for (int i = 0; (i < elNonTouchingTest.length); i++) {
			for (int j = i + 1; (j < elNonTouchingTest.length); j++) {
				if (!isTouching(elNonTouchingTest[i], elNonTouchingTest[j])) {
					maxNumber++;
				}
			}
		}
		if (maxNumber == (elNonTouchingTest.length)) {
			toReturn = merge(elNonTouchingTest);
		}
		return toReturn;
	}

	private Path merge(Path[] pathsToBeMerged) {
		Path path = new Path();
		
		LinkedList<String> newPath = pathsToBeMerged[0].getPath();
		newPath.add(" ");
		Double gain = pathsToBeMerged[0].getGain();
		
		for (int i = 1; i < pathsToBeMerged.length; i++) {
			newPath = pathsToBeMerged[i].getPath();
			gain = gain * pathsToBeMerged[i].getGain();
			
			if ((i + 1) != (pathsToBeMerged.length - 1)) {
				newPath.add(" ");
			}
		}
		path.setPath(newPath);
		path.setGain(gain);
		
		return path;
	}
	
	private ArrayList<Double> generateDeltas() {
		ArrayList<Double> returnDeltas = new ArrayList<>();
		int noOfDeltas = forwardPaths.size();
		Path forwardPath;
		int result;
		ArrayList oneKindLoops;
		Path loop;
		for (int i = 0; i < noOfDeltas; i++) {
//			delta = 1 - sum(loops - forward path) + sum (2 non touching - forward path) .........
			forwardPath = forwardPaths.get(i);
			result = 1;
			
			for (int j = 0; j < loops.size(); j++) { // 1 -> loops // 2 -> non touching 
				oneKindLoops = loops.get(j);
				for (int k = 0; k < oneKindLoops.size(); k++) { // list of loops(paths)
					loop = (Path) oneKindLoops.get(k);
						if (!isTouching(forwardPath, loop)) {
							result += Math.pow(-1, j+1) * (loop.getGain());
						}
					}
			}
			returnDeltas.add((double) result);
		}
		return returnDeltas;
	}
	
	
	private double generateDelta() {
		double returndDelta = 1;
		ArrayList<Path> oneKindLoops;
		for (int i = 0; i < loops.size(); i++) {
			oneKindLoops = loops.get(i);
			for (int j = 0; j < oneKindLoops.size(); j++) {
				returndDelta += Math.pow(-1, i+1)  * oneKindLoops.get(j).getGain();
			}
		}
		return returndDelta;
	}


	private boolean isTouching(Path path1, Path path2) {
		LinkedList<String> forwardPathString = path1.getPath();
		LinkedList<String> loopPathString = path2.getPath();
		for (int i = 0; i < forwardPathString.size(); i++) {
			if (loopPathString.contains(forwardPathString.get(i))) {
				return true;
			}
		}
		return false;
	}


	private double generateTransfereFunction() {
		double returnTF = 0;
		for (int i = 0; i < forwardPaths.size(); i++) {
			returnTF += forwardPaths.get(i).getGain() * deltas.get(i);
		}
		returnTF = returnTF / delta;
		return returnTF;
	}


	public Map<String, LinkedHashMap<String, Double>> getNodes() {
		return nodes;
	}


	public ArrayList<Path> getForwardPaths() {
		return forwardPaths;
	}


	public ArrayList<ArrayList<Path>> getLoops() {
		return loops;
	}


	public ArrayList<Double> getDeltas() {
		return deltas;
	}


	public double getDelta() {
		return delta;
	}


	public double getTransferFunction() {
		return transferFunction;
	}


	public MainController getMainController() {
		return mainController;
	}
	
	
}

