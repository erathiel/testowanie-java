package com.example.restservicedemo;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.restservicedemo.domain.Artist;
import com.example.restservicedemo.domain.ArtistResponse;
import com.jayway.restassured.RestAssured;

public class ArtistServiceTest {
	
	private static final String ARTIST_FIRST_NAME = "Iron";
	private static final String ARTIST_LAST_NAME = "Maiden";
	private static final String ARTIST_TEST_NAME = "Test";
	
	@BeforeClass
	public static void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
		RestAssured.basePath = "/restservicedemo/api";
	}
	
	@Test
	public void addArtistTest() {	
		//given an artist
		Artist artist = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_LAST_NAME);
		
		//when creating new artist
		given().
			contentType(MediaType.APPLICATION_JSON).
			body(artist).
		when().
			post("/artist/").
		then().
			assertThat().statusCode(201);
		
		//then artist should be added to database
		Artist rartist = get("/artist/1").as(Artist.class);
		assertEquals(ARTIST_FIRST_NAME, rartist.getFirstName());
		assertEquals(ARTIST_LAST_NAME, rartist.getLastName());
	}
	
	@Test
	public void deleteSpecificArtistTest() {
		//given an artist
		Artist artist = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_LAST_NAME);
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		
		//when deleting artist with name Iron
		given().
			request().
		when().
			delete("/artist/Iron").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		String response = get("/artist/").body().asString();
		assertEquals("null", response);
	}
	
	@Test
	public void deleteAllArtistsTest() {
		//given new artists to test
		Artist artist = new Artist(3L, ARTIST_FIRST_NAME, ARTIST_TEST_NAME);
		Artist artist2 = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_LAST_NAME);
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		given().contentType(MediaType.APPLICATION_JSON).body(artist2).post("/artist/");
		
		//when delete all artists
		given().
			request().
		when().
			delete("/artist/").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		String response = get("/artist/").body().asString();
		assertEquals("null", response);
	}
	
	@Test
	public void getArtistsTest() {
		//given 2 artists
		Artist artist = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_LAST_NAME);
		Artist artist2 = new Artist(2L, ARTIST_LAST_NAME, ARTIST_FIRST_NAME);
		
		given().contentType(MediaType.APPLICATION_JSON).body(artist).
		when().post("/artist/").
		then().assertThat().statusCode(201);
		given().contentType(MediaType.APPLICATION_JSON).body(artist2).
		when().post("/artist/").
		then().assertThat().statusCode(201);
		
		//when database is obtained
		get("/artist/").then().assertThat().statusCode(200);
		
		ArtistResponse rartists = get("/artist/").as(ArtistResponse.class);
		List<Artist> artists2 = rartists.getArtist();
	
		//then verify results
		assertEquals(2, artists2.size());
		assertEquals(ARTIST_FIRST_NAME, artists2.get(0).getFirstName());
		assertEquals(ARTIST_LAST_NAME, artists2.get(1).getFirstName());
	}
	
	@Test
	public void getArtistByIdTest() {
		//given an artist
		Artist artist = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_LAST_NAME);
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		
		//when asking for specific entity
		get("/artist/1").then().assertThat().statusCode(200);
		
		//then verify correctness
		Artist rartist = get("/artist/1").as(Artist.class);
		assertEquals(ARTIST_FIRST_NAME, rartist.getFirstName());
		assertEquals(ARTIST_LAST_NAME, rartist.getLastName());
	}
	
	@Test
	public void getArtistsByFirstNameTest() {
		//given two artists with same name
		Artist artist = new Artist(1L, ARTIST_LAST_NAME, ARTIST_FIRST_NAME);
		Artist artist2 = new Artist(3L, ARTIST_LAST_NAME, ARTIST_TEST_NAME);
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		given().contentType(MediaType.APPLICATION_JSON).body(artist2).post("/artist/");
		
		//when requested to find artists with first name Maiden
		RestAssured.when().get("/artist/find/Maiden").
		then().
			assertThat().body(containsString("Iron")).
		and().statusCode(200);
		
		ArtistResponse rartists = get("/artist/find/Maiden").as(ArtistResponse.class);
		List<Artist> artists = rartists.getArtist();
		
		//then should return Maiden Iron and Maiden Test
		assertEquals(2, artists.size());
		assertEquals(ARTIST_FIRST_NAME, artists.get(0).getLastName());
		assertEquals(ARTIST_TEST_NAME, artists.get(1).getLastName());;		
	}
	
	@Test
	public void updateArtistNameTest() {	
		//given artist with id 1 and name Iron
		Artist artist = new Artist(1L, ARTIST_FIRST_NAME, ARTIST_TEST_NAME);
		given().contentType(MediaType.APPLICATION_JSON).body(artist).post("/artist/");
		
		//when updating from Iron to Test
		RestAssured.when().put("/artist/1/Lol").
		then().
			assertThat().statusCode(200);
		
		//then result name should be Test
		Artist rartist = get("/artist/1").as(Artist.class);
		assertEquals("Lol", rartist.getFirstName());
		assertEquals(ARTIST_TEST_NAME, rartist.getLastName());
	}
	
	@After
	public void cleanUp() {
		delete("/artist/").then().assertThat().statusCode(200);
	}
	
}
