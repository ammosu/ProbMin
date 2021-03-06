package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

public class InfGraph {
	private G infGraph = new G();
	private String networkPath = "C:/Users/userpc/workspace/SocExp/Brightkite_edges.txt"; // Brightkite_edges.txt com-dblp.ungraph-small.txt
	private String probabilityPath = "C:/Users/userpc/workspace/SocExp/prop_dblp_8020";
	private HashSet<Integer> seeds = new HashSet<Integer>();
	private ArrayList<Integer> blockNodes = new ArrayList<Integer>();
	private int blockSize = 20;
	private int mcIteration = 10; // default
	private int eta = 16;
	private double error = 0.05;
	
	public G getInfG()
	{
		return this.infGraph;
	}
	public void setEta(int Eta)
	{
		this.eta = Eta;
	}
	public void info()
	{
		System.out.println("Network Data: "+this.networkPath.substring(this.networkPath.lastIndexOf("/")+1)
				+ "\nNode size: " + this.infGraph.getKey().size()
				+ "\nEta: " + this.eta
				+ "\nBlocking size: " + this.blockSize);
		System.out.println("--------------------------");
	}
	public void readData(String network)
	{
		FileReader FileStream;
		try {
			FileStream = new FileReader(network);
			BufferedReader BufferedStream = new BufferedReader(FileStream);
			try {
				do{
					while(BufferedStream.ready())
					{
						String readline = BufferedStream.readLine();
						String[] readlines = readline.split("\t"); // format: ID\tNbrID
	
						int u = Integer.parseInt(readlines[0]);
						int v = Integer.parseInt(readlines[1]);
						
						this.infGraph.addEdge(u,v);
						
						/*if(Math.random()>0.5) //
							this.infGraph.addEdge(v, u);*/
					}
				}
				while(BufferedStream.ready());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void argReader(String[] args) throws IOException /** network path, probability path, block size, Monte Carlo iteration **/
	{
		if(args.length >= 1)
			this.networkPath = args[0];
		if(args.length >= 2)
			this.probabilityPath = args[1];
		if(args.length >= 4)
			this.blockSize = Integer.parseInt(args[3]);
		if(args.length >= 5)
			this.mcIteration = Integer.parseInt(args[4]);
	}
	public void dataReading() // read data and probabilities
	{
		this.readData(this.networkPath); // com-dblp.ungraph.txt Brightkite_edges.txt
		this.infGraph.setTrivProb();
		this.infGraph.testTrivalue();
			
	}
	public void dataReading(int i) // read data and probabilities
	{
		if(i==0)
			this.dataReading(); // tri-value
		else if(i==1) // 8020
		{
			this.readData(this.networkPath);
			this.seedReading();
			try {
				this.infGraph.setProb(this.probabilityPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(this.infGraph.checkGraph())
				System.out.println("Influence Cascade Network Build\n--------------------------------------");
		}
		else if(i==2) // uniform
		{
			this.readData(this.networkPath); // com-dblp.ungraph.txt Brightkite_edges.txt
			this.infGraph.setUniProb();
			this.infGraph.testUnivalue();
		}
	}

	public void seedReading()
	{
		FileReader FileStream;
		try {
			FileStream = new FileReader("randomSeed");
			BufferedReader BufferedStream = new BufferedReader(FileStream);
			try {
				do{
					while(BufferedStream.ready())
					{
						String readline = BufferedStream.readLine();
						this.seeds.add(Integer.parseInt(readline));
					}
				}
				while(BufferedStream.ready());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public boolean setSeed(HashSet<Integer> seed)
	{
		if(this.seeds.size() != 0)
		{
			//System.out.println("Seed is set already");
			this.seeds.clear();
			this.seeds = seed;
			return false;
		}
		else
			this.seeds = seed;
		//System.out.println("Seed: "+seed);
		return true;
	}
	
	public boolean setSeed(String s)
	{
		HashSet<Integer> seed = new HashSet<Integer>();
		for(String ss : s.split(", "))
			seed.add(Integer.parseInt(ss));
		if(this.seeds.size() != 0)
		{
			System.out.println("Seed is set already");
			return false;
		}
		else
			this.seeds = seed;
		System.out.println("Seed: "+seed);
		return true;
	}
	
	public HashSet<Integer> randomSeedGenerate(int size)
	{
		while(this.seeds.size()<size)
		{
			int ran = new Random().nextInt(this.infGraph.getKey().size());
			int i = 0;
			for(int key : this.infGraph.getKey())
			{
				if(i==ran)
				{
					if(!this.seeds.contains(key)) //not target
						this.seeds.add(key);
					break;
				}
				i++;
			}
		}
		System.out.println("Seed size: " + size);
		return this.seeds;
	}
	
	public int propagate()
	{
		HashSet<Integer> traversal = new HashSet<Integer>(); // active nodes
		HashSet<Integer> s0 = new HashSet<Integer>();
		HashSet<Integer> s1 = new HashSet<Integer>();
		s1.addAll(this.seeds);
		
		while(s1.size() != 0) // new activate node size
		{
			traversal.addAll(s1);
			s0.clear();
			s0.addAll(s1);
			s1.clear();
			for(int s0Element : s0)
			{
				for(E nbr : this.infGraph.getNbrs(s0Element))
				{
					if(!traversal.contains(nbr.idv()) && nbr.getProb()>Math.random())
						s1.add(nbr.idv());
				}
			}
		}
		return traversal.size();
	}
	
	public boolean propagateFastCheck()
	{
		HashSet<Integer> traversal = new HashSet<Integer>(); // active nodes
		HashSet<Integer> s0 = new HashSet<Integer>();
		HashSet<Integer> s1 = new HashSet<Integer>();
		s1.addAll(this.seeds);
		traversal.addAll(s1);
		
		while(s1.size() != 0) // new activate node size
		{
			if(traversal.size()>=this.eta) // check
				return false;
			s0.clear();
			s0.addAll(s1);
			s1.clear();
			for(int s0Element : s0)
			{
				for(E nbr : this.infGraph.getNbrs(s0Element))
				{
					if(!traversal.contains(nbr.idv()) && nbr.getProb()>Math.random())
					{
						s1.add(nbr.idv());
						traversal.add(nbr.idv());
					}
				}
			}
		}
		if((double)traversal.size()*(1+this.error)<(double)this.eta)
			return true;
		else
			return false;
	}
	
	public double sigma(int times)
	{
		double num = 0.0, p;
		for(int i = 0; i< times;i++)
		{
			p = (double)this.propagate();
			num+=p;
			//System.out.println(p);
		}
		return num/=(double)times;
	}
	
	public double probOfLessThanEta()
	{
		double overTimes = 0.0;
		for(int i = 0; i < this.mcIteration; i++)
		{
			if(this.propagateFastCheck())
				overTimes += 1.0;
		}
		return (double)overTimes/(double)this.mcIteration;
	}
	
	public void probTime(int times)
	{
		double startTime, endTime, totalTime;

		startTime = System.currentTimeMillis();
		System.out.println(this.propagate());
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000+" sec");
		for(int i = 0; i < times; i++)
		{
			this.propagate();
				
		}
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000+" sec");
	}
	
	public void pSettingTest()
	{
		for(int u : this.infGraph.getKey())
		{
			if(this.seeds.contains(u))
				continue;
			for(E edge : this.infGraph.getNbrs(u))
			{
				
				double p = edge.getProb();
				
				edge.setProb(0.0);
				double change = this.probOfLessThanEta();
				System.out.println("change: "+change);
				edge.setProb(p);
				double beforeChange = this.probOfLessThanEta();
				System.out.println("after: "+ beforeChange);
				if(beforeChange > change) // error
					System.out.println(edge);
			}
		}
	}
	
	public void testRemove()
	{
		int u = 750, v = 3592;
		int count = 0;
		double p = this.probOfLessThanEta(), p2 = 0.0;
		
		for(int i = 0; i < 1000; i++)
		{
			for(E e : this.infGraph.getNbrs(u))
			{
				if(e.idv() == v)
				{
					p2 = e.getProb();
					e.setProb(0.0);
					if(p>this.probOfLessThanEta())
					{
						count ++;
						System.out.print(".");
					}
					e.setProb(p2);
				}
			}
		}
		System.out.println(count);
	}
	
	public ArrayList<E> greedy()
	{
		double maxProb = -1.0;
		int maxEdgeU = -1;
		int maxEdgeV = -1;
		ArrayList<E> blockEdges = new ArrayList<E>();
		System.out.println("Original Probability: "+ this.probOfLessThanEta());
		
		while(blockEdges.size() < this.blockSize)
		{
			for(int u : this.infGraph.getKey())
			{
				if(this.seeds.contains(u))
					continue;
				for(E edge : this.infGraph.getNbrs(u))
				{
					if(blockEdges.contains(edge))
						continue;
					
					double p = edge.getProb();
					edge.setProb(0.0);
					double probInf = this.probOfLessThanEta();
					if(probInf > maxProb)
					{
						System.out.println(probInf);
						maxEdgeU = u;
						maxEdgeV = edge.idv();
						maxProb = probInf;
					}
					edge.setProb(p);
				}
			}
			
			E e = new E(maxEdgeU, maxEdgeV);
			e.setProb(0.0);
			this.infGraph.getNbrs(maxEdgeU).add(e);
			blockEdges.add(e);
			System.out.println(e);
			maxProb = -1.0;
			maxEdgeU = -1;
			maxEdgeV = -1;
		}
		return blockEdges;
	}
	
	public ArrayList<Integer> greedyNode()
	{
		int count = 0;
		double maxProb = -1.0;
		int maxNodeID = -1;
		double lastMax = -1.0;
		double startTime, endTime, totalTime;

		System.out.println("Original Probability: "+ this.probOfLessThanEta());
		startTime = System.currentTimeMillis();
		
		while(blockNodes.size() < this.blockSize)// k block nodes
		{
			for(int u : this.infGraph.getKey())// for each node
			{
				if(this.seeds.contains(u))
					continue;
				if(count++%100==0)
					System.out.print(".");
				for(E edge : this.infGraph.getEdgesOf(u)) // in-edge and out-edge
				{
					edge.block();
				}
				double prob = this.probOfLessThanEta();
				if( prob > maxProb)
				{
					maxProb = prob;
					maxNodeID = u;
				}
				for(E edge : this.infGraph.getEdgesOf(u)) // unblock all
				{
					edge.unblock();;
				}
			}
			if(maxProb == 0.0||maxProb<=lastMax||this.blockNodes.contains(maxNodeID))
			{
				maxNodeID = this.nbrFirst();
			}
			for(E edge : this.infGraph.getEdgesOf(maxNodeID)) // block maxNode
			{
				edge.block();
			}
			System.out.println("\nNode "+blockNodes.size()+": "+maxNodeID +"\nMaximum probability: "+maxProb);
			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("Currently Execution Time: " + totalTime/1000+" sec");
			blockNodes.add(maxNodeID);
			maxNodeID = -1;
			maxProb = -1.0;
		}

		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000+" sec");
		System.out.println(blockNodes);
		
		return blockNodes;
	}
	public void testBlock()
	{
		int count = 0;
		for(int u : this.infGraph.getKey())// for each node
		{
			count ++;
			for(E e: this.infGraph.getEdgesOf(u))
			{
				e.block();
			}
			this.probOfLessThanEta();
			for(E e: this.infGraph.getEdgesOf(u))
			{
				e.unblock();
			}
			if(count == 500)
				break;
		}
	}
	
	public void evalExp(ArrayList<Integer> B)
	{
		this.mcIteration = 100000;
		for(int u : B)
			for(E e:this.infGraph.getEdgesOf(u))
				e.unblock();
		for(int u : B)
		{
			for(E e:this.infGraph.getEdgesOf(u))
			{
				e.block();
			}
			System.out.println(this.probOfLessThanEta()+",");
		}
	}
	
	public int degreeFirst()
	{
		int maxDegree = 0;
		int maxID = -1;
		for(int id:this.infGraph.getKey())
		{
			if(this.blockNodes.contains(id))
				continue;
			int degree = this.infGraph.getNbrs(id).size();
			if(degree > maxDegree)
			{
				maxDegree = degree;
				maxID = id;
			}
		}
		return maxID;
	}
	
	public int nbrFirst()
	{
		HashMap<Integer, Double> idScore = new HashMap<Integer, Double>();
		for(int u : this.seeds)
		{
			for(E seedNbr : this.infGraph.getNbrs(u))
			{
				if(this.blockNodes.contains(seedNbr.idv())||this.seeds.contains(seedNbr.idv()))
					continue;
				if(idScore.containsKey(seedNbr.idv()))
				{
					idScore.put(seedNbr.idv(), idScore.get(seedNbr.idv())+seedNbr.getProb());
				}
				else
					idScore.put(seedNbr.idv(), seedNbr.getProb());
			}	
		}
		double max = -1.0;
		int maxID = -1;
		for(Entry<Integer, Double> e : idScore.entrySet())
			if(e.getValue() >= max)
			{
				maxID = e.getKey();
				max = e.getValue();
			}
		//System.out.print("\nNBRFIRST: "+maxID+"\tP:"+max);
		return maxID;
	}
	
	public static void main(String[] args) throws IOException
	{
		double startTime, endTime, totalTime;
		
		InfGraph ifg = new InfGraph();
		ifg.dataReading(2);//2: uni, 1: 8020, 0: tri 
		//System.out.println("Seed generate: "+ifg.randomSeedGenerate(20)+"\n--------------------------");
		ifg.setSeed("40481, 353, 41636, 9156, 50341, 12710, 7179, 51595, 7435, 44587, 7313, 17425, 33170, 8274, 52468, 30677, 8248, 44606, 36222, 37887");
		
		ifg.info();
		startTime = System.currentTimeMillis();
		
		//System.out.println("sigma: "+ ifg.sigma(100));
		//System.out.println("prob: "+ ifg.probOfLessThanEta());
		ifg.greedyNode();
		//ifg.propagateFastCheck();
		//ifg.testBlock();
		
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000+" sec");

	}
}
