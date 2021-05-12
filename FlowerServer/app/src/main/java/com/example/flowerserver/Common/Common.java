package com.example.flowerserver.Common;

import com.example.flowerserver.Model.User;

public class Common {
    public static User currentUser;

    public static String UPDATE = "Cập nhật";
    public static String DELETE = "Xóa";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
