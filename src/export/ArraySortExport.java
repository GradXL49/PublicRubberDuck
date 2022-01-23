/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * array sort exporter class
 */
package export;

import java.io.*;

public class ArraySortExport extends CodeExport {
	//generate code body for insertion sort
	private static void generateInsertionBody(FileWriter f, Object[] arr, boolean num, String type) throws Exception {
		try {
			generateArrayVariables(f, type, arr, num, null);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n"
					+"\t\tSystem.out.println(\"Sorting...\");\n\n"
					+"\t\tinsertionSort(arr, arr.length);\n\n"
					+"\t\tSystem.out.print(\"Result: \");\n"
					+"\t\tfor(int i=0; i<arr.length; i++) {\n"
					+"\t\t\tSystem.out.print(arr[i]+\", \");\n"
					+"\t\t}\n"
					+"\t}\n\n");
			
			generateInsertion(f, num, type);
			
			f.append("}\n\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for insertion sort
	public static void generateInsertion(FileWriter f, boolean num, String type) throws Exception {
		try {
			if(num) {
				f.append("\t//Insertion Sort modified from https://www.geeksforgeeks.org/recursive-insertion-sort/\n"
						+"\tprivate static void insertionSort("+type+"[] arr, int n) {\n"
						+"\t\tif(n <= 1) return;\n\n"
						+"\t\tinsertionSort(arr, n-1);\n\n"
						+"\t\t"+type+" last = arr[n-1];\n"
						+"\t\tint j = n-2;\n\n"
						+"\t\twhile(j>=0 && arr[j]>last) {\n"
						+"\t\t\tarr[j+1] = arr[j];\n"
						+"\t\t\tj--;\n"
						+"\t\t}\n\n"
						+"\t\tarr[j+1] = last;\n"
						+"\t}\n\n");
			}
			else {
				f.append("\t//Insertion Sort modified from https://www.geeksforgeeks.org/recursive-insertion-sort/\n"
						+"\tprivate static void insertionSort(String[] arr, int n) {\n"
						+"\t\tif(n <= 1) return;\n\n"
						+"\t\tinsertionSort(arr, n-1);\n\n"
						+"\t\tString last = arr[n-1];\n"
						+"\t\tint j = n-2;\n\n"
						+"\t\twhile(j>=0 && arr[j].compareTo(last)>0) {\n"
						+"\t\t\tarr[j+1] = arr[j];\n"
						+"\t\t\tj--;\n"
						+"\t\t}\n\n"
						+"\t\tarr[j+1] = last;\n"
						+"\t}\n\n");
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for insertion sort
	private static void insertionSort(FileWriter f, String className, boolean num, Object[] arr) throws Exception {
		String message = "Insertion sort on a ";
		if(num) message += "number ";
		else message += "String ";
		message += "array.";
		generateHeader(f, message);
		
		String[] imports = {"java.util.*"};
		generateImports(f, imports, className);
		
		String type;
		if(num) {
			if(arr[0] instanceof Integer) type = "int";
			else type = "double";
		}
		else type = "String";
		generateInsertionBody(f, arr, num, type);
	}
	
	//generate code body for quick sort
	private static void generateQuickBody(FileWriter f, Object[] arr, boolean num, String type) throws Exception {
		try {
			generateArrayVariables(f, type, arr, num, null);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n"
					+"\t\tSystem.out.println(\"Sorting...\");\n\n"
					+"\t\tquickSort(arr, 0, arr.length-1);\n\n"
					+"\t\tSystem.out.print(\"Result: \");\n"
					+"\t\tfor(int i=0; i<arr.length; i++) {\n"
					+"\t\t\tSystem.out.print(arr[i]+\", \");\n"
					+"\t\t}\n"
					+"\t}\n\n");
			
			generateQuick(f, num, type);
			
			f.append("}\n\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for quick sort
	public static void generateQuick(FileWriter f, boolean num, String type) throws Exception {
		try {
			if(num) {
				f.append("\t//Quick Sort modified from https://www.geeksforgeeks.org/quick-sort/\n"
						+"\tprivate static void swap("+type+"[] arr, int i, int j) {\n"
						+"\t\t"+type+" temp = arr[i];\n"
						+"\t\tarr[i] = arr[j];\n"
						+"\t\tarr[j] = temp;\n"
						+"\t}\n\n"
						+"\tprivate static int partition("+type+"[] arr, int low, int high) {\n"
						+"\t\t"+type+" pivot = arr[high];\n"
						+"\t\tint i = low-1;\n\n"
						+"\t\tfor(int j=low; j<=high-1; j++) {\n"
						+"\t\t\tif(arr[j] < pivot) {\n"
						+"\t\t\t\ti++;\n"
						+"\t\t\t\tswap(arr, i, j);\n"
						+"\t\t\t}\n"
						+"\t\t}\n"
						+"\t\tswap(arr, i+1, high);\n\t\treturn i+1;\n"
						+"\t}\n\n"
						+"\tprivate static void quickSort("+type+"[] arr, int low, int high) {\n"
						+"\t\tif(low < high) {\n"
						+"\t\t\tint pi=partition(arr, low, high);\n\n"
						+"\t\t\tquickSort(arr, low, pi-1);\n"
						+"\t\t\tquickSort(arr, pi+1, high);\n"
						+"\t\t}\n"
						+"\t}\n\n");
			}
			else {
				f.append("\t//Quick Sort modified from https://www.geeksforgeeks.org/quick-sort/\n"
						+"\tprivate static void swap(String[] arr, int i, int j) {\n"
						+"\t\tString temp = arr[i];\n"
						+"\t\tarr[i] = arr[j];\n"
						+"\t\tarr[j] = temp;\n"
						+"\t}\n\n"
						+"\tprivate static int partition(String[] arr, int low, int high) {\n"
						+"\t\tString pivot = arr[high];\n"
						+"\t\tint i = low-1;\n\n"
						+"\t\tfor(int j=low; j<=high-1; j++) {\n"
						+"\t\t\tif(arr[j].compareTo(pivot) < 0) {\n"
						+"\t\t\t\ti++;\n"
						+"\t\t\t\tswap(arr, i, j);\n"
						+"\t\t\t}\n"
						+"\t\t}\n"
						+"\t\tswap(arr, i+1, high);\t\t\nreturn i+1;\n"
						+"\t}\n\n"
						+"\tprivate static void quickSort(String[] arr, int low, int high) {\n"
						+"\t\tif(low < high) {\n"
						+"\t\t\tint pi=partition(arr, low, high);\n\n"
						+"\t\t\tquickSort(arr, low, pi-1);\n"
						+"\t\t\tquickSort(arr, pi+1, high);\n"
						+"\t\t}\n"
						+"\t}\n\n");
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for quick sort
	private static void quickSort(FileWriter f, String className, boolean num, Object[] arr) throws Exception {
		String message = "Quick sort on a ";
		if(num) message += "number ";
		else message += "String ";
		message += "array.";
		generateHeader(f, message);
		
		String[] imports = {"java.util.*"};
		generateImports(f, imports, className);
		
		String type;
		if(num) {
			if(arr[0] instanceof Integer) type = "int";
			else type = "double";
		}
		else type = "String";
		generateQuickBody(f, arr, num, type);
	}
	
	//main
	public static boolean generateCode(String type, String className, boolean num, Object[] arr) {
		try {
			String path = new File("").getCanonicalPath() + "\\src\\output\\"+className+".java";
			System.out.println(path);
			
			File f = new File(path);
			if(!f.createNewFile()) {
				System.out.println("Error: File already exists!");
				return false;
			}
			FileWriter w = new FileWriter(f);
			
			switch(type) {
			case "insertion": insertionSort(w, className, num, arr); break;
			case "quick": quickSort(w, className, num, arr); break;
			default: System.out.println("Error: Improper type passed."); w.close(); return false;
			}
			
			w.close();
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//for testing
	public static void main(String[] args) {
		Object[] arr = {8.7, 3.56, 0.22, 4.5};
		generateCode("quick", "Test", true, arr);
	}
}
