package com.returnlive.healthinspectioninstrument.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 下午 4:06
 * 描述： gson解析json
 */

public class GsonParsing {



    public static ArrayList<Integer> getEcgJson(String json) throws Exception{
        Gson gson = new Gson();
        ArrayList<Integer> list = gson.fromJson(json, new TypeToken<ArrayList<Integer>>(){}.getType());
        return list;
    }

}


class GsonUtils {
    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }


}