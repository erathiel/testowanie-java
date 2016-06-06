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

import com.example.restservicedemo.domain.Artist;
import com.example.restservicedemo.domain.ArtistResponse;
import com.jayway.restassured.RestAssured;

@FixMethodOrder(MethodSorters.JVM)
public class ArtistServiceDBTest {

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
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(
				new FileInputStream(new File("src/test/resources/artistInitialData.xml")));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}
	
	@Test
	public void addArtistDB() throws Exception{
		Artist artist = new Artist(6L, "Antonio", "Banderas");
		given().contentType(MediaType.APPLICATION_JSON).body(artist)
				.when().post("/artist/").then().assertThat().statusCode(201);
		
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("ARTIST");
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable
				(actualTable, new String[]{"ID"});
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistAfterAdd.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		Assertion.assertEquals(expectedTable, filteredTable);
	}
	
	@Test
	public void deleteSpecificArtistTestDB() throws Exception {
		//when deleting artist with name Antonio
		given().
			request().
		when().
			delete("/artist/Antonio").
		then().
			assertThat().statusCode(200);
		
		//then database should be the same as on setup
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("ARTIST");
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		Assertion.assertEquals(expectedTable, actualTable);;
	}
	
	@Test
	public void updateArtistNameTestDB() throws Exception {			
		//when updating from Iron to Test
		RestAssured.when().put("/artist/1/Test").
		then().
			assertThat().statusCode(200);
		
		//then result name should be Test
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("ARTIST");
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistAfterUpdate.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		Assertion.assertEquals(expectedTable, actualTable);;
	}
	
	@Test
	public void getArtistsTestDB() throws Exception {
		//when database is obtained
		get("/artist/").then().assertThat().statusCode(200);
		
		ArtistResponse rartists = get("/artist/").as(ArtistResponse.class);
		List<Artist> actual = rartists.getArtist();
	
		
		//then verify results
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		int i = 0;
		for(Artist artist : actual) {
			assertEquals(expectedTable.getValue(i, "firstName"), artist.getFirstName());
			assertEquals(expectedTable.getValue(i, "lastName"), artist.getLastName());
			i++;
		}
	}
	
	@Test
	public void getArtistByIdTestDB() throws Exception {
		//when asking for specific entity
		get("/artist/1").then().assertThat().statusCode(200);
		
		Artist rartist = get("/artist/1").as(Artist.class);
		
		//then verify correctness
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		assertEquals(expectedTable.getValue(0, "firstName"), rartist.getFirstName());
		assertEquals(expectedTable.getValue(0, "lastName"), rartist.getLastName());
	}
	
	@Test
	public void getArtistsByFirstNameTestDB() throws Exception{
		//when requested to find artists with first name Metal
		RestAssured.when().get("/artist/find/Metal").
		then().
			assertThat().body(containsString("Bus")).
		and().statusCode(200);
		
		ArtistResponse rartists = get("/artist/find/Metal").as(ArtistResponse.class);
		List<Artist> artists = rartists.getArtist();
		
		//then should return Metal Bus and Metal Lica
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
				new File("src/test/resources/artistInitialData.xml"));
		ITable expectedTable = expectedDataSet.getTable("ARTIST");
		
		assertEquals(2, artists.size());
		for(Artist artist : artists) {
			assertEquals(expectedTable.getValue((int) artist.getId() - 1, "firstName"), artist.getFirstName());
			assertEquals(expectedTable.getValue((int) artist.getId() - 1, "lastName"), artist.getLastName());
		}
	}
	
	@Test
	public void deleteAllArtistsTestDB() throws Exception {
		//when delete all artists
		given().
			request().
		when().
			delete("/artist/").
		then().
			assertThat().statusCode(200);
		
		//then database should be empty
		IDataSet dbDataSet = connection.createDataSet();
		ITable actualTable = dbDataSet.getTable("ARTIST");
		
		assertEquals(0, actualTable.getRowCount());
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		databaseTester.onTearDown();
		RestAssured.when().delete("/artist/");
	}	
}
