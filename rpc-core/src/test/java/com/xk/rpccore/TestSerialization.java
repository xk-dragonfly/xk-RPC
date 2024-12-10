package com.xk.rpccore;

import com.xk.rpccore.codec.RpcFrameDecoder;
import com.xk.rpccore.codec.RpcMessageCodec;
import com.xk.rpccore.constant.SerializationType;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.RpcResponse;
import com.xk.rpccore.protocol.MessageHeader;
import com.xk.rpccore.protocol.RpcMessage;
import com.xk.rpccore.serialization.Serialization;
import com.xk.rpccore.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.UnknownHostException;

/**
 * @author xk
 * @version 1.0
 * @ClassName TestJdkSerialization
 * @Date 2024/12/5 17:22
 */
public class TestSerialization {

    public static void main(String[] args) throws UnknownHostException {
        RpcMessageCodec REQUEST_CODE = new RpcMessageCodec();
        LoggingHandler LOGGING = new LoggingHandler(LogLevel.DEBUG);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(LOGGING, new RpcFrameDecoder(), REQUEST_CODE, LOGGING);

//        RpcRequest request = new RpcRequest();
//        request.setServiceName("com.xk.common.service.HelloService");
//        request.setMethod("sayHello");
//        request.setParameterTypes(new Class[]{String.class});
//        request.setParameterValues(new Object[]{"zhangsan"});
        RpcResponse response = new RpcResponse();
        response.setReturnValue("Hello, zhangsan1");

        RpcMessage protocol = new RpcMessage();
        MessageHeader header = MessageHeader.build("JSON");
        protocol.setHeader(header);
        protocol.setBody(response);

//        embeddedChannel.writeOutbound(protocol); // encode

        ByteBuf buf = embeddedChannel.alloc().buffer();
        buf.writeBytes(header.getMagicNum());
        buf.writeByte(header.getVersion());
        buf.writeByte(header.getSerializerType());
        buf.writeByte(header.getMessageType());
        buf.writeByte(header.getMessageStatus());
        buf.writeInt(header.getSequenceId());
        Serialization serialization = SerializationFactory
                .getSerialization(SerializationType.parseByType(header.getSerializerType()));
        byte[] bytes = serialization.serialize(response);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        /*
         * JDK          369B
         * JSON         156B
         * HESSIAN      226B
         * KRYO         99B
         * PROTOSTUFF   144B
         */
        embeddedChannel.writeInbound(buf); // decode
    }

}
