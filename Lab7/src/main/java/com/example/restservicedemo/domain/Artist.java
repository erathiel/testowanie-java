package com.example.restservicedemo.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Artist {
	
	private long id;
	private String firstName;
	private String LastName;
	
	public Artist(long l, String artistFirstName, String artistLastName) {
		this.id = l;
		this.firstName = artistFirstName;
		this.LastName = artistLastName;
	}
	
	public Artist() { }
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	
}
