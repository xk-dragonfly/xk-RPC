package com.xk.rpccore.codec;

import com.xk.rpccore.constant.MessageType;
import com.xk.rpccore.constant.SerializationType;
import com.xk.rpccore.constant.TransConstants;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.RpcResponse;
import com.xk.rpccore.protocol.MessageHeader;
import com.xk.rpccore.protocol.RpcMessage;
import com.xk.rpccore.serialization.Serialization;
import com.xk.rpccore.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Arrays;
import java.util.List;

/**
 * @author xk
 * @date 2024/8/27--19:51
 *
 * 消息协议：
 * <pre>
 *   --------------------------------------------------------------------
 *  | 魔数 (4byte) | 版本号 (1byte)  | 序列化算法 (1byte) | 消息类型 (1byte) |
 *  -------------------------------------------------------------------
 *  |    状态类型 (1byte)  |    消息序列号 (4byte)   |    消息长度 (4byte)   |
 *  --------------------------------------------------------------------
 *  |                        消息内容 (不固定长度)                         |
 *  -------------------------------------------------------------------
 * </pre>
 */
public class RpcMessageCodec extends MessageToMessageCodec<ByteBuf, RpcMessage> {
    
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage msg, List<Object> list) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        MessageHeader header = msg.getHeader();
        buffer.writeBytes(header.getMagicNum());
        buffer.writeByte(header.getVersion());
        buffer.writeByte(header.getSerializerType());
        buffer.writeByte(header.getMessageType());
        buffer.writeByte(header.getMessageStatus());
        buffer.writeInt(header.getSequenceId());

        Object body = msg.getBody();
        Serialization serialization = SerializationFactory.getSerialization(SerializationType.parseByType(header.getSerializerType()));
        byte[] bytes = serialization.serialize(body);
        header.setLength(bytes.length);
        buffer.writeInt(header.getLength());
        buffer.writeBytes(bytes);

        list.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        int length = TransConstants.MAGIC_NUM.length;
        byte[] magicNum = new byte[length];
        msg.readBytes(magicNum,0,length);

        for (int i = 0; i < length; i++) {
            if (magicNum[i] != TransConstants.MAGIC_NUM[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(magicNum));
            }
        }

        byte version = msg.readByte();
        if (version != TransConstants.VERSION) {
            throw new IllegalArgumentException("The version isn't compatible " + version);
        }
        
        byte serializerType = msg.readByte();
        byte mtype = msg.readByte();
        byte status = msg.readByte();
        byte sequenceId = msg.readByte();
        
        int len = msg.readInt();
        byte[] body = new byte[len];
        msg.readBytes(body,0,len);
        
        MessageHeader header = MessageHeader.builder().magicNum(magicNum)
                .version(version)
                .serializerType(serializerType)
                .messageType(mtype)
                .messageStatus(status)
                .sequenceId(sequenceId)
                .length(len).build();

        Serialization serialization = SerializationFactory.getSerialization(SerializationType.parseByType(serializerType));
        MessageType messageType = MessageType.parseByType(mtype);
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setHeader(header);
        if(messageType == MessageType.REQUEST){
            RpcRequest request = serialization.deserialize(RpcRequest.class, body);
            rpcMessage.setBody(request);
        }else if(messageType == MessageType.RESPONSE){
            RpcResponse response = serialization.deserialize(RpcResponse.class, body);
            rpcMessage.setBody(response);
        }else if (messageType == MessageType.HEARTBEAT_REQUEST || messageType == MessageType.HEARTBEAT_RESPONSE){
            String message = serialization.deserialize(String.class, body);
            rpcMessage.setBody(message);
        }
        list.add(rpcMessage);
    }
}
