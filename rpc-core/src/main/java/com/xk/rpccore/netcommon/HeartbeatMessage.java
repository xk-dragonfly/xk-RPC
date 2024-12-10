package com.xk.rpccore.netcommon;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 心跳检查消息类
 *
 * @author xk
 * @version 1.0
 * @ClassName HeartbeatMessage
 * @Date 2024/8/19--23:58
 */
@Data
@Builder
public class HeartbeatMessage implements Serializable {

    /**
     * 消息
     */
    private String msg;

}
