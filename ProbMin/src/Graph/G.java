package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;

public class G { //adjacency list
	private int id;
	private Hashtable<Integer, HashSet<E>> graphList //out edges -> use to traversal
		= new Hashtable<Integer, HashSet<E>>();
	private Hashtable<Integer, HashSet<Integer>> inedges = new Hashtable<Integer, HashSet<Integer>>();
	private Hashtable<Integer, Boolean> activeTable = new Hashtable<Integer, Boolean>();
	private boolean status = false;
	
	protected void initialActive()
	{
		for(int key : this.graphList.keySet())
		{
			this.activeTable.put(key, false);
		}
	}
	
	public String toString()
	{
		String ID = String.valueOf(this.id);
		return ID;
	}
	
	public boolean isEdgeExist(int from, int to)
	{
		E e = new E(to);
		if(this.graphList.containsKey(from))
			if(this.graphList.get(from).contains(e))
				return true;
		return false;
	}
	
	public void addEdge(int u, int v)
	{
		E e = new E(v);
		HashSet<E> edge = new HashSet<E>();
		edge.add(e);
		if(!this.graphList.containsKey(u)) //u not exist => add u, v 
		{
			this.graphList.put(u, edge);
		}
		else
		{
			this.graphList.get(u).add(e);
		}
		if(this.inedges.containsKey(v)) // revise edge
			//if(this.inedges.get(v).contains(u))
			this.inedges.get(v).add(u);
		else
		{
			HashSet<Integer> set = new HashSet<Integer>();
			set.add(u);
			this.inedges.put(v, set);
		}
	}
	
	public boolean getstatus()
	{
		if(this.status)
			return this.status;
		else
		{
			if(this.checkGraph())
			{
				this.status = true;
				return this.status;
			}
		}
		return false;
	}
	public boolean checkGraph()
	{
		for(Entry<Integer, HashSet<E>> entry : this.graphList.entrySet())
		{
			for(E e : entry.getValue())
			{
				if(!e.getStatus())
					return false;
			}
		}
		return true;
	}
	public void setProb(String graphProbPath) throws IOException
	{
		FileReader fr;
		try {
			fr = new FileReader(graphProbPath);
			BufferedReader br = new BufferedReader(fr);
			
			for(HashSet<E> Set : this.graphList.values())
			{
				for(E e : Set)
					if(br.ready())
					{
						String s = br.readLine();
						e.setProb(Double.parseDouble(s));
					}
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void show()
	{
		for(int i : this.graphList.keySet())
		{
			for(E j : this.graphList.get(i))
				System.out.print("P(e("+i+", "+j+"))="+j.getProb()+", ");
				//System.out.print("("+i+", "+j+"), ");
			System.out.println();
		}
	}
	
	public boolean testSet()
	{
		E e1 = new E(1);
		E e2 = new E(0);
		E e3 = new E(1);
		E e4 = new E(2);
		HashSet<E> set = new HashSet<E>();
		set.add(e1);
		set.add(e2);
		set.add(e3);
		set.add(e4);
		/*for(E e : set)
			System.out.println(e);*/
		if(set.size() == 3)
			return true;
		return false;
	}
	public boolean testGraph()
	{
		G g = new G();
		g.addEdge(0, 1);
		g.addEdge(2, 3);
		g.addEdge(1, 3);
		g.addEdge(2, 3);
		g.addEdge(3, 1);
		if(g.inedges.get(1).size() == 2 && g.inedges.get(3).size() == 2)
			
		if(g.isEdgeExist(3, 1) && g.isEdgeExist(1, 3) && g.isEdgeExist(0, 1) && g.isEdgeExist(2, 3) && !g.isEdgeExist(3, 2))
			return true;
		return false;
	}
	public boolean testAll()
	{
		if(this.testSet() && this.testGraph())
			return true;
		return false;
	}
	public static void main(String[] args) throws IOException
	{
		System.out.println(new G().testAll());
		
	}
}
