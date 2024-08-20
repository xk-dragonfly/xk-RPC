package com.xk.rpcclient.transmission.http;

import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.netcommon.RequestMetadata;
import com.xk.rpccore.netcommon.RpcResponse;
import com.xk.rpccore.protocol.RpcMessage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author xk
 * @date 2024/8/20--19:36
 */
public class HttpTransClient implements TransClient {

    @Override
    public RpcMessage sendRpcRequest(RequestMetadata requestMetadata) {
        try {
            URL url = new URL("http", requestMetadata.getServerAddr(), requestMetadata.getPort(), "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(requestMetadata.getRpcMessage().getBody());
            objectOutputStream.flush();
            objectOutputStream.close();

            RpcMessage rpcMessage = new RpcMessage();

            InputStream inputStream = httpURLConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            RpcResponse response = (RpcResponse) objectInputStream.readObject();
            rpcMessage.setBody(response);
            return rpcMessage;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
