# Distributed xk-RPC System

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.2-green)
![Netty](https://img.shields.io/badge/Netty-4.1.65.Final-red)
![Zookeeper](https://img.shields.io/badge/Zookeeper-3.7.1-yellow)
![Nacos](https://img.shields.io/badge/Nacos-2.1.1-orange)
![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen)

A distributed RPC (Remote Procedure Call) system implemented using **Spring Boot**, **Netty**, and **Zookeeper**. The system allows for service registration and discovery, enabling efficient communication between distributed services. The system supports customizable components, with **Zookeeper** for service registration (which can be replaced by **Nacos**) and **Netty** for communication (which can be switched to the **HTTP** protocol).

## Features

- **Service Registration and Discovery**: Utilizes Zookeeper for managing service instances. Nacos can be used as an alternative.
- **RPC Communication**: Uses Netty for fast, asynchronous communication. Alternatively, the HTTP protocol can be used for simplicity and ease of integration.
- **Scalability**: Designed to support large-scale distributed environments with multiple services.
- **Spring Boot Integration**: Leverages Spring Boot for dependency management, configuration, and ease of use.

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven** 
- **Zookeeper** (or **Nacos** if preferred)
- **Netty** (or HTTP if preferred)

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/xk-dragonfly/xk-RPC.git
    ```

2. **Configure Zookeeper**:

   Install and start a Zookeeper instance locally or use an existing one. Update the `application.yml` or `application.properties` file with the Zookeeper connection string:

    ```yaml
    zookeeper:
      connect-string: localhost:2181
    ```

   If using Nacos, update the configuration accordingly:

    ```yaml
    nacos:
      server-addr: localhost:8848
    ```

3. **Build and Run the Application**:

   Use Maven or Gradle to build and start the `consumer` and `provider` modules:

    ```bash
    # Start the Provider module
    cd provider
    mvn clean install
    mvn spring-boot:run
    ```

   or

    ```bash
    # Start the Consumer module
    cd ../consumer
    mvn clean install
    mvn spring-boot:run
    ```


### Configuration

- **Switching to HTTP Protocol**: To replace Netty with HTTP, adjust the communication-related classes to use `RestTemplate` or `WebClient` from Spring.

- **Using Nacos for Service Registration**: Replace Zookeeper's configuration with Nacos. Update the dependencies and service registration logic accordingly.

### Example Usage

- **Service Registration**: A service can register itself with Zookeeper or Nacos by defining a unique service name and endpoint.
- **Service Discovery**: Services can discover other registered services by querying Zookeeper or Nacos.
- **Remote Procedure Call**: The client can invoke methods on remote services as if they were local, using Netty or HTTP for communication.

### Architecture Overview

- **Service Provider**: Registers itself with the service registry (Zookeeper/Nacos) and listens for incoming RPC requests.
- **Service Consumer**: Discovers available services from the registry and sends RPC requests.
- **Service Registry**: Maintains the list of active services and their locations, facilitating service discovery.

### Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Contact

For any questions or issues, please open an issue on GitHub or contact [xk0708666@gmail.com](mailto:your-email@example.com).
