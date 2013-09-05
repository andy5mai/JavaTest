package sample;

public class TestEnumSample implements IExec
{
	public interface AttrClass
	{
		public void setValue(Object objValue);
		public Object getValue();
	}

	public enum TestEnum implements AttrClass
	{
		first(null)
		, second(null)
		, third(null);
		
		private Object objValue;
		 
		private TestEnum(Object objValue)
		{
			this.objValue = objValue;
		}
		
		@Override
		public void setValue(Object objValue)
		{
			this.objValue = objValue;
		}
		
		@Override
		public Object getValue()
		{
			return this.objValue;
		}
	}
	
	public TestEnumSample()
	{
		
	}
	
	@Override
	public void Exec()
	{
		
	}
}
