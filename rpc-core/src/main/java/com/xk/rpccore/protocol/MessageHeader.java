package com.xk.rpccore.protocol;

import com.xk.rpccore.constant.MessageType;
import com.xk.rpccore.constant.ProtocolConstants;
import com.xk.rpccore.constant.SerializationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xk
 * @date 2024/8/19--23:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {

    /**
     * 4字节 魔数
     */
    private byte[] magicNum;

    /**
     * 1字节 版本号
     */
    private byte version;

    /**
     * 1字节 序列化算法
     */
    private byte serializerType;

    /**
     * 1字节 消息类型
     */
    private byte messageType;

    /**
     * 消息状态类型
     */
    private byte messageStatus;

    /**
     * 4字节 消息的序列号 ID
     */
    private int sequenceId;

    /**
     * 4字节 数据内容长度
     */
    private int length;

    /**
     * 根据输入的序列化算法构造一个 MessageHeader 对象
     *
     * @param serializeName 序列化算法名称
     * @return 构造指定序列化算法的默认协议头对象
     */
    public static MessageHeader build(String serializeName) {
        return MessageHeader.builder()
                .magicNum(ProtocolConstants.MAGIC_NUM)
                .version(ProtocolConstants.VERSION)
                .serializerType(SerializationType.parseByName(serializeName).getType())
                .messageType(MessageType.REQUEST.getType())
                .sequenceId(ProtocolConstants.getSequenceId()) // 添加唯一 ID 生成
                .build();
    }
}
