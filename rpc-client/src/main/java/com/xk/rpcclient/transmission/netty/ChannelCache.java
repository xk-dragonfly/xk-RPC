package com.xk.rpcclient.transmission.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xk
 * @date 2024/8/20--19:51
 */

public class ChannelCache {
    /**
     * 存储 Channel，key 为 ip:port，val 为 channel 对象
     */
    private final Map<String, Channel> channelsCache = new ConcurrentHashMap<>();

    public Channel get(String host,Integer port) {
        String key = host + ":" + port;
        if (channelsCache.containsKey(key)) {
            Channel channel = channelsCache.get(key);
            if(channel != null && channel.isActive()) {
                return channel;
            }else{
                channelsCache.remove(key);
            }
        }
        return null;
    }
    
    public Channel get(InetSocketAddress inetSocketAddress){
        return get(inetSocketAddress.getAddress().getHostAddress(),inetSocketAddress.getPort());
    }
    
    public void set(String host,Integer port,Channel channel) {
        String key = host + ":" + port;
        channelsCache.put(key,channel);
    }
    
    public void set(InetSocketAddress inetSocketAddress,Channel channel) {
        this.set(inetSocketAddress.getHostName(),inetSocketAddress.getPort(),channel);
    }
}
