package com.moregeek.persistence.redis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BytesRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.CommonUtil;
import util.DataAccessUtil;
import util.DateUtil;
import util.JsonUtil;
import util.ZlibUtil;

import attrs.AttrClass;
import attrs.AvatarCountAttrs;
import attrs.AvatarInboxIdsAttrs;
import attrs.AvatarMapAttrs;
import attrs.SysInboxIdsAttrs;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moregeek.persistence.redis.RedisData.RedisTypes;

public class RedisClient
{
	private static final Logger log = LoggerFactory.getLogger(RedisClient.class);
	
	@Autowired
	protected BytesRedisTemplate bytesRedisTemplate;
	
	@Autowired
	protected JedisPool jedisPool;
	
	public static final int COMMON = 0; ///< which redis db
		
	public static final String strSplitSymbol = ";";
	
	public enum Result
	{
		Exist((byte)0),
		Success((byte)1),
		Failed((byte)-1),
		Result1((byte)-2),
		Result2((byte)-3),
		Result3((byte)-4);
		
		private byte bValue;
		private Result(byte bValue)
		{
			this.bValue = bValue;
		}
		
		public byte getValue()
		{
			return this.bValue;
		}
	}
	
	public RedisClient(BytesRedisTemplate bytesRedisTemplate, JedisPool jedisPool) throws Exception
	{
		this.bytesRedisTemplate = bytesRedisTemplate;
		this.jedisPool = jedisPool;
		
		if (RedisTypes.hash.getRedisClient() == null) RedisTypes.hash.setRedisClient(new RedisHashClient(this.bytesRedisTemplate));
		if (RedisTypes.set.getRedisClient() == null) RedisTypes.set.setRedisClient(new RedisSetClient(this.bytesRedisTemplate));
		if (RedisTypes.string.getRedisClient() == null) RedisTypes.string.setRedisClient(new RedisValueClient(this.bytesRedisTemplate));
		if (RedisTypes.list.getRedisClient() == null) RedisTypes.list.setRedisClient(new RedisListClient(this.bytesRedisTemplate));
		if (RedisTypes.zset.getRedisClient() == null) RedisTypes.zset.setRedisClient(new RedisZSetClient(this.bytesRedisTemplate));
	}

	public RedisClient() throws Exception
	{
		if (RedisTypes.hash.getRedisClient() == null) RedisTypes.hash.setRedisClient(new RedisHashClient());
		if (RedisTypes.set.getRedisClient() == null) RedisTypes.set.setRedisClient(new RedisSetClient());
		if (RedisTypes.string.getRedisClient() == null) RedisTypes.string.setRedisClient(new RedisValueClient(this.bytesRedisTemplate));
		if (RedisTypes.list.getRedisClient() == null) RedisTypes.list.setRedisClient(new RedisListClient());
		if (RedisTypes.zset.getRedisClient() == null) RedisTypes.zset.setRedisClient(new RedisZSetClient());
		
//		this.methodSetMTUMailId = RedisClient.class.getDeclaredMethod("setMTUMailId", Object.class);
//		this.methodSetMTUMailId.setAccessible(true);
	}
	
//	private Method methodGetBytesString;
//	public Method getMethodGetBytesString()
//	{
//		return this.methodGetBytesString;
//	}
//	 
//	private Method methodUnCompressValue;
//	public Method getMethodUnCompressValue()
//	{
//		return this.methodUnCompressValue;
//	}
	 
	//private Method methodGetBytesValueFromRedis;
	 
//	private Method methodGetBytesValueFromMap;
//	public Method getMethodGetBytesValueFromMap()
//	{
//		return this.methodGetBytesValueFromMap;
//	}
//
//	private Method methodGetFromJsonObj;
//	public Method getMethodGetFromJsonObj()
//	{
//		return this.methodGetFromJsonObj;
//	}

//	private Method methodGetStrFromJson;
	
	protected IRedisClient getExecutor(RedisTypes redisType) throws Exception
	{
		redisType.getRedisClient().setRedisTemplate(this.bytesRedisTemplate);
		return redisType.getRedisClient();
	}
	
	public Set<Object> getKeys(RedisKey redisKey, String strPrefixName) throws Exception
	{
		return this.getExecutor(redisKey.getRedisType()).getKeys(redisKey.getPrefix() + strPrefixName);
	}
	
	public String getValue(AttrClass attrClass
						   , String strPrefixKey) throws Exception
	{
		return (String)this.getValue(attrClass
							 		 , strPrefixKey
							 		 , attrClass.getName());
	}
	
	public Object getValue(AttrClass attrClass
						   , String strPrefixKey
						   , String strPropertyName) throws Exception
	{
		return this.getExecutor(attrClass.getRedisField().getRedisKey().getRedisType())
					   			.get(attrClass.getRedisField().getRedisKey().getPrefix() + strPrefixKey, strPropertyName);
		
//		String strJsonValue = (String)this.getValue(attrClass.getBelong()
//										    		, strPrefixKey
//										    		, strPropertyName);
//		
//		return (String)((HashMap<String, String>)JsonUtil.fromJson(strJsonValue
//														 		   , new TypeToken<HashMap<String, String>>(){}.getType()))
//						 .get(attrClass.name());
	}
	
	public Object getValues(RedisKey redisKey, String strKey) throws Exception
	{
		return this.getExecutor(redisKey.getRedisType()).get(strKey);
	}
	
	public Object getValues(RedisKey redisKey, String strPrefixKey, double dKey1, double dKey2) throws Exception
	{
		return this.getExecutor(redisKey.getRedisType()).get(redisKey.getPrefix() + strPrefixKey, dKey1, dKey2);
	}
	
	public Object getNoTypePrefixValues(RedisKey redisKey, String strName) throws Exception
	{
		return this.getExecutor(redisKey.getRedisType()).get(strName);
	}
	
	public Object getInterValues(RedisKey redisKeyData, String strDataName, RedisKey redisKeyFilter, String strFilterName) throws Exception
	{
		return this.getExecutor(redisKeyData.getRedisType()).getInter(redisKeyData.getPrefix() + strDataName
															 	  	  , redisKeyFilter.getPrefix() + strFilterName);
	}
	
	public Object getRandomValues(RedisKey redisKey, String strPrefixKey, long lCount) throws Exception
	{
		return this.getExecutor(redisKey.getRedisType()).getRandomValues(redisKey.getPrefix() + strPrefixKey, lCount);
	}
	
	public void setKeyValue(RedisKey redisKey, String strName, Object objKey, String strValue) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).set(redisKey.getPrefix() + strName, objKey, strValue);
	}
	
	public void setKeyValueByName(RedisKey redisKey, String strName, Object objKey, String strValue) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).set(strName, objKey, strValue);
	}
	
	public void setKeyValues(RedisKey redisKey, String strName, Collection<String> set) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).set(redisKey.getPrefix() + strName, set);
	}
	
	public void setKeyValues(RedisKey redisKey, String strName, Map<String, Object> mapObjects) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).set(redisKey.getPrefix() + strName, mapObjects);
	}
	
//	public void setKeyValuesByName(Types type, String strName, Object obj) throws Exception
//	{
//		this.getExecutor(type.getRedisType()).set(strName, obj);
//	}
	
	public Object union(RedisKey redisKeySource, String strSourceName, RedisKey redisKeyDest, String strDestName) throws Exception
	{
		return this.getExecutor(redisKeySource.getRedisType()).union(redisKeySource.getPrefix() + strSourceName
														  		 	 , redisKeyDest.getPrefix() + strDestName);
	}
	
	public void unionAndStoreDest(RedisKey redisKey, String strSourceName, String strDestName) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).union(strSourceName, strDestName);
		this.getExecutor(redisKey.getRedisType()).remove(strSourceName);
	}
	
	public Long incrementValue(RedisTypes redisType, String strKey, String strField, long lIncrement) throws Exception
	{
		return this.getExecutor(redisType).increment(strKey, strField, lIncrement);
	}
	
	public void removeKeyValue(RedisKey redisKey, String strName, String strKey) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).remove(redisKey.getPrefix() + strName, strKey);
	}
	
	public void removeNoPrefixKeyValue(RedisKey redisKey, String strName, String strKey) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).remove(strName, strKey);
	}
	
	public void removeKeyValues(RedisKey redisKey, String strName) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).remove(redisKey.getPrefix() + strName);
	}
	
	public void removeKeyLikeValues(RedisKey redisKey, String strName) throws Exception
	{
		this.getExecutor(redisKey.getRedisType()).remove(redisKey.getPrefix() + strName + "*");
	}
	
//	public Object[] getExecMultiParams(int nLimitSize, String strName, String strKey, String strValue)
//	{
//		return new Object[]{ null, null, nLimitSize, strName, strKey, strValue };
//	}

	/*
	public Result execMulti(IRedisClient redisClient
							, RedisKey redisKey
							, String strName
							, Method methodGet
							, Method methodCheck
							, Method methodInvoke
							, Object objKey
							, Object objValue
							, Object objLimit)
	{
		strName = redisKey.getPrefix() + strName;
		
		return (Result)this.bytesRedisTemplate.execute(new OpsSessionCallback(strName
																		 , redisClient
																		 , methodGet
																		 , methodCheck
																		 , methodInvoke
																		 , strName
																		 , objKey
																		 , objValue
																		 , objLimit));
	}
	
	public Result execMulti(IRedisClient redisClient
							, RedisKey redisKey
							, String strName1
							, String strName2
							, Method methodGet1
							, Method methodGet2
							, Method methodCheck1
							, Method methodCheck2
							, Method methodInvoke1
							, Method methodInvoke2
							, String strKey1
							, String strKey2
							, String strValue1
							, String strValue2
							, Object objLimit1
							, Object objLimit2)
	{
		strName1 = redisKey.getPrefix() + strName1;
		strName2 = redisKey.getPrefix() + strName2;
	
		return (Result)this.bytesRedisTemplate.execute(new OpsSessionCallback(strName1
																		 , strName2
																		 , redisClient
																		 , methodGet1
																		 , methodGet2
																		 , methodCheck1
																		 , methodCheck2
																		 , methodInvoke1
																		 , methodInvoke2
																		 , strName1
																		 , strName2
																		 , strKey1
																		 , strKey2
																		 , strValue1
																		 , strValue2
																		 , objLimit1
																		 , objLimit2));
	}
	
	public Collection execMultiTake(IRedisClient redisClient
									, RedisKey redisKey
									, String strName
									, Method methodGet
									, Method methodCheck
									, Method methodInvoke
									, String strKey
									, Object objLimit
									, Collection colValues
									, int nCount)
	{
		return (Collection)this.bytesRedisTemplate.execute(new OpsSessionCallback(strName
																			 , redisClient
																			 , methodGet
																			 , methodCheck
																			 , methodInvoke
																			 , strName
																			 , strKey
																			 , objLimit
																			 , colValues
																			 , nCount));
	}
	
	public Collection execMultiTake2(IRedisClient redisClient1
									 , RedisKey redisKey1
									 , String strName1
									 , Method methodGet1
									 , Method methodCheck1
									 , Method methodInvoke1
									 , String strKey1
									 , String strValue1
									 , Object objLimit1
									 , Collection colValues1
									 , int nCount1
									 , IRedisClient redisClient2
									 , RedisKey redisKey2
									 , String strName2
									 , Method methodGet2
									 , Method methodCheck2
									 , Method methodInvoke2
									 , String strKey2
									 , String strValue2
									 , Object objLimit2
									 , Collection colValues2
									 , int nCount2
									 , boolean bTakeByList)
	{
		strName1 = redisKey1.getPrefix() + strName1;
		strName2 = redisKey2.getPrefix() + strName2;
		
		return (Collection)this.bytesRedisTemplate.execute(new OpsSessionCallback(strName1
																			 , strName2
															 				 , redisClient1
															 				 , redisClient2
															 				 , methodGet1
															 				 , methodGet2
																			 , methodCheck1
																			 , methodCheck2
																			 , methodInvoke1
																			 , methodInvoke2
																			 , strName1
																			 , strName2
																			 , strKey1
																			 , strKey2
																			 , strValue1
																			 , strValue2
																			 , objLimit1
																			 , objLimit2
																			 , colValues1
																			 , colValues2
																			 , nCount1
																			 , nCount2
																			 , bTakeByList));
	}
	
	/// token ----------------------------------------\\\\
	public void setUserToken(String key, String value) {
			
		this.bytesRedisTemplate.opsForValue().set(key.getBytes(Config.chartset)
												  , value.getBytes(Config.chartset));
	}

	public Identifier getUserToken(Object key) {

		if (Config.isMRLBEnabled)
		{
			log.info("user token key is " + key);
			key = ((String)key).split("\\|")[0];
		}
		
		String jsonString = new String(bytesRedisTemplate.opsForValue().get(((String)key).getBytes(Config.chartset))
									   , Config.chartset);
			
		try
		{
			return new Identifier(Integer.parseInt(jsonString));
		}
		catch(Exception exc)
		{
			log.error("RedisClient getUserToken change jsonString to Identifier Error:" + exc.getMessage());
			return null;
		}
					
		//return (Identifier)JsonUtil.fromJson(jsonString, Identifier.class);
	}
		
	public void deleteUserToken(String key) {
		bytesRedisTemplate.delete(key.getBytes(Config.chartset));
			
	}
	/// token -----------------------------------------////
		
		
		
	/// account -----------------------------------------\\\\
	public AccountVO findAccountByName(String account) {
//		String accountJson = accountRedisTemplate.opsForValue().get(account);
//		return gson.fromJson(accountJson, AccountVO.class);
		return null;
	}
	*/
	//
//	public void addAccount(AccountVO accountVO) {
//		int nextId = nextId(accountRedisTemplate);
////		String nextIdString = nextId+"";
//			accountVO.setID(nextId);
//			accountRedisTemplate.opsForValue().set(nextId+"", gson.toJson(accountVO));
//			
//			addHistory(RedisClient.ACCOUNT, nextId, RedisHistoryType.INSERT);
//	}
//	public void updateAccount(int id, AccountVO accountVO) {
////		String idString = id+"";
//		accountRedisTemplate.opsForValue().set(id+"", gson.toJson(accountVO));
	//
