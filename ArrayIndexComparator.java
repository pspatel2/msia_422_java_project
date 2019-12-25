package MSiA_422_Project_2;

import java.util.Comparator;
import java.util.List;

public class ArrayIndexComparator implements Comparator<Integer>
{
    private final List<Comparable> col;

    public ArrayIndexComparator(List<Comparable> col)
    {
        this.col = col;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[col.size()];
        for (int i = 0; i < col.size(); i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return col.get(index1).compareTo(col.get(index2));
    }
}