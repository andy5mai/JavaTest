package sample;

public class DynamicParamsSample implements IExec
{
	public DynamicParamsSample()
	{
		
	}
	
	@Override
	public void Exec()
	{
		String str = "123";
		Integer n = 213;
		Object obj = null;
		Long l = (long)456;
		
		this.test(str, n, obj, l);
	}

	private void test(Object... objs)
	{
		for(Object obj : objs)
		{
			System.out.println(obj);
		}
		
		objs[2] = 777;
		
		this.test2(objs);
	}
	
	private void test2(Object... objs)
	{
		System.out.println("=================");
		
		for(Object obj : objs)
		{
			System.out.println(obj);
		}
	}
}
