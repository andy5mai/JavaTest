package com.moregeek.persistence.redis;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BytesRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;

import util.CommonUtil;

public class RedisHashClient implements IRedisClient
{	
	private static Logger log = LoggerFactory.getLogger(RedisHashClient.class);

	private BytesRedisTemplate bytesRedisTemplate;
	private Method methodMultiGet;
	private Method methodMultiCheckSize;
	private Method methodMultiCheckValue;
	private Method methodMultiAdd;
	private Method methodMultiAddAll;
	private Method methodMultiUnion;
	private Method methodMultiRemove;
	private Method methodMultiRemoveByName;
	private Method methodMultiRename;
	
	public RedisHashClient(BytesRedisTemplate bytesRedisTemplate)
	{
		super();
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	public RedisHashClient() throws Exception
	{
		this.methodMultiGet = RedisHashClient.class.getDeclaredMethod("multiGet"
																	  , RedisOperations.class
																	  , String.class
																	  , Object.class
																	  , Collection.class
																	  , int.class);
		this.methodMultiGet.setAccessible(true);

		this.methodMultiCheckSize = RedisHashClient.class.getDeclaredMethod("multiCheckSize"
																		    , RedisOperations.class
																		    , Object.class
																		    , String.class
																		    , Object.class
																		    , Object.class);
		this.methodMultiCheckSize.setAccessible(true);
		
		this.methodMultiAdd = RedisHashClient.class.getDeclaredMethod("multiAdd"
																	  , RedisOperations.class
																	  , Object.class
																	  , String.class
																	  , Object.class
																	  , Object.class);
		this.methodMultiAdd.setAccessible(true);
		
		this.methodMultiAddAll = RedisHashClient.class.getDeclaredMethod("multiAddAll"
																		 , RedisOperations.class
																		 , Object.class
																		 , String.class
																		 , Object.class
																		 , Object.class);
		this.methodMultiAddAll.setAccessible(true);
		
		this.methodMultiRemove = RedisHashClient.class.getDeclaredMethod("multiRemove"
																		 , RedisOperations.class
																		 , Object.class
																		 , String.class
																		 , Object.class
																		 , String.class);
		this.methodMultiRemove.setAccessible(true);
		
		this.methodMultiRemoveByName = RedisHashClient.class.getDeclaredMethod("multiRemoveByName"
																		  	   , RedisOperations.class
																		  	   , Object.class
																		  	   , String.class
																		  	   , Object.class
																		  	   , String.class);
		this.methodMultiRemoveByName.setAccessible(true);
	}
	
	@Override
	public void setRedisTemplate(BytesRedisTemplate bytesRedisTemplate)
	{
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	@Override
	public Set<Object> getKeys(String strPrefixName)
	{
		return this.bytesRedisTemplate.opsForHash().keys(CommonUtil.getStrBytes(strPrefixName));
	}

	@Override
	public Map<Object, Object> get(String strName)
	{
		System.out.println("RedisHash" + strName);
		return this.bytesRedisTemplate.opsForHash().entries(CommonUtil.getStrBytes(strName));
	}
	
	@Override
	public byte[] get(String strName, String strKey)
	{
		return (byte[])this.bytesRedisTemplate.opsForHash().get(CommonUtil.getStrBytes(strName)
																, CommonUtil.getStrBytes(strKey));
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
	public void set(String strName, Collection<String> colStrs) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void set(String strName, Map<String, Object> mapKeyValues)
	{
		Map<byte[], byte[]> mapBytesKeyValues = new HashMap<byte[], byte[]>();
		Object objValue;
		byte[] bsValue;
		for(String str : mapKeyValues.keySet())
		{
			objValue = mapKeyValues.get(str);
			if (objValue instanceof byte[])
			{
				bsValue = (byte[])objValue;
			}
			else
			{
				bsValue = CommonUtil.getStrBytes((String)objValue);
			}
			
			mapBytesKeyValues.put(CommonUtil.getStrBytes(str)
							   , bsValue);
		}
		
		this.bytesRedisTemplate.opsForHash().putAll(CommonUtil.getStrBytes(strName)
													, mapBytesKeyValues);
	}
	
	@Override
	public void set(String strName, Object obj)
	{
		this.set(strName, (Map<String, Object>) obj);
	}
	
	@Override
	public void set(String strName, Object objKey, String strValue)
	{
		this.bytesRedisTemplate.opsForHash().put(CommonUtil.getStrBytes(strName)
												 , CommonUtil.getStrBytes((String)objKey)
												 , CommonUtil.getStrBytes(strValue));
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
		return this.bytesRedisTemplate.opsForHash().increment(CommonUtil.getStrBytes(strName)
															  , CommonUtil.getStrBytes((String)objKey)
															  , lIncrement);
	}
	
	@Override
	public void remove(String strName)
	{
		this.bytesRedisTemplate.delete(CommonUtil.getStrBytes(strName));
	}

	@Override
	public void remove(String strName, String strKey)
	{
		this.bytesRedisTemplate.opsForHash().delete(CommonUtil.getStrBytes(strName)
													, CommonUtil.getStrBytes(strKey));
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
	public Method getMultiCheckSizeMethod()
	{
		return this.methodMultiCheckSize;
	}
	
	@Override
	public Method getMultiCheckValueMethod()
	{
		return this.methodMultiCheckValue;
	}
	
	@Override
	public Method getMultiAddMethod()
	{
		return this.methodMultiAdd;
	}
	
	@Override
	public Method getMultiAddAllMethod()
	{
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
	public Method getMultiRemoveMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiRemove;
	}
	
	@Override
	public Method getMultiRemoveByNameMethod()
	{
		return this.methodMultiRemoveByName;
	}
	
	@Override
	public Method getMultiRenameMethod()
	{
		return this.methodMultiRename;
	}

	@Override
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount)
	{
		return operations.opsForHash().entries(strName);
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
								  , String strName
								  , Object objKey
								  , Object objLimit)
	{
		return objGet != null && ((Map)objGet).size() > 0;
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
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object objValue)
	{
		operations.opsForHash().putAll(strName, (Map)objValue);
	}
	
	@Override
	public void multiAdd(RedisOperations operations
						 , Object objGet
						 , String strName
						 , Object objKey
						 , Object objValue)
	{
		// TODO Auto-generated method stub
		
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
		operations.delete(strName);
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
