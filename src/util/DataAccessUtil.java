package util;

import java.lang.reflect.Method;

public class DataAccessUtil
{
	public enum SolveSteps
	{
		//RedisClient
		getValue(null),
		unCompressValue(null),
		getBytesString(null),
		
		//AObject
		getJsonObjFromStr(null),
		getJsonObjAsStr(null);
		
		private Method method;
		private SolveSteps(Method method)
		{
			this.method = method;
		}
		
		public void setMethod(Method method)
		{
			this.method = method;
		}
		
		public Method getMethod()
		{
			return this.method;
		}
	}
	
	public static Object doNothing(Object obj)
	{
		return obj;
	}
}
