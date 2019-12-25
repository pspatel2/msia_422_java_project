package MSiA_422_Project_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MyDataFrame {
    ArrayList<ArrayList<?>> data;
    LinkedHashMap<String,Integer> colNameMap = new LinkedHashMap<String,Integer>();
    LinkedHashMap<String,String> colDataTypes = new LinkedHashMap<String,String>();
    Integer[] shape = new Integer[2];

//-----------------------------------------------------------------------------------------------------------------------------------------
//DEFINE CONSTRUCTORS	
    MyDataFrame(ArrayList<ArrayList<?>> data, LinkedHashMap<String, Integer> colNames, LinkedHashMap<String, String> colTypes) {
    	this.data = data;
		this.colNameMap = colNames;
		this.colDataTypes = colTypes; 
	    this.shape[0] = data.get(0).size(); //number of rows
	    this.shape[1] = colNameMap.size(); // number of columns
    }

	MyDataFrame() {
		this.data = new ArrayList<ArrayList<?>>();
	}

//--------------------------------------------------------------------------------------------------------------------------------------------
//GETTERS AND SETTERS
     LinkedHashMap<String,Integer> getColNames() {
		return(this.colNameMap);
    }

    void setColNames(LinkedHashMap<String,Integer> colNames) {
		this.colNameMap = colNames;
    }
    
    void setColTypes(LinkedHashMap<String,String> colTypes) {
		this.colDataTypes = colTypes;
    }
    
    void setSize(ArrayList<ArrayList<?>> data) {
		this.shape[0] = data.get(0).size();
		this.shape[1] = data.size();
    }
    
    private String getColName(int index)
    {
		String col = "";
        for (String key : this.colNameMap.keySet()){
        		if(this.colNameMap.get(key).equals(index)) {
        			col = key;
        		}
        }
        return col;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------	
//DATAFRAME METHODS (PARTS 1-3) head/tail, dType, slicing
  
    //Head method--allows user to see (and get a dataframe object of) the first n number of rows of the dataframe
    public MyDataFrame head(int n) {
		MyDataFrame df = new MyDataFrame();
		try {
			for(int i = 0; i < this.shape[1]; i++) {
				ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(0, n));
				df.data.add(tmp);
			}
		}catch(Exception e) {
				System.out.println("Input entered is either not an integer or it is larger than the number of rows in the DataFrame");
			}
		df.setColNames(this.getColNames());
		df.setColTypes(this.colDataTypes);
		df.setSize(df.data);
		return df;
	}
	
    //Tail method; allows user to see (and get a dataframe object of) the last n rows of the dataframe
	public MyDataFrame tail(int n) {
		MyDataFrame df = new MyDataFrame();
		try {
			for (int i = 0; i < this.shape[1]; i++) {
				ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(this.shape[0]-n, this.shape[0]));
				df.data.add(tmp);
			}
		}catch(Exception e) {
			System.out.println("Input entered is either not an integer or it is larger than the number of rows in the DataFrame");
		}
		df.setColNames(this.getColNames());
		df.setColTypes(this.colDataTypes);
		df.setSize(df.data);
		return df;	
	}
	
	//returns the data type of a column specified by its index
	public String dType(int index) {
        String col = getColName(index);
		if(col.equals("")) {
        	return("Not a valid index");
        }
        else {
        String dtype = this.colDataTypes.get(col);
//        System.out.println("The data type of "+col+" is "+ dtype+"\n");
        return(dtype);
        }
	}
	
	//returns the data type of a column specified by its name
	public String dType(String name) {
		if(!this.colNameMap.containsKey(name)) {
			return "Not a valid column name.";
		}
		else {
			String dtype = this.colDataTypes.get(name);
//	        System.out.println("The data type of "+name+" is "+ dtype+"\n");
	        return(dtype);
		}
	}
	
	//slices the DataFrame to a column of interested when provided the index of the column
	public MyDataFrame slice(int index) {
		MyDataFrame df = new MyDataFrame();
		df.data.add(this.data.get(index));
        String col = getColName(index);
        LinkedHashMap<String,Integer> colName = new LinkedHashMap<String,Integer>() {{put(col,0);}};
        String dtype = this.colDataTypes.get(col);
        LinkedHashMap<String,String> colDType = new LinkedHashMap<String,String>() {{put(col,dtype);}};
		df.setColNames(colName);
		df.setColTypes(colDType);
		df.setSize(df.data);
		return df;
	}	
		
	//slices the DataFrame to a column of interested when provided the name of the column
	public MyDataFrame slice(String name) {
		MyDataFrame df = new MyDataFrame();
		int index = this.getColNames().get(name);
		df.data.add(this.data.get(index));
        LinkedHashMap<String,Integer> colName = new LinkedHashMap<String,Integer>() {{put(name,0);}};
        String dtype = this.colDataTypes.get(name);
        LinkedHashMap<String,String> colDType = new LinkedHashMap<String,String>() {{put(name,dtype);}};
		df.setColNames(colName);
		df.setColTypes(colDType);
		df.setSize(df.data);
		return this.slice(index);
	}
	
	//slices the DataFrame to column(s) of interested when provided the index(es) of the column(s)
	public MyDataFrame slice(int[] indexArr) {
		MyDataFrame df = new MyDataFrame();
		LinkedHashMap<String,Integer> colName = new LinkedHashMap<String,Integer>();
		LinkedHashMap<String,String> colDType = new LinkedHashMap<String,String>();
		for(int i = 0; i<indexArr.length;i++) {
			String key = this.slice(indexArr[i]).getColNames().keySet().toString();
			colName.put(key, i);
			colDType.put(key, this.colDataTypes.get(key));
			df.data.add(this.slice(indexArr[i]).data.get(0));
		}
		df.setColNames(colName);
		df.setColTypes(colDType);
		df.setSize(df.data);
		return df;
	}
	
	//slices the DataFrame to column(s) of interested when provided the name(s) of the column(s)
	public MyDataFrame slice(String[] nameArr) {
		int[] Indexes = new int[nameArr.length];
		for(int i = 0; i<nameArr.length;i++) {
			Indexes[i] = this.colNameMap.get(nameArr[i]);
		}
		return slice(Indexes);
	}

