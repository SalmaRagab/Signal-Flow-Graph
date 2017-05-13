package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public interface GraphIF {

	
	/**
	 * @param source the starting node
	 * @param destination the final node
	 * @return arraylist contains arrays each represent a valid path between source and destination
	 */
	
	public ArrayList<LinkedList<String>> findPaths
	(String source, String destination,	LinkedHashMap<String, LinkedHashMap<String, Double>> nodes);
}
