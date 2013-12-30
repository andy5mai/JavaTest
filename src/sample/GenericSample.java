package sample;

public class GenericSample implements IExec
{
	public GenericSample()
	{
		
	}
	
	@Override
	public void Exec()
	{
		TSample<Integer, String, Integer> tsample = new TSample<Integer, String, Integer>(1, "2", 3);
		System.out.println(tsample.getT1());
		System.out.println(tsample.getT2());
		System.out.println(tsample.getT3());
	}
}
