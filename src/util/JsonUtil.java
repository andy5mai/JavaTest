package util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtil
{
	/// for convert from/to Object
	@Deprecated
	private static Gson gson = new Gson();
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static JsonParser jsonParser = new JsonParser();
	
	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
	
	public static Object fromJson(String json, Class<?> c) {
		return gson.fromJson(json, c);
	}
	
	public static Object fromJson(String json, Type type)
	{
		return gson.fromJson(json, type);
	}
	
	public static String coreObj2Json(ObjectNode node) 
	{
		ObjectNode root = mapper.createObjectNode();
		root.put("class", "avatars");
		root.putNull("dynamic");
		ObjectNode putObject = root.putObject("partDefinition");
		putObject.putAll(node);
		return root.toString();
	}

	public static ObjectNode createJsonNode()
	{
		return mapper.createObjectNode(); 
	}
	
	public static JsonObject getJsonObject(String strJson)
	{
		return jsonParser.parse(strJson).getAsJsonObject();
	}
	
	public static JsonElement getJsonElement(String strJson, String strKey)
	{
		return getJsonObject(strJson).get(strKey);
	}
	
	public static <T> T[] getObjArrayFromJsonElement(JsonElement jsonElement, T[] objs, String strMethod) throws Exception
	{
		Method method = JsonElement.class.getDeclaredMethod(strMethod);
		
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		for(int i = 0; i < jsonArray.size(); i++)
		{
			objs[i] = (T)method.invoke(jsonArray.get(i));
		}
		
		return objs;
	}
}
