package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class InfGraph {
	private G infGraph = new G();
	private String network = "C:/Users/userpc/workspace/SocExp/com-dblp.ungraph.txt"; // Brightkite_edges.txt
	private String probabilityPath = "C:/Users/userpc/workspace/SocExp/prop_dblp_8020";
	private HashSet seeds = new HashSet();
	private int blockSize = 10;
	private int mcIteration = 1000;
	
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
	
	public void argReader(String[] args) throws IOException
	{
		if(args.length >= 1)
			this.network = args[0];
		if(args.length >= 2)
			this.probabilityPath = args[1];
		if(args.length >= 4)
			this.blockSize = Integer.parseInt(args[3]);
		if(args.length >= 5)
			this.mcIteration = Integer.parseInt(args[4]);
	}
	public void dataReading() // read data and probabilities
	{
		this.readData(this.network); // com-dblp.ungraph.txt Brightkite_edges.txt
		try {
			this.infGraph.setProb(this.probabilityPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.infGraph.checkGraph());
	}
	
	public static void main(String[] args) throws IOException
	{
		new InfGraph().dataReading();
	}
}
