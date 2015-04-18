package com.example.shanky.scuoncampusjobs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 4/13/15.
 */
public class assignShiftJSONParser {

    public static List<assignShiftPlainOldJavaObjects> parseFeed(String content){

        try{
            JSONArray ar = new JSONArray(content);
            List<assignShiftPlainOldJavaObjects> assignShiftPlainOldJavaObjectsList = new ArrayList<>();

            for(int i = 0; i<ar.length(); i++){
                JSONObject obj = ar.getJSONObject(i);
                assignShiftPlainOldJavaObjects as_POJO = new assignShiftPlainOldJavaObjects();
                as_POJO.setEmployee_id(obj.getString("employee_id"));
                as_POJO.setEmployee_name(obj.getString("employee_name"));
                as_POJO.setShift_date(obj.getString("shift_date"));
                as_POJO.setStart_time(obj.getString("start_time"));
                as_POJO.setEnd_time(obj.getString("end_time"));

                assignShiftPlainOldJavaObjectsList.add(as_POJO);
            }
            return assignShiftPlainOldJavaObjectsList;

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}