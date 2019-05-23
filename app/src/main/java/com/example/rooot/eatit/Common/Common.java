package com.example.rooot.eatit.Common;

import com.example.rooot.eatit.Model.User;

public class Common {
    public static User current_user;

    public static String convertCodeToStatus(String code){
        if(code.equals("0"))
            return "placed";
        else if (code.equals("1"))
            return "On the way";
        else
            return "Delivered";
    }
}
