package graph;

import java.util.LinkedList;

public class Path {
	
	private LinkedList<String> path;
	private double gain;
	
	public Path() {
		path = new LinkedList<>();
	}
	
	
	public LinkedList<String> getPath() {
		return path;
	}
	public void setPath(LinkedList<String> path) {
		this.path = path;
	}
	public double getGain() {
		return gain;
	}
	public void setGain(double gain) {
		this.gain = gain;
	}
	
	
	
}