//		addHistory(RedisClient.ACCOUNT, id, RedisHistoryType.UPDATE);
//	}
	/// account ------------------------------------------////

		
		
	/// history -------------------------------------------\\\\
//		private void addHistory(RedisHistory history) {
//			String json = JsonUtil.toJson(history);
//			redisTemplate.opsForList().rightPush("history", json);
//		}
		
//		private void addHistory(int db, int id, int type) {
//			RedisHistory history = new RedisHistory();
//			history.setDB(db);
//			history.setID(id);
//			history.setTYPE(type);
//			addHistory(history);
//		}
		/// history -------------------------------------------////

		
		
		
		/// inbox/offlineMsg ---------------------------------------------\\\\
		public void putOfflineMsg(Map<Object, Object> ele)
		{
			/*
			String increment = (String)ele.get("ID");
			redisTemplate.opsForHash().putAll(getINBOX_Name((String)ele.get(INBOX_RECEIVER)
																   , increment)
												   , ele);
			
			RedisHistory history = new RedisHistory(INBOX_PREFIX.substring(0, INBOX_PREFIX.length()-1)
														  , increment
														  , RedisHistory.RedisHistoryType.INSERT);
			redisTemplate.opsForValue().set(HISTORY, JsonUtil.toJson(history));
			*/
		}
		
		public Map<Object, Object> getOfflineMsg(int msgId)
		{
			//return redisTemplate.opsForHash().entries(INBOX_PREFIX+msgId);
			return null;
		}
		
		public TreeSet<String> getSortedMailStatusIdsByAvatarName(String strAvatarName) throws Exception
		{
			return new TreeSet<String>((Set<String>)this.union(RedisKey.Types.AVATAR_INBOXIDS.getRedisKey()
															   , strAvatarName
															   , RedisKey.Types.AVATAR_SYSINBOXIDS.getRedisKey()
															   , strAvatarName));
		}
		/*
		public Result addAvatarInboxId(RedisKey redisKey
									   , String strAvatarName
									   , int nLimitSize
									   , String strMailStatusId) throws Exception
		{
			IRedisClient redisClient = this.getExecutor(redisKey.getRedisType());
			
			return this.execMulti(redisClient
								  , redisKey
								  , strAvatarName
								  , redisClient.getMultiGetMethod()
								  , redisClient.getMultiCheckSizeMethod()
								  , redisClient.getMultiAddMethod()
								  , null
								  , strMailStatusId
								  , nLimitSize);
		}
		*/
		
		public boolean removeAvatarInboxId(RedisKey redisKey
										   , String strAvatarName
										   , String strMailStatusId) throws Exception
		{
//			IRedisClient redisClient = this.getExecutor(type.getRedisType());
//			
//			return this.execMulti(redisClient
//								  , type
//								  , strAvatarName
//								  , null
//								  , null
//								  , redisClient.getMultiRemoveMethod()
//								  , null
//								  , strMailStatusId
//								  , null);
			this.removeKeyValue(redisKey, strAvatarName, strMailStatusId);
			return true;
		}
		
//		public TreeSet<String> getAvatarInboxIds(RedisOperations operations
//												 , TreeSet<String> setInboxIds
//												 , int nLimitSize
//												 , String strKey
//												 , String strValue) throws Exception
//		{
//			return this.getSortedMailStatusIdsByAvatarName(strKey);
//		}
//		
//		public boolean checkAvatarInboxIds(RedisOperations operations
//										   , TreeSet<String> setInboxIds
//										   , int nLimitSize
//										   , String strKey
//										   , String strValue)
//		{
//			return (setInboxIds.size() < nLimitSize);
//		}
//		
//		public void setAvatarInboxIds(RedisOperations operations
//									  , TreeSet<String> setInboxIds
//									  , int nLimitSize
//									  , String strKey
//									  , String strValue)
//		{
//			setInboxIds.add(strValue);
//			
//			operations.opsForValue().set(strKey, strValue);
//		}
		
//		public List<Long> getMailStatusIdsByAvatarName(String strAvatarName) throws Exception
//		{
//			String strMailIds = this.getValue(AVATAR_INBOXIDS.MAIL_IDS, strAvatarName);
//			
//			if (strMailIds == null)
//			{
//				log.warn("strMailIds is null! avatar name is " + strAvatarName);
//				return new ArrayList<Long>();
//			}
//			
//			return (List<Long>)JsonUtil.fromJson(strMailIds, new TypeToken<ArrayList<Long>>(){}.getType());
//		}
		
//		public void setMailIds(String strAvatarName, Set<Long> setMailIds) throws Exception
//		{
//			this.setKeyValue(Types.AVATAR_INBOXIDS, strAvatarName, null, JsonUtil.toJson(setMailIds));
//		}
		
		/*
		public TreeSet<Long> getMailsByAvatarName(String strAvatarName) throws Exception
		{
			return this.getMailIdsByAvatarName(strAvatarName);
		}
		*/
		
//		public Map<String, Object> getMailByAvatarNameMailId(String strKey) throws Exception
//		{
//			return this.getMailByAvatarNameMailId(strKey);
//		}
		
		public Map<String, Object> getMailByAvatarNameMailId(String strKey) throws Exception
		{
			return (Map<String, Object>) this.getValues(RedisKey.Types.INBOX.getRedisKey()
			 										  	, strKey);
		}
		
//		public long getInboxCount(AttrClass attrClassCount) throws Exception
//		{
//			String strInboxCount = this.getValue(attrClassCount, "");
//			if (strInboxCount == null)
//			{
//				log.warn("RedisClient getInboxCount Inbox Count is null! AttrClass is " + attrClassCount.name());
//				return 0;
//			}
//			
//			return Long.parseLong(strInboxCount);
//		}
		

		
		public void setMail(String strInboxKey
							, Map<String, Object> mapHash) throws Exception
		{
			this.setKeyValues(RedisKey.Types.INBOX.getRedisKey()
							  , strInboxKey
							  , mapHash);
		}
		
//		public void addMail(String strReceiverAvatarName
//							, Inbox mail) throws Exception
//		{
//			this.setMail(strReceiverAvatarName, mail);
			
//			if (bNeedInterServerConfirm) return;
//
//			this.setMailIds(strReceiverAvatarName
//							, this.getMailStatusId(mail.getSTATUS()
//												   , mail.getID())
//						    , true);
//		}
		
		public boolean updateMail(RedisField redisField
								  , String strKey
								  , String strValue) throws Exception
		{
			this.setKeyValue(RedisKey.Types.INBOX.getRedisKey()
							 , strKey
							 , redisField.getName()
							 , strValue);
			
			return true;
		}
		
//		public boolean updateMail(String strKey
//								  , byte bStatus) throws Exception
//		{
//			this.setKeyValue(RedisKey.Types.INBOX.getRedisKey()
//							 , strKey
//							 , STATUS.name()
//							 , bStatus + "");
//			
//			return true;
//		}
		
		public void deleteMail(String strKey) throws Exception
		{
			this.removeKeyValues(RedisKey.Types.INBOX.getRedisKey()
								 , strKey);
		}
		
//INBOX ATTACHMENT
		
//		public String getWebAttachmentId() throws Exception
//		{
//			return RedisKey.Types.INBOX_AID_WEB.getRedisKey().getPrefix()
//				   + this.incrementValue(RedisKey.Types.MAIL_SERIAL.getRedisKey(), "", AttachmentTags.WEB.name(), 1);
//		}
		
		public Map<byte[], byte[]> getInboxAttachment(String strAttachmentId)
		{
			Jedis jedis = this.jedisPool.getResource();
			Map<byte[], byte[]> mapInboxAttachment = null;
			try
			{
				mapInboxAttachment = jedis.hgetAll(strAttachmentId.getBytes());
			
				this.jedisPool.returnResource(jedis);
			}
			catch(Exception exc)
			{
				this.jedisPool.returnBrokenResource(jedis);
				log.error("error! ", exc);
			}
			
			return mapInboxAttachment;
		}
		
//		public String createNewInboxWebAttachment(Map<byte[], byte[]> mapAttachment) throws Exception
//		{
//			String strAttachmentId = this.getWebAttachmentId();
//			Jedis jedis = this.jedisPool.getResource();
//			try
//			{
//				jedis.hmset(strAttachmentId.getBytes(), mapAttachment);
//				this.jedisPool.returnResource(jedis);
//			}
//			catch(Exception exc)
//			{
//				this.jedisPool.returnBrokenResource(jedis);
//				log.error("error! ", exc);
//				strAttachmentId = null;
//			}
//			
//			return strAttachmentId;
//		}
		
//SYSINBOX
		
		public String getSysInboxIdsKey(Date date)
		{
			return DateUtil.getDayStr(date);
		}
		
		public Map<Object, Object> getAllSysInboxIds() throws Exception
		{
			return (Map<Object, Object>)this.getValues(RedisKey.Types.SYSINBOXIDS.getRedisKey(), "");
		}
		
		public Set<String> getSysInboxIdsByDate(Date date) throws Exception
		{
			return this.getSysInboxIdsByKey(this.getSysInboxIdsKey(date));
		}
		
		public Set<String> getSysInboxIdsByJson(String strJson)
		{
			return (Set<String>)JsonUtil.fromJson(strJson, new TypeToken<HashSet<String>>(){}.getType());
		}
		
		public Set<String> getSysInboxIdsByKey(String strKey) throws Exception
		{
			String strJson = (String)this.getValue(SysInboxIdsAttrs.DATE_MAIL_IDS, "", strKey);
			return this.getSysInboxIdsByJson(strJson);
		}
		
		public void addSysInboxId(long lMailId) throws Exception
		{
			String strKey = this.getSysInboxIdsKey(Calendar.getInstance().getTime());
			Set<String> setDateMailIds;
			if ((setDateMailIds = this.getSysInboxIdsByKey(strKey)) == null) setDateMailIds = new HashSet<String>();
			
			setDateMailIds.add(lMailId + "");
			
			this.setKeyValue(RedisKey.Types.SYSINBOXIDS.getRedisKey(), "", strKey, JsonUtil.toJson(setDateMailIds));
		}
		
		public void deleteSysInboxIds(String strDate) throws Exception
		{
			this.removeKeyValue(RedisKey.Types.SYSINBOXIDS.getRedisKey(), "", strDate);
		}
				
//		public void deleteInbox(Integer key)
//		{
//			redisTemplate.delete(INBOX_PREFIX+key);		
//			
//			RedisHistory history = new RedisHistory(INBOX_PREFIX.substring(0, INBOX_PREFIX.length()-1), key+"", DELETE);
//			redisTemplate.opsForValue().set(HISTORY, JsonUtil.toJson(history));
//		}

		/// counter --------------------------------------------\\\\
		
		public boolean isAvatarSelected(Map<Object, Object> mapAvatarProperties) throws Exception
		{
			return true;
			//return (mapAvatarProperties.get(AVATAR_SELECTED).equals(AVATAR_SELECTED_VALUE));
			
			/*
			RedisDataType redisDataType = RedisDataTypes.get(1);
			String selectedValue;
			if ((selectedValue = this.getValue(redisDataType, strAvatarId, 106)) == null) return false;
			
			return selectedValue == "";
			*/
		}
		
