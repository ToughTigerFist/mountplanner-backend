package com.kjk.mountplanner.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class AppSettings {

	@Id
	public String id;

	public boolean appRunning;

	public String outageMessage;

	public LocalDateTime lastMountLoad;

	public LocalDateTime lastServerLoad;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAppRunning() {
		return appRunning;
	}

	public void setAppRunning(boolean appRunning) {
		this.appRunning = appRunning;
	}

	public String getOutageMessage() {
		return outageMessage;
	}

	public void setOutageMessage(String outageMessage) {
		this.outageMessage = outageMessage;
	}

	public LocalDateTime getLastMountLoad() {
		return lastMountLoad;
	}

	public void setLastMountLoad(LocalDateTime lastMountLoad) {
		this.lastMountLoad = lastMountLoad;
	}

	public LocalDateTime getLastServerLoad() {
		return lastServerLoad;
	}

	public void setLastServerLoad(LocalDateTime lastServerLoad) {
		this.lastServerLoad = lastServerLoad;
	}

}
