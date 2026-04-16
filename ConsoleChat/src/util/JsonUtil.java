package util;
// Convert the message into the json string for the frame payload

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class JsonUtil {
    // Json string to Map<String,Object>
    static class Pointer { int pos = 0; }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> parseJson(String s){
        Object result = parse(s, new Pointer());
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        throw new IllegalArgumentException("Root element is not a JSON Object");
    }

    private static Object parse(String s, Pointer p) {
        skipWhitespace(s, p);
        char c = s.charAt(p.pos);

        if (c == '{') return parseObject(s, p);
        if (c == '[') return parseList(s, p);
        return readValue(s, p);
    }

    private static Map<String, Object> parseObject(String s, Pointer p) {
        Map<String, Object> map = new LinkedHashMap<>();
        p.pos++; // skip '{'
        while (p.pos < s.length()) {
            skipWhitespace(s, p);
            if (s.charAt(p.pos) == '}') { p.pos++; return map; }

            String key = readToken(s, p, "&$");
            p.pos += 2; // skip "&$"
            
            map.put(key, parse(s, p));
            
            skipWhitespace(s, p);
            if (p.pos < s.length() && s.charAt(p.pos) == ',') p.pos++;
        }
        return map;
    }

    private static List<Object> parseList(String s, Pointer p) {
        List<Object> list = new ArrayList<>();
        p.pos++; // skip '['
        while (p.pos < s.length()) {
            skipWhitespace(s, p);
            if (s.charAt(p.pos) == ']') { p.pos++; return list; }

            list.add(parse(s, p));
            
            skipWhitespace(s, p);
            if (p.pos < s.length() && s.charAt(p.pos) == ',') p.pos++;
        }
        return list;
    }

    private static String readToken(String s, Pointer p, String endChars) {
        skipWhitespace(s, p);
        if (s.charAt(p.pos) == '"') p.pos++;
        StringBuilder sb = new StringBuilder();
        while (p.pos < s.length() && endChars.indexOf(s.charAt(p.pos)) == -1 && s.charAt(p.pos) != ',' && s.charAt(p.pos) != '}' && s.charAt(p.pos) != ']') {
            if (s.charAt(p.pos) != '"') sb.append(s.charAt(p.pos));
            p.pos++;
        }
        return sb.toString().trim();
    }

    private static String readValue(String s, Pointer p) {
        return readToken(s, p, ",}]");
    }

    private static void skipWhitespace(String s, Pointer p) {
        while (p.pos < s.length() && Character.isWhitespace(s.charAt(p.pos))) p.pos++;
    }

    // Json object to Json string to send it through websocket
    public static String jsonToString(Object obj) {
    if (obj instanceof Map) {
        StringBuilder sb = new StringBuilder("{");
        Map<?, ?> map = (Map<?, ?>) obj;
        Iterator<? extends Map.Entry<?, ?>> it = map.entrySet().iterator();
        
        while (it.hasNext()) {
            Map.Entry<?, ?> entry = it.next();
            sb.append("\"").append(entry.getKey()).append("\"&$")
              .append(jsonToString(entry.getValue()));
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
        
    } else if (obj instanceof List) {
        StringBuilder sb = new StringBuilder("[");
        List<?> list = (List<?>) obj;
        for (int i = 0; i < list.size(); i++) {
            sb.append(jsonToString(list.get(i)));
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
        
    } else {
        // Assume primitive/string
        return "\"" + obj.toString() + "\"";
    }
}
}
