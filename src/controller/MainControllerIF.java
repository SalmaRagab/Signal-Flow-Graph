package controller;

public interface MainControllerIF {

	
	/**
	 * add the node to the hashmap of nodes
	 * @param node node name
	 * @throws Exception when name is not correct or previously defined
	 */
	public void addNode(String node) throws Exception;
	/**
	 * add the node2 and gain to adjacency list of node 1
	 * @param node1
	 * @param node2
	 * @param gain
	 * @throws Exception when nodes are repeated or do not exist
	 */
	public void addBranch(String node1, String node2, Double gain) throws Exception;
	
	public void clearNodes();
	
	public void clearBranches();
	
}
