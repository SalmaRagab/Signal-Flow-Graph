package controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import signalFlowGraph.SFGException;

public class MainController implements MainControllerIF {

	private LinkedHashMap<String, LinkedHashMap<String, Double>> nodes = new LinkedHashMap();
	private String c; //first node
	private String r; //last node
	
	public String getC() {
		return c;
	}
	
	public String getR() {
		return r;
	}
	
	
	@Override
	public void addNode(String node) throws Exception {
		
		if (!nodes.containsKey(node)) {
			if (nodes.size() == 0) {
				c = node;
			}
			nodes.put(node, null);
			r = node;
		} else {
			throw new SFGException("You entered a duplicate node (" + node + ").");
		}
	}

	@Override
	public void addBranch(String node1, String node2, Double gain) throws Exception {

		if ((!nodes.containsKey(node1)) || (!nodes.containsKey(node2))) {
			throw new SFGException("One of the nodes you entered doesn't exist");
		}
		
		LinkedHashMap<String, Double> adjacencyMap = nodes.get(node1); //origin
		
		if (adjacencyMap == null) {
			adjacencyMap = new LinkedHashMap<>();
            nodes.put(node1, adjacencyMap);
        }
		if (adjacencyMap.containsKey(node2)) {
			throw new SFGException("There is a branch already between " + node1 + " and " + node2 + ".");
		}
        adjacencyMap.put(node2, gain);
	}

	@Override
	public void clearNodes() {
		 nodes.clear();
	}

	@Override
	public void clearBranches() {
		nodes.values().clear();

	}


	public LinkedHashMap<String, LinkedHashMap<String, Double>> getNodes() {
		return nodes;
	}

}
