package Graph;

public class E {

	int v ;
	private double prob = -1.0; // for ic model
	private double thres = -1.0; // for lt model
	private boolean status = false;
	
	
	public E(int v)
	{
		this.v = v;
	}
	
	public int v()
	{
		return this.v;
	}
	
	public String toString()
	{
		return String.valueOf(this.v);
	}
	@Override
	public boolean equals(Object d){ 
	    if (!(d instanceof E)) {
	        return false;
	    }
	    E edge = (E) d;
	    if(this.v == edge.v())
	    	return true;
	    return false;
	}
	@Override
	public int hashCode() {
	    return this.v;
	}
	public void setProb(double p)
	{
		if(p<=1.0 && p>=0.0)
		{
			this.prob = p;
			this.status = true;
		}
		else
			System.out.println("Probability setting error");
	}
	public double getProb()
	{
		return this.prob;
	}
	public boolean getStatus()
	{
		return this.status;
	}
	public void setThres(double threshold)
	{
		if(threshold<=1.0 && threshold >=0.0)
		{
			this.thres = threshold;
			this.status = true;
		}
		else
			System.out.println("Threshold setting error");
	}
	public double getThres()
	{
		return this.thres;
	}
	
	public double infto()
	{
		if(this.getStatus())
			return this.thres;
		else
			return 0.0;
	}
	/**/
}
