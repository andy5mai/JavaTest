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

public class RedisZSetClient implements IRedisClient
{	
	private static Logger log = LoggerFactory.getLogger(RedisZSetClient.class);

	private BytesRedisTemplate bytesRedisTemplate;
	
	private Method methodMultiGet;
	private Method methodMultiGetByScoreRange;
	private Method methodMultiCheckSize;
	private Method methodMultiCheckValue;
	private Method methodMultiAdd;
	private Method methodMultiAddAll;
	private Method methodMultiUnionAndStore;
	private Method methodMultiRemove;
	private Method methodMultiRemoveByName;
	private Method methodMultiRename;
	
	public RedisZSetClient(BytesRedisTemplate bytesRedisTemplate)
	{
		super();
		this.bytesRedisTemplate = bytesRedisTemplate;
	}
	
	public RedisZSetClient() throws Exception
	{
		this.methodMultiGet = RedisZSetClient.class.getDeclaredMethod("multiGet"
																	  , RedisOperations.class
																	  , String.class
																	  , Object.class
																	  , Collection.class
																	  , int.class);
		this.methodMultiGet.setAccessible(true);
		
		this.methodMultiGetByScoreRange = RedisZSetClient.class.getDeclaredMethod("multiGetByScoreRange"
																			 	  , RedisOperations.class
																			 	  , String.class
																			 	  , Object.class
																			 	  , Collection.class
																			 	  , int.class);
		this.methodMultiGetByScoreRange.setAccessible(true);

		this.methodMultiCheckSize = RedisZSetClient.class.getDeclaredMethod("multiCheckSize"
																		    , RedisOperations.class
																		    , Object.class
																		    , String.class
																		    , Object.class
																		    , Object.class);
		this.methodMultiCheckSize.setAccessible(true);
		
		this.methodMultiAdd = RedisZSetClient.class.getDeclaredMethod("multiAdd"
																	  , RedisOperations.class
																	  , Object.class
																	  , String.class
																	  , Object.class
																	  , Object.class);
		this.methodMultiAdd.setAccessible(true);
		
		this.methodMultiAddAll = RedisZSetClient.class.getDeclaredMethod("multiAddAll"
																		 , RedisOperations.class
																		 , Object.class
																		 , String.class
																		 , Object.class
																		 , Object.class);
		this.methodMultiAddAll.setAccessible(true);
		
		this.methodMultiUnionAndStore = RedisZSetClient.class.getDeclaredMethod("multiUnionAndStore"
																			    , RedisOperations.class
																			    , Object.class
																				, String.class
																				, Object.class
																				, Object.class);
		this.methodMultiUnionAndStore.setAccessible(true);
		
		this.methodMultiRename = RedisZSetClient.class.getDeclaredMethod("multiRename"
																		, RedisOperations.class
																		, Object.class
																		, String.class
																		, Object.class
																		, String.class);
		this.methodMultiRename.setAccessible(true);
		
		this.methodMultiRemove = RedisZSetClient.class.getDeclaredMethod("multiRemove"
																		 , RedisOperations.class
																		 , Object.class
																		 , String.class
																		 , Object.class
																		 , String.class);
		this.methodMultiRemove.setAccessible(true);
		
		this.methodMultiRemoveByName = RedisZSetClient.class.getDeclaredMethod("multiRemoveByName"
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(String strName) {
		// TODO Auto-generated method stub
		return this.bytesRedisTemplate.opsForZSet().range(strName.getBytes(Config.chartset), 0, -1);
	}

	@Override
	public Object get(String strName, String strKey) {
		// TODO Auto-generated method stub
		return this.bytesRedisTemplate.opsForZSet().score(strName.getBytes(Config.chartset)
														  , strKey.getBytes(Config.chartset));
	}
	
	@Override
	public Object get(String strName, double dKey1, double dKey2)
	{
		return this.bytesRedisTemplate.opsForZSet().rangeByScore(strName.getBytes(Config.chartset)
																 , dKey1
																 , dKey2);
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
	public void set(String strName, Map<String, Object> mapObjects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(String strName, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(String strName, Object objKey, String strValue)
	{
		// TODO Auto-generated method stub
		this.bytesRedisTemplate.opsForZSet().add(strName.getBytes(Config.chartset)
												 , strValue.getBytes(Config.chartset)
												 , ((Long)objKey).doubleValue());
	}
	
	@Override
	public Object union(String strSourceName, String strDestName)
	{
		return null;
		//this.bytesRedisTemplate.opsForZSet().unionAndStore(strDestName, strSourceName, strDestName);
	}
	
	@Override
	public void unionAndStore(String strSourceName, String strDestName)
	{
		this.bytesRedisTemplate.opsForZSet().unionAndStore(strDestName.getBytes(Config.chartset)
														   , strSourceName.getBytes(Config.chartset)
														   , strDestName.getBytes(Config.chartset));
	}

	@Override
	public Long increment(String strName, Object objKey, long lIncrement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getMultiGetMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiGet;
	}
	
	@Override
	public Method getMultiGetByScoreRangeMethod() {
		return this.methodMultiGetByScoreRange;
	}

	@Override
	public Method getMultiCheckSizeMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiCheckSize;
	}

	@Override
	public Method getMultiCheckValueMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiCheckValue;
	}

	@Override
	public Method getMultiAddMethod() {
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
		return null;
	}
	
	@Override
	public Method getMultiUnionAndStoreMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiUnionAndStore;
	}

	@Override
	public Method getMultiRemoveMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiRemove;
	}

	@Override
	public Method getMultiRemoveByNameMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiRemoveByName;
	}

