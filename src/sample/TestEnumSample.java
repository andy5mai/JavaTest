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
	
	public enum TestIntEnum implements AttrClass
	{
		first(1)
		, second(2)
		, third(3);
		
		private int nValue;
		private TestIntEnum(int nValue)
		{
			this.nValue = nValue;
		}
		@Override
		public void setValue(Object objValue) {
			// TODO Auto-generated method stub
			this.nValue = (int) objValue;
		}
		@Override
		public Object getValue() {
			// TODO Auto-generated method stub
			return this.nValue;
		}
		
		
	}
	
	public TestEnumSample()
	{
		
	}
	
	@Override
	public void Exec()
	{
		System.out.println(TestIntEnum.first.name() + " is " + TestIntEnum.first.getValue());
	}
}