//-----------------------------------------------------------------------------------------------------------------------------------------
//DATAFRAME METHODS PART 4 (FILTERING)
//filter on any column by the following operations: =, !=, >, <, >=, <=
public MyDataFrame filter(String col, String op, Object o) {
		
		ArrayList<String> state = new ArrayList<String>();
		ArrayList<String> gender= new ArrayList<String>();
		ArrayList<Integer> year= new ArrayList<Integer>();
		ArrayList<String> name= new ArrayList<String>();
		ArrayList<Integer> count= new ArrayList<Integer>();
		ArrayList<ArrayList<?>> filteredData = new ArrayList<ArrayList<?>> ();
		
		String dtype = this.dType(col);
		Integer colNum = this.colNameMap.get(col);
		
		//logic for greater than operator; branch for integer and string
		if(op.equals(">")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if((int)this.data.get(colNum).get(i) > (int)o) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().compareTo(o.toString())>0) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
		
		//logic for less than operator; branch for integer and string
		else if(op.contentEquals("<")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if(((int)this.data.get(colNum).get(i) < (int)o)) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().compareTo(o.toString())<0) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
		
		//logic for equals operator; branch for integer and string
		else if(op.contentEquals("=")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if(((int)this.data.get(colNum).get(i) == (int)o)) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().equals(o.toString())) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
		
		//logic for 'greater than or equal to' operator; branch for integer and string
		else if(op.contentEquals(">=")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if((int)this.data.get(colNum).get(i) >= (int)o) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().equals(o.toString()) || this.data.get(colNum).toString().compareTo(o.toString())>0) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
		//logic for 'greater than or equal to' operator; branch for integer and string
		else if(op.contentEquals("<=")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if((int)this.data.get(colNum).get(i) <= (int)o) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().equals(o.toString()) || this.data.get(colNum).toString().compareTo(o.toString())<0) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
		//logic for equals operator; branch for integer and string
		else if(op.contentEquals("!=")){
			if(dtype.equalsIgnoreCase("Integer")) {
				for(int i =0; i<this.shape[0]; i++) {
					if(((int)this.data.get(colNum).get(i) != (int)o)) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
			else {
				for(int i =0; i<this.shape[0]; i++) {
					if(this.data.get(colNum).toString().equals(o.toString())==false) {
						state.add((String) this.data.get(0).get(i));
						gender.add((String) this.data.get(1).get(i));
						year.add((Integer) this.data.get(2).get(i));
						name.add((String) this.data.get(3).get(i));
						count.add((Integer) this.data.get(4).get(i));
					}
				}
			}
		}
			
		if(state.size()==0) {
			System.out.println("The filters you applied led to an empty dataframe");
			return new MyDataFrame();
		}
		else {
			filteredData.add(state);
			filteredData.add(gender);
			filteredData.add(year);
			filteredData.add(name);
			filteredData.add(count);
			MyDataFrame df= new MyDataFrame(filteredData,this.getColNames(),this.colDataTypes);
			df.setSize(df.data);
//			df.print();
			return df;
		}
	}

//---------------------------------------------------------------------------------------------------------------------------------------------
//DATAFRAME METHODS PART 5: INDEXING METHODS
	public MyDataFrame loc(int index) {
		MyDataFrame df = new MyDataFrame();
		try {
			for(int i = 0; i < this.shape[1]; i++) {
				ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(index, this.shape[0]));
				df.data.add(tmp);
			}
		}catch(Exception e) {
				System.out.println("Input entered is either not an integer or it is larger than the number of rows in the DataFrame");
			}
		df.setColNames(this.getColNames());
		df.setColTypes(this.colDataTypes);
		df.setSize(df.data);
//		df.print();
		return df;
	}
	
	public MyDataFrame loc(String label) {
		MyDataFrame df = new MyDataFrame();
		boolean matchFlag = false;
		int iter = 0;
		while(!matchFlag) {
			if(iter>=this.shape[0]) {
				System.out.println("No match");
				return df;
			}
			else if(label.equals(this.data.get(0).get(iter))||label.equals(this.data.get(1).get(iter))||label.equals(this.data.get(3).get(iter))){
				matchFlag = true;
			}
			iter++;
		}
		for (int i = 0; i < this.shape[1]; i++) {
			ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(iter-1, this.shape[0]));
			df.data.add(tmp);
		}
		df.setColNames(this.getColNames());
		df.setColTypes(this.colDataTypes);
		df.setSize(df.data);
//		df.print();
		return df;
	}
	
	public MyDataFrame loc(String from, String to){
		MyDataFrame df = new MyDataFrame();
		boolean matchFromFlag = false;
		int iterFrom = 0;
		int iterTo = 0;
		boolean matchToFlag= false;
		while(!matchFromFlag) {
			if(iterFrom>=this.shape[0]) {
				System.out.println("No match");
				return df;
			}
			else if(from.equals(this.data.get(0).get(iterFrom))||from.equals(this.data.get(1).get(iterFrom))||from.equals(this.data.get(3).get(iterFrom))){
				matchFromFlag = true;
			}
			iterFrom++;
		}
		
		while(!matchToFlag) {
			if(iterTo>=this.shape[0]) {
				System.out.println("No match");
				return df;
			}
			else if(to.equals(this.data.get(0).get(iterTo))||to.equals(this.data.get(1).get(iterTo))||to.equals(this.data.get(3).get(iterTo))){
				matchToFlag = true;
			}
			iterTo++;
		}
		
		if(matchToFlag == false || matchFromFlag == false) {
			System.out.println("Not valid options; one of the inputs was not found in the data.");
			return df;
		}
		else {
			for (int i = 0; i < this.shape[1]; i++) {
				ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(iterFrom-1,iterTo));
				df.data.add(tmp);
			}
			df.setColNames(this.getColNames());
			df.setColTypes(this.colDataTypes);
			df.setSize(df.data);
//			df.print();
			return df;
		}
	}
	
	public MyDataFrame loc(int from, int to) {
		MyDataFrame df = new MyDataFrame();
		try {
			for(int i = 0; i < this.shape[1]; i++) {
				ArrayList<?> tmp  =  new ArrayList<>(this.data.get(i).subList(from, to));
				df.data.add(tmp);
			}
		}catch(Exception e) {
				System.out.println("Input entered is either not an integer or it is larger than the number of rows in the DataFrame");
			}
		df.setColNames(this.getColNames());
		df.setColTypes(this.colDataTypes);
		df.setSize(df.data);
//		df.print();
		return df;
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------
//DATAFRAME METHODS PART 6: SORTING 
	
	public MyDataFrame sort(int index) {
		ArrayList<String> state = new ArrayList<String>();
		ArrayList<String> gender= new ArrayList<String>();
		ArrayList<Integer> year= new ArrayList<Integer>();
		ArrayList<String> name= new ArrayList<String>();
		ArrayList<Integer> count= new ArrayList<Integer>();
		ArrayList<ArrayList<?>> sortedData = new ArrayList<ArrayList<?>> ();
		
        List<Comparable> key = (List<Comparable>) this.data.get(index);
        ArrayIndexComparator comparator = new ArrayIndexComparator(key);

        for (int j = 0; j < this.shape[1]; j++) {
            List<Comparable> column = (List<Comparable>) this.data.get(j);
//            ArrayList<?> sortedColumn = new ArrayList<>();
            Integer[] indexes = comparator.createIndexArray();
            Arrays.sort(indexes, comparator);
            //add the items of each column to sortedColumn based on the sorted keys
            for (int i : indexes) {
                if(j==0) {
                	state.add((String) this.data.get(0).get(i));
                }
                if(j==1) {
                	gender.add((String) this.data.get(1).get(i));
                }
                if(j==2) {
                	year.add((int) this.data.get(2).get(i));
                }
                if(j==3) {
                	name.add((String) this.data.get(3).get(i));
                }
                if(j==4) {
                	count.add((int) this.data.get(4).get(i));
                }
            }
        }
        sortedData.add(state);
        sortedData.add(gender);
        sortedData.add(year);
        sortedData.add(name);
        sortedData.add(count);
		MyDataFrame df= new MyDataFrame(sortedData,this.getColNames(),this.colDataTypes);
		df.setSize(df.data);
//		df.print();
		return df;
	}
	
	public MyDataFrame sort(String label) {
		ArrayList<String> state = new ArrayList<String>();
		ArrayList<String> gender= new ArrayList<String>();
		ArrayList<Integer> year= new ArrayList<Integer>();
		ArrayList<String> name= new ArrayList<String>();
		ArrayList<Integer> count= new ArrayList<Integer>();
		ArrayList<ArrayList<?>> sortedData = new ArrayList<ArrayList<?>> ();
		
		int colNum = this.getColNames().get(label);
        List<Comparable> key = (List<Comparable>) this.data.get(colNum);
        ArrayIndexComparator comparator = new ArrayIndexComparator(key);

        for (int j = 0; j < this.shape[1]; j++) {
            List<Comparable> column = (List<Comparable>) this.data.get(j);
//            ArrayList<?> sortedColumn = new ArrayList<>();
            Integer[] indexes = comparator.createIndexArray();
            Arrays.sort(indexes, comparator);
            //add the items of each column to sortedColumn based on the sorted keys
            for (int i : indexes) {
                if(j==0) {
                	state.add((String) this.data.get(0).get(i));
                }
                if(j==1) {
                	gender.add((String) this.data.get(1).get(i));
                }
                if(j==2) {
                	year.add((int) this.data.get(2).get(i));
                }
                if(j==3) {
                	name.add((String) this.data.get(3).get(i));
                }
                if(j==4) {
                	count.add((int) this.data.get(4).get(i));
                }
            }
        }
        sortedData.add(state);
        sortedData.add(gender);
        sortedData.add(year);
        sortedData.add(name);
        sortedData.add(count);
		MyDataFrame df= new MyDataFrame(sortedData,this.getColNames(),this.colDataTypes);
		df.setSize(df.data);
//		df.print();
		return df;
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------------
//DATAFRAME METHODS PART 7: AGGREGATIONS	
	public Object getMin(int index) {
		String col = this.getColName(index);
		String dtype = this.dType(index);
		if(dtype.equalsIgnoreCase("Integer")) {
			ArrayList<Integer> selectedCol = (ArrayList<Integer>) this.data.get(index);
			System.out.println(col);
			System.out.println(Collections.min(selectedCol));
			return Collections.min(selectedCol);
		}
		else{
			ArrayList<String> selectedCol = (ArrayList<String>) this.data.get(index);
			System.out.println(col);
			System.out.println(Collections.min(selectedCol));
			return Collections.min(selectedCol);
		}
	}
	
	public Object getMin(String name) {
		int colNum = this.getColNames().get(name);
		String dtype = this.dType(colNum);
		if(dtype.equalsIgnoreCase("Integer")) {
			ArrayList<Integer> selectedCol = (ArrayList<Integer>) this.data.get(colNum);
			System.out.println(name);
			System.out.println(Collections.min(selectedCol));
			return Collections.min(selectedCol);
		}
		else{
			ArrayList<String> selectedCol = (ArrayList<String>) this.data.get(colNum);
			System.out.println(name);
			System.out.println(Collections.min(selectedCol));
			return Collections.min(selectedCol);
		}
	}
	
	public Object getMax(int index) {
		String col = this.getColName(index);
		String dtype = this.dType(index);
		if(dtype.equalsIgnoreCase("Integer")) {
			ArrayList<Integer> selectedCol = (ArrayList<Integer>) this.data.get(index);
			System.out.println(col);
			System.out.println(Collections.max(selectedCol));
			return Collections.max(selectedCol);
		}
		else{
			ArrayList<String> selectedCol = (ArrayList<String>) this.data.get(index);
			System.out.println(col);
			System.out.println(Collections.max(selectedCol));
			return Collections.max(selectedCol);
		}
	}
	
	public Object getMax(String name) {
		int colNum = this.getColNames().get(name);
		String dtype = this.dType(colNum);
		if(dtype.equalsIgnoreCase("Integer")) {
			ArrayList<Integer> selectedCol = (ArrayList<Integer>) this.data.get(colNum);
			System.out.println(name);
			System.out.println(Collections.max(selectedCol));
			return Collections.max(selectedCol);
		}
		else{
			ArrayList<String> selectedCol = (ArrayList<String>) this.data.get(colNum);
			System.out.println(name);
			System.out.println(Collections.max(selectedCol));
			return Collections.max(selectedCol);
		}
	}
	
//----------------------------------------------------------------------------------------------------------------------------------------------
//OTHER
	//method to print DataFrame object
    public void print() {
    	System.out.println(this.colNameMap.keySet().toString().substring(1,this.colNameMap.keySet().toString().length()-1));
        System.out.println("----------------------------------");
        for (int i = 0; i < this.shape[0]; i++) {
            String row = "";
            for (int j = 0; j < this.shape[1]; j++) {
                row += this.data.get(j).get(i) + ",";
            }
            System.out.println(row.substring(0, row.length() - 1));
        }
        System.out.println("----------------------------------");
    }
	
}
