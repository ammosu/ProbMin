package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class G { //adjacency list
	private int id = -1;
	private Hashtable<Integer, HashSet<E>> graphList //out edges -> use to traversal
		= new Hashtable<Integer, HashSet<E>>();
	private Hashtable<Integer, HashSet<Integer>> inedges = new Hashtable<Integer, HashSet<Integer>>();
	private Hashtable<Integer, Boolean> activeTable = new Hashtable<Integer, Boolean>();
	private boolean status = false;
	
	protected void initialActive() // all nodes are inactive initially
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
		E e = new E(from, to);
		if(this.graphList.containsKey(from))
			if(this.graphList.get(from).contains(e))
				return true;
		return false;
	}
	
	public HashSet<E> getNbrs(int u) // neighbors of u
	{
		if(!this.graphList.containsKey(u))
		{
			return new HashSet<E>();
		}
		return this.graphList.get(u);
	}
	
	public void addEdge(int u, int v) // add edge (u, v)
	{
		E e = new E(u, v);
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
		if(this.inedges.containsKey(v)) // opposite edge
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
		else if(this.checkGraph())
		{
			this.status = true;
			return this.status;
		}
		return false;
	}
	
	public boolean checkGraph()
	{
		for(Entry<Integer, HashSet<E>> entry : this.graphList.entrySet())
		{
			for(E e : entry.getValue()) //check whether all edges are regular setting
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
	
	public void setUniProb() //set tri-value probabilities
	{
		for(HashSet<E> Set : this.graphList.values())
		{
			for(E e : Set)
				e.setProb(1/(double)this.inedges.get(e.idv()).size());
		}
	}
	
	public void setTrivProb() //set tri-value probabilities
	{
		Random r = new Random();
		double[] ds = new double[3];
		ds[0] = 0.1; ds[1] = 0.01; ds[2] = 0.001;
		for(HashSet<E> Set : this.graphList.values())
		{
			for(E e : Set)
				e.setProb(ds[Math.abs(r.nextInt()%3)]);
		}
	}
	
	public boolean testTrivalue()
	{
		int v01 = 0, v001 = 0, v0001 = 0;
		for(HashSet<E> Set : this.graphList.values())
			for(E edge : Set)
			{
				if(edge.getProb() == 0.1)
					v01++;
				else if(edge.getProb() == 0.01)
					v001++;
				else if(edge.getProb() == 0.001)
					v0001++;
				else
					return false;
			}
		System.out.println("# 0.1: "+v01);
		System.out.println("# 0.01: "+v001);
		System.out.println("# 0.001: "+v0001);
		return true;
	}
	
	public boolean testUnivalue()
	{
		int n1 = 0, n2 = 0, n3 = 0;
		for(HashSet<E> Set : this.graphList.values())
			for(E edge : Set)
			{
				if(edge.getProb() < 0.25)
					n1++;
				else if(edge.getProb() < 0.5)
					n2++;
				else if(edge.getProb() <= 1)
					n3++;
				else
					return false;
			}
		System.out.println("# p < 0.25: "+n1);
		System.out.println("# 0.25 <= p < 0.5: "+n2);
		System.out.println("# 0.5 <= p < 1: "+n3);
		return true;
	}
	public Set<Integer> getKey()
	{
		return this.graphList.keySet();
	}
	
	public E getEdge(int u, int v)
	{
		E e = new E(u,v);
		for(E edge : this.graphList.get(u))
		{
			if(edge.equals(e))
				return e;
		}
		e.setProb(0.0);
		return e;
	}
	
	public Set<E> getEdgesOf(int nodeV)
	{
		Set<E> edges = new HashSet<E>();
		if(this.graphList.containsKey(nodeV))
			edges.addAll(this.graphList.get(nodeV));
		if(this.inedges.containsKey(nodeV))
			for(int u : this.inedges.get(nodeV))
				edges.addAll(this.graphList.get(u));
		return edges;
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
		E e1 = new E(0,1);
		E e2 = new E(1,0);
		E e3 = new E(0,1);
		E e4 = new E(1,2);
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
		this.addEdge(0, 1);
		this.addEdge(2, 3);
		this.addEdge(1, 3);
		this.addEdge(2, 3);
		this.addEdge(3, 1);
		if(this.inedges.get(1).size() == 2 && this.inedges.get(3).size() == 2)
			
		if(this.isEdgeExist(3, 1) && this.isEdgeExist(1, 3) && this.isEdgeExist(0, 1) && this.isEdgeExist(2, 3) && !this.isEdgeExist(3, 2))
			return true;
		return false;
	}
	public boolean testAll()
	{
		if(this.testSet() && this.testGraph())
			return true;
		return false;
	}
	public void testProb()
	{
		this.addEdge(0, 1);
		this.addEdge(2, 3);
		this.addEdge(1, 3);
		this.addEdge(2, 3);
		this.addEdge(3, 1);
		for(Entry<Integer, HashSet<E>> e : this.graphList.entrySet())
		{
			for(E edge : e.getValue())
				if(this.getEdgesOf(3).contains(edge))
					edge.setProb(0.1);
				else
					edge.setProb(0.2);
		}
		/*
		for(Entry<Integer, HashSet<E>> e : this.graphList.entrySet())
		{
			for(E edge : e.getValue())
				System.out.println(edge.getProb());
		}
		*/
	}
	public static void main(String[] args) throws IOException
	{
		G g = new G();
		g.testProb();
		//System.out.println(new G().testAll());
		g.setTrivProb();
		g.testTrivalue();
		
	}
}
