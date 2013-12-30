package sample;

public class TSample<T1, T2, T3>
{
	private T1 value1;
	private T2 value2;
	private T3 value3;
	
	public TSample(T1 t1, T2 t2, T3 t3)
	{
		this.value1 = t1;
		this.value2 = t2;
		this.value3 = t3;
	}
	
	public T1 getT1()
	{
		return this.value1;
	}
	
	public T2 getT2()
	{
		return this.value2;
	}
	
	public T3 getT3()
	{
		return this.value3;
	}
}
