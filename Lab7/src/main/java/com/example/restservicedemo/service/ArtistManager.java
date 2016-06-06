package com.example.restservicedemo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.restservicedemo.domain.Artist;
	
public class ArtistManager {

	private Connection connection;
	private static final String URL = "jdbc:hsqldb:hsql://localhost/workdb";
	private static final String CREATE_TABLE_ARTIST = "CREATE TABLE Artist(id bigint GENERATED BY DEFAULT AS IDENTITY, firstName varchar(20), lastName varchar(40), UNIQUE(lastName), PRIMARY KEY(id))";
	
	private PreparedStatement addArtistStmt;
	private PreparedStatement deleteArtistStmt;
	private PreparedStatement deleteAllArtistsStmt;
	private PreparedStatement updateArtistStmt;
	private PreparedStatement getAllArtistsStmt;
	private PreparedStatement getArtistByIdStmt;
	private PreparedStatement findArtistByNameStmt;
	
	private Statement statement;
	
	public ArtistManager() {
		try {
			connection = DriverManager.getConnection(URL);
			statement = connection.createStatement();

			ResultSet rs = connection.getMetaData().getTables(null, null, null,
					null);
			boolean tableExists = false;
			while (rs.next()) {
				if ("Artist".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}
			
			if (!tableExists)
				statement.executeUpdate(CREATE_TABLE_ARTIST);

			addArtistStmt = connection
					.prepareStatement("INSERT INTO Artist(id, firstName, lastName) VALUES (?, ?, ?)");
			deleteAllArtistsStmt = connection
					.prepareStatement("DELETE FROM Artist");
			deleteArtistStmt = connection
					.prepareStatement("DELETE FROM Artist WHERE firstName = ?");
			getAllArtistsStmt = connection
					.prepareStatement("SELECT ID, FIRSTNAME, LASTNAME FROM Artist");
			getArtistByIdStmt = connection
					.prepareStatement("SELECT * FROM Artist WHERE id = ?");
			updateArtistStmt = connection
					.prepareStatement("UPDATE Artist SET firstName = ? WHERE id = ?");
			findArtistByNameStmt = connection
					.prepareStatement("SELECT * FROM Artist WHERE firstName = ?");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	Connection getConnection() {
		return connection;
	}
	
	public void deleteArtists() {
		try {
			deleteAllArtistsStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addArtist(Artist artist) {
		int count = 0;
		try {
			addArtistStmt.setLong(1, artist.getId());
			addArtistStmt.setString(2, artist.getFirstName());
			addArtistStmt.setString(3, artist.getLastName());

			count = addArtistStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int deleteSingleArtist(String name) {
		int count = 0;
		try {
			deleteArtistStmt.setString(1, name);
			
			count = deleteArtistStmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateArtistName(String new_name, Long id) {
		int count = 0;
		try {
			updateArtistStmt.setString(1, new_name);
			updateArtistStmt.setLong(2, id);
			
			count = updateArtistStmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public List<Artist> getAllArtists() {
		List<Artist> artists = new ArrayList<Artist>();

		try {
			ResultSet rs = getAllArtistsStmt.executeQuery();

			while (rs.next()) {
				Artist a = new Artist();
				a.setId(rs.getLong("id"));
				a.setFirstName(rs.getString("firstName"));
				a.setLastName(rs.getString("lastName"));

				artists.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artists;
	}
	
	public Artist getArtist(Long id) {
		Artist a = new Artist();
		try {
			getArtistByIdStmt.setLong(1, id);
			ResultSet rs = getArtistByIdStmt.executeQuery();

			while (rs.next()) {
				a.setId(rs.getLong("id"));
				a.setFirstName(rs.getString("firstName"));
				a.setLastName(rs.getString("lastName"));
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return a;
	}
	
	public List<Artist> findArtistByName(String name) {
		List<Artist> artists = new ArrayList<Artist>();

		try {
			findArtistByNameStmt.setString(1, name);
			ResultSet rs = findArtistByNameStmt.executeQuery();

			while (rs.next()) {
				Artist a = new Artist();
				a.setId(rs.getLong("id"));
				a.setFirstName(rs.getString("firstName"));
				a.setLastName(rs.getString("lastName"));

				artists.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artists;
	}

}