package com.xk.rpcserver.transmission.netty;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * @author xk
 * @date 2024/12/16--22:38
 */
public class ServerSslContext {
    public static SslContext createServerSslContext() throws SSLException {
        // 加载服务器端证书和私钥
        File serverCert = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\server.crt");
        File serverKey = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\server.key");
        File caCert = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\ca.crt"); // CA证书用于验证客户端

        // 使用 SslContextBuilder 配置 SSL
        return SslContextBuilder.forServer(serverCert, serverKey)
                .trustManager(caCert)  // 设置信任的 CA 证书（客户端证书）
                .clientAuth(ClientAuth.REQUIRE)  // 启用客户端认证（双向认证）
                .build();
    }
}
