package unit;

import attrs.AttrClass;

public class EnumUtil
{
	/*
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
	
	public enum Types
	{
		AVATAR(null, null, null, false, null)
		, AVATAR_PROPERTIES(null, null, null, false, null)
		, AVATAR_MAP(null, null, null, false, null)
		, AVATAR_COUNT(null, null, null, false, null)
		, MEMBER_AVATAR(null, null, null, false, null)
		, AVATAR_GUILD_RELATIONS(null, null, null, false, null)
		, GUILD(null, null, null, false, null)
		, GUILD_PROPERTIES(null, null, null, false, null)
		, GUILD_MAP(null, null, null, false, null)
		//, GUILD_INBOX(null, null, null, false, null)
		, GUILD_COUNT(null, null, null, false, null)
		, GUILD_MEMBER(null, null, null, false, null)
		, GUILD_MEMBER_PROPERTIES(null, null, null, false, null)
		, GUILD_CADRE(null, null, null, false, null)
		, GUILD_NEW_MEMBER(null, null, null, false, null)
		, GUILD_MEMBER_STATUS(null, null, null, false, null)
		, GUILD_POSITIONS(null, null, null, false, null)
		, GUILD_EVENTS(null, null, null, false, null)
		, AVATAR_SCHOOL(null, null, null, false, null)
		, INBOX(null, null, null, false, null)
		, INBOX_PROPERTIES(null, null, null, false, null)
		, INBOX_ITEMS_PROPERTIES(null, null, null, false, null)
		, INBOX_COUNT(null, null, null, false, null)
		, AVATAR_INBOXIDS(null, null, null, false, null)
		, AVATAR_SYSINBOXIDS(null, null, null, false, null)
		//, ALLUSER_INBOX(null, null, null, false, null)
		//, ALLGUILD_INBOX(null, null, null, false, null)
		, GUILD_ADVERSE(null, null, null, false, null)
		, GUILD_AVATAR_RELATIONS(null, null, null, false, null)
		, BATTLE(null, null, null, false, null)
		, BATTLE_COUNT(null, null, null, false, null)
		, MTUMAIL(null, null, null, false, null)
		, MTUMAIL_IDS(null, null, null, false, null)
		, MTUMAIL_COUNT(null, null, null, false, null)
		, INVITE_REQUEST(null, null, null, false, null)
		, REQUEST(null, null, null, false, null)
		, RELATION(null, null, null, false, null)
		, VISITED_LIST(null, null, null, false, null)
		, VISITED_COUNT(null, null, null, false, null)
		, BILLBOARD(null, null, null, false, null)
		, SYSINBOXIDS(null, null, null, false, null)
		, DB_PVPScore(null, null, null, false, null)
		, GLICKO_R_RANGE(null, null, null, false, null)
		, STATISTIC_AVATAR(null, null, null, false, null)
		, VISITED_LIST_CLEAR_DATE(null, null, null, false, null)
		, INBOX_AID_WEB(null, null, null, false, null)
		, AVATAR_ONLINE(null, null, null, false, null)
		, MAIL_SERIAL(null, null, null, false, null)
		, MEMBER(null, null, null, false, null)
		, LTD(null, null, null, false, null)
		, MTGD(null, null, null, false, null)
//		, SOCIAL_SN(null, null, null, false, null)
//		, SOCIAL_SNMAP(null, null, null, false, null)
		, Team(null, null, null, false, null)
		, TEAM_MEMS(null, null, null, false, null)
		, TEAM_MEM(null, null, null, false, null)
		, TEAM_NRMEMS(null, null, null, false, null)
		, GROUP_INFO(null, null, null, false, null)
		, GROUP_MEMS(null, null, null, false, null)
		, PROFILE(null, null, null, false, null)
		, HISTORY(null, null, null, false, null);
		
		private String strPrefix;
		private RedisTypes redisType;
		private String strBelongPropertyName;
		private boolean bCheckWhenStart;
		private AttrClass[] attrs;
		
		private Types(String strPrefix
					  , RedisTypes redisType
					  , String strBelongPropertyName
					  , boolean bCheckWhenStart
					  , AttrClass[] attrs)
		{
			this.redisType = redisType;
			this.strPrefix = strPrefix;
			this.strBelongPropertyName = strBelongPropertyName;
			this.bCheckWhenStart = bCheckWhenStart;
			this.attrs = attrs;
		}
		
		public void setPrefix(String strPrefix)
		{
			this.strPrefix = strPrefix;
		}
		
		public String getPrefix()
		{
			return this.strPrefix;
		}
		
		public void setRedisDataType(RedisTypes redisType)
		{
			this.redisType = redisType;
		}
		
		public RedisTypes getRedisType()
		{
			return this.redisType;
		}
		
		public void setBelongPropertyName(String strBelongPropertyName)
		{
			this.strBelongPropertyName = strBelongPropertyName;
		}
		
		public String getBelongPropertyName()
		{
			return this.strBelongPropertyName;
		}
		
		public void setCheckWhenStart(boolean bCheckWhenStart)
		{
			this.bCheckWhenStart = bCheckWhenStart;
		}
		
		public boolean getCheckWhenStart()
		{
			return this.bCheckWhenStart;
		}
		
		public void setAttrs(AttrClass[] attrs)
		{
			this.attrs = attrs;
		}
		
		public AttrClass[] getAttrs()
		{
			return this.attrs;
		}
	}
	*/
/*
	public interface AttrClass
	{
		public String name();
		
		public void setType(Types type);
		public Types getType();
		public void setDefaultValue(String strDefaultValue);
		public String getDefaultValue();
		public void setBelong(AttrClass attrs);
		public AttrClass getBelong();
		public void setDataType(Class<?> dataType);
		public Class<?> getDataType();
	}
	
	public enum Inbox implements AttrClass
	{
		ID(null, null, null, null)
		, SENDER(null, null, null, null)
		, RECEIVER(null, null, null, null)
		, STATUS(null, null, null, null)
		, TITLE(null, null, null, null)
		, MESSAGE_TYPE(null, null, null, null)
		, MESSAGE_PARAMS(null, null, null, null)
		, CREATE_DATE(null, null, null, null)
		, ATTACHMENT_ID(null, null, null, null);
//		, ATTACHMENT(null, null, null, null)
//		, PROPERTIES(null, null, null, null)
//		, TEST(null, null, null, null);
		
		private Types type;
		private String strDefaultValue;
		private AttrClass attrClass;
		private Class<?> dataType;
		
		private Inbox(Types type
					  , String strDefaultValue
					  , AttrClass attrClass
					  , Class<?> dataType)
		{
			this.type = type;
			this.strDefaultValue = strDefaultValue;
			this.attrClass = attrClass;
			this.dataType = dataType;
		}

		@Override
		public void setType(Types type)
		{
			this.type = type;
		}

		@Override
		public Types getType() {
			// TODO Auto-generated method stub
			return this.type;
		}

		@Override
		public void setDefaultValue(String strDefaultValue) {
			// TODO Auto-generated method stub
			this.strDefaultValue = strDefaultValue;
		}

		@Override
		public String getDefaultValue() {
			// TODO Auto-generated method stub
			return this.strDefaultValue;
		}

		@Override
		public void setBelong(AttrClass attrClass) {
			// TODO Auto-generated method stub
			this.attrClass = attrClass;
		}

		@Override
		public AttrClass getBelong() {
			// TODO Auto-generated method stub
			return this.attrClass;
		}

		@Override
		public void setDataType(Class<?> dataType) {
			// TODO Auto-generated method stub
			this.dataType = dataType;
		}

		@Override
		public Class<?> getDataType() {
			// TODO Auto-generated method stub
			return this.dataType;
		}
	}
	
	public enum Mail implements AttrClass
	{
		ID(null, null, null, null)
		, SENDER(null, null, null, null)
		, RECEIVER(null, null, null, null)
		, STATUS(null, null, null, null)
		, TITLE(null, null, null, null)
		, MESSAGE_TYPE(null, null, null, null)
		, MESSAGE_PARAMS(null, null, null, null)
		, CREATE_DATE(null, null, null, null)
		, ATTACHMENT_ID(null, null, null, null)
		, portraitObjId(null, null, null, null);
		
		private Types type;
		private String strDefaultValue;
		private AttrClass attrClass;
		private Class<?> dataType;
		
		private Mail(Types type
					  , String strDefaultValue
					  , AttrClass attrClass
					  , Class<?> dataType)
		{
			this.type = type;
			this.strDefaultValue = strDefaultValue;
			this.attrClass = attrClass;
			this.dataType = dataType;
		}

		@Override
		public void setType(Types type)
		{
			this.type = type;
		}

		@Override
		public Types getType() {
			// TODO Auto-generated method stub
			return this.type;
		}

		@Override
		public void setDefaultValue(String strDefaultValue) {
			// TODO Auto-generated method stub
			this.strDefaultValue = strDefaultValue;
		}

		@Override
		public String getDefaultValue() {
			// TODO Auto-generated method stub
			return this.strDefaultValue;
		}

		@Override
		public void setBelong(AttrClass attrClass) {
			// TODO Auto-generated method stub
			this.attrClass = attrClass;
		}

		@Override
		public AttrClass getBelong() {
			// TODO Auto-generated method stub
			return this.attrClass;
		}

		@Override
		public void setDataType(Class<?> dataType) {
			// TODO Auto-generated method stub
			this.dataType = dataType;
		}

		@Override
		public Class<?> getDataType() {
			// TODO Auto-generated method stub
			return this.dataType;
		}
	}
	
	*/
}
