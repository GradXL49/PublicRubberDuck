/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * graph code exporter class
 */
package export;

import java.io.File;
import java.io.FileWriter;

public class GraphExport extends CodeExport {
	//generate code body for DFS
	private static void generateDFSBody(FileWriter f, Boolean num, Object[][] graph, String type, Object target) throws Exception {
		try {
			generateGraphVariables(f, num, graph, type, target);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n");
			
			f.append("\t\t//make data into graph object\n"
					+"\t\tArrayList<Object> path = new ArrayList<Object>();\n"
					+"\t\tGraph g = new Graph(graph.length);\n");
			
			String node = "";
			if(graph[0][0] == null) {
				node = "\"Node \"+";
				f.append("\t\tObject[] d = new Object[graph[0].length];\n"
						+"\t\tfor(int i=0; i<graph.length; i++) {\n"
						+"\t\t\tfor(int j=0; j<graph[0].length; j++) {\n"
						+"\t\t\t\tif(i == 0) d[j] = j+1;\n"
						+"\t\t\t\telse if((Integer)graph[i][j] != 0) g.addEdge(i-1, j);\n"
						+"\t\t\t}\n"
						+"\t\t}\n\n"
						+"\t\t//perform search\n"
						+"\t\tboolean[] success = {false};\n"
						+"\t\tDFS(0, g, new boolean[g.getSize()], d, target, success, path);\n");
			}
			else {
				f.append("\t\tfor(int i=1; i<graph.length; i++) {\n"
						+"\t\t\tfor(int j=0; j<graph[1].length; j++) {\n"
						+"\t\t\t\tif((Integer)graph[i][j] != 0) g.addEdge(i-1, j);\n"
						+"\t\t\t}\n"
						+"\t\t}\n\n"
						+"\t\t//perform search\n"
						+"\t\tboolean[] success = {false};\n"
						+"\t\tDFS(0, g, new boolean[g.getSize()], graph[0], target, success, path);\n");
			}
			
			f.append("\t\tif(success[0]) System.out.println("+node+"target+\" was found in the graph\");\n"
					+"\t\telse System.out.println("+node+"target+\" was not found in the graph\");\n\t}\n\n");
			
			generateDFS(f);
			
			f.append("}\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for DFS
	private static void generateDFS(FileWriter f) throws Exception {
		try {
			f.append("\t//Depth-First Search modified from https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/\n"
					+"\tprivate static void DFS(int v, Graph g, boolean[] visited, Object[] data, Object target, boolean[] success, ArrayList<Object> path) {\n"
					+"\t\tvisited[v] = true;\n"
					+"\t\tpath.add(\"node\"+(v+1));\n"
					+"\t\tif(data[v].equals(target)) success[0] = true;\n\n"
					+"\t\tIterator<Integer> i = g.adj[v].listIterator();\n"
					+"\t\twhile(i.hasNext()) {\n"
					+"\t\t\tint n = i.next();\n"
					+"\t\t\tif(!visited[n]) DFS(n, g, visited, data, target, success, path);\n"
					+"\t\t}\n"
					+"\t}\n\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for DFS
	private static void search(FileWriter f, String className, boolean num, Object[][] graph, String type, Object target) throws Exception {
		try {
			String message = "Depth-first Search on a graph.";
			generateHeader(f, message);
			
			String[] imports = {"java.util.Iterator", "java.util.ArrayList"};
			generateImports(f, imports, className);
			
			generateDFSBody(f, num, graph, type, target);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code body for dijkstra
	private static void generateDijkstraBody(FileWriter f, boolean num, Object[][] graph, int src) throws Exception {
		try {
			generateGraphVariables(f, num, graph, "src", src);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n"
					+"\t\t//remove unnecessary data from graph\n"
					+"\t\tint V = graph[0].length;\n"
					+"\t\tint[][] g = new int[V][V];\n"
					+"\t\tfor(int i=1; i<graph.length; i++) {\n"
					+"\t\t\tfor(int j=0; j<V; j++) {\n"
					+"\t\t\t\tg[i-1][j] = (Integer)graph[i][j];\n"
					+"\t\t\t}\n\t\t}\n\n"
					+"\t\tSystem.out.println(\"Result:\");\n"
					+"\t\tString output = dijkstra(g, src-1, V);\n"
					+"\t\tSystem.out.println(output);\n"
					+"\t}\n\n");
			
			generateDijkstra(f);
			
			f.append("}\n");
		}
		catch(Exception e) {
			
		}
	}
	
	//generate algorithm for dijkstra
	private static void generateDijkstra(FileWriter f) throws Exception {
		try {
			f.append("\t//Dijkstra's Shortest Path modified from https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/\n"
					+"	//utility for calculation\n"
					+"	private static int minDistance(int[] dist, boolean[] sptSet, int V) {\n"
					+"		int min = Integer.MAX_VALUE, min_index = -1;\n"
					+"		\n"
					+"		for(int i=0; i<V; i++) {\n"
					+"			if(sptSet[i]==false && dist[i]<=min) {\n"
					+"				min = dist[i];\n"
					+"				min_index = i;\n"
					+"			}\n"
					+"		}\n"
					+"		\n"
					+"		return min_index;\n"
					+"	}\n"
					+"	\n"
					+"	//utility for output\n"
					+"	private static String printSolution(int[] dist, int V) {\n"
					+"        String out = \"Vertex       Distance from Source\\n\";\n"
					+"        for(int i=0; i<V; i++) {\n"
					+"            if(dist[i] == Integer.MAX_VALUE) out += \"Node\" + (i+1) + \"        \" + \"No path\" + \"\\n\";\n"
					+"            else out += \"Node\" + (i+1) + \"        \" + dist[i] + \"\\n\";\n"
					+"        }"
					+"        \n"
					+"        return out;\n"
					+"    }\n"
					+"	\n"
					+"	//algorithm\n"
					+"	public static String dijkstra(int[][] graph, int src, int V) {\n"
					+"		int[] dist = new int[V];\n"
					+"		boolean[] sptSet = new boolean[V];\n"
					+"		\n"
					+"		for(int i=0; i<V; i++) {\n"
					+"			dist[i] = Integer.MAX_VALUE;\n"
					+"			sptSet[i] = false;\n"
					+"		}\n"
					+"		\n"
					+"		dist[src] = 0;\n"
					+"		\n"
					+"		for(int count=0; count<V-1; count++) {\n"
					+"			int u = minDistance(dist, sptSet, V);\n"
					+"			\n"
					+"			sptSet[u] = true;\n"
					+"			\n"
					+"			for(int v=0; v<V; v++) {\n"
					+"				if(!sptSet[v] && graph[u][v]!=0 && dist[u]!=Integer.MAX_VALUE && dist[u]+graph[u][v]<dist[v]) {\n"
					+"					dist[v] = dist[u] + graph[u][v];\n"
					+"				}\n"
					+"			}\n"
					+"		}\n"
					+"		\n"
					+"		return printSolution(dist, V);\n"
					+"	}\n\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for dijkstra
	private static void dijkstra(FileWriter f, String className, boolean num, Object[][] graph, int src) throws Exception {
		try {
			String message = "Dijkstra's pathing algorithm on a graph.";
			generateHeader(f, message);
			
			String[] imports = {};
			generateImports(f, imports, className);
			
			generateDijkstraBody(f, num, graph, src);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code body for A*
	private static void generateAStarBody(FileWriter f, boolean num, Object[][] graph, int target) throws Exception {
		try {
			generateGraphVariables(f, num, graph, "int", target);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n"
					+"\t\t//convert 2D array to graph object\n"
					+"\t\tArrayList<Node> g = new ArrayList<Node>();\n"
					+"\t\tString id = \"\";\n"
					+"\t\tfor(int i=1; i<graph.length; i++) {\n"
					+"\t\t\tif(graph[0][i-1] != null) id = graph[0][i-1].toString();\n"
					+"\t\t\telse id = \"node\"+i;\n"
					+"\t\t\tg.add(new Node(id));\n\t\t}\n\n"
					+"\t\tfor(int i=1; i<graph.length; i++) {\n"
					+"\t\t\tint count = 0;\n"
					+"\t\t\tfor(int j=0; j<graph[0].length; j++) {\n"
					+"\t\t\t\tif((Integer)graph[i][j] != 0) {\n"
					+"\t\t\t\t\tg.get(i-1).addBranch((Integer)graph[i][j], g.get(j));\n"
					+"\t\t\t\t\tcount++;\n"
					+"\t\t\t\t}\n\t\t\t}\n"
					+"\t\tg.get(i-1).setH(count);\n\t\t}\n\n"
					+"\t\t//get result\n"
					+"\t\ttry {\n"
					+"\t\t\tString path = aStar(g.get(0), g.get(target-1));\n"
					+"\t\t\tSystem.out.println(\"Path to target starting at root:\\n\"+path);\n"
					+"\t\t}\n"
					+"\t\tcatch(Exception e) { System.out.println(\"There is no node \"+target); }\n"
					+"\t}\n\n");
			
			generateAStar(f);
			
			f.append("}\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for A*
	private static void generateAStar(FileWriter f) throws Exception {
		try {
			f.append("\t//A* Search modified from https://stackabuse.com/graphs-in-java-a-star-algorithm/\n"
					+"	public static String aStar(Node start, Node target){\n"
					+"	    PriorityQueue<Node> closedList = new PriorityQueue<>();\n"
					+"	    PriorityQueue<Node> openList = new PriorityQueue<>();\n"
					+"\n"
					+"	    start.f = start.g + start.calculateHeuristic(target);\n"
					+"	    openList.add(start);\n"
					+"\n"
					+"	    while(!openList.isEmpty()){\n"
					+"	        Node n = openList.peek();\n"
					+"	        if(n == target){\n"
					+"	            return printPath(n);\n"
					+"	        }\n"
					+"\n"
					+"	        for(Node.Edge edge : n.neighbors){\n"
					+"	            Node m = edge.node;\n"
					+"	            double totalWeight = n.g + edge.weight;\n"
					+"\n"
					+"	            if(!openList.contains(m) && !closedList.contains(m)){\n"
					+"	                m.parent = n;\n"
					+"	                m.g = totalWeight;\n"
					+"	                m.f = m.g + m.calculateHeuristic(target);\n"
					+"	                openList.add(m);\n"
					+"	            } else {\n"
					+"	                if(totalWeight < m.g){\n"
					+"	                    m.parent = n;\n"
					+"	                    m.g = totalWeight;\n"
					+"	                    m.f = m.g + m.calculateHeuristic(target);\n"
					+"\n"
					+"	                    if(closedList.contains(m)){\n"
					+"	                        closedList.remove(m);\n"
					+"	                        openList.add(m);\n"
					+"	                    }\n"
					+"	                }\n"
					+"	            }\n"
					+"	        }\n"
					+"\n"
					+"	        openList.remove(n);\n"
					+"	        closedList.add(n);\n"
					+"	    }\n"
					+"	    return printPath(null);\n"
					+"	}\n"
					+"\n"
					+"	//utility for A* search\n"
					+"	public static String printPath(Node target){\n"
					+"	    Node n = target;\n"
					+"	    String out = \"\";\n"
					+"\n"
					+"	    if(n==null)\n"
					+"	        return \"A path was not found\";\n"
					+"\n"
					+"	    ArrayList<String> ids = new ArrayList<>();\n"
					+"\n"
					+"	    while(n.parent != null){\n"
					+"	        ids.add(n.id);\n"
					+"	        n = n.parent;\n"
					+"	    }\n"
					+"	    ids.add(n.id);\n"
					+"	    Collections.reverse(ids);\n"
					+"\n"
					+"	    for(String id : ids){\n"
					+"	        out += id + \" \";\n"
					+"	    }\n"
					+"	    out += \"\\n\";\n"
					+"	    \n"
					+"	    return out;\n"
					+"	}\n\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for A*
	private static void aStar(FileWriter f, String className, boolean num, Object[][] graph, int target) throws Exception {
		try {
			String message = "A* pathing algorithm on a graph";
			generateHeader(f, message);
			
			String[] imports = {"java.util.ArrayList", "java.util.Collections", "java.util.PriorityQueue"};
			generateImports(f, imports, className);
			
			generateAStarBody(f, num, graph, target);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//main
	public static boolean generateCode(String type, String[] className, boolean num, Object[][] graph, Object target) {
		try {
			String name = className[0];
			String path = new File("").getCanonicalPath() + "\\src\\output\\";
			
			File f = new File(path+name+".java");
			int i = 0;
			while(!f.createNewFile()) {
				f = new File(path+name+i+".java");
				className[0] = name+i;
				i++;
			}
			FileWriter w = new FileWriter(f);
			
			String dt = "Object";
			if(target != null) {
				if(target instanceof String) dt = "String";
				else if(target instanceof Integer) dt = "int";
				else if(target instanceof Double) dt = "double";
			}
			
			if(type.contentEquals("search")) {
				generateDependencies(path);
				search(w, className[0], num, graph, dt, target);
			}
			else if(type.contentEquals("dijkstra") || type.contentEquals("aStar")) {
				if(target instanceof Integer) {
					if(type.contentEquals("dijkstra")) {
						dijkstra(w, className[0], num, graph, (int)target);
					}
					else {
						generateDependencies(path);
						aStar(w, className[0], num, graph, (int)target);
					}
				}
				else {
					System.out.println("Error: Improper src passed for dijkstra. Expecting int.");
					w.close();
					return false;
				}
			}
			else {
				System.out.println("Error: Improper type passed.");
				w.close();
				return false;
			}
			
			w.close();
			System.out.println(path+className[0]+".java");
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//for testing
	public static void main(String[] args) {
		try {
			Object[][] graph = {{"a", "b", "c", "d", "e"},
								{0, 1, 1, 0, 0},
								{0, 0, 0, 0, 0},
								{0, 0, 0, 1, 1},
								{0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0}};
			
			String[] className = {"DFS"};
			generateCode("search", className, false, graph, "c");
			
			className[0] = "Dijkstra";
			generateCode("dijkstra", className, false, graph, 1);
			
			className[0] = "AStar";
			generateCode("aStar", className, false, graph, 5);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
