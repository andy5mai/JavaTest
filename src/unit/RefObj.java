package unit;

public class RefObj
{
	private Object obj;
	public RefObj(Object obj)
	{
		this.set(obj);
	}
	
	public void set(Object obj)
	{
		this.obj = obj;
	}
	
	public Object get()
	{
		return this.obj;
	}
}
