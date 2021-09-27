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
	}
	
	public String processMessage(String message) {
		message = message.toLowerCase();
		String response = "";
		int end = this.messages.size()-2;
		String question = this.messages.get(end+1);
		
		while(question.contains("I'm sorry")) {
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
			}
			else {
				this.messages.add("Hmmm... Looks like you need more time to decide.");
				this.messages.add("What kind of data structure are we working with today?");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDataArray(Object[] d) {
		data = new Object[1][d.length];
		
		for(int i=0; i<d.length; i++) {
			data[0][i] = d[i];
		}
	}
	
	public void setDataGraph(Object[][] d) {
		data = new Object[d.length][d.length];
		
		for(int i=0; i<d.length; i++) {
			for(int j=0; j<d.length; j++) {
				data[i][j] = d[i][j];
			}
		}
	}
}
