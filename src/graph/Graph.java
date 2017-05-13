package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Graph implements GraphIF {

	private ArrayList<LinkedList<String>> paths = new ArrayList<>();
	
	
	@Override
	public ArrayList<LinkedList<String>> findPaths(String source, String destination,
			LinkedHashMap<String, LinkedHashMap<String, Double>> nodes) {
		
		LinkedList<String> visited = new LinkedList<>();
		visited.add(source);
		boolean firstTime = true;
		paths = new ArrayList<>();
		return dfs(nodes, visited, source, destination, firstTime);
	}

	private ArrayList<LinkedList<String>> dfs(LinkedHashMap<String, LinkedHashMap<String, Double>> nodes,
    		LinkedList<String> visited, java.lang.String source, String destination, boolean firstTime) {

		int pos = new ArrayList<String>(nodes.keySet()).indexOf(source);

    	// examine adjacent nodes
		LinkedHashMap<String, Double> adjacentNodes = nodes.get(source);
		if (adjacentNodes == null) {
			return null;
		}
		String[] keys= adjacentNodes.keySet().toArray(new String[adjacentNodes.size()]);
        for (String node : keys) {
    		
        	if ((node.equals(destination)) && (firstTime)) {
                visited.add(node);
                copyToPaths(visited);
                visited.removeLast();
                break;
            }
        	int compPos = new ArrayList<String>(nodes.keySet()).indexOf(node);

/*        	if (compPos < pos) {
        		continue;
        	}*/
            if (visited.contains(node) && (!firstTime)) {
                continue;
            }
            
        }
        for (String node : keys) {
            if ((visited.contains(node) || node.equals(destination)) && (firstTime)) {
                continue;
            }
            int compPos = new ArrayList<String>(nodes.keySet()).indexOf(node);

/*        	if (compPos < pos) {
        		continue;
        	}*/
            visited.addLast(node);
            source = node;
            firstTime = true;
            dfs(nodes, visited, source, destination, firstTime);
            visited.removeLast();
        }
        return paths;
    }

	private void copyToPaths(LinkedList<String> visited) {
		LinkedList<String> temp = new LinkedList<>();
		for (String path : visited) {
			temp.add(path);
		}
		paths.add(temp);
	}

}
