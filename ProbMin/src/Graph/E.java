package Graph;

public class E {
	int u;
	int v;
	private double prob = -1.0; // for ic model
	private double thres = -1.0; // for lt model
	private boolean status = false; // return true if parameter(prob/thres) set
	private boolean blocked = false;
	private double tempProb = 0.0;
	
	public E(int u, int v)
	{
		this.u = u;
		this.v = v;
	}
	
	public int idv()
	{
		return this.v;
	}
	
	public int idu()
	{
		return this.u;
	}
	
	public String toString()
	{
		return String.valueOf("("+this.u +", "+this.v+")");
	}
	@Override
	public boolean equals(Object d){ 
	    if (!(d instanceof E)) {
	        return false;
	    }
	    E edge = (E) d;
	    if(this.v == edge.idv() && this.u == edge.idu())
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
	
	public void block()
	{
		this.tempProb = this.prob;
		this.prob = 0.0;
		this.blocked = true;
	}
	
	public boolean isBlocked()
	{
		return this.blocked;
	}
	
	public void unblock()
	{
		this.prob = this.tempProb;
		this.tempProb = 0.0;
		this.blocked = false;
	}
	
	public boolean activing(double num)
	{
		if(num >= 0.0 && num < this.prob)
		{
			return true;
		}
		return false;
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
