package sample;

public abstract class ALineBuilder<T>
{
	protected String strText;
	protected String strSymbol;
	protected int nExpectedSize;
	
	public ALineBuilder(String strText, String strSymbol, int nExpectedSize)
	{
		this.strText = strText;
		this.strSymbol = strSymbol;
		this.nExpectedSize = nExpectedSize;
	}
	
	public abstract T build(String strText);
}
