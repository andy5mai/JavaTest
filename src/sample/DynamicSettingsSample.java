package sample;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BytesRedisTemplate;

import redis.clients.jedis.JedisPool;

import com.moregeek.persistence.redis.RedisClient;
import com.moregeek.persistence.redis.RedisData;
import com.moregeek.persistence.redis.RedisField;
import com.moregeek.persistence.redis.RedisKey;
import com.moregeek.persistence.redis.RedisData.RedisTypes;

import attrs.AttrClass;
import attrs.Attrs;
import attrs.AvatarAttrs;
import attrs.AvatarCountAttrs;
import attrs.AvatarMapAttrs;
import attrs.InboxCountAttrs;
import attrs.InboxListViewAttrs;
import attrs.MailAttrs;
import unit.AObject;
import unit.Avatar;
import unit.Data;
import unit.InboxListView;
import unit.Mail;
import unit.PrintDataOutput;
import unit.SerializableObj;
import util.CommonUtil;
import util.DataAccessUtil;

public class DynamicSettingsSample implements IExec
{
	@Override
	public void Exec() throws Exception
	{
		Map<String, Object> mapProperties = new HashMap<String, Object>();
//		mapProperties.put(EnumUtil.Inbox.ID.name(), "1");
//		mapProperties.put(EnumUtil.Inbox.SENDER.name(), "andy");
//		mapProperties.put(EnumUtil.Inbox.RECEIVER.name(), "mai");
//		mapProperties.put(EnumUtil.Inbox.STATUS.name(), "0");
//		mapProperties.put(EnumUtil.Inbox.TITLE.name(), "title");
		
//		Mail mail = new Mail(mapProperties);
		
//		String[] strsNames
//		 , String[] strsDefaultValues
//		 , AttrClass[] serializeArrs
//		 , AttrClass[] deserializeArrs
		
//		public AttrClass ID;
//		public AttrClass SENDER;
//		public AttrClass RECEIVER;
//		public AttrClass STATUS;
//		public AttrClass TITLE;
//		public AttrClass MESSAGE_TYPE;
//		public AttrClass MESSAGE_PARAMS;
//		public AttrClass ATTACHMENT_ID;
//		public AttrClass CREATE_DATE;
		
		/*
		Map<String, RedisKey> mapRedisKeys = new HashMap<String, RedisKey>();
		
		RedisKey redisKey = new RedisKey("INBOX", RedisData.RedisTypes.hash, "INBOX:", true);
		this.addNewDefaultField(redisKey, new RedisField("ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("SENDER", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("RECEIVER", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("STATUS", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("TITLE", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("MESSAGE_TYPE", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("MESSAGE_PARAMS", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("ATTACHMENT_ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("CREATE_DATE", redisKey));
		
		mapRedisKeys.put(redisKey.getName(), redisKey);
		
		PrintDataOutput printDataOutput = new PrintDataOutput();
		Attrs.setAttrClass(InboxListViewAttrs.class
						   , "ID"
						   , ""
						   , getRedisFieldByName(mapRedisKeys, "INBOX", "ID")
						   , Data.Types.valueOf("Long"));
		
		Attrs.setAttrClass(InboxListViewAttrs.class
						   , "SENDER"
						   , ""
						   , getRedisFieldByName(mapRedisKeys, "INBOX", "SENDER")
						   , Data.Types.valueOf("String"));
		
		Attrs.setAttrClass(InboxListViewAttrs.class
						   , "RECEIVER"
						   , ""
						   , getRedisFieldByName(mapRedisKeys, "INBOX", "RECEIVER")
						   , Data.Types.valueOf("String"));
		
		Attrs.setAttrClass(InboxListViewAttrs.class
						   , "STATUS"
						   , ""
						   , getRedisFieldByName(mapRedisKeys, "INBOX", "STATUS")
						   , Data.Types.valueOf("Short"));
		
		InboxListViewAttrs inboxListViewAttrs = new InboxListViewAttrs();
		inboxListViewAttrs.setSerializeAttrs(new AttrClass[]{ InboxListViewAttrs.ID, InboxListViewAttrs.SENDER, InboxListViewAttrs.RECEIVER, InboxListViewAttrs.STATUS });
		inboxListViewAttrs.setDeserializeAttrs(new AttrClass[]{ InboxListViewAttrs.ID, InboxListViewAttrs.SENDER, InboxListViewAttrs.RECEIVER, InboxListViewAttrs.STATUS });
		
//		Attrs.setAttrClass(MailAttrs.class, "ID", "");
//		Attrs.setAttrClass(MailAttrs.class, "SENDER", "");
//		Attrs.setAttrClass(MailAttrs.class, "RECEIVER", "");
//		Attrs.setAttrClass(MailAttrs.class, "STATUS", "");
		Attrs.setAttrClass(MailAttrs.class
						   , "portraitObjId"
						   , "13010001"
						   , getRedisFieldByName(mapRedisKeys, "INBOX", "portraitObjId")
						   , Data.Types.valueOf("String"));
		
		MailAttrs mailAttrs = new MailAttrs();
		mailAttrs.setSerializeAttrs(new AttrClass[]{ MailAttrs.ID, MailAttrs.SENDER, MailAttrs.RECEIVER, MailAttrs.STATUS, MailAttrs.portraitObjId });
		mailAttrs.setDeserializeAttrs(new AttrClass[]{ MailAttrs.ID, MailAttrs.SENDER, MailAttrs.RECEIVER, MailAttrs.STATUS, MailAttrs.portraitObjId });
		
//		System.out.println(MailAttrs.portraitObjId == null);
//		System.out.println(MailAttrs.STATUS == null);
//		System.out.println(InboxListViewAttrs.STATUS == null);
		
//		for(AttrClass attrClass : mailAttrs.getSerializeAttrs())
//		{
//			System.out.println(attrClass.getName());
//		}
//		
//		System.out.println("==========================================");
//		
//		for(AttrClass attrClass : inboxListViewAttrs.getSerializeAttrs())
//		{
//			System.out.println(attrClass.getName());
//		}
		
		
		
		Map<String, Object> mapInboxListView = new HashMap<String, Object>();
		mapInboxListView.put(InboxListViewAttrs.ID.getName(), "123");
		mapInboxListView.put(InboxListViewAttrs.SENDER.getName(), "456");
		mapInboxListView.put(InboxListViewAttrs.RECEIVER.getName(), "789");
		mapInboxListView.put(InboxListViewAttrs.STATUS.getName(), "5");
		
		InboxListView inboxListView = new InboxListView(mapInboxListView, inboxListViewAttrs);
		
		System.out.println(InboxListViewAttrs.ID.getDataType());
		
		long lTimeStart = Calendar.getInstance().getTimeInMillis();
		this.print(inboxListView);
		
		System.out.println("time used : " + (Calendar.getInstance().getTimeInMillis() - lTimeStart));
		
		
		
		Map<String, Object> mapMail = new HashMap<String, Object>();
		mapMail.put(MailAttrs.ID.getName(), "111");
		mapMail.put(MailAttrs.SENDER.getName(), "222");
		mapMail.put(MailAttrs.RECEIVER.getName(), "333");
		mapMail.put(MailAttrs.STATUS.getName(), "6");
		
		mapMail.put(MailAttrs.portraitObjId.getName(), "130300001");
		
		Mail mail = new Mail(mapMail, mailAttrs);
		System.out.println("=============================");
		
		lTimeStart = Calendar.getInstance().getTimeInMillis();
		this.print(mail);
		
		System.out.println("time used : " + (Calendar.getInstance().getTimeInMillis() - lTimeStart));
		*/
		
		this.setSolveStepsMethod("getBytesString");
		this.setSolveStepsMethod("unCompressValue");
		
		this.setRefSolveStepsMethod("getJsonObjFromStr");
		this.setRefSolveStepsMethod("getJsonObjAsStr");

		
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setUsePool(true);
		factory.setHostName("192.168.80.128");
		factory.setDatabase(RedisClient.COMMON);
		
		factory.afterPropertiesSet();
		
		JedisPool jedisPool = new JedisPool(factory.getHostName()
											, factory.getPort());
		
		BytesRedisTemplate bytesRedisTemplate = new BytesRedisTemplate(factory);
		bytesRedisTemplate.afterPropertiesSet();
		
		RedisClient redisClient = new RedisClient(bytesRedisTemplate, jedisPool);
		
		//MEMBER_AVATAR
		RedisKey redisKey = this.setRedisKey("MEMBER_AVATAR", RedisData.RedisTypes.valueOf("set"), "MEMBER_AVATAR:");
		this.addNewDefaultField(redisKey, new RedisField("AVATAR_IDS", redisKey));
//		MemberAvatarAttrs memberAvatarAttrs = new MemberAvatarAttrs();
//		this.setAttrClass(memberAvatarAttrs, RedisKey.Types.MEMBER_AVATAR.getRedisKey(), "AVATAR_IDS", "String");
		
		redisKey = this.setRedisKey("INBOX_COUNT", RedisData.RedisTypes.valueOf("string"), "INBOX_COUNT");
		this.addNewDefaultField(redisKey, new RedisField("COUNT", redisKey));
		
		InboxCountAttrs inboxCountAttrs = new InboxCountAttrs();
		this.setAttrClass(inboxCountAttrs, RedisKey.Types.INBOX_COUNT.getRedisKey(), "COUNT", "String");
		
		redisKey = this.setRedisKey("AVATAR_COUNT", RedisData.RedisTypes.valueOf("string"), "AVATAR_COUNT");
		this.addNewDefaultField(redisKey, new RedisField("COUNT", redisKey));
		
		AvatarCountAttrs avatarCountAttrs = new AvatarCountAttrs();
		this.setAttrClass(avatarCountAttrs, RedisKey.Types.AVATAR_COUNT.getRedisKey(), "COUNT", "String");
		
		redisKey = this.setRedisKey("AVATAR_MAP", RedisData.RedisTypes.valueOf("hash"), "AVATAR_MAP");
		this.addNewDefaultField(redisKey, new RedisField("AvatarName", redisKey));
		
		AvatarMapAttrs avatarMapAttrs = new AvatarMapAttrs();
		this.setAttrClass(avatarMapAttrs, RedisKey.Types.AVATAR_MAP.getRedisKey(), "AvatarName", "String");
		
		//AVATAR
		redisKey = this.setRedisKey("AVATAR", RedisData.RedisTypes.valueOf("hash"), "AVATAR:");
		
		this.addNewDefaultField(redisKey, new RedisField("ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("NAME", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("USERID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("SELECTED", redisKey));
		
		RedisField redisFieldProperties = new RedisField("PROPERTIES", redisKey);
		redisFieldProperties.setSolveSteps("unCompressValue;getBytesString");
		redisKey.addRedisField(redisFieldProperties);
		
		RedisField redisField = new RedisField("portraitObjId", redisKey);
		redisField.setSolveSteps("getJsonObjFromStr;getJsonObjAsStr");
		redisField.setValueRef(RedisKey.Types.valueOf("AVATAR").getRedisKey().getRedisFieldByName("PROPERTIES"));
		redisField.setJsonLevels("dynamic");
		redisKey.addRedisField(redisField);
		
//		redisKey.addRedisRefField(redisField);

		AvatarAttrs avatarAttrs = new AvatarAttrs();
		this.setAttrClass(avatarAttrs, RedisKey.Types.AVATAR.getRedisKey(), "ID", "String");
		this.setAttrClass(avatarAttrs, RedisKey.Types.AVATAR.getRedisKey(), "NAME", "String");
		this.setAttrClass(avatarAttrs, RedisKey.Types.AVATAR.getRedisKey(), "USERID", "String");
		this.setAttrClass(avatarAttrs, RedisKey.Types.AVATAR.getRedisKey(), "SELECTED", "String");
		this.setAttrClass(avatarAttrs, RedisKey.Types.AVATAR.getRedisKey(), "portraitObjId", "String");
		
		avatarAttrs.setSerializeAttrs(new AttrClass[]{ AvatarAttrs.ID, AvatarAttrs.NAME, AvatarAttrs.USERID, AvatarAttrs.portraitObjId });
		avatarAttrs.setDeserializeAttrs(new AttrClass[]{ AvatarAttrs.ID, AvatarAttrs.NAME, AvatarAttrs.USERID, AvatarAttrs.portraitObjId });
		
		//Inbox_Count
		redisKey = this.setRedisKey("INBOX_COUNT", RedisData.RedisTypes.valueOf("string"), "INBOX_COUNT");
		this.addNewDefaultField(redisKey, new RedisField("COUNT", redisKey));
		
		//InboxListView
		redisKey = this.setRedisKey("INBOX", RedisData.RedisTypes.valueOf("hash"), "INBOX:");
		
		this.addNewDefaultField(redisKey, new RedisField("ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("SENDER", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("RECEIVER", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("STATUS", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("TITLE", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("MESSAGE_TYPE", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("MESSAGE_PARAMS", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("ATTACHMENT_ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("CREATE_DATE", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("ID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("NAME", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("USERID", redisKey));
		this.addNewDefaultField(redisKey, new RedisField("SELECTED", redisKey));
		
		InboxListViewAttrs inboxListViewAttrs = new InboxListViewAttrs();
		this.setAttrClass(inboxListViewAttrs, RedisKey.Types.INBOX.getRedisKey(), "ID", "String");
		this.setAttrClass(inboxListViewAttrs, RedisKey.Types.INBOX.getRedisKey(), "SENDER", "String");
		this.setAttrClass(inboxListViewAttrs, RedisKey.Types.INBOX.getRedisKey(), "RECEIVER", "String");
		this.setAttrClass(inboxListViewAttrs, RedisKey.Types.INBOX.getRedisKey(), "STATUS", "String");
		
		inboxListViewAttrs.setSerializeAttrs(new AttrClass[]{ InboxListViewAttrs.ID, InboxListViewAttrs.SENDER, InboxListViewAttrs.RECEIVER, InboxListViewAttrs.STATUS });
		inboxListViewAttrs.setDeserializeAttrs(new AttrClass[]{ InboxListViewAttrs.ID, InboxListViewAttrs.SENDER, InboxListViewAttrs.RECEIVER, InboxListViewAttrs.STATUS });
		
		MailAttrs mailAttrs = new MailAttrs();
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "ID", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "SENDER", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "RECEIVER", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "STATUS", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "portraitObjId", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "ATTACHMENT_ID", "String");
		this.setAttrClass(mailAttrs, RedisKey.Types.INBOX.getRedisKey(), "Attachment", "MapBytes");
		//		Attrs.setAttrClass(MailAttrs.class, "ID", "");
//		Attrs.setAttrClass(MailAttrs.class, "SENDER", "");
//		Attrs.setAttrClass(MailAttrs.class, "RECEIVER", "");
//		Attrs.setAttrClass(MailAttrs.class, "STATUS", "");
		
		mailAttrs.setSerializeAttrs(new AttrClass[]{ MailAttrs.ID, MailAttrs.SENDER, MailAttrs.RECEIVER, MailAttrs.STATUS, MailAttrs.portraitObjId, MailAttrs.Attachment });
		mailAttrs.setDeserializeAttrs(new AttrClass[]{ MailAttrs.ID, MailAttrs.SENDER, MailAttrs.RECEIVER, MailAttrs.STATUS, MailAttrs.portraitObjId });
		
		
//		RedisField redisField;
//		
//		for(String strFieldName : RedisKey.Types.AVATAR.getRedisKey().getRedisFields().keySet())
//		{
//			System.out.println("field : " + RedisKey.Types.AVATAR.getRedisKey().getRedisFieldByName(strFieldName).getName());
//		}
		
		Object objAllValues;
		
//		objAllValues = redisClient.getAllValues(RedisKey.Types.MEMBER_AVATAR.getRedisKey(), RedisKey.Types.MEMBER_AVATAR.getRedisKey().getPrefix() + "1");
//		Set<String> setAvatars = (Set<String>)objAllValues;
//		String strSelectedAvatarId = setAvatars.iterator().next();
		
		System.out.println("avatar count is " + redisClient.getAvatarCount());
		
		String strAvatarName = redisClient.getSelectedAvatarNameByUserId(1, AvatarAttrs.NAME.getRedisField());
		System.out.println("avatar name is " + strAvatarName);
		
		System.out.println(strAvatarName + " 's id : " + redisClient.getAvatarIdByName(strAvatarName));
		
		Map<Object, Object> mapAllAvatarNameIds = redisClient.getAllAvatarNameIds();
		for(Object objKey : mapAllAvatarNameIds.keySet())
		{
			System.out.println(objKey + " 's id : " + mapAllAvatarNameIds.get(objKey));
		}
		
		long lTime = Calendar.getInstance().getTimeInMillis();
		objAllValues = redisClient.getAllValues(RedisKey.Types.AVATAR.getRedisKey(), RedisKey.Types.AVATAR.getRedisKey().getPrefix() + 1);
		Map<String, Object> mapAllValues = (Map<String, Object>)objAllValues;
//		for(String strName : mapAllValues.keySet())
//		{
//			if (strName.equals("PROPERTIES")) continue;
//			System.out.println("Field name : " + strName
//							   + ", value : " + mapAllValues.get(strName));
//		}
		
		redisKey = this.setRedisKey("INBOX_AID_WEB", RedisData.RedisTypes.valueOf("hash"), "INBOX_AID_WEB:");
		
		Avatar avatar = new Avatar(mapAllValues, RedisKey.Types.AVATAR.getRedisKey(), avatarAttrs, redisClient);
		this.print(avatar);
		
		System.out.println("use time of avatar : " + (Calendar.getInstance().getTimeInMillis() - lTime));
		
		lTime = Calendar.getInstance().getTimeInMillis();
		objAllValues = redisClient.getAllValues(RedisKey.Types.INBOX.getRedisKey(), RedisKey.Types.INBOX.getRedisKey().getPrefix() + "robot_1_1");
		Mail mail = new Mail((Map<String, Object>)objAllValues, RedisKey.Types.INBOX.getRedisKey(), mailAttrs, redisClient);
		mail.setPortraitObjId(avatar);
		mail.setAttachment((Map<byte[], byte[]>)redisClient.getAllValues(RedisKey.Types.INBOX_AID_WEB.getRedisKey(), "1"));
		
		this.print(mail);
		
		System.out.println("use time of mail : " + (Calendar.getInstance().getTimeInMillis() - lTime));
		
		lTime = Calendar.getInstance().getTimeInMillis();
		InboxListView inboxListView = new InboxListView((Map<String, Object>)objAllValues, RedisKey.Types.INBOX.getRedisKey(), inboxListViewAttrs, redisClient);
//		System.out.println(InboxListViewAttrs.ID);
		System.out.println("mapAllValues size : " + mapAllValues.size());
		this.print(inboxListView);
		System.out.println("use time of inbox list view : " + (Calendar.getInstance().getTimeInMillis() - lTime));
		
		
		System.out.println(redisClient.addInboxCount(InboxCountAttrs.COUNT.getRedisField(), (long)9999999));
		
//		System.out.println(avatar.getProperty(AvatarAttrs.ID));
//		System.out.println(avatar.getProperty(AvatarAttrs.NAME));
//		System.out.println(avatar.getProperty(AvatarAttrs.USERID));
//		System.out.println(avatar.getProperty(AvatarAttrs.SELECTED));
//		System.out.println(avatar.getProperty(AvatarAttrs.portraitObjId));
		
	}
	
	public void print(SerializableObj serializableObj) throws Exception
	{
		serializableObj.serializeTo(new PrintDataOutput());
	}

	public void addNewDefaultField(RedisKey redisKey, RedisField redisField)
	{
		redisField.setWithDefault();
		redisKey.addRedisField(redisField);
	}
	
	public RedisKey setRedisKey(String strRedisKey, RedisData.RedisTypes redisType, String strPrefix)
	{
		RedisKey redisKey = new RedisKey(strRedisKey, redisType, strPrefix, true);
		RedisKey.Types.valueOf(strRedisKey).setRedisKey(redisKey);
		
		return redisKey;
	}
	
	public void setAttrClass(Attrs attrs, RedisKey redisKey, String strFieldName, String strDataType) throws Exception
	{
		attrs.setAttrClass(strFieldName
				   		   , ""
				   		   , Data.Types.valueOf(strDataType)
				   		   , redisKey.getRedisFieldByName(strFieldName));
	}
	
//	public RedisField getRedisFieldByName(Map<String, RedisKey> mapRedisKeys, String strRedisKey, String strRedisField)
//	{
//		RedisKey redisKey;
//		if ((redisKey = mapRedisKeys.get(strRedisKey)) == null) return null;
//		
//		return redisKey.getRedisFieldByName(strRedisField);
//	}
	
	public void setSolveStepsMethod(String strMethod) throws Exception
	{
		DataAccessUtil.SolveSteps.valueOf(strMethod).setMethod(CommonUtil.getAccessibleMethod(RedisClient.class
														  , strMethod
														 , RedisData.AccessTypes.class
														 , RedisTypes.class
														 , String.class
														 , RedisField.class
														 , Object.class));
	}
	
	public void setRefSolveStepsMethod(String strMethod) throws Exception
	{
		DataAccessUtil.SolveSteps.valueOf(strMethod).setMethod(CommonUtil.getAccessibleMethod(AObject.class
														  , strMethod
														 , RedisField.class
														 , Object.class));
	}
}