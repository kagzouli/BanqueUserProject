package com.exakaconsulting.websocket;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class DisplayDateService {
	
	@Autowired
	private DisplayDateHandler displayDateHandler;

	@Scheduled(fixedDelay = 10000)
	public void sendCounterUpdate() {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		displayDateHandler.sendMessageToDisplay(dateFormat.format(new Date()));
	}

}
 