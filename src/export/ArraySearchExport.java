/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * array search exporter class
 */
package export;

import java.io.*;

public class ArraySearchExport extends CodeExport {
	//generate code body for binary search
	private static void generateBinaryBody(FileWriter f, Object[] arr, Object target, String needSort,boolean num, String type) throws Exception {
		try {
			generateArrayVariables(f, type, arr, num, target);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n");
			
			if(needSort != null) {
				switch(needSort) {
				case "insertion":
					f.append("\t\tSystem.out.println(\"Array is not sorted. Performing insertion sort...\");\n"
							+"\t\tinsertionSort(arr, arr.length);\n\n");
					break;
				case "quick":
					f.append("\t\tSystem.out.println(\"Array is not sorted. Performing quick sort...\");\n"
							+"\t\tquickSort(arr, 0, arr.length-1);\n\n");
					break;
				default: throw new Exception("Improper sort input.");
				}
				f.append("\t\tSystem.out.print(\"Result: \");\n"
						+"\t\tfor(int i=0; i<arr.length; i++) {\n"
						+"\t\t\tSystem.out.print(arr[i]+\", \");\n"
						+"\t\t}\n"
						+"\t\tSystem.out.print('\\n');\n\n");
			}
			
			f.append("\t\tSystem.out.println(\"Searching for: \"+target);\n\n");
			
			if(type.contentEquals("int"))
				f.append("\t\tint result = binarySearch(arr, 0, arr.length-1, (int)target);\n\n");
			else
				f.append("\t\tint result = binarySearch(arr, 0, arr.length-1, target);\n\n");
			
			f.append("\t\tif(result > -1) System.out.println(target+\" was found at position \"+result);\n"
					+"\t\telse System.out.println(target+\" was not found in the array\");\n\n"
					+"\t}\n\n");
			
			if(needSort != null) {
				switch(needSort) {
				case "insertion": ArraySortExport.generateInsertion(f, num, type); break;
				case "quick": ArraySortExport.generateQuick(f, num, type); break;
				}
			}
			
			generateBinary(f, num, type);
			
			f.append("}\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for binary search
	private static void generateBinary(FileWriter f, boolean num, String type) throws Exception {
		try {
			if(num) {
				f.append("\t//Binary Search modified from https://www.geeksforgeeks.org/binary-search/\n"
						+"\tprivate static int binarySearch("+type+"[] arr, int l, int r, "+type+" x) {\n"
						+"\t\tif(r >= l) {\n"
						+"\t\t\tint mid = l+(r-1)/2;\n\n"
						+"\t\t\tif(arr[mid] == x)\n"
						+"\t\t\t\treturn mid;\n\n"
						+"\t\t\tif(arr[mid] > x)\n"
						+"\t\t\t\treturn binarySearch(arr, l, mid-1, x);\n\n"
						+"\t\t\treturn binarySearch(arr, mid+1, r, x);\n"
						+"\t\t}\n\n"
						+"\t\treturn -1;\n"
						+"\t}\n\n");
			}
			else {
				f.append("\t//Binary Search modified from https://www.geeksforgeeks.org/binary-search/\n"
						+"\tprivate static int binarySearch(String[] arr, int l, int r, String x) {\n"
						+"\t\tif(r >= l) {\n"
						+"\t\t\tint mid = l+(r-1)/2;\n\n"
						+"\t\t\tif(arr[mid].contentEquals(x))\n"
						+"\t\t\t\treturn mid;\n\n"
						+"\t\t\tif(arr[mid].compareTo(x) > 0)\n"
						+"\t\t\t\treturn binarySearch(arr, l, mid-1, x);\n\n"
						+"\t\t\treturn binarySearch(arr, mid+1, r, x);\n"
						+"\t\t}\n\n"
						+"\t\treturn -1;\n"
						+"\t}\n\n");
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for binary search
	private static void binarySearch(FileWriter f, String className, boolean num, String needSort, Object[] arr, Object target) throws Exception {
		try {
			String message = "Binary search on a ";
			if(num) message += "number ";
			else message += "String ";
			message += "array.";
			generateHeader(f, message);
			
			String[] imports = {};
			generateImports(f, imports, className);
			
			String type;
			if(num) {
				if(arr[0] instanceof Integer) type = "int";
				else type = "double";
			}
			else type = "String";
			generateBinaryBody(f, arr, target, needSort, num, type);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code body for exponential search code
	private static void generateExponentialBody(FileWriter f, Object[] arr, Object target, String needSort, boolean num, String type) throws Exception {
		try {
			generateArrayVariables(f, type, arr, num, target);
			
			f.append("\t//main\n"
					+"\tpublic static void main(String[] args) {\n");
			
			if(needSort != null) {
				switch(needSort) {
				case "insertion":
					f.append("\t\tSystem.out.println(\"Array is not sorted. Performing insertion sort...\");\n"
							+"\t\tinsertionSort(arr, arr.length);\n");
					break;
				case "quick":
					f.append("\t\tSystem.out.println(\"Array is not sorted. Performing quick sort...\");\n"
							+"\t\tquickSort(arr, 0, arr.length-1);\n");
					break;
				default: throw new Exception("Improper sort input.");
				}
				f.append("\t\tSystem.out.print(\"Result: \");\n"
						+"\t\tfor(int i=0; i<arr.length; i++) {\n"
						+"\t\t\tSystem.out.print(arr[i]+\", \");\n"
						+"\t\t}\n"
						+"\t\tSystem.out.print('\\n');\n\n");
			}
			
			f.append("\t\tSystem.out.println(\"Searching for: \"+target);\n\n");
			
			if(type.contentEquals("int"))
				f.append("\t\tint result = exponentialSearch(arr, arr.length, (int)target);\n\n");
			else
				f.append("\t\tint result = exponentialSearch(arr, arr.length, target);\n\n");
					
			f.append("\t\tif(result > -1) System.out.println(target+\" was found at position \"+result);\n"
					+"\t\telse System.out.println(target+\" was not found in the array\");\n\n"
					+"\t}\n\n");
			
			if(needSort != null) {
			switch(needSort) {
				case "insertion": ArraySortExport.generateInsertion(f, num, type); break;
				case "quick": ArraySortExport.generateQuick(f, num, type); break;
				}
			}
			
			generateBinary(f, num, type);
			
			generateExponential(f, num, type);
			
			f.append("}\n");
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate algorithm for exponential search
	private static void generateExponential(FileWriter f, boolean num, String type) throws Exception {
		try {
			if(num) {
				f.append("\t//Exponential Search modified from https://www.geeksforgeeks.org/exponential-search/\n"
						+"\tprivate static int exponentialSearch("+type+"[] arr, int n, "+type+" x) {\n"
						+"\t\tif(arr[0] == x)\n"
						+"\t\t\treturn 0;\n\n"
						+"\t\tint i=1;\n"
						+"\t\twhile(i<n && arr[i]<=x)\n"
						+"\t\t\ti = i*2;\n\n"
						+"\t\tif(i/2 == Math.min(i, n-1)) return -1;\n"
						+"\t\treturn binarySearch(arr, i/2, Math.min(i, n-1), x);\n"
						+"\t}\n\n");
			}
			else {
				f.append("\t//Exponential Search modified from https://www.geeksforgeeks.org/exponential-search/\n"
						+"\tprivate static int exponentialSearch(String[] arr, int n, String x) {\n"
						+"\t\tif(arr[0].contentEquals(x))\n"
						+"\t\t\treturn 0;\n\n"
						+"\t\tint i=1;\n"
						+"\t\twhile(i<n && arr[i].compareTo(x)<=0)\n"
						+"\t\t\ti = i*2;\n\n"
						+"\t\treturn binarySearch(arr, i/2, Math.min(i, n-1), x);\n"
						+"\t}\n\n");
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//generate code for exponential search
	private static void exponentialSearch(FileWriter f, String className, boolean num, String needSort, Object[] arr, Object target) throws Exception {
		try {
			String message = "Exponential search on a ";
			if(num) message += "number ";
			else message += "String ";
			message += "array.";
			if(needSort != null) message += " With "+needSort+" sort.";
			generateHeader(f, message);
			
			String[] imports = {};
			generateImports(f, imports, className);
			
			String type;
			if(num) {
				if(arr[0] instanceof Integer) type = "int";
				else type = "double";
			}
			else type = "String";
			generateExponentialBody(f, arr, target, needSort, num, type);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	//main
	public static boolean generateCode(String type, String[] className, boolean num, String needSort, Object[] arr, Object target) {
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
			
			switch(type) {
			case "exponential": exponentialSearch(w, className[0], num, needSort, arr, target); break;
			case "binary": binarySearch(w, className[0], num, needSort, arr, target); break;
			default: System.out.println("Error: Improper type passed."); w.close(); return false;
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
			Object[] arr = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};//{0.22, 3.56, 8.7, 4.5};//{1, 2, 3, 4};//{0.22, 3.56, 8.7, 4.5};
			String needSort = null;
			String[] className = {"Test"};
			generateCode("binary", className, false, needSort, arr, 'e');
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
