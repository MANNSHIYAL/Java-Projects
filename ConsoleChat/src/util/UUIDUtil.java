package util;

import java.util.UUID;

public class UUIDUtil {
    public static String getNewUUIDString(String key){
        return getUUID(key).toString();
    }
    public static UUID getUUID(String key){
        return UUID.nameUUIDFromBytes(key.getBytes());
    }
}
