package cn.chinotan.zeus.framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  Map构建工具类
 * </p>
 * @author xingcheng
 * @date 2019-05-23
 **/
public class MapUtil {

    private Map<String,Object> map;

    private MapUtil(){
        map = new HashMap<>();
    }

    public static MapUtil builder(){
        return new MapUtil();
    }

    public MapUtil put(String key,Object value){
        this.map.put(key,value);
        return this;
    }

    public Map<String,Object> build(){
        return this.map;
    }

}
