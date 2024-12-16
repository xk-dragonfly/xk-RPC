package com.xk.rpcclient.transmission.netty;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * @author xk
 * @date 2024/12/16--23:01
 */
public class ClientSslContext {
    public static SslContext createClientSslContext() throws SSLException {
        // 加载客户端证书和私钥
        File clientCert = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\client.crt");
        File clientKey = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\client.key");
        File caCert = new File("C:\\Users\\yohan\\Desktop\\TLSSSL\\double\\ca.crt"); // CA证书用于验证服务器

        // 使用 SslContextBuilder 配置 SSL
        return SslContextBuilder.forClient()
                .keyManager(clientCert, clientKey)  // 设置客户端证书和私钥
                .trustManager(caCert)  // 设置信任的 CA 证书（服务器证书）
                .build();
    }
}
