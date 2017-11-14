package com.exakaconsulting.websocket;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DisplayDateService {
	
	@Autowired
	private DisplayDateHandler displayDateHandler;

	@Scheduled(fixedDelay = 5000)
	public void sendCounterUpdate() {
		displayDateHandler.sendDateToDisplay(new Date());
	}

}
 