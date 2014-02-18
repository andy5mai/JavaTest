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

public class RedisValueClient implements IRedisClient
{	
	private static Logger log = LoggerFactory.getLogger(RedisValueClient.class);

	private BytesRedisTemplate bytesRedisTemplate;
	
	public RedisValueClient(BytesRedisTemplate bytesRedisTemplate)
	{
		super();
		this.bytesRedisTemplate = bytesRedisTemplate;
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

	public Object get(String strName)
	{
		return this.bytesRedisTemplate.opsForValue().get(strName.getBytes(Config.chartset));
	}
	
	public Object get(String strName, String strKey)
	{
		return this.get(strName);
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
	public void set(String strName, Map<String, Object> mapObjects) {
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
		this.bytesRedisTemplate.opsForValue().set(strName.getBytes(Config.chartset)
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
		return this.bytesRedisTemplate.opsForValue().increment(strName.getBytes(Config.chartset)
															   , lIncrement);
	}
	
	@Override
	public void remove(String strName)
	{
		this.bytesRedisTemplate.delete(strName.getBytes(Config.chartset));
	}

	@Override
	public void remove(String strName, String strKey)
	{
		this.bytesRedisTemplate.delete(strName.getBytes(Config.chartset));
	}
	
	@Override
	public Method getMultiGetMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiGetByScoreRangeMethod() {
		return null;
	}

	@Override
	public Method getMultiCheckSizeMethod(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiCheckValueMethod(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getMultiAddMethod(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiAddAllMethod()
	{
		return null;
	}
	
	@Override
	public Method getMultiUnionMethod()
	{
		return null;
	}
	
	@Override
	public Method getMultiUnionAndStoreMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiRemoveMethod(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Method getMultiRemoveByNameMethod()
	{
		return null;
	}
	
	@Override
	public Method getMultiRenameMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object multiGet(RedisOperations operations
						   , String strName
						   , Object objKey
						   , Collection colValues
						   , int nCount)
	{
		return null;
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
								  , Object objLimit) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean multiCheckValue(RedisOperations operations
								   , Object objGet
								   , String strName
								   , Object objKey
								   , Object objLimit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void multiAdd(RedisOperations operations
						 , Object objGet
						 , String strName
						 , Object objKey
						 , Object objValue) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void multiAddAll(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, Object objValue)
	{
	}
	
	@Override
	public void multiUnion(RedisOperations operations
						   , Object objGet
						   , String strName
						   , Object objKey
						   , Object objValue)
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
	public void multiRemove(RedisOperations operations
							, Object objGet
							, String strName
							, Object objKey
							, String strValue)
	{
		
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
