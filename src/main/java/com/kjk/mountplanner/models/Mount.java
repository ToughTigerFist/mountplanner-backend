package com.kjk.mountplanner.models;

import org.springframework.data.annotation.Id;

public class Mount {
	
	@Id
	public String id;
	public String name;
	public boolean useable;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUseable() {
		return useable;
	}
	public void setUseable(boolean useable) {
		this.useable = useable;
	}
	

	
	
	
	
}
