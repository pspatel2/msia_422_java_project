package MSiA_422_Project_2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import MSiA_422_Project_2.MyDataFrame;
import MSiA_422_Project_2.MyPandas;

public class MyTest {
	public static void main(String[] args) throws IOException {
		//Save all console output to a text file.
		PrintStream printStream = new PrintStream(new FileOutputStream("TestDocument.txt"));
		System.setOut(printStream);
//---------------------------------------------------------------------------------------------------------------------------------------------
//Announcement
		System.out.println("To avoid the text file being to long, all output with multiple lines only print head(5)");
		System.out.println();
//---------------------------------------------------------------------------------------------------------------------------------------------
//readCSV()
		System.out.println("Testing MyPandas.readCSV(String path) with valid path.");
		MyDataFrame df = MyPandas.readCSV("IL_sub.txt");
		System.out.println();

		System.out.println("Testing MyPandas.readCSV(String path) with invalid path.");
		MyPandas.readCSV("DO_NOT_EXIST_FILE_sub.txt");
		System.out.println();
//---------------------------------------------------------------------------------------------------------------------------------------------
//concat()
		System.out.println("Testing MyPandas.concat(MyDataFrame df1, MyDataFrame df2) with valid path.");
		MyDataFrame df2 = MyPandas.readCSV("MA_sub.txt");
		MyDataFrame df3 = MyPandas.concat(df,df2);
		System.out.println();

		System.out.println("Testing MyPandas.concat(MyDataFrame df1, MyDataFrame df2) with invalid path.");
		MyDataFrame df4 = df2.head(5).slice(new String[]{"gender","year"});
		MyDataFrame df5 = MyPandas.concat(df,df4);
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//writeCSV()
		System.out.println("Testing MyPandas.writeCSV(MyDataFrame data, String path) with valid path.");
		MyPandas.writeCSV(df, "test_out.csv");
		System.out.println();

		System.out.println("Testing MyPandas.writeCSV(MyDataFrame data, String path) with invalid path.");
		MyPandas.readCSV("INVALID.pojs");
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//head() and tail()
		System.out.println("Testing head(int n).");
		df.head(5).print();
		System.out.println();

		System.out.println("Testing tail(int n).");
		df.tail(5).print();
		System.out.println();
//---------------------------------------------------------------------------------------------------------------------------------------------
//dType()
		System.out.println("Testing dType(String name).");
		System.out.println("gender: "+df.dType("gender"));
		System.out.println("count: "+df.dType("count"));
		System.out.println("Count: "+df.dType("Count"));
		System.out.println();

		System.out.println("Testing dType(int index).");
		System.out.println("Column2(gender): "+df.dType(1));
		System.out.println("Column5(count): "+df.dType(4));
		System.out.println("Column20(count): "+df.dType(20));
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//slice()
		System.out.println("Testing slice(int index).");
		df.head(5).slice(1).print();
		System.out.println();


		System.out.println("Testing slice(String name).");
		df.head(5).slice("gender").print();
		System.out.println();

		System.out.println("Testing slice(int[] indexArr).");
		df.head(5).slice(new int[]{1,2}).print();
		System.out.println();

		System.out.println("Testing slice(String[] nameArr).");
		df.head(5).slice(new String[]{"gender","year"}).print();
		System.out.println();
//---------------------------------------------------------------------------------------------------------------------------------------------
//filter()
		System.out.println("Testing filter(String col, String op, Object o).");
		df.filter("count", ">",10).head(5).print();
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//indexing
		System.out.println("Testing loc(int index).");
		df.loc(50).head(5).print();
		System.out.println();

		System.out.println("Testing loc(String label).");
		df.loc("Sultan").head(5).print();
		System.out.println();

		System.out.println("Testing loc(int from, int to).");
		df.loc(50,100).head(5).print();
		System.out.println();

		System.out.println("Testing loc(String from, String to).");
		df.loc("Daniel","Lucas").head(5).print();
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//sorting

		System.out.println("Testing sort(int index).");
		df.sort(4).head(5).print();
		System.out.println();

		System.out.println("Testing sort(String name).");
		df.sort("count").head(5).print();
		System.out.println();

//---------------------------------------------------------------------------------------------------------------------------------------------
//Aggregation

		System.out.println("Testing getMin(int index).");
		df.getMin(4);
		System.out.println();

		System.out.println("Testing getMin(String label).");
		df.getMin("count");
		System.out.println();

		System.out.println("Testing getMax(int index).");
		df.getMax(4);
		System.out.println();

		System.out.println("Testing getMax(String label).");
		df.getMax("count");
		System.out.println();





	}
}
