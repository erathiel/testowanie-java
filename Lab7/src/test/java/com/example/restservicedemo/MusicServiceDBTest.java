package com.example.restservicedemo;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.example.restservicedemo.domain.Music;
import com.example.restservicedemo.domain.MusicResponse;
import com.jayway.restassured.RestAssured;

@FixMethodOrder(MethodSorters.JVM)
public class MusicServiceDBTest {

	private static IDatabaseConnection connection ;
	private static IDatabaseTester databaseTester;
	
	@BeforeClass
	public static void setUp() throws Exception{
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8080;
		RestAssured.basePath = "/restservicedemo/api";
		
		Connection jdbcConnection;
		jdbcConnection = DriverManager.getConnection(
				"jdbc:hsqldb:hsql://localhost/workdb", "sa", "");
		connection = new DatabaseConnection(jdbcConnection);
		
		databaseTester = new JdbcDatabaseTester(
				"org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/workdb", "sa", "");	
	}
	
	@Before
	public void setupDB() throws Exception {
		//easy way to create table if it does not exist
		get("/artist/");
		get("/music/");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(
				new FileInputStream(new File("src/test/resources/musicInitialData.xml")));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}
	
	@Test
	public void addMusicDB() throws Exception{
		Music music = new Music(4L, "Keep_Away", 4, "", "IV", "Rock", 2000);
		given().contentType(MediaType.APPLICATION_JSON).body(music)
				.when().post("/music/");
		
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("MUSIC");
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable
				(actualTable, new String[]{"ID"});
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicAfterAdd.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		Assertion.assertEquals(expectedTable, filteredTable);
	}
	
	@Test
	public void deleteSpecificMusicTestDB() throws Exception {
		//when deleting music with name Keep_away
		given().
			request().
		when().
			delete("/music/Keep_Away").
		then().
			assertThat().statusCode(200);
		
		//then database should be the same as on setup
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("MUSIC");
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		Assertion.assertEquals(expectedTable, actualTable);;
	}
	
	@Test
	public void updateMusicNameTestDB() throws Exception {			
		RestAssured.when().put("/music/3/Kept_Away").
		then().
			assertThat().statusCode(200);
		
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("MUSIC");
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicAfterUpdate.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		Assertion.assertEquals(expectedTable, actualTable);
	}
	
	@Test
	public void getMusicTestDB() throws Exception {
		//when database is obtained
		get("/music/").then().assertThat().statusCode(200);
		
		MusicResponse rmusic = get("/music/").as(MusicResponse.class);
		List<Music> actual = rmusic.getMusic();
		
		//then verify results
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		int i = 0;
		for(Music music : actual) {
			assertEquals(expectedTable.getValue(i, "name"), music.getName());
			assertEquals(expectedTable.getValue(i, "album"), music.getAlbum());
			i++;
		}
	}
	
	@Test
	public void getMusicByIdTestDB() throws Exception {
		//when asking for specific entity
		get("/music/1").then().assertThat().statusCode(200);
		
		Music actual = get("/music/1").as(Music.class);
		
		//then verify correctness
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		assertEquals(expectedTable.getValue(0, "name"), actual.getName());
		assertEquals(expectedTable.getValue(0, "album"), actual.getAlbum());
	}
	
	@Test
	public void getMusicByNameTestDB() throws Exception{
		RestAssured.when().get("/music/find/Absolution_Calling").
		then().
			assertThat().body(containsString("Bus")).
		and().statusCode(200);
		
		MusicResponse rmusic = get("/music/find/Absolution_Calling").as(MusicResponse.class);
		List<Music> actual = rmusic.getMusic();
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/musicInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("MUSIC");
		
		assertEquals(2, actual.size());
		for(Music music : actual) {
			assertEquals(expectedTable.getValue((int) music.getId() - 1, "name"), music.getName());
			assertEquals(expectedTable.getValue((int) music.getId() - 1, "album"), music.getAlbum());
		}
	}
	
	@Test
	public void deleteAllMusicTestDB() throws Exception {
		//when delete all music
		given().
			request().
		when().
			delete("/music/").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("MUSIC");
		
		assertEquals(0, actualTable.getRowCount());
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		databaseTester.onTearDown();
		RestAssured.when().delete("/music/");
		RestAssured.when().delete("/artist/");
	}
}
