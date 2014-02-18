package com.moregeek.persistence.redis;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.BytesRedisTemplate;

import util.Config;

public class RedisSetClient implements IRedisClient
{	
	private static Logger log = LoggerFactory.getLogger(RedisSetClient.class);

	private BytesRedisTemplate bytesRedisTemplate;
	
	private Method methodMultiGet;
	private Method methodMultiCheckSize;
	private Method methodMultiCheckValue;
	private Method methodMultiAdd;
	private Method methodMultiAddAll;
	private Method methodMultiUnion;
	private Method methodMultiUnionAndStore;
	private Method methodMultiRemove;
	private Method methodMultiRemoveByName;
	private Method methodMultiRename;
	
	public RedisSetClient(BytesRedisTemplate bytesRedisTemplate)
	{
		super();
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	public RedisSetClient() throws Exception
	{
		this.methodMultiGet = RedisSetClient.class.getDeclaredMethod("multiGet"
																	 , RedisOperations.class
																	 , String.class
																	 , Object.class
																	 , Collection.class
																	 , int.class);
		this.methodMultiGet.setAccessible(true);
		
		this.methodMultiCheckSize = RedisSetClient.class.getDeclaredMethod("multiCheckSize"
																		   , RedisOperations.class
																		   , Object.class
																		   , String.class
																		   , Object.class
																		   , Object.class);
		this.methodMultiCheckSize.setAccessible(true);
		
		this.methodMultiCheckValue = RedisSetClient.class.getDeclaredMethod("multiCheckValue"
																			, RedisOperations.class
																			, Object.class
																			, String.class
																			, Object.class
																			, Object.class);
		this.methodMultiCheckValue.setAccessible(true);
		
		this.methodMultiAdd = RedisSetClient.class.getDeclaredMethod("multiAdd"
																	 , RedisOperations.class
																	 , Object.class
																	 , String.class
																	 , Object.class
																	 , Object.class);
		this.methodMultiAdd.setAccessible(true);
		
		this.methodMultiAddAll = RedisSetClient.class.getDeclaredMethod("multiAddAll"
																	 	, RedisOperations.class
																	 	, Object.class
																	 	, String.class
																	 	, Object.class
																	 	, Object.class);
		this.methodMultiAddAll.setAccessible(true);
		
		this.methodMultiRemove = RedisSetClient.class.getDeclaredMethod("multiRemove"
																		, RedisOperations.class
																		, Object.class
																		, String.class
																		, Object.class
																		, String.class);
		this.methodMultiRemove.setAccessible(true);
		
		this.methodMultiRemoveByName = RedisSetClient.class.getDeclaredMethod("multiRemoveByName"
																			  , RedisOperations.class
																			  , Object.class
																			  , String.class
																			  , Object.class
																			  , String.class);
		this.methodMultiRemoveByName.setAccessible(true);
		
		this.methodMultiRename = RedisSetClient.class.getDeclaredMethod("multiRename"
																		, RedisOperations.class
																		, Object.class
																		, String.class
																		, Object.class
																		, String.class);
		this.methodMultiRename.setAccessible(true);
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
		return this.bytesRedisTemplate.opsForSet().members(strName.getBytes(Config.chartset));
	}
	
	@Override
	public Object get(String strName, String strKey)
	{
		if (!this.bytesRedisTemplate.opsForSet().isMember(strName.getBytes(Config.chartset), strKey)) return null;
		return strKey;
	}
	
	@Override
	public Object get(String strName, double dKey1, double dKey2)
	{
		return null;
	}
	
	@Override
	public Object getInter(String strDataName, String strFilterName)
	{
		return this.bytesRedisTemplate.opsForSet().intersect(strDataName.getBytes(Config.chartset)
															 , strFilterName.getBytes(Config.chartset));
	}
	
	@Override
	public Object getRandomValues(String strName, long lCount)
	{
		return this.bytesRedisTemplate.opsForSet().distinctRandomMembers(strName.getBytes(Config.chartset), lCount);
	}
	
	@Override
	public void set(String strName, Collection<String> colStrs) {
		// TODO Auto-generated method stub
		
		Collection colBytes = new HashSet<String>();
		for(String str : colStrs)
		{
			colBytes.add(str.getBytes(Config.chartset));
		}
		
		this.bytesRedisTemplate.opsForSet().union(strName.getBytes(Config.chartset), colBytes);
	}

	@Override
	public void set(String strName, Map<String, Object> mapObjects) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(String strName, Object obj)
	{
		this.set(strName, (Collection<String>)obj);
	}

	@Override
	public void set(String strName, Object objKey, String strValue) {
		// TODO Auto-generated method stub
		this.bytesRedisTemplate.opsForSet().add(strName.getBytes(Config.chartset)
												, strValue.getBytes(Config.chartset));
	}
	
	@Override
	public Object union(String strSourceName, String strDestName)
	{
		return this.bytesRedisTemplate.opsForSet().union(strDestName.getBytes(Config.chartset)
														 , strSourceName.getBytes(Config.chartset));
	}
	
	@Override
	public void unionAndStore(String strSourceName, String strDestName)
	{
		this.bytesRedisTemplate.opsForSet().unionAndStore(strDestName.getBytes(Config.chartset)
														  , strSourceName.getBytes(Config.chartset)
														  , strDestName.getBytes(Config.chartset));
	}
	
	@Override
	public Long increment(String strName, Object objKey, long lIncrement)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Method getMultiGetMethod()
	{
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
		return this.methodMultiUnionAndStore;
	}

	
	@Override
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount)
	{
		return operations.opsForSet().members(strName);
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
	
	public boolean multiCheckSize(RedisOperations operations
							  	  , Object objGet
							  	  , String strName
							  	  , Object objKey
							  	  , Object objLimit)
	{
		int nLimitSize = (Integer)objLimit;
		if (nLimitSize < 0) return true;
		
		Set setMembers = (Set)objGet;
		if (setMembers == null)
		{
			log.warn("members is null! strName is " + strName
					 + ", objKey is " + objKey
					 + ", nLimitSize is " + nLimitSize);
			return true;
		}
		
		return setMembers.size() < nLimitSize;
	}
	
	public boolean multiCheckValue(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objLimit)
	{
		return ((Set<String>)objGet).contains(objLimit);
	}
	
	@Override
	public void multiAdd(RedisOperations operations
						 , Object objGet
						 , String strName
						 , Object objKey
						 , Object objValue)
	{
		operations.opsForSet().add(strName, (String)objValue);
	}
	
	@Override
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object strValue)
	{
		operations.opsForSet().union(strName, (Collection)objGet);
	}
	
	@Override
	public void multiUnion(RedisOperations operations
						   , Object objGet
						   , String strName
						   , Object objKey
						   , Object strValue)
	{
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
	public Method getMultiRemoveMethod()
	{
		return this.methodMultiRemove;
	}
	
	@Override
	public void multiRemove(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		operations.opsForSet().remove(strName, strValue);
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
		operations.rename(strName, objKey + strName);
	}
	
//	@Override
//	public Boolean multi(Object objKey
//						 , Method methodGet
//						 , Method methodCheck
//						 , Method methodInvoke
//						 , Object[] objParams)
//	{
//		return this.bytesRedisTemplate.execute(new OpsSessionCallback(objKey
//														  		 , this
//														  		 , methodGet
//																 , methodCheck
//																 , methodInvoke
//																 , objParams));
//	}
	
	@Override
	public void remove(String strName)
	{
		this.bytesRedisTemplate.delete(strName.getBytes(Config.chartset));
	}

	@Override
	public void remove(String strName, String strKey)
	{
		this.bytesRedisTemplate.opsForSet().remove(strName.getBytes(Config.chartset)
												   , strKey.getBytes(Config.chartset));
	}
}
