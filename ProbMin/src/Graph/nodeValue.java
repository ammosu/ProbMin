package Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class nodeValue {
	private int ID;
	private double value;
	
	public nodeValue(int id, double v)
	{
		this.ID = id;
		this.value = v;
	}
	
	public String toString()
	{
		return this.ID + "\t" + this.value;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public void setValue(double v)
	{
		this.value = v;
	}
	
	public void printStringValue()
	{
		System.out.println(this.ID + ": " + this.value);
	}
	
	public static void main(String[] args) {
		ArrayList<nodeValue> list = new ArrayList<nodeValue>();
		list.add(new nodeValue(1,0.2));
		list.add(new nodeValue(2,0.1));
		list.add(new nodeValue(500,0.5));
		list.add(new nodeValue(7,2.1));
		list.add(new nodeValue(400,1.0));
		
		//sort by value
		Collections.sort(list,
		        new Comparator<nodeValue>() {
		            public int compare(nodeValue o1, nodeValue o2) {
		                if(o2.getValue()-o1.getValue()>0)
		                	return 1;
		                else
		                	return -1;
		            }
		        });
		
		for(Object o:list)
		{
			System.out.println(o);
		}
		
	}

}
