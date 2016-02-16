package com.sharkfeed.managers;

import com.google.gson.Gson;

/**
 * Created by vraman on 2/9/16.
 */
public class JsonManager {
    /************************************************************************************/
    public static Object parseFromJSONToObject(String json, Class<?> type) {
        try {
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /************************************************************************************/
    public static String parseObjectToJsonString(Object object) {
        return new Gson().toJson(object);
    }
    /************************************************************************************/
}
