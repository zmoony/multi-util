package com.boot.websocket.stomp;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * 指定特定用户的WebSocket连接
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private Principal extractUsernameFromRequest(ServerHttpRequest request) {
        // 实现从HTTP请求中提取用户名逻辑
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        String uid = servletRequest.getServletRequest().getParameter("uid");
        if (uid != null) {
            return new StompPrincipal(uid);
        }
        return new StompPrincipal("anonymous-" + UUID.randomUUID().toString());
    }
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // 可以从HTTP请求中获取用户信息
        // 例如从session、cookie或URL参数中获取
        return extractUsernameFromRequest(request);
    }
}
