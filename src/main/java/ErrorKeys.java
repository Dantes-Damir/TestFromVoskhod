import java.util.HashMap;
import java.util.Map;

public class ErrorKeys {
    public static Map<String, String> errorKeyMap = new HashMap<>();

    public static boolean checkKey(String key) {
        for (Map.Entry<String, String> entry : errorKeyMap.entrySet()) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
