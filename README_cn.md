

# 🚀 分布式 xk-RPC 系统

[English](README.md) | 中文

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.2-green)
![Netty](https://img.shields.io/badge/Netty-4.1.65.Final-red)
![Zookeeper](https://img.shields.io/badge/Zookeeper-3.7.1-yellow)
![Nacos](https://img.shields.io/badge/Nacos-2.1.1-orange)
![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen)

一个基于 **Spring Boot**、**Netty** 和 **Zookeeper** 构建的分布式 RPC（远程过程调用）系统，旨在实现分布式服务之间高效的通信。该系统高度可定制，并支持 **Nacos** 作为服务注册中心，并且可以通过 **HTTP 协议** 替代 Netty。

---

## 🌟 **特性**

- 🔧 **服务注册与发现**：利用 Zookeeper 进行服务管理，并支持使用 Nacos 作为替代。
- ⚡ **高性能 RPC 通信**：使用 Netty 进行异步通信，支持 HTTP 协议作为可选方案。
- 📈 **可扩展性**：专为大规模分布式系统设计。
- ☕ **Spring Boot 集成**：简化配置，加速开发。

---

## 🛠️ **快速开始**

### 🧩 **前提条件**

- ☑️ **Java 17** 或更高版本
- ☑️ **Maven**
- ☑️ **Zookeeper**（或 Nacos）
- ☑️ **Netty**（或 HTTP）

#### ⚠️ **JDK 版本兼容性**

如果使用 **JDK 9 或更高版本**，你 **必须** 在启动 `provider` 模块之前添加以下 JVM 参数：

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```  

这可以防止由于 Java 模块化反射限制引起的以下错误：

```
java.lang.reflect.InaccessibleObjectException: Unable to make field 'detailMessage' accessible
```  

---

### 📥 **安装**

1. **克隆仓库**

    ```bash
    git clone https://github.com/xk-dragonfly/xk-RPC.git
    ```

2. **设置 Zookeeper**

   安装并启动一个 Zookeeper 实例，或者使用现有的 Zookeeper。更新 `application.yml` 或 `application.properties` 配置文件：

    ```yaml
    zookeeper:
      connect-string: localhost:2181
    ```  

   如果你更愿意使用 Nacos，可以替换为以下配置：

    ```yaml
    nacos:
      server-addr: localhost:8848
    ```  

3. **构建并运行**

   使用 Maven 构建并启动 `consumer` 和 `provider` 模块：

    ```bash
    # 启动 Provider 模块
    cd provider
    mvn clean install
    mvn spring-boot:run
    ```  

    ```bash
    # 启动 Consumer 模块
    cd ../consumer
    mvn clean install
    mvn spring-boot:run
    ```  

---

## ⚙️ **配置**

- **切换为 HTTP 协议**：修改通信类，使用 Spring 的 `RestTemplate` 或 `WebClient`。
- **使用 Nacos 进行服务注册**：更新配置文件，并替换掉与 Zookeeper 相关的依赖和逻辑。

---

## 🎯 **示例用法**

- 📝 **服务注册**：服务通过唯一名称和端点向 Zookeeper 或 Nacos 注册。
- 🔍 **服务发现**：客户端查询注册中心以定位并连接到服务。
- 📡 **远程过程调用**：客户端通过 Netty 或 HTTP 无缝调用远程服务的方法。

---

## 📂 **项目结构**

### 🔑 `rpc-core`
RPC 系统的核心功能，包括：

- 📦 **消息设计**：定义 RPC 消息的结构。
- 🔒 **编码与解码**：处理消息的序列化和反序列化。
- 📜 **服务管理**：管理服务的注册与发现。
- ⚙️ **序列化**：将对象转换为可传输的格式。
- 🎯 **负载均衡**：实现分配工作负载的策略。

---

### 💻 `rpc-client`
处理客户端操作：

- 🌉 **客户端代理**：动态生成远程方法调用的代理。
- 🔗 **通信**：管理请求和响应的消息传输。

---

### 🖥️ `rpc-server`
管理服务器端操作：

- 📨 **消息处理**：解码传入消息，执行方法并返回结果。

---

### 🛒 `consumer`
模拟客户端，并演示如何发起 RPC 调用，作为用户的入口点。

---

### 🏗️ `provider`
实现服务器端服务，展示如何为 RPC 客户端注册和暴露方法。

---

## 🤝 **贡献**

欢迎贡献！请将代码 fork 并提交 pull request。

---

## 📜 **许可证**

本项目使用 MIT 许可证。详情请参见 [LICENSE](LICENSE) 文件。

---

## 📬 **联系方式**

如有任何问题或疑问，请在 GitHub 上提问或发送邮件至 [xk0708666@gmail.com](mailto:xk0708666@gmail.com)。

---