package owt.sample.conference.hexmeet.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * gson转换
 * Created by hanqq on 2020/7/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class GsonUtils {

    public static final Type STRING_MAP_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    public static final Type STRING_BOOLEAN_MAP_TYPE = new TypeToken<Map<String, Boolean>>() {
    }.getType();

    public static final Type STRING_OBJECT_MAP_TYPE = new TypeToken<Map<String, Object>>() {
    }.getType();
    private final static Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T toObject(String src, Class<T> clz) {
        return gson.fromJson(src, clz);
    }

    public static <T> T toObject(InputStream in, Class<T> clz){
        InputStreamReader reader = new InputStreamReader(in);
        T result = gson.fromJson(reader, clz);
        try {reader.close();} catch (IOException e) {}
        return result;
    }

}
