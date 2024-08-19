package com.xk.rpccore.util;

import com.google.gson.Gson;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.Collections;
import java.util.Map;

/**
 * @author xk
 * @date 2024/8/19--23:08
 */
public class ServiceUtil {
    public static final Gson gson = new Gson();


    /**
     * 根据 服务名称 + 版本号 生成注册服务的 key
     *
     * @param serverName 服务名
     * @param version    版本号
     * @return 生成最终的服务名称: serverName-version
     */
    public static String serviceKey(String serverName, String version) {

        return String.join("-", serverName, version);
    }

    /**
     * 将 serviceInfo 对象转换为 map
     *
     * @param serviceInfo serviceInfo实列
     * @return Map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map toMap(ServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            return Collections.emptyMap();
        }
        Map map = gson.fromJson(gson.toJson(serviceInfo), Map.class);
        map.put("port", serviceInfo.getPort().toString());
        return map;
    }

    /**
     * 将 map 转换为 serviceInfo 实例
     *
     * @param map Map 实例
     * @return serviceInfo 实例
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ServiceInfo toServiceInfo(Map map) {
        map.put("port", Integer.parseInt(map.getOrDefault("port", "0").toString()));
        return gson.fromJson(gson.toJson(map), ServiceInfo.class);
    }
}
