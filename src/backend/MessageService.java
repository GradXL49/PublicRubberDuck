/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end message service
 */

package backend;

import java.text.DecimalFormat;
import java.util.ArrayList;

import agent.AgentModel;
import export.*;
import gui.ArrayDesigner;
import gui.GraphDesigner;

public class MessageService {
	//variables
	private ArrayList<String> messages;
	private int current;
	private AgentModel agent;
	private String[] options;
	private boolean dataMade;
	private boolean sorted;
	Object[][] data;
	DecimalFormat dec = new DecimalFormat("#.00");
	
	//constructor
	MessageService() {
		agent = new AgentModel();
		options = new String[5];
		dataMade = false;
		sorted = false;
		for(int i=0; i<options.length; i++) {
			options[i] = null;
		}
		this.messages = new ArrayList<String>();
		this.current = 0;
		this.messages.add("Welcome to the Rubber Duck Program!");
		this.messages.add("What can I help you with today?");
	}
	
	public ArrayList<String> getNewMessages() {
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i=this.current; i<this.messages.size(); i++) {
			temp.add(this.messages.get(i));
		}
		this.current = this.messages.size();
		
		return temp;
	}
	
	public void receiveMessage(String message) {
		String response = this.processMessage(message);
		
		if(response.contains("Okay, let's make an array!")) {
			this.messages.add(response);
			makeArray();
		}
		else if(response.contentEquals("Okay, let's make an unweighted graph.")) {
			if(options[3] != null)
				makeGraph(options[3].contentEquals("weighted"));
		}
		/* else if(response.contentEquals("Okay, let's sort it!")) {
			sort();
		}*/
		
		if(optionsFull()) {
			if(!this.dataMade) {
				if(options[2].contentEquals("array")) makeArray();
				else makeGraph(options[3].contentEquals("weighted"));
			}
			
			if(this.dataMade) {
				if(options[1] == null) {
					response = "";
					giveRecommendation();
				}
				else if(options[1].contentEquals("search")) {
					if(options[4] == null) response = "What are we looking for?";
					else search(options[4]);
				}
				else if(options[1].contentEquals("sort")) {
					if(options[2].contentEquals("graph"))
						response = "We can't sort a graph. Would you like to search it or find a path?";
					else sort(true);
				}
				else if(options[2].contentEquals("array")) response = "We can't find a path on an array. Would you like to search or sort?";
				else if(options[1].contentEquals("path dijkstra")) {
					if(options[4] == null) response = "Which is the starting node?";
					else shortestPath(options[4]);
				}
				else {
					if(options[4] == null) response = "Which is the target node?";
					else aStar(options[4]);
				}
			
				if(!response.isEmpty()) this.messages.add(response);
				options[4] = null;
			}
		}
		else this.messages.add(response);
	}
	
	public String processMessage(String message) {
		if(message.contentEquals("restart")) {
			for(int i=0; i<options.length; i++) {
				options[i] = null;
			}
			dataMade = false;
			sorted = false;
			return "What can I help you with today?";
		}
		
		boolean input;
		try {
			String last = messages.get(messages.size()-1);
			input = last.contentEquals("What are we looking for?") || last.contentEquals("Which is the starting node?") || last.contentEquals("Which is the target node?");
		}
		catch(Exception e) {
			input = false;
		}
		agent.getResponse(message, options, input);
		return options[0];
		
		/* THIS IS THE ORIGINAL HANDMADE AI USED FOR INITIAL DEVELOPMENT/TESTING
		message = message.toLowerCase();
		String response = "";
		
		if(message.contains("restart")) return "What kind of data structure are we working with today?";
		
		int end = this.messages.size()-2;
		String question = this.messages.get(end+1);
		while(!question.contains("?")) {
			question = this.messages.get(end);
			end--;
			if(end < 0) {
				System.out.println("MessageService: ERROR. Could not find last question asked.");
				return "MessageService: ERROR. Could not find last question asked.";
			}
		}
		
		if(question.contentEquals("What kind of data structure are we working with today?")) {
			if(message.contains("array")) response = "Okay, let's make an array!";
			else if(message.contains("graph")) response = "A graph, okay, do you want to make it a weighted graph?";
			else response = "I'm sorry, I don't understand. Enter 'array' to make an array, or 'graph' to make a graph.";
		}
		else if(question.contentEquals("A graph, okay, do you want to make it a weighted graph?")) {
			if(message.contains("no") || message.contains("un")) response = "Okay, let's make an unweighted graph!";
			else if(message.contains("yes") || message.contains("weighted")) response = "Okay, let's make a weighted graph!";
			else response = "I'm sorry, I don't understand. Enter 'yes' to make a weighted graph, or 'no' to make an unweighted graph.";
		}
		else if(question.contentEquals("Would you like to search, or sort your array?")) {
			if(message.contains("search")) response = "Okay, let's do a search! Without entering unnecesarry text, what are you looking for?";
			else if(message.contains("sort")) response = "Okay, let's sort it!";
			else response = "I'm sorry, I don't understand. Enter 'search' to perform a search, or 'sort' to sort it.";
		}
		else if(question.contentEquals("Would you like to search, or find a path on your graph?")) {
			if(message.contains("search")) response = "Okay, let's do a search! Without entering unnecesarry text, what are you looking for?";
			else if(message.contains("path")) response = "Do you want all shortest paths or one specific path?";
		}
		else if(question.contentEquals("Okay, let's do a search! Without entering unnecesarry text, what are you looking for?")) {
			search(message);
		}
		else if(question.contentEquals("Do you want all shortest paths or one specific path?")) {
			if(message.contains("all") || message.contains("shortest")) response = "Okay, let's find all shortest paths. Which is the source node?";
			else if(message.contains("one") || message.contains("specific")) response = "Okay, let's find the path. Which is the destination node?";
		}
		else if(question.contains("Okay, let's find all shortest paths.")) {
			shortestPath(message);
		}
		else if(question.contains("Okay, let's find the path.")) {
			astar(message);
		}
		else {
			System.out.println("MessageService: ERROR. Could not find last question asked. Last said: "+question);
			response = "MessageService: ERROR. Could not find last question asked. Last said: "+question;
		}
		
		return response;
		*/
	}
	
	private void makeArray() {
		try {
			boolean[] success = new boolean[1];
			success[0] = false;
			ArrayDesigner frame = new ArrayDesigner(success, this);
			frame.setVisible(true);
			
			if(success[0]) {
				this.dataMade = true;
				this.messages.add("Your array: ");
				String[] output = MyUtilities.printArr(data[0]).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
				//this.messages.add("Would you like to search, or sort your array?");
			}
			else {
				this.messages.add("Hmmm... Looks like you need more time to decide.");
				this.messages.add("What kind of data structure are we working with today?");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void makeGraph(boolean weighted) {
		try {
			boolean[] success = {false, weighted};
			GraphDesigner frame = new GraphDesigner(success, this);
			frame.setVisible(true);
			
			if(success[0]) {
				this.dataMade = true;
				this.messages.add("Your graph: ");
				String[] output = MyUtilities.printGraph(data).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
				//this.messages.add("Would you like to search, or find a path on your graph?");
			}
			else {
				this.messages.add("Hmmm... Looks like you need more time to decide.");
				this.messages.add("What kind of data structure are we working with today?");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search(String input) {
		String needSort = null;
		String[] className = new String[1];
		boolean num;
		Object[][] copy = new Object[0][0];
		if(data.length==1 && !sorted) {
			sorted = MyUtilities.checkSorted(data[0]);
			if(!sorted) {
				this.messages.add("Looks like the array isn't sorted, we should do that to make the search more efficient.");
				copy = new Object[data.length][data[0].length];
				for(int i=0; i<data.length; i++) {
					for(int j=0; j<data[0].length; j++) {
						copy[i][j] = data[i][j];
					}
				}
				needSort = sort(false);
			}
		}
		
		if(data.length == 1) { //if it is an array
			int pos = -1;
			boolean export = false;
			if(data[0][0] instanceof String) { //string array
				num = false;
				String[] text = new String[data[0].length];
				for(int i=0; i<data[0].length; i++) {
					text[i] = data[0][i].toString();
				}
				
				if(text.length > 100) {
					this.messages.add("Because your array is so large, I recommend an Exponential Search.");
					pos = SearchAlgorithms.exponentialTextSearch(text, text.length, input);
					className[0] = "ExponentialTextSearch";
					if(needSort == null) export = ArraySearchExport.generateCode("exponential", className, num, needSort, text, input);
					else export = ArraySearchExport.generateCode("exponential", className, num, needSort, copy[0], input);
				}
				else {
					this.messages.add("Your array isn't very big, so I recommend a Binary Search.");
					pos = SearchAlgorithms.binaryTextSearch(text, 0, text.length-1, input);
					className[0] = "BinaryTextSearch";
					if(needSort == null) export = ArraySearchExport.generateCode("binary", className, num, needSort, text, input);
					else export = ArraySearchExport.generateCode("binary", className, num, needSort, copy[0], input);
				}
			}
			else { //number array
				num = true;
				double target = 0;
				try {
					target = Double.parseDouble(input);
				}
				catch(Exception e) {
					this.messages.add(input + " could not be determined as a number. Please try again.");
					return;
				}
				
				double[] numbers = new double[data[0].length];
				for(int i=0; i<data[0].length; i++) {
					numbers[i] = Double.parseDouble(data[0][i].toString());
				}
				
				if(numbers.length > 100) {
					this.messages.add("Because your array is so large, I recommend an Exponential Search.");
					pos = SearchAlgorithms.exponentialNumberSearch(numbers, numbers.length, target);
					className[0] = "ExponentialNumSearch";
					if(needSort == null) export = ArraySearchExport.generateCode("exponential", className, num, needSort, data[0], target);
					else export = ArraySearchExport.generateCode("exponential", className, num, needSort, copy[0], target);
				}
				else {
					this.messages.add("Your array isn't very big, so I recommend a Binary Search.");
					pos = SearchAlgorithms.binaryNumberSearch(numbers, 0, numbers.length-1, target);
					className[0] = "BinaryNumSearch";
					if(needSort == null) export = ArraySearchExport.generateCode("binary", className, num, needSort, data[0], target);
					else export = ArraySearchExport.generateCode("binary", className, num, needSort, copy[0], target);
				}
			}
			
			if(pos < 0) this.messages.add(input + " was not found in the array.");
			else this.messages.add(input + " was found at position " + pos);
			
			if(export) this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
			else this.messages.add("Code generation failed. Check console for details.");
		}
		else { //if it is a graph
			//String path = "";
			num = true;
			boolean[] success = {false};
			ArrayList<Object> path = new ArrayList<Object>();
			Graph g = new Graph(data.length);
			
			if(data[0][0] == null) {
				className[0] = "NoDataDFS";
				int target;
				try {
					target = Integer.parseInt(input)-1;
				}
				catch(Exception e) {
					this.messages.add(input + " could not be determined as an integer. Please reference the node by its index."); //Graphs with no data type must search by node index.");
					return;
				}
				
				Object[] d = new Object[data[0].length];
				for(int i=0; i<data.length; i++) {
					for(int j=0; j<data[0].length; j++) {
						if(i == 0) d[j] = j;
						else if((Integer)data[i][j] != 0) {
							g.addEdge(i-1, j);
						}
					}
				}
				 
				//path = SearchAlgorithms.BFS(0, g, d, target);
				SearchAlgorithms.DFS(0, g, new boolean[g.getSize()], d, target, success, path);
				input = "Node " + input;
				
				if(GraphExport.generateCode("search", className, num, data, target))
					this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
				else this.messages.add("Code generation failed. Check console for details.");
			}
			else {
				Object target;
				if(data[0][0] instanceof Double) {
					target = Double.parseDouble(input);
					className[0] = "DoubleDFS";
				}
				else if(data[0][0] instanceof Integer) {
					target = Integer.parseInt(input);
					className[0] = "IntDFS";
				}
				else {
					target = input;
					className[0] = "StringDFS";
					num = false;
				}
				
				for(int i=1; i<data.length; i++) {
					for(int j=0; j<data[1].length; j++) {
						if((Integer)data[i][j] != 0)
							g.addEdge(i-1, j);
					}
				}
				
				SearchAlgorithms.DFS(0, g, new boolean[g.getSize()], data[0], target, success, path);
				
				if(GraphExport.generateCode("search", className, num, data, target))
					this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
				else this.messages.add("Code generation failed. Check console for details.");
			}
			
			/*if(path.contentEquals("")) this.messages.add(input + " was not found in the graph.");
			else this.messages.add(input + " was found on the path: " + path);*/
			if(success[0]) {
				this.messages.add(input + " was found in the graph.");
				this.messages.add("Path traveled: " + MyUtilities.printArr(path));
			}
			else this.messages.add(input + " was not found in the graph.");
		}
	}
	
	private void shortestPath(String input) {
		try {
			int src = Integer.parseInt(input)-1;
			
			String[] className = {"Dijkstra"};
			boolean num = true;
			if(data[0][0] != null && data[0][0] instanceof String)
				num = false;
			
			int V = data[0].length;
			int[][] graph = new int[V][V];
			for(int i=1; i<data.length; i++) {
				for(int j=0; j<V; j++) {
					graph[i-1][j] = (Integer)data[i][j];
				}
			}
			
			String[] output = SearchAlgorithms.dijkstra(graph, src, V).split("\n", 0);
			for(int i=0; i<output.length; i++) {
				this.messages.add(output[i]);
			}
			
			if(GraphExport.generateCode("dijkstra", className, num, data, src+1))
				this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
			else this.messages.add("Code generation failed. Check console for details.");
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occurred.");
		}
		
	}
	
	private void aStar(String input) {
		try {
			int target = Integer.parseInt(input)-1;
			
			String[] className = {"AStar"};
			boolean num = true;
			if(data[0][0] != null && data[0][0] instanceof String)
				num = false;
			
			ArrayList<Node> graph = new ArrayList<Node>();
			String id = "";
			for(int i=1; i<data.length; i++) {
				if(data[0][i-1] != null) id = data[0][i-1].toString();
				else id = "node"+i;
				graph.add(new Node(id));
			}
			for(int i=1; i<data.length; i++) {
				int count = 0;
				for(int j=0; j<data[0].length; j++) {
					if((Integer)data[i][j] != 0) {
						graph.get(i-1).addBranch((Integer)data[i][j], graph.get(j));
						count++;
					}
				}
				graph.get(i-1).setH(count);
			}
			
			graph.get(0).g = 0;
			String path = SearchAlgorithms.aStar(graph.get(0), graph.get(target));
			this.messages.add(path);
			
			if(GraphExport.generateCode("aStar", className, num, data, target+1))
				this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
			else this.messages.add("Code generation failed. Check console for details.");
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occured.");
		}
	}
	
	private String sort(boolean generate) {
		try {
			if(data.length == 1) {
				String s, type;
				String[] className = new String[1];
				boolean num, export=false;
				double invRate = MyUtilities.countInversions(data[0]);
				if(num = data[0][0] instanceof String) type="String";
				else type="Num";
				
				if(data[0].length <= 10) {
					this.messages.add("Your array isn't very big, so I recommend an Insertion Sort.");
					s = "insertion";
					className[0] = "Insertion"+type+"Sort";
					if(generate) export = ArraySortExport.generateCode(s, className, num, data[0]);
					SortAlgorithms.insertionSort(data[0]);
				}
				else if(MyUtilities.isHeap(data[0], 0, data[0].length)) {
					this.messages.add("Looks like your array is already in max-heap form, I recommend a Heap Sort.");
					s = "heap";
					className[0] = "Heap"+type+"Sort";
					if(generate) export = ArraySortExport.generateCode(s, className, num, data[0]);
					SortAlgorithms.heapSort(data[0]);
				}
				else if(invRate <= 5) {
					this.messages.add("Your array contains only " + dec.format(invRate) + "% inversions, so I recommend an Insertion Sort.");
					s = "insertion";
					className[0] = "Insertion"+type+"Sort";
					if(generate) export = ArraySortExport.generateCode(s, className, num, data[0]);
					SortAlgorithms.insertionSort(data[0]);
				}
				else {
					this.messages.add("Your array doesn't appear to be mostly sorted, so Quick Sort is the best choice.");
					s = "quick";
					className[0] = "Quick"+type+"Sort";
					if(generate) export = ArraySortExport.generateCode(s, className, num, data[0]);
					SortAlgorithms.quickSort(data[0], 0, data[0].length-1);
				}
				
				this.messages.add("Your sorted array:");
				String[] output = MyUtilities.printArr(data[0]).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
				
				if(generate) {
					if(export) this.messages.add("Code that performs your desired task was exported to the output package as "+className[0]+".java");
					else this.messages.add("Code generation failed. Check console for details.");
				}
				
				sorted = true;
				return s;
			}
			else {
				this.messages.add("You can't sort a graph.");
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occured.");
			return null;
		}
	}
	
	public void setDataArray(Object[] d) {
		data = new Object[1][d.length];
		
		for(int i=0; i<d.length; i++) {
			data[0][i] = d[i];
		}
	}
	
	public void setDataGraph(Object[][] d) {
		data = new Object[d.length][d.length-1];
		
		for(int i=0; i<d.length; i++) {
			for(int j=0; j<d.length-1; j++) {
				data[i][j] = d[i][j+1];
			}
		}
	}
	
	private void giveRecommendation() {
		this.messages.add("Looks like you might need a recommendation for the operation to perform.");
		try {
			if(data.length == 1) {
				this.messages.add("Since you made an array, we can search it or sort it.");
				
				if(data[0].length > 100) {
					this.messages.add("Because your array is so large, I would recommend an Exponential Search.");
				}
				else {
					this.messages.add("Your array isn't very big, so I would recommend a Binary Search.");
				}
				
				double invRate = MyUtilities.countInversions(data[0]);
				if(data[0].length <= 10) {
					this.messages.add("Your array isn't very big, so for a sort I would recommend an Insertion Sort.");
				}
				else if(MyUtilities.isHeap(data[0], 0, data[0].length)) {
					this.messages.add("Looks like your array is already in max-heap form, so for a sort I would recommend a Heap Sort.");
				}
				else if(invRate <= 5) {
					this.messages.add("Your array contains only " + dec.format(invRate) + "% inversions, so for a sort I would recommend an Insertion Sort.");
				}
				else {
					this.messages.add("Your array doesn't appear to be mostly sorted, so I think Quick Sort would be the best choice.");
				}
			}
			else {
				this.messages.add("Since you made a graph, we can search it or find a path on it.");
				this.messages.add("If you would like to find a path from the root (Node 1) to another specific node, I would recommend the A* Search.");
				this.messages.add("Or you can find the path to all other nodes from a specific starting point with Dijkstra's Algorithm.");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occured.");
		}
	}
	
	private boolean optionsFull() {
		if(options[2] != null) {
			if(options[1] == null && options[2].contentEquals("array")) {
				return true;
			}
			else if(options[1]!=null && (options[1].contentEquals("search") || options[1].contentEquals("sort") || options[1].contains("path"))) {
				if(options[2].contentEquals("array"))
					return true;
				else if(options[2].contentEquals("graph")) {
					if(options[3] != null) {
						if(options[3].contentEquals("weighted") || options[3].contentEquals("unweighted"))
							return true;
						else {
							System.out.println("ERROR: Invalid g_type "+options[3]);
							return false;
						}
					}
					else return false;
				}
				else {
					System.out.println("ERROR: Invalid db_type "+options[2]);
					return false;
				}
			}
			else {
				System.out.println("ERROR: Invalid operation "+options[1]);
				return false;
			}
		}
		else return false;
	}
}
