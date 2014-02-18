package com.moregeek.persistence.redis;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.BytesRedisTemplate;

import util.Config;

public class RedisListClient implements IRedisClient
{	
	private static Logger log = LoggerFactory.getLogger(RedisListClient.class);

	private BytesRedisTemplate bytesRedisTemplate;
	
	private Method methodMultiGet;
	private Method methodMultiCheckSize;
	private Method methodMultiCheckValue;
	private Method methodMultiAdd;
	private Method methodMultiAddAll;
	private Method methodMultiUnion;
	private Method methodMultiRemove;
	private Method methodMultiRename;
	
	public RedisListClient(BytesRedisTemplate bytesRedisTemplate)
	{
		super();
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	public RedisListClient() throws Exception
	{
		this.methodMultiGet = RedisListClient.class.getDeclaredMethod("multiGet"
																	  , RedisOperations.class
																	  , String.class
																	  , Object.class
																	  , Collection.class
																	  , int.class);
		this.methodMultiGet.setAccessible(true);
		 
		this.methodMultiCheckSize = RedisListClient.class.getDeclaredMethod("multiCheckSize"
																		    , RedisOperations.class
																		    , Object.class
																		    , String.class
																		    , Object.class
																		    , Object.class);
		this.methodMultiCheckSize.setAccessible(true);
		
		this.methodMultiAdd = RedisListClient.class.getDeclaredMethod("multiAdd"
																	  , RedisOperations.class
																	  , Object.class
																	  , String.class
																	  , Object.class
																	  , Object.class);
		this.methodMultiAdd.setAccessible(true);
		
//		this.methodMultiRemove = RedisSetClient.class.getDeclaredMethod("multiRemove"
//																		, RedisOperations.class
//																		, Object.class
//																		, String.class
//																		, String.class
//																		, String.class);
	}
	
	@Override
	public void setRedisTemplate(BytesRedisTemplate bytesRedisTemplate)
	{
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	@Override
	public Set<Object> getKeys(String strPrefixName)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(String strName)
	{
		return this.bytesRedisTemplate.opsForList().rightPop(strName.getBytes(Config.chartset));
	}
	
	@Override
	public Object get(String strName, String strKey)
	{
		return null;
	}
	
	@Override
	public Object get(String strName, double dKey1, double dKey2)
	{
		return null;
	}
	
	@Override
	public Object getInter(String strDataName, String strFilterName)
	{
		return null;
	}
	
	@Override
	public Object getRandomValues(String strName, long lCount)
	{
		return null;
	}
	
	@Override
	public void set(String strName, Collection colStrs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(String strKey, Map<String, Object> mapObjects) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(String strName, Object obj)
	{
	}

	@Override
	public void set(String strName, Object objKey, String strValue) {
		// TODO Auto-generated method stub
		this.bytesRedisTemplate.opsForList().leftPush(strName.getBytes(Config.chartset)
													  , strValue.getBytes(Config.chartset));
	}
	
	@Override
	public Object union(String strName1, String strName2)
	{
		return null;
	}
	
	@Override
	public void unionAndStore(String strSourceName, String strDestName)
	{
		
	}
	
	@Override
	public Long increment(String strName, Object objKey, long lIncrement)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(String strName)
	{
		this.bytesRedisTemplate.delete(strName.getBytes(Config.chartset));
	}

	@Override
	public void remove(String strName, String strKey)
	{
		this.bytesRedisTemplate.opsForList().remove(strName.getBytes(Config.chartset)
													, 0
													, strKey.getBytes(Config.chartset));
	}

	@Override
	public Method getMultiGetMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiGet;
	}
	
	@Override
	public Method getMultiGetByScoreRangeMethod() {
		return null;
	}

	@Override
	public Method getMultiCheckSizeMethod(){
		// TODO Auto-generated method stub
		return this.methodMultiCheckSize;
	}
	
	@Override
	public Method getMultiCheckValueMethod(){
		// TODO Auto-generated method stub
		return this.methodMultiCheckValue;
	}
	
	@Override
	public Method getMultiAddMethod()
	{
		// TODO Auto-generated method stub
		return this.methodMultiAdd;
	}
	
	@Override
	public Method getMultiAddAllMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiAddAll;
	}

	@Override
	public Method getMultiUnionMethod()
	{
		return this.methodMultiUnion;
	}
	
	@Override
	public Method getMultiUnionAndStoreMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiRemoveMethod()
	{
		// TODO Auto-generated method stub
		return this.methodMultiRemove;
	}
	
	@Override
	public Method getMultiRemoveByNameMethod()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiRenameMethod()
	{
		return null;
	}
	
	@Override
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount)
	{
		for(int i = 0; i < nCount; i++)
		{
			colValues.add(operations.opsForList().rightPop(strName));
		}

		return colValues;
	}
	
	@Override
	public Object multiGetByScoreRange(RedisOperations operations
									   , String strName
									   , Object objKey
									   , Collection colValues
									   , int nCount)
	{
		return null;
	}
	
	@Override
	public boolean multiCheckSize(RedisOperations operations
								  , Object objGet
								  ,	String strName
								  , Object objKey
								  , Object objLimit)
	{
		// TODO Auto-generated method stub
		int nLimit = (Integer)objLimit;
		
		return (operations.opsForList().size(strName) <= nLimit);
	}
	
	@Override
	public boolean multiCheckValue(RedisOperations operations
							  	   , Object objGet
							  	   , String strName
							  	   , Object objKey
							  	   , Object objLimit)
	{
		return false;
	}

	@Override
	public void multiAdd(RedisOperations operations
						, Object objGet
						, String strName
						, Object objKey
						, Object strValue)
	{
		return;
	}
	
	@Override
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object strValue)
	{
	}
	
	@Override
	public void multiUnion(RedisOperations operations
						   , Object objGet
						   , String strName
						   , Object objKey
						   , Object objValue) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void multiUnionAndStore(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objValue)
	{
	}

	@Override
	public void multiRemove(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		return;
	}
	
	@Override
	public void multiRemoveByName(RedisOperations operations
							   	  , Object objGet
							   	  , String strName
							   	  , Object objKey
							   	  , String strValue)
	{
		operations.delete(strName);
	}
	
	@Override
	public void multiRename(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		operations.rename(strValue, strName);
	}
}
