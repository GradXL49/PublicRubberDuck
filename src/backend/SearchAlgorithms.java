/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end search algorithms
 */

package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class SearchAlgorithms {
	/*
	 * Binary Searches
	 * modified from https://www.geeksforgeeks.org/binary-search/
	 */
	public static int binaryNumberSearch(double[] arr, int l, int r, double x) {
		if (r >= l) {
            int mid = l + (r - l) / 2;
  
            // If the element is present at the
            // middle itself
            if (arr[mid] == x)
                return mid;
  
            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr[mid] > x)
                return binaryNumberSearch(arr, l, mid - 1, x);
  
            // Else the element can only be present
            // in right subarray
            return binaryNumberSearch(arr, mid + 1, r, x);
        }
  
        // We reach here when element is not present
        // in array
        return -1;
	}
	
	public static int binaryTextSearch(String[] arr, int l, int r, String x) {
		if (r >= l) {
            int mid = l + (r - l) / 2;
  
            // If the element is present at the
            // middle itself
            if (arr[mid].contentEquals(x))
                return mid;
  
            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr[mid].compareTo(x) > 0)
                return binaryTextSearch(arr, l, mid - 1, x);
  
            // Else the element can only be present
            // in right subarray
            return binaryTextSearch(arr, mid + 1, r, x);
        }
  
        // We reach here when element is not present
        // in array
        return -1;
	}
	
	/*
	 * Exponential Searches
	 * modified from https://www.geeksforgeeks.org/exponential-search/
	 */
	public static int exponentialNumberSearch(double[] arr, int n, double x) {
		// If x is present at first location itself
        if (arr[0] == x)
            return 0;
     
        // Find range for binary search by
        // repeated doubling
        int i = 1;
        while (i < n && arr[i] <= x)
            i = i*2;
     
        // Call binary search for the found range.
        return binaryNumberSearch(arr, i/2, Math.min(i, n-1), x);
	}
	
	public static int exponentialTextSearch(String[] arr, int n, String x) {
		// If x is present at first location itself
        if (arr[0].contentEquals(x))
            return 0;
     
        // Find range for binary search by
        // repeated doubling
        int i = 1;
        while (i < n && arr[i].compareTo(x) <= 0)
            i = i*2;
     
        // Call binary search for the found range.
        return binaryTextSearch(arr, i/2, Math.min(i, n-1), x);
	}
	
	/*
	 * Breadth-First Search
	 * modified from https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
	 */
	public static String BFS(int s, Graph g, Object[] data, Object target) {
		String path = "";
		
		boolean visited[] = new boolean[g.getSize()];
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		visited[s] = true;
		queue.add(s);
		
		while(queue.size() != 0) {
			s = queue.poll();
			path += "node"+ (s+1) + " ";
			if(data[s] == target) 
				return path;
			
			Iterator<Integer> i = g.adj[s].listIterator();
			while(i.hasNext()) {
				int n = i.next();
				if(!visited[n]) {
					visited[n] = true;
					queue.add(n);
				}
			}
		}
		
		return "";
	}
	
	/*
	 * Depth-First Search
	 * modified from https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/
	 */
	public static void DFS(int v, Graph g, boolean[] visited, Object[] data, Object target, boolean[] success, ArrayList<Object> path) {
		visited[v] = true;
		path.add("node"+(v+1));
		if(data[v] == target) success[0] = true;
		
		Iterator<Integer> i = g.adj[v].listIterator();
		while(i.hasNext()) {
			int n = i.next();
			
			if(!visited[n])
				DFS(n, g, visited, data, target, success, path);
		}
	}
	
	/*
	 * Dijkstra's Shortest Path
	 * modified from https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
	 */
	//utility for calculation
	private static int minDistance(int[] dist, boolean[] sptSet, int V) {
		int min = Integer.MAX_VALUE, min_index = -1;
		
		for(int i=0; i<V; i++) {
			if(sptSet[i]==false && dist[i]<=min) {
				min = dist[i];
				min_index = i;
			}
		}
		
		return min_index;
	}
	
	//utility for output
	private static String printSolution(int[] dist, int V) {
        String out = "Vertex        Distance from Source\n";
        for(int i=0; i<V; i++)
            out += "Node" + (i+1) + "        " + dist[i] + "\n";
        
        return out;
    }
	
	//algorithm
	public static String dijkstra(int[][] graph, int src, int V) {
		int[] dist = new int[V];
		boolean[] sptSet = new boolean[V];
		
		for(int i=0; i<V; i++) {
			dist[i] = Integer.MAX_VALUE;
			sptSet[i] = false;
		}
		
		dist[src] = 0;
		
		for(int count=0; count<V-1; count++) {
			int u = minDistance(dist, sptSet, V);
			
			sptSet[u] = true;
			
			for(int v=0; v<V; v++) {
				if(!sptSet[v] && graph[u][v]!=0 && dist[u]!=Integer.MAX_VALUE && dist[u]+graph[u][v]<dist[v]) {
					dist[v] = dist[u] + graph[u][v];
				}
			}
		}
		
		return printSolution(dist, V);
	}
	
	/*
	 * A* Search
	 * modified from https://stackabuse.com/graphs-in-java-a-star-algorithm/
	 */
	public static String aStar(Node start, Node target){
	    PriorityQueue<Node> closedList = new PriorityQueue<>();
	    PriorityQueue<Node> openList = new PriorityQueue<>();

	    start.f = start.g + start.calculateHeuristic(target);
	    openList.add(start);

	    while(!openList.isEmpty()){
	        Node n = openList.peek();
	        if(n == target){
	            return printPath(n);
	        }

	        for(Node.Edge edge : n.neighbors){
	            Node m = edge.node;
	            double totalWeight = n.g + edge.weight;

	            if(!openList.contains(m) && !closedList.contains(m)){
	                m.parent = n;
	                m.g = totalWeight;
	                m.f = m.g + m.calculateHeuristic(target);
	                openList.add(m);
	            } else {
	                if(totalWeight < m.g){
	                    m.parent = n;
	                    m.g = totalWeight;
	                    m.f = m.g + m.calculateHeuristic(target);

	                    if(closedList.contains(m)){
	                        closedList.remove(m);
	                        openList.add(m);
	                    }
	                }
	            }
	        }

	        openList.remove(n);
	        closedList.add(n);
	    }
	    return printPath(null);
	}

	//utility for A* search
	public static String printPath(Node target){
	    Node n = target;
	    String out = "";

	    if(n==null)
	        return "A path was not found";

	    ArrayList<String> ids = new ArrayList<>();

	    while(n.parent != null){
	        ids.add(n.id);
	        n = n.parent;
	    }
	    ids.add(n.id);
	    Collections.reverse(ids);

	    for(String id : ids){
	        out += id + " ";
	    }
	    out += "\n";
	    
	    return out;
	}
}
