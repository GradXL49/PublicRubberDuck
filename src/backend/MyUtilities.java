/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end utilities
 */

package backend;

import java.util.ArrayList;

public class MyUtilities {
	//check if a string can be an int
	//modified from https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
	public static boolean isInt(String in) {
		if(in == null) return false;
		int length = in.length();
		if(length ==0 ) return false;
		int i = 0;
	    if(in.charAt(0) == '-') {
	    	if(length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for(; i < length; i++) {
	        char c = in.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	//check if a string can be a double
	public static boolean isDouble(String in) {
		try {
			Double.parseDouble(in);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	//print an array
	public static String printArr(Object[] arr) {
		String out = "";
		
		for(int i=0; i<arr.length; i++) { 
			out += "array["+i+"] = ";
			try {
				out += arr[i].toString();
			}
			catch(Exception e) {
				if(arr[i] == null) out += "null";
				else {
					e.printStackTrace();
					return "There was an unprecidented error printing the given array.";
				}
			}
			if(i < arr.length-1) out += "\n";
		}
		
		return out;
	}
	
	//print an array list
	public static String printArr(ArrayList<Object> arr) {
		String out = "";
		
		for(int i=0; i<arr.size(); i++) {
			out += arr.get(i).toString() + " ";
		}
		
		return out;
	}
	
	//print graph
	public static String printGraph(Object[][] graph) {
		String out = "";
		
		for(int i=0; i<graph.length; i++) {
			for(int j=0; j<graph[0].length; j++) {
				out += graph[i][j] + ", ";
			}
			out += "\n";
		}
		
		return out;
	}
}
