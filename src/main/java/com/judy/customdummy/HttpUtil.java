package com.judy.customdummy;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        return ipAddress == null ? request.getRemoteAddr() : ipAddress;
    }

}
