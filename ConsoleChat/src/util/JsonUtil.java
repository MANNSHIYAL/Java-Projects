package util;
// Convert the message into the json string for the frame payload

import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import model.ChatType;
import model.Message;

public class JsonUtil {
    public static Message jsonToMessage(String json){
        return new Message(json, json, json,ChatType.COMMAND);
    }

    public static Map<String,Object> jsonToMap(String json) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("json");

        if(engine == null){
            throw new RuntimeException("Json parser enginer not found!");
        }

        String script = "Java.asJSONCompatible(" + json + ")";

        Object result = engine.eval(script);
        
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) result;
            return map;
        } else {
            throw new ScriptException("JSON conversion did not result in a Map.");
        }
    }
}
