package com.example.restservicedemo;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.restservicedemo.domain.Artist;
import com.example.restservicedemo.domain.Music;
import com.example.restservicedemo.domain.MusicResponse;
import com.jayway.restassured.RestAssured;

public class MusicServiceTest {

	private static final String MUSIC_NAME = "Absolution_Calling";
	private static final String MUSIC_ALBUM = "Trust_Fall";
	private static final String MUSIC_GENRE = "Rock";
	private static final int MUSIC_RD = 2015;
	
	@BeforeClass
	public static void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
		RestAssured.basePath = "/restservicedemo/api";
		
		//add artists - foreign key
		Artist artist = new Artist(1L, "Iron", "Maiden");
		Artist artist2 = new Artist(2L, "Incubus", "Incubus");
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		given().contentType(MediaType.APPLICATION_JSON).body(artist2).post("/artist/");
	}
	
	@Test
	public void addMusicTest() {
		delete("/music/").then().assertThat().statusCode(200); //Foreign key constraint requirement
		
		//given a music
		Music music = new Music(1L, MUSIC_NAME, 1L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		
		//when creating new artist
		given().
			contentType(MediaType.APPLICATION_JSON).
			body(music).
		when().
			post("/music/").
		then().
			assertThat().statusCode(201);
		
		//then music should be added to database
		Music rmusic = get("/music/1").as(Music.class);
		assertEquals(MUSIC_NAME, rmusic.getName());
		assertEquals(MUSIC_ALBUM, rmusic.getAlbum());
		assertEquals(MUSIC_GENRE, rmusic.getGenre());
		assertEquals(MUSIC_RD, rmusic.getRd());
	}
	
	@Test
	public void deleteSpecificMusicTest() {
		//given a music
		Music music = new Music(1L, MUSIC_NAME, 2L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		
		//when deleting music with name Absolution_Calling
		given().
			request().
		when().
			delete("/music/Absolution_Calling").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		String response = get("/music/").body().asString();
		assertEquals("null", response);
	}
	
	@Test
	public void deleteAllMusicTest() {
		//given new music to test
		Music music = new Music(1L, MUSIC_NAME, 2L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		Music music2 = new Music(2L, MUSIC_NAME, 1L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		given().contentType(MediaType.APPLICATION_JSON).body(music2).post("/music/");
		
		//when delete all artists
		given().
			request().
		when().
			delete("/music/").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		String response = get("/music/").body().asString();
		assertEquals("null", response);
	}
	
	@Test
	public void getMusicTest() {
		//given 2 artists
		Music music = new Music(1L, MUSIC_NAME, 2L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		Music music2 = new Music(2L, MUSIC_NAME, 1L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		given().contentType(MediaType.APPLICATION_JSON).body(music2).post("/music/");
		
		//when database is obtained
		get("/music/").then().assertThat().statusCode(200);
		
		MusicResponse rmusic = get("/music/").as(MusicResponse.class);
		List<Music> musics = rmusic.getMusic();
	
		//then verify results
		assertEquals(2, musics.size());
		assertEquals(MUSIC_NAME, musics.get(0).getName());
		
		//special check for filling full author name field
		assertEquals("Iron Maiden", musics.get(1).getAuthor());
	}
	
	@Test
	public void getMusicByIdTest() {
		//given a music
		Music music = new Music(3L, MUSIC_NAME, 2L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		
		//when asking for specific entity
		get("/music/3").then().assertThat().statusCode(200);
		
		//then verify correctness
		Music rmusic = get("/music/3").as(Music.class);
		assertEquals(MUSIC_NAME, rmusic.getName());
		assertEquals("Incubus Incubus", rmusic.getAuthor());
		assertEquals(MUSIC_ALBUM, rmusic.getAlbum());
		assertEquals(MUSIC_GENRE, rmusic.getGenre());
		assertEquals(MUSIC_RD, rmusic.getRd());
	}
	
	@Test
	public void getMusicByFirstNameTest() {
		//given two music with same name
		Music music = new Music(1L, MUSIC_NAME, 2L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		Music music2 = new Music(2L, MUSIC_NAME, 1L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		given().contentType(MediaType.APPLICATION_JSON).body(music2).post("/music/");
		
		//when requested to find artists with first name Maiden
		RestAssured.when().get("/music/find/Absolution_Calling").
		then().
			assertThat().body(containsString("Iron")).
		and().statusCode(200);
		
		MusicResponse rmusic = get("/music/find/Absolution_Calling").as(MusicResponse.class);
		List<Music> musics = rmusic.getMusic();
		
		//then should return Maiden Iron and Maiden Test
		assertEquals(2, musics.size());
		assertEquals("Incubus Incubus", musics.get(0).getAuthor());
		assertEquals("Iron Maiden", musics.get(1).getAuthor());
	}
	
	@Test
	public void updateMusicNameTest() {	
		//given music with id 2 and name Absolution_Calling
		Music music = new Music(2L, MUSIC_NAME, 1L, "", MUSIC_ALBUM, MUSIC_GENRE, MUSIC_RD);
		given().contentType(MediaType.APPLICATION_JSON).body(music).post("/music/");
		
		//when updating from Absolution_Calling to Called_Already
		RestAssured.when().put("/music/2/Called_Already").
		then().
			assertThat().statusCode(200);
		
		//then result name should be Test
		Music rmusic = get("/music/2").as(Music.class);
		assertEquals("Called_Already", rmusic.getName());
		assertEquals("Iron Maiden", rmusic.getAuthor());
	}
	
	@After
	public void cleanUp() {
		delete("/music/").then().assertThat().statusCode(200);
	}
	
	@AfterClass
	public static void deleteArtists() { //all tests run delete temporary artists
		delete("/artist/").then().assertThat().statusCode(200);
	}
}
