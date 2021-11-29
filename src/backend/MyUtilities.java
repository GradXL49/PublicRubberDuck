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
		
		if(arr.length < 1) return "the array is empty";
		
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
	
	//check that an array is sorted
	public static boolean checkSorted(Object[] arr) {
		if(arr[0] instanceof Double) {
			for(int i=1; i<arr.length; i++) {
				if((Double)arr[i] < (Double)arr[i-1])
					return false;
			}
		}
		else {
			for(int i=1; i<arr.length; i++) {
				if(arr[i].toString().compareTo(arr[i-1].toString()) < 0)
					return false;
			}
		}
		
		return true;
	}
	
	//count the number of inversions in an array and return as percentage of array size
	public static double countInversions(Object[] arr) {
		double count = 0;
		
		if(arr[0] instanceof Double || arr[0] instanceof Integer) {
			for(int i=0; i<arr.length-1; i++) {
				if(Double.parseDouble(arr[i].toString()) > Double.parseDouble(arr[i+1].toString())) {
					count++;
				}
			}
		}
		else {
			for(int i=0; i<arr.length-1; i++) {
				if(arr[i].toString().compareTo(arr[i+1].toString()) > 0)
					count++;
			}
		}
		
		System.out.println("number of inversions: " + count);
		System.out.println("array length: " + arr.length);
		System.out.println("percentage: " + (count/arr.length)*100);
		
		return (count/arr.length)*100;
	}
	
	//verify whether an array is in heap form
	//modified from https://www.geeksforgeeks.org/how-to-check-if-a-given-array-represents-a-binary-heap/
	public static boolean isHeap(Object[] arr, int i, int n) {
		if(i >= (n-2)/2)
			return true;
		
		if(arr[0] instanceof Double || arr[0] instanceof Integer) {
			if(Double.parseDouble(arr[i].toString()) >= Double.parseDouble(arr[2*i+1].toString()) &&
					Double.parseDouble(arr[i].toString()) >= Double.parseDouble(arr[2*i+2].toString()) &&
					isHeap(arr, 2*i+1, n) &&
					isHeap(arr, 2*i+2, n))
				return true;
		}
		else {
			if(arr[i].toString().compareTo(arr[2*i+1].toString()) >= 0 &&
					arr[i].toString().compareTo(arr[2*i+2].toString()) >= 0 &&
					isHeap(arr, 2*i+1, n) &&
					isHeap(arr, 2*i+2, n))
				return true;
		}
		
		return false;
	}
}
