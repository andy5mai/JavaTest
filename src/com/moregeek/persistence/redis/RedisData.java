package com.moregeek.persistence.redis;

import java.lang.reflect.Method;

public class RedisData {
	
	public enum RedisTypes
	{
		hash(null)
		, set(null)
		, list(null)
		, zset(null)
		, string(null)
		, none(null);
		
		private IRedisClient iRedisClient;
		
		private RedisTypes(IRedisClient iRedisClient)
		{
			this.iRedisClient = iRedisClient; 
		}
		
		public void setRedisClient(IRedisClient iRedisClient)
		{
			this.iRedisClient = iRedisClient;
		}
		
		public IRedisClient getRedisClient()
		{
			return this.iRedisClient;
		}
	}
	
	public enum AccessTypes
	{
		Bytes,
		String;
	}
}