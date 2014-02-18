package com.moregeek.persistence.redis;

import util.DataAccessUtil;

public class RedisField
{
	private String strName;
	private RedisKey redisKey;
	private RedisData.AccessTypes accessType;
	private String strSolveSteps = "";
	private DataAccessUtil.SolveSteps[] solveSteps;
	private RedisField valueRef = null;
	private String[] strsJsonLevels = null;
	
//	private Object objValue;
	
	private final String strSplitSymbol = ";";
	
	public RedisField(RedisKey redisKey)
	{
		this("DefaultField", redisKey);
		this.setWithDefault();
	}
	
	public RedisField(String strName, RedisKey redisKey)
	{
		this.strName = strName;
		this.redisKey = redisKey;
	}
	
	public void setWithDefault()
	{
		this.accessType = RedisData.AccessTypes.String;
		this.setSolveSteps("getBytesString");
	}
	
	public String getName()
	{
		return this.strName;
	}
	
	public RedisKey getRedisKey()
	{
		return this.redisKey;
	}
	
	public void setAccessType(RedisData.AccessTypes accessType)
	{
		this.accessType = accessType;
	}
	
	public RedisData.AccessTypes getAccessType()
	{
		return this.accessType;
	}
	
	public void setSolveSteps(DataAccessUtil.SolveSteps[] solveSteps)
	{
		this.solveSteps = solveSteps;
		for(DataAccessUtil.SolveSteps solveStep : this.solveSteps)
		{
			this.strSolveSteps = this.strSolveSteps + solveStep.name() + this.strSplitSymbol;
		}
		
		if (this.solveSteps.length > 0) this.strSolveSteps = this.strSolveSteps.substring(0, this.strSolveSteps.length() - 1);
	}
	
	public DataAccessUtil.SolveSteps[] getSolveSteps()
	{
		return this.solveSteps;
	}
	
	public void setSolveSteps(String strSolveSteps)
	{
		if (strSolveSteps == null
			|| strSolveSteps.isEmpty())
		{
			this.strSolveSteps = "";
			this.solveSteps = new DataAccessUtil.SolveSteps[0];
			return;
		}
		
		this.strSolveSteps = strSolveSteps;
		
		String[] strsSolveSteps = this.strSolveSteps.split(this.strSplitSymbol);
		
		this.solveSteps = new DataAccessUtil.SolveSteps[strsSolveSteps.length];
		for(int i = 0; i < strsSolveSteps.length; i++)
		{
			if (strsSolveSteps[i].isEmpty()) return;
			this.solveSteps[i] = DataAccessUtil.SolveSteps.valueOf(strsSolveSteps[i]);
		}
	}
	
	public String getSolveStepsStr()
	{
		return this.strSolveSteps;
	}
	
	public void setValueRef(RedisField valueRef)
	{
		this.valueRef = valueRef;
	}
	
	public RedisField getValueRef()
	{
		return this.valueRef;
	}
	
	public void setJsonLevels(String strJsonLevels)
	{
		if (strJsonLevels == null
			|| strJsonLevels.isEmpty()) this.strsJsonLevels = null;
		
		this.strsJsonLevels = strJsonLevels.split(this.strSplitSymbol);
	}
	
	public String[] getJsonLevels()
	{
		return this.strsJsonLevels;
	}
	
//	public Object getValue(RedisAccess redisData) throws Exception
//	{
//		RefObj refObj = new RefObj(null);
//		for(RedisData.solveSteps solveStep : this.solveSteps)
//		{
//			if (solveStep == null) continue;
//			solveStep.getMethod().invoke(redisData
//										 , this.accessType
//										 , this.redisKey.getRedisType()
//										 , this.redisKey.getName()
//										 , this.strName
//										 , refObj);
//		}
//		
//		return refObj.get();
//	}
	
//	public void setValue(Object objValue)
//	{
//		this.objValue = objValue;
//	}
//	
//	public Object getValue()
//	{
//		return this.objValue;
//	}
}
