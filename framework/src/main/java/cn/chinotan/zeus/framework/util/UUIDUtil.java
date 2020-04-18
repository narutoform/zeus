package cn.chinotan.zeus.framework.util;

import java.util.UUID;

/**
 * @author xingcheng
 * @date 2018-11-08
 */
public class UUIDUtil {

    public static String getUuid(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
    
}
