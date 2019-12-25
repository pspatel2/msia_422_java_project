package MSiA_422_Project_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MyPandas {

//	public static void main(String[] args) throws IOException 
//	{
//		MyDataFrame df = readCSV("IL.txt");
////		writeCSV(df,"IL.csv");
//	}
	
	//readcsv like function
	public static MyDataFrame readCSV(String pathname) throws IOException {
		//initialize 'columns'
		ArrayList<String> state = new ArrayList<String>();
		ArrayList<String> gender = new ArrayList<String>();
		ArrayList<Integer> year = new ArrayList<Integer>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<Integer> count = new ArrayList<Integer>();
	    LinkedHashMap<String,Integer> colNameMap = new LinkedHashMap<String,Integer>();
	    LinkedHashMap<String,String> colDataTypes = new LinkedHashMap<String,String>();
	    ArrayList<ArrayList<?>> data = new ArrayList<ArrayList<?>> ();
		
	 	File file = new File(pathname);
	 	try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
 			String line;
 			while ((line = bufferedReader.readLine()) != null) {
 				state.add(line.split(",")[0]);
 				gender.add(line.split(",")[1]);
 				year.add(Integer.valueOf(line.split(",")[2]));
 				name.add(line.split(",")[3]);
 				count.add(Integer.valueOf(line.split(",")[4]));
 			}
	 	}catch(Exception e) {
	 		System.out.println("Read failed, verify that the provided path is correct.");
	 	}
	 	
	 	String headers[] = {"state", "gender", "year", "name", "count"};
	    String types[] = {"String", "String", "Integer", "String", "Integer"};
	    for(int i = 0; i < headers.length; i++) {
	    		colNameMap.put(headers[i], i);
	    		colDataTypes.put(headers[i], types[i]);
	    }

	    data.add(state);
	    data.add(gender);
	    data.add(year);
	    data.add(name);
	    data.add(count);
	 	MyDataFrame df = new MyDataFrame(data,colNameMap,colDataTypes); 
	 	return df;
	}
	
	public static void writeCSV(MyDataFrame data, String path) {
		String pathname = path;
	 	File file = new File(pathname);
	 	String header="";
 		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathname));) {
 			for(String s:data.getColNames().keySet()) {
 				header=header+s+",";
 			}
 			bufferedWriter.write(header.substring(0,header.length()-1)+"\n");
 			String[] writeLines = new String[data.shape[0]];
 			for (int k = 0; k < data.shape[0]; k++) {
 				writeLines[k] = data.data.get(0).get(k) + "," + data.data.get(1).get(k)+ "," +data.data.get(2).get(k)+ "," +data.data.get(3).get(k)+ "," +data.data.get(4).get(k);
 			}
 			for (String line : writeLines) {
 				bufferedWriter.write(line+"\n");
 			}
 			bufferedWriter.close();
 			System.out.println("A csv file has been written out to your provided path.");
 		} catch(Exception e) {
 			System.out.println("Write failed, verify that the provided path is of correct form");
 		}
	}
	
	public static MyDataFrame concat(MyDataFrame df1, MyDataFrame df2) {
		MyDataFrame df = new MyDataFrame();
		if (df1.shape[1] == df2.shape[1]) {
			for(int i = 0; i < df1.shape[1]; i++) {
				if (df1.dType(i).equals("String")) {
					ArrayList<String> tmp  =  new ArrayList<String> ();
					tmp.addAll((Collection<? extends String>) df1.data.get(i)); 
					tmp.addAll((Collection<? extends String>) df2.data.get(i));
					df.data.add(tmp);
				}
				else if (df1.dType(i).equals("Integer")) {
					ArrayList<Integer> tmp  =  new ArrayList<Integer>();
					tmp.addAll((Collection<? extends Integer>) df1.data.get(i)); 
					tmp.addAll((Collection<? extends Integer>) df2.data.get(i));
					df.data.add(tmp);
				}
    		}
    		df.setColNames(df1.getColNames());
    		df.setColTypes(df1.colDataTypes);
    		df.setSize(df.data);
    		return df;
		}
		else {
			System.out.println("Your dataframes do not have the same number of columns");
			return df;
		}
	}
}
	
