package com.moregeek.persistence.redis;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.BytesRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;

public interface IRedisClient
{
	public void setRedisTemplate(BytesRedisTemplate bytesRedisTemplate);
	
	public Set<Object> getKeys(String strPrefixName);
	
	public Object get(String strName);
	
	public Object get(String strName, String strKey);
	
	public Object get(String strName, double dKey1, double dKey2);
	
	public Object getInter(String strDataName, String strFilterName);
	
	public Object getRandomValues(String strName, long lCount);
	
	public void set(String strName, Collection<String> colStrs);
	
	public void set(String strName, Map<String, Object> mapObjects);
	
	public void set(String strName, Object obj);
	
	public void set(String strName, Object objKey, String strValue);
	
	public Object union(String strSourceName, String strDestName);
	
	public void unionAndStore(String strSourceName, String strDestName);
	
	public Long increment(String strName, Object objKey, long lIncrement);
	
	public abstract Method getMultiGetMethod();
	
	public abstract Method getMultiGetByScoreRangeMethod();
	
	public abstract Method getMultiCheckSizeMethod();
	
	public abstract Method getMultiCheckValueMethod();
	
	public abstract Method getMultiAddMethod();
	
	public abstract Method getMultiAddAllMethod();
	
	public abstract Method getMultiUnionMethod();
	
	public abstract Method getMultiUnionAndStoreMethod();
	
	public abstract Method getMultiRemoveMethod();
	
	public abstract Method getMultiRemoveByNameMethod();
	
	public abstract Method getMultiRenameMethod();
	
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount);
	
	public Object multiGetByScoreRange(RedisOperations operations
									   , String strName
									   , Object objKey
									   , Collection colValues
									   , int nCount);
	
	public boolean multiCheckSize(RedisOperations operations
							   	  , Object objGet
								  , String strName
								  , Object objKey
								  , Object objLimit);
	
	public boolean multiCheckValue(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objLimit);
	
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object objValue);
	
	public void multiAdd(RedisOperations operations
						 , Object objGet
						 , String strName
						 , Object objKey
						 , Object objValue);
	
	public void multiUnion(RedisOperations operations
						   , Object objGet
						   , String strName
						   , Object objKey
						   , Object objValue);
	
	public void multiUnionAndStore(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objValue);
	
	public void multiRename(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue);
	
	public void multiRemove(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue);
	
	public void multiRemoveByName(RedisOperations operations
								  , Object objGet
								  , String strName
								  , Object objKey
								  , String strValue);
	
	public void remove(String strName);
	
	public void remove(String strName, String objKey);
}
