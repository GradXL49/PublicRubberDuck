/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end message service
 */

package backend;

import java.util.ArrayList;

import gui.ArrayDesigner;
import gui.GraphDesigner;

public class MessageService {
	//variables
	private ArrayList<String> messages;
	private int current;
	Object[][] data;
	
	//constructor
	MessageService() {
		this.messages = new ArrayList<String>();
		this.current = 0;
		this.messages.add("Welcome to the Rubber Duck Program!");
		this.messages.add("What kind of data structure are we working with today?");
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
		this.messages.add(response);
		
		if(response.contentEquals("Okay, let's make an array!")) {
			makeArray();
		}
		else if(response.contentEquals("Okay, let's make an unweighted graph!")) {
			makeGraph(false);
		}
		else if(response.contentEquals("Okay, let's make a weighted graph!")) {
			makeGraph(true);
		}

		else if(response.contentEquals("Okay, let's sort it!")) {
			sort();
		}
	}
	
	public String processMessage(String message) {
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
	}
	
	private void makeArray() {
		try {
			boolean[] success = new boolean[1];
			success[0] = false;
			ArrayDesigner frame = new ArrayDesigner(success, this);
			frame.setVisible(true);
			
			if(success[0]) {
				this.messages.add("Your array: ");
				String[] output = MyUtilities.printArr(data[0]).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
				this.messages.add("Would you like to search, or sort your array?");
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
				this.messages.add("Your graph: ");
				String[] output = MyUtilities.printGraph(data).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
				this.messages.add("Would you like to search, or find a path on your graph?");
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
		if(data.length == 1) { //if it is an array
			int pos = -1;
			if(data[0][0] instanceof String) { //string array
				String[] text = new String[data[0].length];
				for(int i=0; i<data[0].length; i++) {
					text[i] = data[0][i].toString();
				}
				
				pos = SearchAlgorithms.exponentialTextSearch(text, text.length, input);
			}
			else { //number array
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
				
				pos = SearchAlgorithms.exponentialNumberSearch(numbers, numbers.length, target);
			}
			
			if(pos < 0) this.messages.add(input + " was not found in the array.");
			else this.messages.add(input + " was found at position " + pos);
		}
		else { //if it is a graph
			//String path = "";
			boolean[] success = {false};
			ArrayList<Object> path = new ArrayList<Object>();
			Graph g = new Graph(data.length);
			
			if(data[0][0] == null) {
				int target;
				try {
					target = Integer.parseInt(input)-1;
				}
				catch(Exception e) {
					this.messages.add(input + " could not be determined as an integer. Graphs with no data type must search by node index.");
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
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occurred.");
		}
		
	}
	
	private void astar(String input) {
		try {
			int target = Integer.parseInt(input)-1;
			
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
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occured.");
		}
	}
	
	private void sort() {
		try {
			if(data.length == 1) {
				//SortAlgorithms.quickSort(data[0], 0, data[0].length-1);
				SortAlgorithms.heapSort(data[0]);
				
				this.messages.add("Your sorted array:");
				String[] output = MyUtilities.printArr(data[0]).split("\n", 0);
				for(int i=0; i<output.length; i++) {
					this.messages.add(output[i]);
				}
			}
			else {
				this.messages.add("You can't sort a graph.");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			this.messages.add("An error occured.");
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
}
