package com.hswie.educaremobile.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hswie on 11/5/2015.
 */
public class PreferencesManager {

    private static final String TAG = "PreferencesManager";

    private static SharedPreferences sp = null;

    public static void prepare(Context context) {
        sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static void setCurrentResidentIndex(int index){
        if(sp != null && index != -1)
            sp.edit().putInt("CURRENT_RESIDENT", index).commit();
    }

    public static int getCurrentResidentIndex(){
        int index = -1;
        if(sp != null)
            index = sp.getInt("CURRENT_RESIDENT", -1);

        return index;
    }
}
