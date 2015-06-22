package Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class listNodeValue {
	ArrayList<nodeValue> list = new ArrayList<nodeValue>();
	
	public void add(int id, double v)
	{
		list.add(new nodeValue(id, v));
	}
	public nodeValue getTop()
	{
		if(this.list.size()>0)
			return this.list.get(0);
		else
			return new nodeValue(-1, -1.0);
	}
	public nodeValue remove(int index)
	{
		return this.list.remove(index);
	}
	public void setValueof(int index, double value)
	{
		if(this.list.size()> index && index>=0)
			this.list.get(index).setValue(value);
		else
			System.out.println("List index error");
	}
	public void sort()
	{
		Collections.sort(this.list,
		        new Comparator<nodeValue>() {
		            public int compare(nodeValue o1, nodeValue o2) {
		                if(o2.getValue()-o1.getValue()>0)
		                	return 1;
		                else
		                	return -1;
		            }
		        });
	}
	public void printList()
	{
		for(Object o:this.list)
		{
			System.out.println(o);
		}
	}
	
	public static void main(String[] args) {
		listNodeValue list = new listNodeValue();
		list.add(1,0.2);
		list.add(2,0.1);
		list.add(500,0.5);
		list.add(7,2.1);
		list.add(400,1.0);
		list.sort();
		list.printList();
		list.remove(0);
		System.out.println(list.getTop());
		
	}
}
