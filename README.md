# 🚀 Distributed xk-RPC System

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.2-green)
![Netty](https://img.shields.io/badge/Netty-4.1.65.Final-red)
![Zookeeper](https://img.shields.io/badge/Zookeeper-3.7.1-yellow)
![Nacos](https://img.shields.io/badge/Nacos-2.1.1-orange)
![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen)

A distributed RPC (Remote Procedure Call) system built with **Spring Boot**, **Netty**, and **Zookeeper**, designed to enable efficient communication between distributed services. This highly customizable system also supports **Nacos** as a service registry and the **HTTP protocol** as an alternative to Netty.

---

## 🌟 **Features**

- 🔧 **Service Registration & Discovery**: Leverages Zookeeper for service management and supports Nacos as an alternative.
- ⚡ **High-Performance RPC Communication**: Uses Netty for asynchronous communication, with optional HTTP support.
- 📈 **Scalability**: Designed for large-scale distributed systems.
- ☕ **Spring Boot Integration**: Simplifies configuration and accelerates development.

---

## 🛠️ **Getting Started**

### 🧩 **Prerequisites**

- ☑️ **Java 17** or higher
- ☑️ **Maven**
- ☑️ **Zookeeper** (or Nacos)
- ☑️ **Netty** (or HTTP)

#### ⚠️ **JDK Version Compatibility**

If using **JDK 9 or higher**, you **must** add the following JVM parameter before starting the `provider` module:

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```  

This prevents the following error caused by Java's modular restrictions on reflection:

```
java.lang.reflect.InaccessibleObjectException: Unable to make field 'detailMessage' accessible
```  

---

### 📥 **Installation**

1. **Clone the repository**

    ```bash
    git clone https://github.com/xk-dragonfly/xk-RPC.git
    ```

2. **Set Up Zookeeper**

   Install and start a Zookeeper instance or use an existing one. Update the `application.yml` or `application.properties` configuration file:

    ```yaml
    zookeeper:
      connect-string: localhost:2181
    ```  

   If you prefer to use Nacos, replace the configuration:

    ```yaml
    nacos:
      server-addr: localhost:8848
    ```  

3. **Build and Run**

   Use Maven to build and start the `consumer` and `provider` modules:

    ```bash
    # Start the Provider module
    cd provider
    mvn clean install
    mvn spring-boot:run
    ```  

    ```bash
    # Start the Consumer module
    cd ../consumer
    mvn clean install
    mvn spring-boot:run
    ```  

---

## ⚙️ **Configuration**

- **Switching to HTTP Protocol**: Modify communication classes to use Spring's `RestTemplate` or `WebClient`.
- **Using Nacos for Service Registration**: Update the configuration file and replace Zookeeper-related dependencies and logic.

---

## 🎯 **Example Usage**

- 📝 **Service Registration**: Services register with Zookeeper or Nacos using a unique name and endpoint.
- 🔍 **Service Discovery**: Clients query the registry to locate and connect to services.
- 📡 **Remote Procedure Call**: Clients invoke methods on remote services seamlessly using Netty or HTTP.

---

## 📂 **Project Structure**

### 🔑 `rpc-core`
Core functionalities of the RPC system, including:

- 📦 **Message Design**: Defines the structure of RPC messages.
- 🔒 **Encoding & Decoding**: Handles serialization and deserialization of messages.
- 📜 **Service Management**: Manages service registration and discovery.
- ⚙️ **Serialization**: Converts objects to transmission-ready formats.
- 🎯 **Load Balancing**: Implements strategies to distribute workloads efficiently.

---

### 💻 `rpc-client`
Handles client-side operations:

- 🌉 **Client Proxy**: Dynamically generates proxies for remote method invocation.
- 🔗 **Communication**: Manages message transport for requests and responses.

---

### 🖥️ `rpc-server`
Manages server-side operations:

- 📨 **Message Processing**: Decodes incoming messages, executes methods, and returns results.

---

### 🛒 `consumer`
Simulates the client and demonstrates how to initiate RPC calls, acting as an entry point for users.

---

### 🏗️ `provider`
Implements server-side services, showcasing how to register and expose methods for RPC clients.

---

## 🤝 **Contributing**

Contributions are welcome! Please fork the repository and submit a pull request.

---

## 📜 **License**

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

## 📬 **Contact**

For any questions or issues, open an issue on GitHub or email [xk0708666@gmail.com](mailto:xk0708666@gmail.com).

