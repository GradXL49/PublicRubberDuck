/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * graph class for back-end
 * modified from https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
 */

package backend;

import java.util.LinkedList;

public class Graph {
	private int V; //No. of vertices
	public LinkedList<Integer> adj[]; //Adjacency Lists
	
	// Constructor
	@SuppressWarnings("unchecked")
	public Graph(int v) {
		V = v;
		adj = new LinkedList[v];
		for(int i=0; i<v; i++)
			adj[i] = new LinkedList<Integer>();
	}
	
	// Function to add edge into the graph
	public void addEdge(int v, int w) {
		adj[v].add(w); //add w to v's list
	}
	
	// Function to get the size of the graph
	public int getSize() {
		return this.V;
	}
}
