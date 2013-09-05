package sample;

public class ImmutableSample implements IExec
{
	public ImmutableSample()
	{
		
	}
	
	@Override
	public void Exec()
	{
		Long l = Long.parseLong("123456789");
		this.testLong(l);
		System.out.println("[ Long ] after deduct..." + l);
		
		String str = "456";
		this.testString(str);
		System.out.println("[ String ] after deduct..." + str);

	}
	
	public void testLong(Long l)
	{
		l = l - 1;
		System.out.println("[ Long ] in the deduct function..." + l);
	}
	
	public void testString(String str)
	{
		str = str + "123";
		System.out.println("[ String ] in the deduct function..." + str);
	}
}
