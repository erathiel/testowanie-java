package com.example.restservicedemo.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Music {
	
	private long id;
	private String name;
	private long authorId;
	private String author;
	private String album;
	private String genre;
	private int rd; // release date
	
	public Music(long id, String name, long authorId, String author, String album,
			String genre, int rd) {
		super();
		this.id = id;
		this.name = name;
		this.authorId = authorId;
		this.author = author;
		this.album = album;
		this.genre = genre;
		this.rd = rd;
	}
	
	public Music() { }
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long id) {
		this.authorId = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}
}
