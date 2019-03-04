package com.orbitview.salesmanagement.config;

/**
 * Created by hamnaro on 21/12/18.
 */

public class Config {
    public static final int BUILD_TYPE = 1; // 0 - development, 1 - production/live system
    public static final String[] LOGIN_URL = {
            "http://10.255.130.221/pos/public/login_api/login/",
            "http://156.67.217.146/validium/public/login_api/",
            "http://192.168.0.116:8080/validium/public/login_api/",
//            "http://192.168.0.109/validium/public/login_api/",
            "http://192.168.97.102/validium/public/login_api/",
            "http://198.168.102.140/validium/public/login_api/",
            "http://avries.hopto.org:9083/validium/public/login_api/login/"};
    public static final String[] UPLOAD_FILE_URL = {
            "http://10.255.130.221/pos/public/file_api/upload/",
            "http://156.67.217.146/validium/public/file_api/upload/",
            "http://192.168.0.116:8080/validium/public/file_api/upload/",
//            "http://192.168.0.109/validium/public/file_api/upload/",
            "http://192.168.97.102/validium/public/file_api/upload/",
            "http://198.168.102.140/validium/public/file_api/upload/",
            "http://avries.hopto.org:9083/validium/public/file_api/upload/"};
    public static final String[] REPORT_URL = {
            "http://192.168.0.116:8080/validium/public/report_api/",
            "http://156.67.217.146/validium/public/report_api/",
            "http://198.168.102.140/validium/public/report_api/",
//            "http://192.168.0.109/validium/public/report_api/",
            "http://192.168.97.102/validium/public/report_api/",
            "http://avries.hopto.org:9083/validium/public/report_api/"};

}
