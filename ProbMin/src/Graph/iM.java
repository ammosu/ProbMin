package Graph;

import java.util.ArrayList;
import java.util.HashSet;

public class iM {
	
	private listNodeValue nvlist = new listNodeValue();
	private int budget = 0;
	private InfGraph ifg = new InfGraph();
	private int MCiter = 1000;
	
	public iM(int k)
	{
		this.budget = k;
	}
	
	public void initialSetting()
	{
		ifg.dataReading(2);
		ifg.info();
	}
	
	public ArrayList<Integer> seedSelection() 
	{
		ArrayList<Integer> selectedSeed = new ArrayList<Integer>();
		for(int v : ifg.getInfG().getKey())
		{
			HashSet<Integer> seed = new HashSet<Integer>();
			seed.add(v);
			ifg.setSeed(seed);
			nvlist.add(v, ifg.sigma(this.MCiter));
		}
		nvlist.sort();
		//nvlist.printList();
		selectedSeed.add(this.nvlist.remove(0).getID());
		
		while(this.budget > selectedSeed.size())
		{
			HashSet<Integer> set = new HashSet<Integer>();
			set.addAll(selectedSeed);
			nodeValue nv = nvlist.remove(0);
			set.add(nv.getID());
			ifg.setSeed(set);
			double value = ifg.sigma(this.MCiter);
			if(nvlist.getTop().getValue()<=value)
			{
				selectedSeed.add(nv.getID());
			}
			else
			{
				nvlist.add(nv.getID(), value);
				nvlist.sort();
				System.out.println();
			}
		}
		
		return selectedSeed;
	}
	public static void main(String[] args) {
		iM im = new iM(50);
		im.initialSetting();
		double startTime, endTime, totalTime;
		startTime = System.currentTimeMillis();
		
		System.out.println("\nSeed: "+im.seedSelection());
		
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000+" sec");
	}

}
