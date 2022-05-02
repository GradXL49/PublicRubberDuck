/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end sort algorithms
 */

package backend;

public class SortAlgorithms {
	/*
	 * Insertion Sorts
	 * modified from https://www.geeksforgeeks.org/recursive-insertion-sort/
	 */
	public static void insertionSort(Object[] arr) {
		if(arr[0] instanceof String)
			insertionTextSort(arr, arr.length);
		else if(arr[0] instanceof Integer)
			insertionIntSort(arr, arr.length);
		else insertionDoubleSort(arr, arr.length);
	}
	
	private static void insertionDoubleSort(Object[] arr, int n) {
		if(n <= 1)
			return;
		
		insertionDoubleSort(arr, n-1);
		
		double last = Double.parseDouble(arr[n-1].toString());
		int j = n-2;
		
		while(j>=0 && Double.parseDouble(arr[j].toString())>last) {
			arr[j+1] = arr[j];
			j--;
		}
		arr[j+1] = last;
	}
	
	private static void insertionIntSort(Object[] arr, int n) {
		if(n <= 1)
			return;
		
		insertionIntSort(arr, n-1);
		
		double last = (int)Double.parseDouble(arr[n-1].toString());
		int j = n-2;
		
		while(j>=0 && (int)Double.parseDouble(arr[j].toString())>last) {
			arr[j+1] = (int)arr[j];
			j--;
		}
		arr[j+1] = (int)last;
	}
	
	private static void insertionTextSort(Object[] arr, int n) {
		if(n <= 1)
			return;
		
		insertionTextSort(arr, n-1);
		
		String last = arr[n-1].toString();
		int j = n-2;
		
		while(j>=0 && arr[j].toString().compareTo(last)>0) {
			arr[j+1] = arr[j];
			j--;
		}
		arr[j+1] = last;
	}
	
	/*
	 * Quick Sort
	 * modified from https://www.geeksforgeeks.org/quick-sort/
	 */
	//swaps two items in an array
	private static void swap(Object[] arr, int i, int j) {
		Object temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	//finds pivot point
	private static int partitionNumbers(Object[] arr, int low, int high) {
		double pivot = Double.parseDouble(arr[high].toString());
		int i = low-1;
		
		for(int j=low; j<=high-1; j++) {
			if(Double.parseDouble(arr[j].toString()) < pivot) {
				i++;
				swap(arr, i, j);
			}
		}
		swap(arr, i+1, high);
		return i+1;
	}
	private static int partitionStrings(Object[] arr, int low, int high) {
		Object pivot = arr[high];
		int i = low-1;
		
		for(int j=low; j<=high-1; j++) {
			if(arr[j].toString().compareTo(pivot.toString()) < 0) {
				i++;
				swap(arr, i, j);
			}
		}
		swap(arr, i+1, high);
		return i+1;
	}
	
	//algorithm
	public static void quickSort(Object[] arr, int low, int high) {
		if(low < high) {
			int pi;
			if(arr[0] instanceof String)
				pi = partitionStrings(arr, low, high);
			else
				pi = partitionNumbers(arr, low, high);
			
			quickSort(arr, low, pi-1);
			quickSort(arr, pi+1, high);
		}
	}
	
	/*
	 * Heap Sort
	 * modified from https://www.geeksforgeeks.org/heap-sort/
	 */
	//utility
	private static void heapify(Object arr[], int n, int i, String type) {
		int largest = i;
		int l = 2*i + 1;
		int r = 2*i + 2;
		
		if(type.contentEquals("string")) {
			if(l<n && arr[l].toString().compareTo(arr[largest].toString())>0) {
				largest = l;
			}
			
			if(r<n && arr[r].toString().compareTo(arr[largest].toString())>0) {
				largest = r;
			}
		}
		else if(type.contentEquals("double")){
			double ln = Double.parseDouble(arr[largest].toString());
			
			if(l<n && Double.parseDouble(arr[l].toString())>ln) {
				largest = l;
				ln = Double.parseDouble(arr[largest].toString());
			}
			
			if(r<n && Double.parseDouble(arr[r].toString())>ln) {
				largest = r;
			}
		}
		else {
			int ln = (int)Double.parseDouble(arr[largest].toString());
			
			if(l<n && Double.parseDouble(arr[l].toString())>ln) {
				largest = l;
				ln = (int)Double.parseDouble(arr[largest].toString());
			}
			
			if(r<n && (int)Double.parseDouble(arr[r].toString())>ln) {
				largest = r;
			}
		}
		
		if(largest != i) {
			Object swap = arr[i];
			arr[i] = arr[largest];
			arr[largest] = swap;
			
			heapify(arr, n, largest, type);
		}
	}
	
	//algorithm
	public static void heapSort(Object arr[]) {
		int n = arr.length;
		String type = "";
		if(arr[0] instanceof String) type = "string";
		else if(arr[0] instanceof Double) type = "double";
		else type = "int";
		
		for(int i = n/2 - 1; i>=0; i--) {
			heapify(arr, n, i, type);
		}
		
		for(int i=n-1; i>0; i--) {
			Object temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;
			
			heapify(arr, i, 0, type);
		}
	}
}
