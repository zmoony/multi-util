同一个项目中同时使用普通的WebSocket和STOMP WebSocket，它们不会冲突。

1. 普通WebSocket与STOMP WebSocket可以共存
   两种WebSocket实现方式可以同时存在于同一个项目中，它们服务于不同的需求：
   - 普通WebSocket (@ServerEndpoint)：更底层，适合自定义协议和精确控制
   - STOMP WebSocket：基于消息代理，适合复杂的消息路由和广播场景

2. 配置文件可以复用

不同端点：
普通WebSocket使用@ServerEndpoint("/ws/simple/{userId}")
STOMP WebSocket使用.addEndpoint("/ws/stomp")

使用场景：
普通WebSocket适合需要精确控制、自定义协议的场景
STOMP WebSocket适合复杂的消息路由、广播和用户特定消息的场景

用户身份管理：
普通WebSocket可以在URL路径参数或首次消息中传递用户身份
STOMP WebSocket可以通过HandshakeHandler设置Principal

|  特性 |  普通WebSocket |  STOMP WebSocket |
|---|---|---|
| 注解  |@ServerEndpoint   | @Configuration + @EnableWebSocketMessageBroker  |
| 注册  |自动扫描   |需要显式配置端点   |
| 协议  |原生WebSocket   |STOMP over WebSocket   |
| 消息路由  |手动实现   | 框架自动处理  |
| 复杂度  | 简单直接  |功能丰富但复杂   |

3. 使用场景
- 普通
  简单的实时通信需求（如聊天室、实时通知）
  需要精确控制连接和消息处理逻辑
  消息格式简单且固定
  对框架依赖要求较低的项

- STOMP
  复杂的消息路由需求
  需要广播、组播、单播等多种消息模式
  与Spring Security集成的安全需求
  需要与消息队列集成的场景
  大型企业级应用