//		public void addTestAvatarNameId(String strAvatarName, String strAvatarId) throws Exception
//		{
//			this.setKeyValue(Types.AVATAR_MAP, "", strAvatarName, strAvatarId);
//		}
//		
//		public void removeTestAvatarNameId(String strAvatarName) throws Exception
//		{
//			this.removeKeyValue(Types.AVATAR_MAP, "", strAvatarName);
//		}
//		
//		public String getAvatarNameById(String strAvatarId) throws Exception
//		{
//			return this.getValue(AVATAR.NAME, strAvatarId);
//		}
//		
//		public String getAvatarNameByUserId(int nUserId) throws Exception
//		{
//			Map<Object, Object> mapAvatarProperties = (Map<Object, Object>)this.getSelectedAvatarByUserId(nUserId);
//			
//			if (mapAvatarProperties == null)
//			{
//				log.error("error! mapAvatarProperties is null! user id is " + nUserId);
//				return null;
//			}
//			
//			return (String)mapAvatarProperties.get(AVATAR.NAME.name());
//		}
//		
//		public Map<Object, Object> getSelectedAvatarByUserId(Integer nUserId) throws Exception
//		{
//			//log.info("RedisClient getSelectedAvatarByUserId User Id is " + nUserId);
//			
//			Set<String> strAvatarIds = (Set<String>)this.getValues(Types.MEMBER_AVATAR, nUserId + "");
//			
//			//BoundSetOperations<String, String> boundSetAvatarIds = redisTemplate.boundSetOps(MEMBER_AVATAR_PREFIX+key);
//			//Set<String> strAvatarIds = boundSetAvatarIds.members();
//			/*fortest*/
//			//log.info("RedisClient getSelectedAvatarByUserId members size : " + strAvatarIds.size());
//			Map<Object, Object> mapAvatarProperties = null;
//			for(String strAvatarId : strAvatarIds)
//			{
//				mapAvatarProperties = this.getSelectedAvatarByAvatarId(strAvatarId);
//				if (mapAvatarProperties != null) break;
//				//return null;
//				
//				/*
//				UserAvatarAttribute userAvatarAttribute = new UserAvatarAttribute(avatar.get(AVATAR_NAME));
//				userAvatarAttribute.setAvatarName();
//				*/
//				//attributes.put(Integer.parseInt(strAvatarId), new UserAvatarAttribute());
//			}
//			
//			return mapAvatarProperties;
//	/*		
//	 		Map<Object, Object> members = redisTemplate.opsForHash().entries(MEMBER_AVATAR_PREFIX+key);
//			for (Object obj: members.keySet())
//			{
//				Integer avatarId = Integer.parseInt((String)obj);
//				String attributeString = (String)members.get(obj);RedisHistoryType
//				attributes.put(avatarId, (UserAvatarAttribute)JsonUtil.fromJson(attributeString, UserAvatarAttribute.class));
//			}
//	*/
//			//return attributes;
//		}
//		
//		public Map<Object, Object> getSelectedAvatarByAvatarId(String strAvatarId) throws Exception
//		{
//			Map<Object, Object> hMapAvatar = null;
//			
//			//check Avatar in Redis
//			if ((hMapAvatar = ((Map<Object, Object>)this.getValues(Types.AVATAR, strAvatarId))) == null
//				|| hMapAvatar.size() == 0
//				|| !isAvatarSelected(hMapAvatar)) return null;
//				
//			return hMapAvatar;
//		}
//		
////		public String getPropertyValueById(Map<Object, Object> mapProperties, AttrClass attrClass) throws Exception
////		{
////			return (String)mapProperties.get(attrClass.name());
////		}
		
		public String getSelectedAvatarNameByUserId(int nUserId, RedisField rfAvatarName) throws Exception
		{
			Set<String> setAvatarIds
			= (Set<String>)this.getAllValues(RedisKey.Types.MEMBER_AVATAR.getRedisKey(), RedisKey.Types.MEMBER_AVATAR.getRedisKey().getPrefix() + nUserId);
			
			if (setAvatarIds == null
				|| setAvatarIds.size() <= 0)
			{
				log.error("avatar ids are null or empty! user id is " + nUserId);
				return null;
			}
				
			//just select first
			String strAvatarId = setAvatarIds.iterator().next();
			
			return (String)this.getValue(rfAvatarName
										 , rfAvatarName.getRedisKey().getPrefix() + strAvatarId
										 , rfAvatarName.getName());
		}
		
		private String getBytesString(RedisData.AccessTypes accessType
				   , RedisTypes redisType
				   , String strKey
				   , RedisField redisField
				   , Object objValue)
		{
			//refObj.set(new String((byte[])refObj.get()));
			return new String((byte[])objValue);
		}
		
		private byte[] unCompressValue(RedisData.AccessTypes accessType
							, RedisTypes redisType
							, String strKey
							, RedisField redisField
							, Object objValue)
		{
			//refObj.set(ZlibUtil.uncompress((byte[])refObj.get()));
			return ZlibUtil.uncompress((byte[])objValue);
		}
		
		private Object getBytesValueFromRedis(RedisData.AccessTypes accessType
									, RedisTypes redisType
									, String strKey
									, RedisField redisField
									, Object objValue) throws Exception
		{
			//refObj.set(this.getExecutor(redisType).get(strKey, redisField.getName()));
			return this.getExecutor(redisType).get(strKey, redisField.getName());
		}

		public Object getValue(RedisField redisField, String strKey, String strFieldName) throws Exception
		{
			Object objValue = this.getExecutor(redisField.getRedisKey().getRedisType())
				   			  	  .get(strKey, strFieldName);
			
			if (objValue == null) return null;
			
			return this.getValueByRedisField(redisField, objValue);
		}
		
		private Object getValueByRedisField(RedisField redisField, Object objValue) throws Exception
		{
			for(DataAccessUtil.SolveSteps solveStep : redisField.getSolveSteps())
			{
				objValue = solveStep.getMethod().invoke(this
														, redisField.getAccessType()
														, redisField.getRedisKey().getRedisType()
														, redisField.getRedisKey().getName()
														, redisField
														, objValue);
			}
			
			return objValue;
		}
		
		public Object getAllValues(RedisKey redisKey, String strKey) throws Exception
		{
			Object objValues = this.getValues(redisKey, strKey);
			
			if (redisKey.getRedisFields().size() <= 0) return objValues;
			
			Object objResult = null;
			
			String strFieldName;
			RedisField redisField = redisKey.getRedisFields().values().iterator().next();
			switch(redisKey.getRedisType())
			{
				case hash:
				case zset:
					
					Map<byte[], byte[]> mapBytesValues = (Map<byte[], byte[]>)objValues;
					Map<String, Object> mapValues = new HashMap<String, Object>();
					
					Object objValue;
					for(byte[] bsField : mapBytesValues.keySet())
					{
						
						strFieldName = CommonUtil.getStr(bsField);
						if (redisKey.isCustomField()) redisField = redisKey.getRedisFieldByName(strFieldName);
						if (redisField == null) continue; // is this necessary???
						
						objValue = mapBytesValues.get(bsField);

						mapValues.put(strFieldName, this.getValueByRedisField(redisField, objValue));
					}
					
					objResult = mapValues;
					
					break;
					
				case set:
					
					Set<byte[]> setBytesValues = (Set<byte[]>)objValues;
					Set setValues = new HashSet();
					
					for(byte[] bsField : setBytesValues)
					{
						setValues.add(this.getValueByRedisField(redisField, bsField));
					}
					
					objResult = setValues;
					
					break;
					
				case string:
					
					objResult = this.getValueByRedisField(redisField, objValues);
					
					break;
					
				case list:
					
					break;
					
				default:
					
					log.error("getAllValues error! redis type is " + redisKey.getRedisType().name());
					break;
			}
			
			return objResult;
		}
		
		//Avatar_Count
		public int getAvatarCount() throws Exception
		{
			return Integer.parseInt((String)this.getValue(AvatarCountAttrs.COUNT.getRedisField()
														  , AvatarCountAttrs.COUNT.getRedisField().getRedisKey().getPrefix()
														  , ""));
		}
				
		//Avatar_Map
		public String getAvatarIdByName(String strAvatarName) throws Exception
		{
			return (String)this.getValue(AvatarMapAttrs.AvatarName.getRedisField()
								 		 , AvatarMapAttrs.AvatarName.getRedisField().getRedisKey().getPrefix()
								 		 , strAvatarName);
		}
		
		public Map<Object, Object> getAllAvatarNameIds() throws Exception
		{
			return (Map<Object, Object>)this.getValues(RedisKey.Types.AVATAR_MAP.getRedisKey(), "");
		}
		
		//Avatar
		public Map<Object, Object> getAvatarDataById(String strAvatarId) throws Exception
		{
			Map<Object, Object> mapAvatarProperties = (Map<Object, Object>)this.getValues(RedisKey.Types.AVATAR.getRedisKey(), strAvatarId);
			return mapAvatarProperties;
		}
		
		public Map<Object, Object> getAllAvatarDataById(String strAvatarId) throws Exception
		{
			try
			{
				Map<Object, Object> mapAvatarProperties = this.getAvatarDataById(strAvatarId);
				
				String strAvatarProperties;
				
				return mapAvatarProperties;
			}
			catch(Exception exc)
			{
				log.error("RedisClient getAvatarPropertiesById Error! Avatar Id is " + strAvatarId);
				throw exc;
			}
		}
		
		public long addInboxCount(RedisField redisFieldInboxCount
			   	  				  , long lInboxMaxRange) throws Exception
		{
			Long lNewInboxCount = this.incrementValue(redisFieldInboxCount.getRedisKey().getRedisType()
													  , redisFieldInboxCount.getRedisKey().getPrefix()
													  , ""
													  , (long)1);
			if (lNewInboxCount > lInboxMaxRange) throw new Exception("new inbox count is out of range!!!" + lNewInboxCount
																 	 + ", redis Field is " + redisFieldInboxCount.getName());
			return lNewInboxCount;
		}
		
//		public synchronized boolean setAvatarPropertyById(String strAvatarId
//														  , AttrClass attrClass
//														  , Object objValue) throws Exception
//		{
//			String strProperties = this.getValue(AVATAR.PROPERTIES, strAvatarId);
//			JsonObject jsonObj = JsonUtil.getJsonObject(strProperties);
//			
//			JsonObject jsonObjDynamic = jsonObj.get(AVATAR.dynamic.name()).getAsJsonObject();
//			JsonElement jsonElement = jsonObjDynamic.get(attrClass.name());
//			if (jsonElement == null)
//			{
//				log.error("get avatar data error! avatar id is " + strAvatarId
//						  + ", attrclass name is " + attrClass.name());
//				//return false;
//			}
//			
//			if (AVATAR.dynamic.getDataType() == String.class)
//			{
//				jsonObjDynamic.addProperty(attrClass.name(), objValue.toString());
//			}
//			else
//			{
//				jsonObjDynamic.addProperty(attrClass.name(), (Number)objValue);
//			}
//			
//			this.setKeyValue(Types.AVATAR, strAvatarId, AVATAR.PROPERTIES.name(), jsonObj.toString());
//			
//			return true;
//		}
//		
//		public synchronized boolean accessAvatarMoneyById(String strAvatarId
//														  , AttrClass attrClass
//														  , int nMoney
//														  , boolean bIsPlus) throws Exception
//		{
//			String strProperties = this.getValue(AVATAR.PROPERTIES, strAvatarId);
//			JsonObject jsonObj = JsonUtil.getJsonObject(strProperties);
//			
//			if (AVATAR.dynamic.getDataType() != null)
//			{
//				JsonObject jsonObjDynamic = jsonObj.get(AVATAR.dynamic.name()).getAsJsonObject();
//				
//				JsonElement jsonElement = jsonObjDynamic.get(attrClass.name());
//				if (jsonElement == null)
//				{
//					log.error("get avatar data error! avatar id is " + strAvatarId
//							  + ", attrclass name is " + attrClass.name());
//					return false;
//				}
//				
//				int nAvatarMoney = Integer.parseInt(jsonElement.getAsString());
//				if (bIsPlus)
//				{
//					nAvatarMoney = nAvatarMoney + (Integer)nMoney;
//				}
//				else if (nAvatarMoney >= nMoney)
//				{
//					nAvatarMoney = nAvatarMoney - nMoney;
//				}
//				else
//				{
//					log.debug("not enough money! avatar money is " + nAvatarMoney
//							  + ", access money is " + nMoney);
//					return false;
//				}
//				
//				jsonObjDynamic.addProperty(attrClass.name(), nAvatarMoney);
//				this.setKeyValue(Types.AVATAR, strAvatarId, AVATAR.PROPERTIES.name(), jsonObj.toString());
//				
//				return true;
//			}
//			
//			return false;
//		}
		