	@Override
	public Method getMultiRenameMethod() {
		// TODO Auto-generated method stub
		return this.methodMultiRename;
	}

	@Override
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount) {
		// TODO Auto-generated method stub
		return operations.opsForZSet().range(strName, 0, -1);
	}
	
	@Override
	public Object multiGetByScoreRange(RedisOperations operations
									   , String strName
									   , Object objKey
									   , Collection colValues
									   , int nCount)
	{
		return operations.opsForZSet().rangeWithScores(strName, 0, -1);
	}

	@Override
	public boolean multiCheckSize(RedisOperations operations
								  , Object objGet
								  , String strName
								  , Object objKey
								  , Object objLimit) 
	{
		// TODO Auto-generated method stub
		Integer nLimit = (Integer)objLimit;
		if (nLimit == null) return true;
		
		Set setMembers = (Set)objGet;
		if (setMembers == null)
		{
			log.warn("members is null! strName is " + strName
					 + ", objKey is " + objKey
					 + ", nLimitSize is " + nLimit);
			return true;
		}
		
		return setMembers.size() < nLimit;
	}

	@Override
	public boolean multiCheckValue(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objLimit)
	{
		// TODO Auto-generated method stub
		return ((Set<String>)objGet).contains(objLimit);
	}

	@Override
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object objValue)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multiAdd(RedisOperations operations
						 , Object objGet
						 , String strName
						 , Object objKey
						 , Object objValue)
	{
		// TODO Auto-generated method stub
		operations.opsForZSet().add(strName, objValue, (Long)objKey);
	}
	
	@Override
	public void multiUnion(RedisOperations operations
						   , Object objGet
						   , String strName
						   , Object objKey
						   , Object objValue)
	{
		// TODO Auto-generated method stub
		//operations.opsForZSet().unionAndStore(strName, objKey, strName);
	}
	
	@Override
	public void multiUnionAndStore(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objValue)
	{
		operations.opsForZSet().unionAndStore(strName, objKey, strName);
	}

	@Override
	public void multiRename(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		// TODO Auto-generated method stub
		operations.rename(strName, objKey + strName);
	}

	@Override
	public void multiRemove(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		// TODO Auto-generated method stub
		operations.opsForZSet().remove(strName, strValue);
	}

	@Override
	public void multiRemoveByName(RedisOperations operations
								  , Object objGet
								  , String strName
								  , Object objKey
								  , String strValue)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String strName) {
		// TODO Auto-generated method stub
		this.bytesRedisTemplate.delete(strName.getBytes(Config.chartset));
	}

	@Override
	public void remove(String strName, String strKey) {
		// TODO Auto-generated method stub
		this.bytesRedisTemplate.opsForZSet().remove(strName.getBytes(Config.chartset)
													, strKey.getBytes(Config.chartset));
	}
}
