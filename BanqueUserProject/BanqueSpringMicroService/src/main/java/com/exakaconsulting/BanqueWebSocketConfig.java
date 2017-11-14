package com.exakaconsulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.exakaconsulting.websocket.DisplayDateHandler;

@Configuration
@EnableWebSocket
@EnableScheduling

public class BanqueWebSocketConfig  implements WebSocketConfigurer {
	
	@Autowired
	private DisplayDateHandler displayDateHandler;

	
	/** Register web socket **/
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(displayDateHandler, "/sendDateToDisplay");

	}

	

}
