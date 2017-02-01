
public class SBEvalPair implements Comparable<SBEvalPair> 
{

	private int binID;
	private int sbeValue;

	public SBEvalPair(int binID, int sbeValue)
	{
		this.binID = binID;
		this.sbeValue = sbeValue;
	}

	public int getBinID() 
	{
		return binID;
	}

	public int getSBEValue() 
	{
		return sbeValue;
	}

	public void setBinID(int binID)
	{
		this.binID = binID;	
	}
	
	public void setSBEValue(int sbeValue)
	{
		this.sbeValue = sbeValue;
	}
	
	@Override
	public int compareTo(SBEvalPair o) 
	{
		if (this.sbeValue > o.sbeValue)
			return 1;
		else if (this.sbeValue < o.sbeValue)
			return -1;
		return 0;
	}
}
