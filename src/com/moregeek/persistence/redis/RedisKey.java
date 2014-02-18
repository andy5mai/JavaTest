package com.moregeek.persistence.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moregeek.persistence.redis.RedisData.RedisTypes;

public class RedisKey
{
	public enum Types
	{
		AVATAR(null)
		, AVATAR_PROPERTIES(null)
		, AVATAR_MAP(null)
		, AVATAR_COUNT(null)
		, MEMBER_AVATAR(null)
		, AVATAR_GUILD_RELATIONS(null)
		, GUILD(null)
		, GUILD_PROPERTIES(null)
		, GUILD_MAP(null)
		//, GUILD_INBOX(null)
		, GUILD_COUNT(null)
		, GUILD_MEMBER(null)
		, GUILD_MEMBER_PROPERTIES(null)
		, GUILD_CADRE(null)
		, GUILD_NEW_MEMBER(null)
		, GUILD_MEMBER_STATUS(null)
		, GUILD_POSITIONS(null)
		, GUILD_EVENTS(null)
		, AVATAR_SCHOOL(null)
		, INBOX(null)
		, INBOX_COUNT(null)
		, AVATAR_INBOXIDS(null)
		, AVATAR_SYSINBOXIDS(null)
		//, ALLUSER_INBOX(null)
		//, ALLGUILD_INBOX(null)
		, GUILD_ADVERSE(null)
		, GUILD_AVATAR_RELATIONS(null)
		, BATTLE(null)
		, BATTLE_COUNT(null)
		, MTUMAIL(null)
		, MTUMAIL_IDS(null)
		, MTUMAIL_COUNT(null)
		, INVITE_REQUEST(null)
		, REQUEST(null)
		, RELATION(null)
		, VISITED_LIST(null)
		, VISITED_COUNT(null)
		, BILLBOARD(null)
		, SYSINBOXIDS(null)
		, DB_PVPScore(null)
		, GLICKO_R_RANGE(null)
		, STATISTIC_AVATAR(null)
		, VISITED_LIST_CLEAR_DATE(null)
		, INBOX_AID_WEB(null)
		, AVATAR_ONLINE(null)
		, MAIL_SERIAL(null)
		, MEMBER(null)
		, LTD(null)
		, MTGD(null)
//		, SOCIAL_SN(null)
//		, SOCIAL_SNMAP(null)
		, Team(null)
		, TEAM_MEMS(null)
		, TEAM_MEM(null)
		, TEAM_NRMEMS(null)
		, GROUP_INFO(null)
		, GROUP_MEMS(null)
		, PROFILE(null)
		, HISTORY(null);
		
		private RedisKey redisKey;
		
		private Types(RedisKey redisKey)
		{
			this.redisKey = redisKey;
		}
		
		public void setRedisKey(RedisKey redisKey)
		{
			this.redisKey = redisKey;
		}
		
		public RedisKey getRedisKey()
		{
			return this.redisKey;
		}
	}
	
	private String strName;
	private RedisTypes redisType;
	private String strPrefix;
	private boolean bIsCustomField;
	private Map<String, RedisField> mapFields = new HashMap<String, RedisField>();
//	private List<RedisField> listRefFields = new ArrayList<RedisField>();
	
	public RedisKey(String strName)
	{
		this.strName = strName;
	}
	
	public RedisKey(String strName, RedisTypes redisType, String strPrefix, boolean bIsCustomField)
	{
		this.strName = strName;
		this.redisType = redisType;
		this.strPrefix = strPrefix;
		this.bIsCustomField = bIsCustomField;
	}
	
	public String getName()
	{
		return this.strName;
	}
	
	public void setRedisType(RedisTypes redisType)
	{
		this.redisType = redisType;
	}
	
	public RedisTypes getRedisType()
	{
		return this.redisType;
	}
	
	public void setPrefix(String strPrefix)
	{
		this.strPrefix = strPrefix;
	}
	
	public String getPrefix()
	{
		return this.strPrefix;
	}
	
	public void setIsCustomField(boolean bIsCustomField)
	{
		this.bIsCustomField = bIsCustomField;
	}
	
	public boolean isCustomField()
	{
		return this.bIsCustomField;
	}
	
	public void addRedisField(RedisField redisField)
	{
		this.mapFields.put(redisField.getName(), redisField);
	}
	
	public RedisField getRedisFieldByName(String strFieldName)
	{
		return this.mapFields.get(strFieldName);
	}
	
	public Map<String, RedisField> getRedisFields()
	{
		return this.mapFields;
	}
	
//	public void addRedisRefField(RedisField redisField)
//	{
//		this.listRefFields.add(redisField);
//	}
//	
//	public List<RedisField> getRedisRefFieldsList()
//	{
//		return this.listRefFields;
//	}
}
