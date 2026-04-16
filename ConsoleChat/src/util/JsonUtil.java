package util;
// Convert the message into the json string for the frame payload

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import model.ChatType;
import model.Message;



public class JsonUtil {
    public static Message jsonToMessage(String json){
        return new Message(json, json, json,ChatType.COMMAND);
    }

    public static Map<String, Object> parseJson(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Object>>(){}.getType();

        Map<String, Object> map = gson.fromJson(json, type);
        return map;
    }
    
    public static <T> String jsonToString(T object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