//		public void setAvatarGuildId(String strAvatarId, String strGuildId) throws Exception
//		{
//			this.setKeyValue(Types.AVATAR, strAvatarId, AVATAR.GUILDID.name(), strGuildId);
//		}
//		
//		/*
//		public Map<Object, Object> getAvatarPropertiesById(String strAvatarId) throws Exception
//		{
//			Map<Object, Object> hMapAvatar = null;
//				
//			if ((hMapAvatar = ((Map<Object, Object>)this.getValuesById(1, strAvatarId))).size() == 0) return null;
//				
//			return hMapAvatar;
//		}
//		*/
//		/*
//		public boolean isGuildNameExisted(String strGuildName) throws Exception
//		{
//			
//			return this.getValue(RedisDataTypes.get(9)
//									, ""
//									, strGuildName
//									, 900) != null;
//		}
//		*/
//		
//		public boolean isGuildExisted(String strGuildName) throws Exception
//		{
//			return this.getValue(GUILD.NAME
//								 , strGuildName) != null;
//		}
//		
//		/*
//		public String getGuildIdByName(String strGuildName) throws Exception
//		{
//			return this.getValue(RedisDataTypes.get(9)
//									, ""
//									, strGuildName
//									, 901);
//		}
//		*/
//		
//		public String getGuildNameById(String strGuildId) throws Exception
//		{
//			return (String)this.getValue(GUILD_MAP.GUILD_ID
//								 		 , ""
//								 		 , strGuildId);
//		}
//		
//		public int getGuildCount() throws Exception
//		{
//			String strNewGuildId = this.getValue(GUILD_COUNT.COUNT, "");
//			return Integer.parseInt(strNewGuildId);
//		}
//		
//		public synchronized int addGuildCount() throws Exception
//		{
//			int nNewGuildCount = this.getGuildCount() + 1;
//			this.setKeyValue(Types.GUILD_COUNT, "", null, nNewGuildCount + "");
//			return nNewGuildCount;
//		}
//		
//		public void createGuild(Map<String, Object> mapGuildProperties) throws Exception
//		{
//			String strGuildId = (String)mapGuildProperties.get(GUILD.ID.name());
//			String strGuildName = (String)mapGuildProperties.get(GUILD.NAME.name());
//			
//			this.setKeyValue(Types.GUILD_MAP, "", strGuildId, strGuildName);
//			
//			//this.setKeyValue(Types.GUILD_INBOX, strGuildName, "", "");
//			
//			this.setKeyValues(Types.GUILD, strGuildName, mapGuildProperties);
//		}
//		
//		public Map<Object, Object> getAllGuildIdNames() throws Exception
//		{
//			return (Map<Object, Object>)this.getValues(Types.GUILD_MAP, "");
//		}
//		
//		public void updateGuild(String strGuildName, Map<String, Object> mapGuildProperties) throws Exception
//		{
//			this.setKeyValues(Types.GUILD, strGuildName, mapGuildProperties);
//		}
//		
//		public void updateGuildProperty(String strGuildName
//										, String strPropertyName
//										, String strPropertyValue) throws Exception
//		{
//			AttrClass attrClass;
//			try
//			{
//				attrClass = GUILD.valueOf(strPropertyName);
//			}
//			catch(Exception exc)
//			{
//				throw new Exception("RedisClient updateGuildProperty error! property name is not exist! property name is " + strPropertyName);
//			}
//			
//			if (attrClass.getBelong() != null)
//			{
//				String strProperties = this.getValue(attrClass.getBelong(), strGuildName);
//			
//				Map<String, String> mapProperties 
//				= (Map<String, String>)JsonUtil.fromJson(strProperties
//														 , new TypeToken<HashMap<String, String>>(){}.getType());
//			
//				mapProperties.put(strPropertyName, strPropertyValue);
//			
//				strPropertyName = attrClass.getBelong().name();
//				strPropertyValue = JsonUtil.toJson(mapProperties);
//			}
//			
//			this.setKeyValue(Types.GUILD
//							 , strGuildName
//							 , strPropertyName
//							 , strPropertyValue);
//		}
//		
//		/*
//		public String createGuild(String strGuildName
//								  , String strAvatarId
//								  , String strAvatarName) throws Exception
//		{
//			//Get Guild_Count
//			String strNewGuildId = this.getValue(GUILD_COUNT.COUNT, "");
//			int nNewGuildId = Integer.parseInt(strNewGuildId) + 1;
//			strNewGuildId = nNewGuildId + "";
//			
//			//Guild
//			Map<Object, Object> mapGuildProperties = new HashMap<Object, Object>();
//			mapGuildProperties.put(GUILD.DECLARATION.name(), "Welcome to join us!");
//			mapGuildProperties.put(GUILD.BULLETIN.name(), "Bulletin");
//			mapGuildProperties.put(GUILD.FLAG.name(), "1");
//			String strJson = JsonUtil.toJson(mapGuildProperties);
//			
//			mapGuildProperties.clear();
//			
//			mapGuildProperties.put(GUILD.ID.name(), nNewGuildId + "");
//			mapGuildProperties.put(GUILD.NAME.name(), strGuildName);
//			mapGuildProperties.put(GUILD.LEVEL.name()	, "1");
//			mapGuildProperties.put(GUILD.COUNT.name(), "1");
//			mapGuildProperties.put(GUILD.STATUS.name(), "1");
//			mapGuildProperties.put(GUILD.MONEY.name(), "0");
//			mapGuildProperties.put(GUILD.EXPLOIT.name(), "0");
//			mapGuildProperties.put(GUILD.CREATED_DATE.name(), Calendar.getInstance().getTime().toString());
//			mapGuildProperties.put(GUILD.PROPERTIES.name(), strJson);
//			
//			this.setKeyValues(Types.GUILD
//							  , strGuildName
//							  , mapGuildProperties);
//			
//			//Guild History
//			
//			//RedisHistory redisHistory = new RedisHistory(RedisDataTypes.get(7).getName()
//			//											 , strNewGuildId
//			//											 , RedisHistory.RedisHistoryType.INSERT);
//			
//			//this.setKeyValue(RedisDataTypes.get(27), "", "", JsonUtil.toJson(redisHistory));
//			
//			//Guild_Map
//			this.setKeyValue(Types.GUILD_MAP, "", strNewGuildId, strGuildName);
//			
//			//Guild_Count
//			this.setKeyValue(Types.GUILD_COUNT, "", "", strNewGuildId);
//			
//			return strGuildName;
//		}
//		*/
//		
//		/*
//		public void addNewGuildMember(int nGuildId
//										  , String strGuildName
//				  						  , int nAvatarId
//				  						  , String strAvatarName
//				  						  , String strStatus
//				  						  , String strDonate
//				  						  , byte bNewMemberPCode
//				  						  , byte bStatus) throws Exception
//		{
//			this.addNewGuildMember(nGuildId
//								   , strGuildName
//								   , nAvatarId + ""
//								   , strAvatarName
//								   , strStatus
//								   , strDonate
//								   , bNewMemberPCode
//								   , bStatus);
//		}
//
//		
//		public void addNewGuildMember(int nGuildId
//										  , String strGuildName
//										  , String strAvatarId
//										  , String strAvatarName
//										  , String strStatus
//										  , byte bNewMemberPCode
//										  , byte bStatus
//										  , int nDonate) throws Exception
//		{
//			String strGuildId = nGuildId + "";
//			this.addNewGuildMember(strGuildId
//								   , strGuildName
//								   , strAvatarId
//								   , strAvatarName
//								   , bNewMemberPCode
//								   , bStatus
//								   , nDonate);
//		}
//		*/
//		
//		public void addNewGuildMember(String strGuildName
//									  , String strAvatarName
//									  , Map<String, Object> mapGuildMemberProperties) throws Exception
//		{
//			//Guild_Member
//			String strJson = JsonUtil.toJson(mapGuildMemberProperties);
//			this.setKeyValue(Types.GUILD_MEMBER, strGuildName, strAvatarName, strJson);
//		}
//		/*
//		public void addNewGuildMember(String strGuildId
//									  , String strGuildName
//									  , String strAvatarId
//									  , String strAvatarName
//									  , int nGuildAvatarsCount
//									  , byte bNewMemberPosition
//									  , byte bStatus
//									  , int nDonate) throws Exception
//		{
//			this.addGuildMember(strGuildId
//								, strGuildName
//								, strAvatarId
//								, strAvatarName
//								, nGuildAvatarsCount + ""
//								, bStatus + ""
//								, nDonate + ""
//								, bNewMemberPosition);
//			*/
//			//Guild_New_Member
//			/*
//			this.setKeyValue(Types.GUILD_NEW_MEMBER
//							 , ""
//							 , strAvatarId
//							 , strGuildId);
//			*/
//		//}
//		
//		/*
//		public void addGuildMember(String strGuildId
//								   , String strGuildName
//								   , String strAvatarId
//								   , String strAvatarName
//								   , String strGuildAvatarsCount
//								   , String strStatus
//								   , String strDonate
//								   , byte bPosition) throws Exception
//		{
//			String strPositionCode = bPosition + "";
//			
//			//Guild_Member
//			Map<Object, Object> mapGuildMemberProperties = new HashMap<Object, Object>();
//
//			mapGuildMemberProperties.put(GUILD_MEMBER.POSITION.name(), strPositionCode);
//			mapGuildMemberProperties.put(GUILD_MEMBER.STATUS.name(), strStatus);
//			mapGuildMemberProperties.put(GUILD_MEMBER.STATUS.DONATE.name(), strDonate);
//			
//			String strJson = JsonUtil.toJson(mapGuildMemberProperties);
//			
//			this.setKeyValue(Types.GUILD_MEMBER, strGuildName, strAvatarName, strJson);
//			
//			//Avatar's Guild Id
//			this.setKeyValue(Types.AVATAR
//							 , strAvatarId
//							 , AVATAR.GUILDID.name()
//							 , strGuildId);
//			
//			//Guild_Positions
//			HashSet setAvatarNames = new HashSet();
//			setAvatarNames.add(strAvatarName);
//			this.setKeyValue(Types.GUILD_POSITIONS
//							 , strGuildName
//							 , strPositionCode
//							 , JsonUtil.toJson(setAvatarNames));
//			
//			//Guild Avatars Count
//			this.setKeyValue(Types.GUILD
//							 , strGuildName
//							 , GUILD.COUNT.name()
//							 , strGuildAvatarsCount);
//			*/
//			//Guild_Member History
//			/*
//			GuildMemberHistory guildMemberHistory = new GuildMemberHistory(RedisDataTypes.get(11).getName()
//																		   , strGuildName
//																		   , strAvatarId
//																		   , RedisHistory.RedisHistoryType.INSERT);
//			
//			this.setKeyValue(RedisDataTypes.get(27), "", "", JsonUtil.toJson(guildMemberHistory));
//			*/
//		//}
//		
//		/*
//		public void getAllGuildMemberNames(int nGuildId
//										   , String strPrefix
//										   , HashSet<String> hSetGuildMemberNames) throws Exception
//		{
//			this.getAllGuildMemberNames(nGuildId + "", strPrefix, hSetGuildMemberNames);
//		}
//		
//		public void getAllGuildMemberNames(String strGuildId
//										   , String strAvatarCacheKey
//										   , HashSet<String> hSetGuildMemberNames) throws Exception
//		{
//			Map<Object, Object> mapGuildMember = (Map<Object, Object>)this.getValuesById(11, strGuildId);
//			
//			for(Object obj : mapGuildMember.keySet())
//			{
//				hSetGuildMemberNames.add(strPrefix + this.getAvatarNameById((String)obj));
//			}
//		}
//		*/
//		
//		public Set<Object> getGuildMemberNames(String strGuildName) throws Exception
//		{
//			return this.getKeys(Types.GUILD_MEMBER, strGuildName);
//		}
//		
//		public String getGuildMemberByName(String strGuildName, String strAvatarName) throws Exception
//		{
//			return (String)this.getValue(GUILD_MEMBER.GUILD_MEMBER_VALUE
//										 , strGuildName
//										 , strAvatarName);
//		}
//		
//		public Map<Object, Object> getGuildMembers(String strGuildName) throws Exception
//		{
//			return (Map<Object, Object>)this.getValues(Types.GUILD_MEMBER, strGuildName);
//		}
//		
//		public Map<Object, Object> getGuildMemberProperties(int nGuildId, int nAvatarId) throws Exception
//		{
//			String strGuildMemberProperties = (String)this.getValue(GUILD_MEMBER.GUILD_MEMBER_VALUE
//																	, nGuildId + ""
//																	, nAvatarId + "");
//			
//			if (strGuildMemberProperties == null) return null;
//			
//			return (Map<Object, Object>)JsonUtil.fromJson(strGuildMemberProperties
//														  , new TypeToken<HashMap<Object, Object>>(){}.getType());
//		}
//		
//		public void setGuildMember(String strGuildName
//								   , String strAvatarName
//								   , Map<String, Object> mapGuildMemberProperties) throws Exception 
//		{
//			this.setKeyValue(Types.GUILD_MEMBER, strGuildName, strAvatarName, JsonUtil.toJson(mapGuildMemberProperties));
//		}
//		
//		public Map<Object, Object> getGuildPositions(String strGuildName) throws Exception
//		{
//			return (Map<Object, Object>)this.getValues(Types.GUILD_POSITIONS, strGuildName);
//		}
//		
//		public Set<String> getGuildPositionAvatarIdsById(String strGuildName, String strPositionCode) throws Exception
//		{
//			String strAvatarNames = (String)this.getValue(GUILD_POSITIONS.AVATAR_NAMES
//														  , strGuildName
//														  , strPositionCode);
//			if (strAvatarNames == null) return null;
//			
//			return (Set<String>)JsonUtil.fromJson(strAvatarNames, new TypeToken<Set<String>>(){}.getType());
//		}
//		
//		public void setGuildPositions(String strGuildName, String strPositionCode, Set<String> setAvatarNames) throws Exception
//		{
//			this.setKeyValue(Types.GUILD_POSITIONS, strGuildName, strPositionCode, JsonUtil.toJson(setAvatarNames));
//		}
//		
//		public Set<String> getGuildMemberStatus(String strGuildName) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GUILD_MEMBER_STATUS, strGuildName);
//		}
//		
//		public void addGuildMemberStatus(String strGuildName, String strTimeAvatarNameStatus) throws Exception
//		{
//			this.setKeyValue(Types.GUILD_MEMBER_STATUS
//							 , strGuildName
//							 , null
//							 , strTimeAvatarNameStatus);
//		}
//		
//		/*
//		public void removeGuildMemberStatus(String strTimeString) throws Exception
//		{
//			Set<String> setGuildMemberStatus = this.getGuildMemberStatus(strGuildName, bStatus);
//						
//			setGuildMemberStatus.remove(strAvatarId);
//													 
//			this.setKeyValue(RedisDataTypes.get(15), strGuildName, bStatus + "", JsonUtil.toJson(setGuildMemberStatus));
//		}
//		*/
//		
//		public String getTimeGuildNameStatus(String strTime, String strGuildName, String strStatus)
//		{
//			return strTime + this.strSplitSymbol
//				   + strGuildName + this.strSplitSymbol
//				   + strStatus;
//		}
//		
//		public String getTimeAvatarNameStatus(String strTime, String strAvatarName, String strStatus)
//		{
//			return strTime + this.strSplitSymbol
//				   + strAvatarName + this.strSplitSymbol
//				   + strStatus;
//		}
//		
//		public Set<String> getTimeGuildNameStatus(String strAvatarName) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.AVATAR_GUILD_RELATIONS, strAvatarName);
//		}
//		
//		public Set<String> getTimeAvatarNameStatus(String strGuildName) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GUILD_AVATAR_RELATIONS, strGuildName);
//		}
//		
//		public void addAvatarGuildRelations(String strAvatarName, String strTimeGuildNameStatus) throws Exception
//		{
//			this.setKeyValue(Types.AVATAR_GUILD_RELATIONS
//					 		 , strAvatarName
//					 		 , null
//					 		 , strTimeGuildNameStatus);
//		}
//		
//		public void addGuildAvatarRelations(String strGuildName, String strTimeAvatarNameStatus) throws Exception
//		{
//			this.setKeyValue(Types.GUILD_AVATAR_RELATIONS
//							 , strGuildName
//							 , null
//							 , strTimeAvatarNameStatus);
//		}
//		
//		public void removeAvatarGuildRelations(String strAvatarName, String strTimeGuildNameStatus) throws Exception
//		{
//			this.removeKeyValue(Types.AVATAR_GUILD_RELATIONS, strAvatarName, strTimeGuildNameStatus);
//		}
//		
//		public void removeGuildAvatarRelations(String strGuildName, String strTimeAvatarNameStatus) throws Exception
//		{
//			this.removeKeyValue(Types.GUILD_AVATAR_RELATIONS, strGuildName, strTimeAvatarNameStatus);
//		}
//		
//		public void removeAllAvatarGuildRelations(String strAvatarName) throws Exception
//		{
//			Set<String> setAvatarGuildRelations = (Set<String>)this.getValues(Types.GUILD_AVATAR_RELATIONS, strAvatarName);
//			String[] strsTimeGuildNameStatus;
//			String strTime, strGuildName, strStatus;
//			//is necessary?
//			for(String strTimeGuildNameStatus : setAvatarGuildRelations)
//			{
//				strsTimeGuildNameStatus = strTimeGuildNameStatus.split(this.strSplitSymbol);
//				strTime = strsTimeGuildNameStatus[0];
//				strGuildName = strsTimeGuildNameStatus[1];
//				strStatus = strsTimeGuildNameStatus[2];
//				this.removeKeyValue(Types.GUILD_AVATAR_RELATIONS
//									, strGuildName
//									, this.getTimeAvatarNameStatus(strTime
//																   , strGuildName
//																   , strStatus));
//			}
//			
//			this.removeKeyValues(Types.AVATAR_GUILD_RELATIONS, strAvatarName);
//		}
//		
//		/*
//		public void removeAvatarGuildRelations(int nGuildId, int nAvatarId)
//		{
//			this.removeAvatarGuildRelations(nGuildId + "", nAvatarId + "");
//		}
//		*/
//		
//		/*
//		public void removeAvatarGuildRelations(String strGuildName
//											   , String strAvatarName
//											   , byte bStatus
//											   , boolean bRemoveAll) throws Exception
//		{
//			Set<String> setAvatarGuildRelations = this.getAvatarGuildRelations(strAvatarName, bStatus);
//			if (setAvatarGuildRelations == null)
//			{
//				log.debug("RedisClient removeAvatarGuildRelations setAvatarGuildRelations is null! guild name is "
//						   + strGuildName
//						   + ", avatar name is "
//						   + strAvatarName
//						   + ", status is "
//						   + bStatus);
//				return;
//			}
//			
//			RedisDataType redisDataType = RedisDataTypes.get(6);
//			if (bRemoveAll)
//			{
//				this.removeKeyValue(redisDataType, strAvatarName, bStatus + "");
//			}
//			else
//			{
//				setAvatarGuildRelations.remove(strGuildName);
//														 
//				this.setKeyValue(redisDataType
//								 , strAvatarName
//								 , bStatus + ""
//								 , JsonUtil.toJson(setAvatarGuildRelations));
//			}
//		}
//		*/
//		
//		public Set<String> getGuildAdverse(String strGuildName) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GUILD_ADVERSE, strGuildName);
//		}
//		
//		public void addGuildAdverse(String strGuildName1, String strGuildName2) throws Exception
//		{
//			this.setKeyValue(Types.GUILD_ADVERSE, strGuildName1, null, strGuildName2);
//			this.setKeyValue(Types.GUILD_ADVERSE, strGuildName2, null, strGuildName1);
//		}
//		
//		public void removeGuildAdverse(String strGuildName1, String strGuildName2) throws Exception
//		{
//			this.removeKeyValue(Types.GUILD_ADVERSE, strGuildName1, strGuildName2);
//			this.removeKeyValue(Types.GUILD_ADVERSE, strGuildName2, strGuildName1);
//		}
//		
//		/*
//		public void quitGuild(String strGuildName
//				 			  , int nAvatarId
//				 			  , byte bStatus) throws Exception 
//		{
//			this.quitGuild(strGuildName
//						   , nAvatarId + ""
//						   , bStatus);
//		}
//
//		public void quitGuild(String strGuildName
//								 , String strAvatarId
//								 , byte bStatus) throws Exception
//		{
//			//subtract Guild's Count
//			int nGuildCount = Integer.parseInt(this.getValue(GUILD.COUNT, strGuildName));
//			nGuildCount --;
//			this.setKeyValue(Types.GUILD, strGuildName, GUILD.COUNT.name(), nGuildCount + "");
//			
//			//set Avatar's GuildId
//			this.setKeyValue(Types.AVATAR, strAvatarId, AVATAR.GUILDID.name(), No_Guild_Id);
//		}
//		*/
//		
//		public void removeGuildMember(String strGuildName
//									  , String strAvatarName
//									  , byte bStatus) throws Exception
//		{
//			//update Guild Member's status
//			String strGuildMemberProperties = (String)this.getValue(GUILD_MEMBER.GUILD_MEMBER_VALUE
//																	, strGuildName
//																	, strAvatarName);
//			Map<String, String> mapProperties
//			= (Map<String, String>)JsonUtil.fromJson(strGuildMemberProperties
//					  							     , new TypeToken<HashMap<String, String>>(){}.getType());
//			
//			mapProperties.put(GUILD_MEMBER.STATUS.name(), bStatus + "");
//			this.setKeyValue(Types.GUILD_MEMBER, strGuildName, strAvatarName, JsonUtil.toJson(mapProperties));
//		}
//		
//		/*
//		public void destroyGuild(int nGuildId, byte bStatus) throws Exception
//		{
//			this.destroyGuild(nGuildId + "", bStatus);
//		}
//		*/
//		
//		public void destroyGuild(String strGuildId, String strGuildName, byte bStatus) throws Exception
//		{
//			//Guild
//			this.setKeyValue(Types.GUILD, strGuildName, GUILD.STATUS.name(), bStatus + "");
//			
//			//Guild_Map
//			this.removeKeyValue(Types.GUILD_MAP, "", strGuildId);
//			
//			/*
//			//Guild_Count
//			redisDataType = RedisDataTypes.get(10);
//			String strGuildCount = this.getValue(redisDataType, "", "", 1000);
//			this.setKeyValue(redisDataType, "", "", (Integer.parseInt(strGuildCount) - 1) + "");
//			*/
//		}
//		
//		/*
//		public void destroyGuildMember(String strGuildName
//									   , byte bStatus
//									   , Map<Object, Object> mapGuildMemberProperties)
//		{
//			String strProperties;
//			for(Object obj : mapGuildMemberProperties.keySet())
//			{
//				strProperties = (String)mapGuildMemberProperties.get(obj);
//				mapProperties
//				= (Map<String, String>)JsonUtil.fromJson(strProperties
//														 , new TypeToken<HashMap<String, String>>(){}.getType());
//				mapProperties.put(GUILD_MEMBER.STATUS.name(), bStatus + "");
//				mapGuildMemberProperties.put(obj, JsonUtil.toJson(mapProperties));
//			}
//			
//			this.setKeyValues(Types.GUILD_MEMBER, strGuildName, mapGuildMemberProperties);
//		}
//		*/
//		
//		/*
//		public void transferGuild(String strGuildName, byte bPosition, String strAvatarName) throws Exception
//		{
//			HashSet setAvatarNames = new HashSet();
//			setAvatarNames.add(strAvatarName);
//			this.setKeyValue(Types.GUILD_POSITIONS, strGuildName, bPosition + "", JsonUtil.toJson(setAvatarNames));
//		}
//		*/
//		
//		public Set<String> getGuildEvents(String strGuildName) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GUILD_EVENTS, strGuildName);
//		}
//		
//		public void logGuildEvent(String strGuildName, String strName, String strStatus) throws Exception
//		{
//			this.setKeyValue(Types.GUILD_EVENTS
//							 , strGuildName
//							 , null
//							 , Calendar.getInstance().getTimeInMillis() + this.strSplitSymbol
//							   + strName + this.strSplitSymbol 
//							   + strStatus);
//		}
//		
//		protected String getRelationKey(String strAvatarName, short sCode)
//		{
//			return strAvatarName + this.strSplitSymbol + sCode;
//		}
//		
//		protected String getRelationKey(String strAvatarName, short sCode, String strInfo)
//		{
//			return this.getRelationKey(strAvatarName, sCode) + this.strSplitSymbol + strInfo;
//		}
//		/*
//		private String getRefRelationKey(String strAvatarName, short sCode)
//		{
//			return strAvatarName + this.strSplitSymbol + "ref" + this.strSplitSymbol + sCode;
//		}
//		*/
//		
//		public Set<String> getRelationAvatarNames(String strAvatarName, short sCode) throws Exception
//		{
//			return this.getRelationAvatarNames(this.getRelationKey(strAvatarName, sCode));
//		}
//		
//		public boolean isRelationExisted(String strAvatarName
//										 , short sCode
//										 , String strTargetAvatarName) throws Exception
//		{
//			strTargetAvatarName = (String)this.getValue(RELATION.AVATAR_NAMES
//												        , this.getRelationKey(strAvatarName, sCode)
//												        , strTargetAvatarName);
//			return (strTargetAvatarName != null);
//		}
//		
//		public Set<String> getOnlineRelationAvatarNames(String strAvatarName
//														, byte bStatus
//														, short sCode) throws Exception
//		{
//			return (Set<String>)this.getInterValues(Types.AVATAR_ONLINE
//													, bStatus + ""
//													, Types.RELATION
//													, this.getRelationKey(strAvatarName, sCode));
//		}
//		
//		/*
//		public Set<String> getAvatarRelativeRefRelation(String strAvatarName, short sCode) throws Exception
//		{
//			return this.getAvatarRelation(this.getRefRelationKey(strAvatarName, sCode));
//		}
//		*/
//		
//		protected Set<String> getRelationAvatarNames(String strRelationKey) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.RELATION, strRelationKey);
//		}
//		
//		public Result addAvatarRelation(String strAvatarNameOwner
//										, String strAvatarNameTarget
//										, short sCode
//										, int nRelationMaxCount) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(Types.RELATION.getRedisType());
//			
//			return this.execMulti(redisClient
//								  , Types.RELATION
//								  , this.getRelationKey(strAvatarNameOwner, sCode)
//								  , redisClient.getMultiGetMethod()
//								  , redisClient.getMultiCheckSizeMethod()
//								  , redisClient.getMultiAddMethod()
//								  , null
//								  , strAvatarNameTarget
//								  , nRelationMaxCount);
//			
////			this.setKeyValue(Types.RELATION
////							 , this.getRelationKey(strAvatarNameOwner, sCode)
////							 , null
////							 , strAvatarNameTarget);
//		}
//		
//		public Result addMutualAvatarRelations(String strAvatarNameOwner
//									     	   , String strAvatarNameTarget
//									     	   , short sCode
//									     	   , int nRelationMaxCount) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(Types.RELATION.getRedisType());
//			
//			return this.execMulti(redisClient
//								  , Types.RELATION
//								  , this.getRelationKey(strAvatarNameOwner, sCode)
//								  , this.getRelationKey(strAvatarNameTarget, sCode)
//								  , redisClient.getMultiGetMethod()
//								  , redisClient.getMultiGetMethod()
//								  , redisClient.getMultiCheckSizeMethod()
//								  , redisClient.getMultiCheckSizeMethod()
//								  , redisClient.getMultiAddMethod()
//								  , redisClient.getMultiAddMethod()
//								  , null
//								  , null
//								  , strAvatarNameTarget
//								  , strAvatarNameOwner
//								  , nRelationMaxCount
//								  , nRelationMaxCount);
//
////			this.setKeyValue(Types.RELATION
////							 , this.getRelationKey(strAvatarNameOwner, sCode)
////							 , null
////							 , strAvatarNameTarget);
//		}
//		
//		/*
//		public void addAvatarRefRelation(String strAvatarNameTarget, String strAvatarNameOwner, short sCode) throws Exception
//		{
//			this.setKeyValue(Types.RELATION
//							 , this.getRefRelationKey(strAvatarNameTarget, sCode)
//							 , null
//							 , strAvatarNameOwner);
//		}
//		*/
//		
//		public boolean removeAvatarRelation(String strAvatarNameOwner
//											, String strAvatarNameTarget
//											, short sCode) throws Exception
//		{
//			String strRelationKey = this.getRelationKey(strAvatarNameOwner, sCode);
//			Set<String> setAvatarNames = this.getRelationAvatarNames(strRelationKey);
//			if (setAvatarNames == null
//				|| !setAvatarNames.contains(strAvatarNameTarget)) return false;
//			
//			this.removeKeyValue(Types.RELATION
//								, strRelationKey
//								, strAvatarNameTarget);
//			
//			strRelationKey = this.getRelationKey(strAvatarNameTarget
//												 , sCode);
//			
//			this.removeKeyValue(Types.RELATION
//								, strRelationKey
//								, strAvatarNameOwner);
//			
//			return true;
//		}
//		
//		public void removeAvatarRelations(String strAvatarNameOwner
//										  , short sCode) throws Exception
//		{
//			String strRelationKey = this.getRelationKey(strAvatarNameOwner, sCode);
//			this.removeKeyValues(Types.RELATION
//								 , strRelationKey);
//								
//		}
//		/*
//		public void putAvatarRelation(Integer[] key, Object value)
//		{
//			redisTemplate.opsForHash().put("AVATAR_RELATION_"+key[0], key[1]+"", value);
//		}
//		
//		public void deleteAvatarRelation(Integer[] key)
//		{
//			this.redisTemplate.opsForHash().delete("AVATAR_RELATION_"+key[0], key[1]+"");
//
//			RedisHistory history = new RedisHistory("AVATAR_RELATION_".substring(0, "AVATAR_RELATION_".length()-1)
//														 , key+""
//														 , RedisHistory.RedisHistoryType.DELETE);
//			redisTemplate.opsForValue().set(HISTORY, JsonUtil.toJson(history));
//		}
//		
//		public Object getAvatarRelation(Integer[] key)
//		{
//			return this.redisTemplate.opsForHash().get("AVATAR_RELATION_"+key[0], key[1]+"");
//		}
//		*/
//		
//		public String getNewBattleId() throws Exception
//		{
//			return this.incrementValue(Types.BATTLE_COUNT, "", null, 1) + "";
//		}
//		
//		public void setBattleData(String strBattleId, String strBattleData) throws Exception
//		{
//			this.setKeyValue(Types.BATTLE
//							 , ""
//							 , strBattleId
//							 , strBattleData);
//		}
//		
//		public String getBattleDataById(String strBattleId) throws Exception
//		{
//			return (String)this.getValue(BATTLE.BATTLE_ID
//								 		 , ""
//								 		 , strBattleId);
//		}
//		
//		public void removeBattleDataById(String strBattleId) throws Exception
//		{
//			this.removeKeyValue(Types.BATTLE, "", strBattleId);
//		}
//		
//		public String getInviteRequestKey(String strTargetAvatarName, short sCode)
//		{
//			return strTargetAvatarName + this.strSplitSymbol + sCode;
//		}
//		
//		public String getInviteRequestKey(String strTargetAvatarName, short sCode, String strInfo)
//		{
//			return this.getInviteRequestKey(strTargetAvatarName, sCode) + this.strSplitSymbol + strInfo;
//		}
//		
////		public String getInviteRequestValue(String strAvatarName, String strTeamId)
////		{
////			return strAvatarName + this.strSplitSymbol + strTeamId;
////		}
//		
//		public Set<String> getInviteRequestAvatarNames(String strAvatarName, short sCode) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.INVITE_REQUEST
//					   						   , this.getInviteRequestKey(strAvatarName, sCode));
//		}
//		
//		public Set<String> getInviteRequestAvatarNames(String strAvatarName, short sCode, String strInfo) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.INVITE_REQUEST
//											   , this.getInviteRequestKey(strAvatarName, sCode, strInfo));
//		}
//		
//		public Set<String> getOnlineInviteRequestAvatarNames(String strAvatarName
//															 , byte bStatus
//															 , short sCode) throws Exception
//		{
//		return (Set<String>) this.getInterValues(Types.AVATAR_ONLINE
//												 , bStatus + ""
//												 , Types.INVITE_REQUEST
//												 , this.getInviteRequestKey(strAvatarName, sCode));
//		}
//		
//		public Set<String> getOnlineInviteRequestAvatarNames(String strAvatarName
//															 , byte bStatus
//															 , short sCode
//															 , String strInfo) throws Exception
//		{
//			return (Set<String>) this.getInterValues(Types.AVATAR_ONLINE
//													 , bStatus + ""
//													 , Types.INVITE_REQUEST
//													 , this.getInviteRequestKey(strAvatarName, sCode, strInfo));
//		}
//		
//		public boolean isInviteRequestExisted(String strAvatarName
//											  , short sCode
//											  , String strTargetAvatarName) throws Exception
//		{
//			strTargetAvatarName = (String)this.getValue(INVITE_REQUEST.INVITED_AVATAR_NAMES
//														, this.getInviteRequestKey(strAvatarName, sCode)
//														, strTargetAvatarName);
//			return (strTargetAvatarName != null);
//		}
//		
//		public boolean isInviteRequestExisted(String strAvatarName
//											  , short sCode
//											  , String strInfo
//											  , String strTargetAvatarName) throws Exception
//		{
//			strTargetAvatarName = (String)this.getValue(INVITE_REQUEST.INVITED_AVATAR_NAMES
//														, this.getInviteRequestKey(strAvatarName, sCode, strInfo)
//														, strTargetAvatarName);
//			return (strTargetAvatarName != null);
//		}
//		/*
//		public Set<String> getTeamInviteRequest(String strTeamId
//												, String strAvatarName
//												, short sCode) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.INVITE_REQUEST
//											   , this.getInviteRequestKey(strTeamId
//													   					  , strAvatarName
//													   					  , sCode));
//		}
//		*/
//		
//		public Set<String> getRequestAvatarNames(String strAvatarName, short sCode) throws Exception
//        {
//			return (Set<String>)this.getValues(Types.REQUEST
//											   , this.getInviteRequestKey(strAvatarName, sCode));
//        }
//		
//		public Set<String> getRequestAvatarNames(String strAvatarName, short sCode, String strInfo) throws Exception
//        {
//			return (Set<String>)this.getValues(Types.REQUEST
//											   , this.getInviteRequestKey(strAvatarName, sCode, strInfo));
//        }
//		
//		public Set<String> getOnlineRequestAvatarNames(String strAvatarName, byte bStatus, short sCode) throws Exception
//		{
//			return (Set<String>)this.getInterValues(Types.AVATAR_ONLINE
//													, bStatus + ""
//													, Types.REQUEST
//													, this.getInviteRequestKey(strAvatarName, sCode));
//		}
//		
//		public Set<String> getOnlineRequestAvatarNames(String strAvatarName, byte bStatus, short sCode, String strInfo) throws Exception
//		{
//			return (Set<String>)this.getInterValues(Types.AVATAR_ONLINE
//													, bStatus + ""
//													, Types.REQUEST
//													, this.getInviteRequestKey(strAvatarName, sCode, strInfo));
//		}
//		
//		public boolean isRequestExisted(String strAvatarName
//										, short sCode
//										, String strTargetAvatarName) throws Exception
//		{
//			strTargetAvatarName = (String)this.getValue(REQUEST.INVITER_AVATAR_NAMES
//											, this.getInviteRequestKey(strAvatarName, sCode)
//											, strTargetAvatarName);
//			return (strTargetAvatarName != null);
//		}
//		
//		public boolean isRequestExisted(String strAvatarName
//										, short sCode
//										, String strInfo
//										, String strTargetAvatarName) throws Exception
//		{
//			strTargetAvatarName = (String)this.getValue(REQUEST.INVITER_AVATAR_NAMES
//														, this.getInviteRequestKey(strAvatarName, sCode, strInfo)
//														, strTargetAvatarName);
//			return (strTargetAvatarName != null);
//		}
//		/*
//		public Set<String> getTeamRequest(String strTeamId
//										  , String strAvatarName
//										  , short sCode) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.REQUEST
//											   , this.getInviteRequestKey(strTeamId
//													   					  , strAvatarName
//													   					  , sCode));
//		}
//		*/
//		
//		public void addInviteRequest(String strInviteRequestKey
//									 , String strInviteRequestValue
//									 , String strRequestKey
//									 , String strRequestValue) throws Exception
//		{
//			this.setKeyValue(Types.INVITE_REQUEST
//							 , strInviteRequestKey
//							 , null
//							 , strInviteRequestValue);
//			
//			this.setKeyValue(Types.REQUEST
//							 , strRequestKey
//							 , null
//							 , strRequestValue);
//			/*
//			String strInviteRequestKey = this.getInviteRequestKey(strTargetAvatarName
//																  , sCode);
//			this.setKeyValue(Types.INVITE_REQUEST
//							 , this.getInviteRequestKey(strAvatarName
//									  					, sCode)
//							 , null
//							 , strTargetAvatarName);
//			
//			this.setKeyValue(Types.REQUEST
//							 , this.getInviteRequestKey(strTargetAvatarName
//									  					, sCode)
//							 , null
//							 , strAvatarName);
//			*/
//		}
//		
//		public void removeInviteRequest(String strInviteRequestKey
//										, String strInviteRequestValue
//										, String strRequestKey
//										, String strRequestValue) throws Exception
//		{
//			this.removeKeyValue(Types.INVITE_REQUEST
//								, strInviteRequestKey
//								, strInviteRequestValue);
//			
//			this.removeKeyValue(Types.REQUEST
//								, strRequestKey
//								, strRequestValue);
//			/*
//			this.removeKeyValue(Types.INVITE_REQUEST
//								, this.getInviteRequestKey(strAvatarNameOwner, sCode)
//								, strAvatarNameTarget);
//			
//			this.removeKeyValue(Types.REQUEST
//								, this.getInviteRequestKey(strAvatarNameTarget, sCode)
//								, strAvatarNameOwner);
//			*/
//		}
//		
//		public void removeInviteRequsts(String strInviteRequestKey) throws Exception
//		{
//			this.removeKeyValues(Types.INVITE_REQUEST, strInviteRequestKey);
//		}
//		
//		public void removeAllInviteRequests(String strAvatarName, short sCode) throws Exception
//		{
//			this.removeKeyLikeValues(Types.INVITE_REQUEST, this.getInviteRequestKey(strAvatarName, sCode, ""));
//		}
//		
//		public void removeRequests(String strRequestKey) throws Exception
//		{
//			this.removeKeyValues(Types.REQUEST, strRequestKey);
//		}
//		
//		public void removeAllRequests(String strAvatarName, short sCode) throws Exception
//		{
//			this.removeKeyLikeValues(Types.REQUEST, this.getInviteRequestKey(strAvatarName, sCode, ""));
//		}
//		
////		Visited List
//		
//		public void addVisitedAvatarId(String strAvatarIdOwner, String strAvatarIdFriend) throws Exception
//		{
//			this.setKeyValue(Types.VISITED_LIST, strAvatarIdOwner, null, strAvatarIdFriend);
//		}
//		
//		public void deleteVisitedList(String strAvatarIdOwner) throws Exception
//		{
//			this.removeKeyValues(Types.VISITED_LIST, strAvatarIdOwner);
//		}
//		
//		public Set<String> getVisitedList(String strAvatarIdOwner) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.VISITED_LIST, strAvatarIdOwner);
//		}
//		
//// 		Visited Count
//		
//		public int getVisitedCount(String strAvatarNameFriend) throws Exception
//		{
//			String strCount = this.getValue(VISITED_COUNT.COUNT, strAvatarNameFriend);
//			
//			if (strCount == null) return 0;
//			
//			return Integer.parseInt(strCount);
//		}
//		
//		public synchronized void addVisitedCount(String strAvatarNameFriend) throws Exception
//		{
//			this.setKeyValue(Types.VISITED_COUNT, strAvatarNameFriend, null, this.getVisitedCount(strAvatarNameFriend) + 1 + "");
//		}
//		
//		public void deleteVisitedCount(String strAvatarNameOwner) throws Exception
//		{
//			this.removeKeyValues(Types.VISITED_COUNT, strAvatarNameOwner);
//		}
//		
////		Visited List Date
//		
//		public Long getVisitedListClearTime(String strHostAddress) throws Exception
//		{
//			String strDate = null;
//			if ((strDate = (String)this.getValue(VISITED_LIST_CLEAR_DATE.VISITED_LIST_CLEAR_DATE
//						  	  			 		 , ""
//						  	  			 		 , strHostAddress)) == null) return null;
//			
//			return Long.parseLong(strDate);
//		}
//		
//		public void setVisitedListClearTime(String strHostAddress) throws Exception
//		{
//			this.setKeyValue(Types.VISITED_LIST_CLEAR_DATE, "", strHostAddress, Calendar.getInstance().getTimeInMillis() + "");
//		}
//		
////		MTUMail
//		
//		public String getMTUIdsKey(Object objSendScope, Object objSendScopeName)
//		{
//			return objSendScope + strSplitSymbol + objSendScopeName;
//		}
//		
//		public int getMTUCount() throws Exception
//		{
//			String strNewMTUId = this.getValue(MTUMAIL_COUNT.COUNT, "");
//			return Integer.parseInt(strNewMTUId);
//		}
//		
//		public long addMTUCount() throws Exception
//		{
//			return this.incrementValue(Types.MTUMAIL_COUNT, "", null, 1);
//		}
//		
//		protected long getMTUSendTypeId(byte bSendType, long lMTUIdBase, long lMTUId)
//		{
//			return bSendType * lMTUIdBase + lMTUId;
//		}
//		
//		protected Method methodSetMTUMailId;
//		
//		public Result setMTUMailId(Object obj)
//		{
//			Object[] objs = (Object[])obj;
//			RedisOperations operations = (RedisOperations)objs[2];
//			String strWatchKey = (String)objs[3];
//			byte bType = (Byte)objs[4];
//			long lMTUIdBase = (Long)objs[5];
//			int nAmount = (Integer)objs[6];
//			long lNewMTUId = (Long)objs[7];
//			
//			int nExecCount = 0;
//			try
//			{
//				List listExec = null;
//				operations.watch(strWatchKey);
//				do
//				{
//					nExecCount++;
//					
//					TreeSet<String> treeSetMTUTypeIds = new TreeSet<String>(this.getMTUTypeIds(strWatchKey));
//					SortedSet<String> sortedSetMTUIds = treeSetMTUTypeIds.subSet(this.getMTUSendTypeId(bType, lMTUIdBase, 0) + ""
//																			 	 , this.getMTUSendTypeId((byte)(bType + 1), lMTUIdBase, 0) + "");
//					
//					operations.multi();
//					if (nAmount > 0
//						&& sortedSetMTUIds.size() >= nAmount)
//					{
//						operations.opsForSet().remove(strWatchKey, sortedSetMTUIds.first());
//					}
//					
//					operations.opsForSet().add(strWatchKey, this.getMTUSendTypeId(bType, lMTUIdBase, lNewMTUId) + "");
//					
//				}while((listExec = operations.exec()) == null);
//				
//				return Result.Success;
//			}
//			catch(Exception exc)
//			{
//				log.error("error! " + exc.getMessage());
//			}
//			finally
//			{
//				log.info("execute watch key : " + strWatchKey
//						 + " with " + nExecCount
//						 + " time");
//				
//				operations.unwatch();
//			}
//			
//			return Result.Failed;
//		}
//		
//		public Result createMTU(long lMTUId
//							  	, Object objSendScope
//								, Object objSendScopeName
//								, byte bType
//								, long lMTUIdBase
//								, int nAmount
//								, Map<String, Object> mapMTUProperties) throws Exception
//		{
//			this.updateMTU(lMTUId + "", mapMTUProperties);
//			
//			String strMTUIdsKey = this.getMTUIdsKey(objSendScope, objSendScopeName);
//			
//			return (Result)this.bytesRedisTemplate.execute(new OpsSessionCallback(this.methodSetMTUMailId
//																			 , this
//																			 , null
//																			 , Types.MTUMAIL_IDS.getPrefix() + strMTUIdsKey
//																			 , bType
//																			 , lMTUIdBase
//																			 , nAmount
//																			 , lMTUId));
//		}
//		
//		public String updateMTU(String strMTUId, Map<String, Object> mapMTUProperties) throws Exception
//		{
//			this.setKeyValues(Types.MTUMAIL, strMTUId, mapMTUProperties);
//			
//			return strMTUId;
//		}
//		
//		public void deleteMTU(long lMTUId
//							  , Object objSendScope
//							  , Object objSendScopeName
//							  , String strStatus
//							  , byte bSendType
//							  , long lMTUIdBase) throws Exception
//		{
//			this.setKeyValue(Types.MTUMAIL, lMTUId + "", MTUMAIL.MTU_STATUS.name(), strStatus + "");
//			
//			String strMTUIdsKey = this.getMTUIdsKey(objSendScope, objSendScopeName);
//			
//			this.removeKeyValue(Types.MTUMAIL_IDS, strMTUIdsKey, this.getMTUSendTypeId(bSendType, lMTUIdBase, lMTUId) + "");
//		}
//		
//		public Set<String> getMTUTypeIds(Object objSendScope, Object objScopeName) throws Exception
//		{
//			return this.getMTUTypeIds(Types.MTUMAIL_IDS.getPrefix() + this.getMTUIdsKey(objSendScope, objScopeName));
//		}
//		
//		public Set<String> getMTUTypeIds(String strWatchKey) throws Exception
//		{
//			return (Set<String>)this.getNoTypePrefixValues(Types.MTUMAIL_IDS, strWatchKey);
//		}
//		
//		//temporary fix for MTUIds
////		public void fixAllMTUIds(long lMTUIdBase, String strOldMTUIdsBackupFileName) throws Exception
////		{
////			Set<String> setKeys = this.redisTemplate.keys(Types.MTUMAIL_IDS.getPrefix() + "*");
////			
////			RedisDataWriter rdw = null;
////			if (strOldMTUIdsBackupFileName != null
////				&& !strOldMTUIdsBackupFileName.isEmpty())
////			{
////				rdw = new RedisDataWriter(strOldMTUIdsBackupFileName + ".xml");
////			}
////			
////			List<RedisTypes> listRedisTypes = new ArrayList<RedisTypes>();
////			List<String> listKeys = new ArrayList<String>();
////			List<Object> listValues = new ArrayList<Object>();
////			
////			Map<Object, Object> mapMTUIds;
////			Map<byte[], byte[]> mapByteMTUIds;
////			String strSendType;
////			byte bSendType;
////			LinkedList<String> linkedListMTUIds;
////			Map<String, Set<String>> mapNewMTUIds = new HashMap<String, Set<String>>();
////			Set<String> setNewMTUIds;
////			for(String strKey : setKeys)
////			{
////				if (!this.redisTemplate.type(strKey).equals(RedisTypes.hash.name())) return;
////				mapMTUIds = this.redisTemplate.opsForHash().entries(strKey);
////				
////				setNewMTUIds = new HashSet<String>();
////				mapByteMTUIds = new HashMap<byte[], byte[]>();
////				for(Object objSendType : mapMTUIds.keySet())
////				{
////					strSendType = (String)objSendType;
////					bSendType = Byte.parseByte(strSendType);
////					linkedListMTUIds = (LinkedList<String>)JsonUtil.fromJson((String)mapMTUIds.get(strSendType)
////														 					 , new TypeToken<LinkedList<String>>(){}.getType());
////					for(String strMTUId : linkedListMTUIds)
////					{
////						setNewMTUIds.add(bSendType * lMTUIdBase + Long.parseLong(strMTUId) + "");
////					}
////					mapByteMTUIds.put(strSendType.getBytes(), ((String)mapMTUIds.get(strSendType)).getBytes());
////				}
////				
////				listRedisTypes.add(RedisTypes.hash);
////				listKeys.add(strKey);
////				listValues.add(mapByteMTUIds);
////				mapNewMTUIds.put(strKey, setNewMTUIds);
////			}
////			
////			if (rdw != null) rdw.write(listRedisTypes, listKeys, listValues);
////			
////			for(String strKey : setKeys)
////			{
////				this.redisTemplate.delete(strKey);
////				setNewMTUIds = mapNewMTUIds.get(strKey);
////				for(String strMTUId : setNewMTUIds)
////				{
////					this.redisTemplate.opsForSet().add(strKey, (Long.parseLong(strMTUId) + (1 * lMTUIdBase)) + "");
////				}
////			}
////		}
//		
//		public Map<String, Object> getMTU(String strMTUId) throws Exception
//		{
//			return (Map<String, Object>)this.getValues(Types.MTUMAIL, strMTUId);
//		}
//		
////Rating
//		/*
//		public String getRatingKey(String strActivityName, String strAvatarId)
//		{
//			return strActivityName + strSplitSymbol + strAvatarId;
//		}
//		
//		public Map<Object, Object> getRatingByKey(String strActivityName, String strAvatarId) throws Exception
//		{
//			Map<Object, Object> hMapRating = null;
//			
//			//check Avatar in Redis
//			if ((hMapRating = ((Map<Object, Object>)this.getValues(Types.RATING
//																   , getRatingKey(strActivityName, strAvatarId)))) == null
//				|| hMapRating.size() == 0) return null;
//				
//			return hMapRating;
//		}
//		*/
//		
////Billboard
//		
//		public Map<Object, Object> getBillboardByKey(String strActivityName, String strAvatarId) throws Exception
//		{
//			Map<Object, Object> hMapBillboard = null;
//			
//			//check Avatar in Redis
//			if ((hMapBillboard = ((Map<Object, Object>)this.getValues(Types.BILLBOARD
//																	  , strActivityName))) == null
//				|| hMapBillboard.size() == 0) return null;
//				
//			return hMapBillboard;
//		}
//		
//		public void setBillboardByKey(String strActivityName, Map<String, Object> mapBillboard) throws Exception
//		{
//			this.setKeyValues(Types.BILLBOARD, strActivityName, mapBillboard);
//		}
//		
//		public void removeBillboardByKey(String strActivityName) throws Exception
//		{
//			this.removeKeyValues(Types.BILLBOARD, strActivityName);
//		}
//		
////Glicko
//		
//		public Long getGlickoByAvatarId(AttrClass attrClass, String strPVPObjectId, String strAvatarId) throws Exception
//		{
//			Double dGlicko = (Double)this.getValue(attrClass, strPVPObjectId, strAvatarId);
//			
//			if (dGlicko == null) return null;
//			
//			return dGlicko.longValue();
//		}
//		
//		public Set<String> getAvatarIdsByScoreRange(AttrClass attrClass
//													, String strPVPObjectId
//													, long lStartScore
//													, long lEndScore) throws Exception
//		{
//			return (Set<String>)this.getValues(attrClass.getType(), strPVPObjectId, lStartScore, lEndScore);
//		}
//		
//		public void setGlickoByAvatarId(AttrClass attrClass, String strAvatarId, String strValue) throws Exception
//		{
//			this.setKeyValue(Types.DB_PVPScore, "", attrClass.name() + strAvatarId, strValue);
//		}
//		
//		public Set<String> getAllGlickoRRangeKeys()
//		{
//			Set<byte[]> setResult
//			= this.bytesRedisTemplate.keys((Types.GLICKO_R_RANGE.getPrefix() + ".*").getBytes(Config.chartset));
//			
//			Set<String> setAllGlickoRRangeKeys = new HashSet<String>();
//			for(byte[] bs : setResult)
//			{
//				setAllGlickoRRangeKeys.add(new String(bs, Config.chartset));
//			}
//			
//			return setAllGlickoRRangeKeys;
//		}
//		
//		public Set<String> getGlickoRRangeList(String strRange) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GLICKO_R_RANGE, strRange);
//		}
//		
//		public void setGlickoRRange(String strRange, String strAvatarId) throws Exception
//		{
//			this.setKeyValue(Types.GLICKO_R_RANGE, strRange, null, strAvatarId);
//		}
//		
//		public void removeGlickoRRange(String strRange) throws Exception
//		{
//			this.removeKeyValues(Types.GLICKO_R_RANGE, strRange);
//		}
//		
//		public void removeGlickoRRangeByAvatarId(String strRange, String strAvatarId) throws Exception
//		{
//			this.removeKeyValue(Types.GLICKO_R_RANGE, strRange, strAvatarId);
//		}
//		
////STATISTIC_AVATAR
//		
//		public void setStatisticCountByAvatarId(AttrClass attrClass, String strAvatarId, boolean bIsAdd) throws Exception
//		{
//			long lIncrement = 1;
//			if (!bIsAdd)
//			{
//				int nCount = this.getStatisticCountByAvatarId(attrClass, strAvatarId);
//				if (nCount <= 0)
//				{
//					this.setKeyValue(Types.STATISTIC_AVATAR, strAvatarId, attrClass.name(), 0 +"");
//					log.error("error! increment failed! count is " + nCount);
//					return;
//				}
//				
//				lIncrement = -1;
//			}
//			
//			this.incrementValue(attrClass.getType(), strAvatarId, attrClass.name(), lIncrement);
//		}
//		
//		public int getStatisticCountByAvatarId(AttrClass attrClass, String strAvatarId) throws Exception
//		{
//			String strValue = (String)this.getValue(attrClass, strAvatarId, attrClass.name());
//			if (strValue == null) return 0;
//			
//			return Integer.parseInt(strValue);
//		}
//		
//		public void fixStatisticCountByAvatarId(AttrClass attrClass, String strAvatarId, int nCount) throws Exception
//		{
//			this.setKeyValue(Types.STATISTIC_AVATAR, strAvatarId, attrClass.name(), nCount + "");
//		}
//		
////Publish
//		
//		public void publishMessage(byte[] bsPublishKey, byte[] bsData)
//		{
//			Jedis jedis = this.jedisPool.getResource();
//			try
//			{
//				jedis.publish(bsPublishKey, bsData);
//				this.jedisPool.returnResource(jedis);
//			}
//			catch(Exception exc)
//			{
//				this.jedisPool.returnBrokenResource(jedis);
//				log.error("error! ", exc);
//			}
//		}
//		
//		public void publishMessage(String strPublishKey, String strMessage)
//		{
//			Jedis jedis = this.jedisPool.getResource();
//			try
//			{
//				jedis.publish(strPublishKey, strMessage);
//				this.jedisPool.returnResource(jedis);
//			}
//			catch(Exception exc)
//			{
//				this.jedisPool.returnBrokenResource(jedis);
//				log.error("error! ", exc);
//			}
//		}
//		
////Match Team
//		
//		public void setTeam(String strTeamId
//							, Map<String, Object> mapProperties) throws Exception
//		{
//			this.setKeyValues(Types.Team, strTeamId, mapProperties);
//		}
//		
//		public void setTeamProperty(String strTeamId
//									, AttrClass attr
//									, String strValue) throws Exception
//		{
//			this.setKeyValue(attr.getType(), strTeamId, attr.name(), strValue);
//		}
//		
//		public Result setExistTeamProperties(String strTeamId
//										     , Map<String, Object> mapProperties) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(Types.Team.getRedisType());
//			
//			return this.execMulti(redisClient
//							   	  , Types.Team
//							   	  , strTeamId
//							   	  , redisClient.getMultiGetMethod()
//							   	  , redisClient.getMultiCheckSizeMethod()
//							   	  , redisClient.getMultiAddAllMethod()
//							   	  , null
//							   	  , mapProperties
//							   	  , null);
//		}
//		
//		public String getTeamProperty(String strTeamId
//									  , AttrClass attr) throws Exception
//		{
//			return this.getValue(attr, strTeamId);
//		}
//		
//		public boolean isTeamExisted(String strTeamId) throws Exception
//		{
//			Set<Object> setKeys = this.getKeys(Types.Team, strTeamId);
//			return (setKeys != null && setKeys.size() >= 1);
//		}
//		
//		public Map<String, Object> getTeam(String strTeamId) throws Exception
//		{
//			return (Map<String, Object>)this.getValues(Types.Team, strTeamId);
//		}
//		
//		public Set<String> getAllTeamPrefixIds()
//		{
//			Set<byte[]> setResult = this.bytesRedisTemplate.keys(Types.Team.getPrefix().getBytes(Config.chartset));
//			Set<String> setAllTeamPrefixIds = new HashSet<String>();
//			
//			for(byte[] bs : setResult)
//			{
//				setAllTeamPrefixIds.add(new String(bs, Config.chartset));
//			}
//			
//			return setAllTeamPrefixIds;
//		}
//		
//		public void removeTeam(String strTeamId) throws Exception
//		{
//			this.removeKeyValues(Types.Team, strTeamId);
//		}
//		
//		public long getTeamMemberSN(String strTeamId) throws Exception
//		{
//			return this.incrementValue(Team.SN.getType(), strTeamId, Team.SN.name(), 1);
//		}
//		
////TEAM MEMS
//		
//		public void addTeamMember(String strTeamId
//								  , long lIndex
//								  , String strAvatarName) throws Exception
//		{
//			this.setKeyValue(Types.TEAM_MEMS, strTeamId, lIndex, strAvatarName);
//		}
//		
//		public Result addTeamMemberInLimit(String strTeamId
//										   , long lIndex
//										   , String strAvatarName
//										   , int nLimit) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(Types.TEAM_MEMS.getRedisType());
//			
//			return this.execMulti(redisClient
//							   	  , Types.TEAM_MEMS
//							   	  , strTeamId
//							   	  , redisClient.getMultiGetMethod()
//							   	  , redisClient.getMultiCheckSizeMethod()
//							   	  , redisClient.getMultiAddMethod()
//							   	  , lIndex
//							   	  , strAvatarName
//							   	  , nLimit);
//		}
//		
//		public void removeAllTeamMembers(String strTeamId) throws Exception
//		{
//			this.removeKeyValues(Types.TEAM_MEMS, strTeamId);
//		}
//		
//		public Set<String> getTeamMembers(String strTeamId) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.TEAM_MEMS, strTeamId);
//		}
//		
//		public boolean isTeamMemberExisted(String strTeamId, String strMemberName) throws Exception
//		{
//			return this.getValue(TEAM_MEMS.AVATAR_NAME, strTeamId, strMemberName) != null;
//		}
//		
//		public void removeTeamMember(String strTeamId
//									 , String strAvatarName) throws Exception
//		{
//			this.removeKeyValue(Types.TEAM_MEMS, strTeamId, strAvatarName);
//		}
//		
////TEAM_MEM
//		
//		public void setTeamMemberProperty(String strMemberName
//										  , AttrClass attrClass
//										  , String strValue) throws Exception
//		{
//			this.setKeyValue(attrClass.getType(), strMemberName, attrClass.name(), strValue);
//		}
//		
//		public void setTeamMemberProperties(String strMemberName
//											, Map<String, Object> mapProperties) throws Exception
//		{
//			this.setKeyValues(Types.TEAM_MEM, strMemberName, mapProperties);
//		}
//		
//		public Result setExistTeamMemberProperties(String strMemberName
//												   , Map<String, Object> mapProperties) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(Types.TEAM_MEM.getRedisType());
//			
//			return this.execMulti(redisClient
//							   	  , Types.TEAM_MEM
//							   	  , strMemberName
//							   	  , redisClient.getMultiGetMethod()
//							   	  , redisClient.getMultiCheckSizeMethod()
//							   	  , redisClient.getMultiAddAllMethod()
//							   	  , null
//							   	  , mapProperties
//							   	  , 0);
//		}
//		
//		public String getTeamMemberProperty(String strMemberName, String strPropertyName) throws Exception
//		{
//			return (String)this.getValue(TEAM_MEM.STATUS, strMemberName, strPropertyName);
//		}
//		
//		public Map<String, Object> getTeamMemberProperties(String strMemberName) throws Exception
//		{
//			return (Map<String, Object>)this.getValues(Types.TEAM_MEM, strMemberName);
//		}
//		
//		public void removeTeamMemberProperties(String strMemberName) throws Exception
//		{
//			this.removeKeyValues(Types.TEAM_MEM, strMemberName);
//		}
//		
////TEAM_NRMEMS
//		
//		public void addTeamNRMember(String strTeamId, String strNRMemberName) throws Exception
//		{
//			this.setKeyValue(Types.TEAM_NRMEMS, strTeamId, "", strNRMemberName);
//		}
//		
//		public Set<String> getTeamNRMembers(String strTeamId) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.TEAM_NRMEMS, strTeamId);
//		}
//		
//		public void removeTeamNRMembers(String strTeamId, String strMember) throws Exception
//		{
//			this.removeKeyValue(Types.TEAM_NRMEMS, strTeamId, strMember);
//		}
//		
//		public void removeAllTeamNRMembers(String strTeamId) throws Exception
//		{
//			this.removeKeyValues(Types.TEAM_NRMEMS, strTeamId);
//		}
//		
////		public void putLackTeam(Types type
////								, String strConKey
////								, String strTeamIds) throws Exception
////		{
////			this.setKeyValue(type, strConKey, null, strTeamIds);
////		}
////		
////		public void putLackTeams(Types type
////							 	 , String strConKey
////							 	 , Set<String> setTeamIds) throws Exception
////		{
////			this.setKeyValues(type, strConKey, setTeamIds);
////		}
//		
//		public void putMatchTeam(Types type
//								 , String strConKey
//								 , long lGlickoR
//								 , String strTeamIds) throws Exception
//		{
//			this.setKeyValue(type, strConKey, lGlickoR, strTeamIds);
//		}
//		
//		public void removeMatchedTeam(Types type
//									  , String strConKey
//									  , String strTeamIds) throws Exception
//		{
//			this.removeNoPrefixKeyValue(type, strConKey, strTeamIds);
//		}
//		
//		public void putUnMatchedTeams(Types type
//									  , String strPrefix
//									  , Map<String, Set<TypedTuple<String>>> mapTeamIds) throws Exception
//		{
//			Iterator<String> itNames = mapTeamIds.keySet().iterator();
//			String strName;
//			while(itNames.hasNext())
//			{
//				strName = itNames.next();
//				this.unionAndStoreDest(type, strName, strPrefix + strName);
//			}
//		}
//		
////		public void putMatchTeams(Types type
////							 	  , String strConKey
////							 	  , Set<TypedTuple<String>> setTeamIds) throws Exception
////		{
////			this.setKeyValues(type, strConKey, setTeamIds);
////		}
//		
////		public void putTeamsAfterMatch(Types type
////									   , Types typeDel
////									   , Map<String, Set<String>> mapTeamIds
////									   , Collection<String> colExcludeTeamIds) throws Exception
////		{
////			Set<String> setTeamIds;
////			Iterator<String> itTeamIds;
////			String strTeamId;
////			for(String strName : mapTeamIds.keySet())
////			{
////				setTeamIds = mapTeamIds.get(strName);
////				//setTeamIds.removeAll(colExcludeTeamIds);
////				//colExcludeTeamIds.removeAll(setTeamIds);
////				itTeamIds = setTeamIds.iterator();
////				while(itTeamIds.hasNext())
////				{
////					strTeamId = itTeamIds.next();
////					if (colExcludeTeamIds.contains(strTeamId))
////					{
////						log.info("exclude team id : " + strTeamId);
////						//itTeamIds.remove();
////						//colExcludeTeamIds.remove(strTeamId);
////
////						if (typeDel != null) this.removeDelMatchTeam(typeDel, strTeamId);
////						
////						continue;
////					}
////					
////					log.info("put team id : " + strTeamId);
////					this.setKeyValueByName(type, strName, null, strTeamId);
////				}
////			}
////		}
//		
////		public void putTeamsAfterMatch(Types type
////									   , Types typeDel
////									   , String strTempMatchPrefix
////									   , Map<String, Set<String>> mapTeamIds
////									   , Collection<String> colExcludeTeamIds) throws Exception
////		{
////			this.putTeamsAfterMatch(type, typeDel, mapTeamIds, colExcludeTeamIds);
////			
////			this.removeTempMatchTeams(type, strTempMatchPrefix);
////		}
//		
//		public void recoverTempMatchTeams(Types type, String strTempMatchPrefix) throws Exception
//		{
//			Set<byte[]> setResult = this.bytesRedisTemplate.keys((strTempMatchPrefix + "*").getBytes(Config.chartset));
//			
//			if (setResult == null) return;
//			
//			Set<String> setTempPrefixNames = new HashSet<String>();
//			for(byte[] bs : setResult)
//			{
//				setTempPrefixNames.add(new String(bs, Config.chartset));
//			}
//					
//			Set<String> setTempTeamIds;
//			String strTempName;
//			for(String strTempPrefixName : setTempPrefixNames)
//			{
//				log.info("recover temp prefix name : " + strTempPrefixName);
//				this.unionAndStoreDest(type, strTempPrefixName, strTempPrefixName.replace(strTempMatchPrefix, ""));
//			}
//		}
//		
////		public void removeTempMatchTeam(Types type, String strName, String strValue) throws Exception
////		{
////			log.info("remove temp match team type : " + type
////					 + ", name : " + strName
////					 + ", value : " + strValue);
////			this.removeKeyValue(type, strName, strValue);
////		}
//		
////		public void removeTempMatchTeams(Types type, String strTempPrefix)
////		{
////			Set<String> setTempNames = this.redisTemplate.keys(strTempPrefix + "*");
////			log.info("remove temp match team type : " + type
////					 + ", prefix : " + strTempPrefix);
////			for(String strTempName : setTempNames)
////			{
////				log.info("remove temp team name : " + strTempName);
////				this.redisTemplate.delete(strTempName);
////			}
////		}
//		
//		public void takeMatchTeamWithMap(Types type
//										 , String strConKey
//										 , String strTempMatchPrefix
//										 , boolean bNeedScore
//										 , Map<String, Set> mapTeamIds) throws Exception
//		{
//			IRedisClient redisClient = this.getExecutor(type.getRedisType());
//			
//			Set<byte[]> setResult = this.bytesRedisTemplate.keys(strConKey.getBytes(Config.chartset));
//			if (setResult == null) return;
//			
//			Set<String> setKeys = new HashSet<String>();
//			for(byte[] bs : setResult)
//			{
//				setKeys.add(new String(bs, Config.chartset));
//			}
//			
//			Method methodMultiGet = (bNeedScore) ? redisClient.getMultiGetByScoreRangeMethod()
//												 : redisClient.getMultiGetMethod();
//			
//			for(String strKey : setKeys)
//			{
//				mapTeamIds.put(strKey, (Set)this.takeMatchTeams(redisClient
//																, type
//																, strKey
//																, strTempMatchPrefix
//																, methodMultiGet));
//			}
//		}
//		
//		public Object takeMatchTeams(IRedisClient redisClient
//									 , Types type
//									 , String strConKey
//									 , String strTempMatchPrefix
//									 , Method methodMultiGet) throws Exception
//		{
//			return this.execMultiTake(redisClient
//						   	   		  , type
//								   	  , strConKey
//								   	  , methodMultiGet
//									  , null
//									  , redisClient.getMultiRenameMethod()
//									  , strTempMatchPrefix
//									  , 0
//									  , null
//									  , 0);
//		}
//		
//		public void removeMatchTeam(Types type
//									, Types typeDel
//						  	   		, String strConKey
//						  	   		, String strTeamIds) throws Exception
//		{
//			this.removeKeyValue(type, strConKey, strTeamIds);
//		}
//		
//		public void putDelMatchTeamIds(Types type, String strTeamIds) throws Exception
//		{
//			log.info("put del match team type : " + type
//					 + ", team ids : " + strTeamIds);
//			this.setKeyValue(type, "", null, strTeamIds);
//		}
//		
//		public Set<String> getDelMatchTeamIds(Types type) throws Exception
//		{
//			return (Set<String>)this.getValues(type, "");
//		}
//		
////		public void removeDelMatchTeam(Types type, String strTeamIds) throws Exception
////		{
////			log.info("remove del match team type : " + type
////					 + ", team ids : " + strTeamIds);
////			this.removeKeyValue(type, "", strTeamIds);
////		}
//		
////		public boolean takeTeamByList(Types typeList
////								   , String strListKey
////								   , Types typeData
////								   , String strDataKey
////								   , Collection colValues
////								   , int nCount) throws Exception
////		{
////			IRedisClient clientList = this.getExecutor(typeList.getRedisType());
////			IRedisClient clientData = this.getExecutor(typeList.getRedisType());
////			
////			TreeSet<String> setTreeData = new TreeSet<String>();
////			
////			return this.execMultiTake2(clientList
////									   , typeList
////									   , strListKey
////									   , null
////									   , null
////									   , clientList.getMultiRemoveMethod()
////									   , null
////									   , null
////									   , null
////									   , colValues
////									   , 0
////									   , clientData
////									   , typeData
////									   , strDataKey
////									   , clientData.getMultiGetMethod()
////									   , clientData.getMultiCheckSizeMethod()
////									   , clientData.getMultiRemoveMethod()
////									   , strDataKey
////									   , null
////									   , nCount
////									   , setTreeData
////									   , nCount
////									   , true) != null;
////			
////			//check list with data, then do something...
////		}
//		
////		public void pushTeamMatch(String strKey, String strTeamId)
////		{
////			this.redisTemplate.opsForList().leftPush(strKey, strTeamId);
////		}
////		
////		public String[] popTeamMatch(String strKey, int nNeedAmount)
////		{
////			return (String[])this.redisTemplateSession.execute(new TeamSessionCallback(strKey, nNeedAmount));
////		}
////		
////		public void removeTeamMatch(String strKey, String strTeamId)
////		{
////			this.redisTemplate.opsForList().remove(strKey, 0, strTeamId);
////		}
//		
////Test Online
//		
//		public boolean getOnlineStatus(String strAvatarName, byte bStatus) throws Exception
//		{
//			String strOnlineStatus = (String)this.getValue(AVATAR_ONLINE.AVATAR_NAMES, bStatus + "", strAvatarName);
//			if (strOnlineStatus == null) return false;
//			
//			return true;
//		}
//		
////		public Set<String> getOnlineAvatarNames() throws Exception
////		{
////			return (Set<String>)this.getValues(Types.AVATAR_ONLINE, "");
////		}
//		
//		public Collection<String> getOnlineRandomAvatarNames(byte bStatus, long lCount) throws Exception
//		{
//			return (Collection<String>)this.getRandomValues(Types.AVATAR_ONLINE, bStatus + "", lCount);
//		}
//		
//		public void setOnlineStatus(String strAvatarName, byte bStatus) throws Exception
//		{
//			this.setKeyValue(Types.AVATAR_ONLINE, bStatus + "", null, strAvatarName);
//		}
//		
//		public void removeOnlineStatus(String strAvatarName, byte bStatus) throws Exception
//		{
//			this.removeKeyValue(Types.AVATAR_ONLINE, bStatus + "", strAvatarName);
//		}
//		
//		public void removeAllOnlineStatus() throws Exception
//		{
//			this.removeKeyLikeValues(Types.AVATAR_ONLINE, "");
//		}
//		
////MEMBER_TABLE
//		
//		public int getUserActiveType(int nUserId) throws Exception
//		{
//			String strActiveType = (String)this.getValue(MEMBER.ACTIVE_TYPE, nUserId + "", MEMBER.ACTIVE_TYPE.name());
//			if (strActiveType == null)
//			{
//				throw new Exception("user active type is null! user id is " + nUserId);
//			}
//			
//			return Integer.parseInt(strActiveType);
//		}
//		
////GROUP_MEMS
//		
//		public Set<String> getGroupMembers(String strGroupId) throws Exception
//		{
//			return (Set<String>)this.getValues(Types.GROUP_MEMS, strGroupId);
//		}
//		
//		public void addGroupMember(String strGroupId, String strAvatarName) throws Exception
//		{
//			this.setKeyValue(Types.GROUP_MEMS, strGroupId, null, strAvatarName);
//		}
//		
//		public boolean isGroupMembersExisted(String strGroupId)
//		{
//			return this.bytesRedisTemplate.hasKey((Types.GROUP_MEMS.getPrefix() + strGroupId).getBytes(Config.chartset));
//		}
//		
//		public void removeGroupMember(String strGroupId, String strAvatarName) throws Exception
//		{
//			this.removeKeyValue(Types.GROUP_MEMS, strGroupId, strAvatarName);
//		}
//		
//		public void deleteGroupMembers(String strGroupId) throws Exception
//		{
//			this.removeKeyValues(Types.GROUP_MEMS, strGroupId);
//		}
//		
////GROUP_INFO
//		
//		public void addGroupInfo(String strAvatarName, String strGroupTypeName, String strGroupInfo) throws Exception
//		{
//			this.setKeyValue(Types.GROUP_INFO, strAvatarName, strGroupTypeName, strGroupInfo);
//		}
//		
//		public Map<Object, Object> getGroupInfo(String strAvatarName) throws Exception
//		{
//			return (Map<Object, Object>)this.getValues(Types.GROUP_INFO, strAvatarName);
//		}
//		
//		public void removeGroupInfo(String strAvatarName, String strGroupTypeName) throws Exception
//		{
//			this.removeKeyValue(Types.GROUP_INFO, strAvatarName, strGroupTypeName);
//		}
//		
//		public void removeAllGroupInfo(String strAvatarName) throws Exception
//		{
//			this.removeKeyValues(Types.GROUP_INFO, strAvatarName);
//		}
//		
////BATTLE_GROUP
//		
////		public void addBattleMember(String strBattleId, String strAvatarName) throws Exception
////		{
////			this.setKeyValue(Types.BATTLE_MEMS, strBattleId, null, strAvatarName);
////		}
////		
////		public Set<String> getBattleMembers(String strBattleId) throws Exception
////		{
////			return (Set<String>)this.getValues(Types.BATTLE_MEMS, strBattleId);
////		}
//
////BATTLE_TEAMGROUP
//		
////PROFILE
//		
//		public void setProfile(String strAvatarName, Map<String, Object> mapProperties) throws Exception
//		{
//			this.setKeyValues(Types.PROFILE, strAvatarName, mapProperties);
//		}
//		
//		public Map<String, Object> getProfile(String strAvatarName) throws Exception
//		{
//			return (Map<String, Object>)this.getValues(Types.PROFILE, strAvatarName);
//		}
//		
//		public void removeProfile(String strAvatarName) throws Exception
//		{
//			this.removeKeyValues(Types.PROFILE, strAvatarName);
//		}
}
