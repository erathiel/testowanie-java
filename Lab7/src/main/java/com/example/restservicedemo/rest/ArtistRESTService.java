package com.example.restservicedemo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.restservicedemo.domain.Artist;
import com.example.restservicedemo.service.ArtistManager;

@Path("artist")
public class ArtistRESTService {

	private ArtistManager am = new ArtistManager();
	
	@GET
	@Path("/{artistId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Artist getMusic(@PathParam("artistId") Long id) {
		Artist a = new Artist();
		a = am.getArtist(id);
		return a;
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artist> getAllArtists() {
		List<Artist> artists = new ArrayList<Artist>();
		artists = am.getAllArtists();
		return artists;
	}
	
	@GET
	@Path("/find/{artistFirstName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artist> findArtist(@PathParam("artistFirstName") String name) {
		List<Artist> artists = new ArrayList<Artist>();
		artists = am.findArtistByName(name);
		return artists;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addArtist(Artist artist) {
		am.addArtist(artist);
		return Response.status(201).entity("Artist").build(); 
	}
	
	@PUT
	@Path("/{id}/{newName}")
	public Response updateArtistByName(@PathParam("id") Long id, @PathParam("newName") String new_name) {
		am.updateArtistName(new_name, id);
		return Response.status(200).build();
	}
	
	@DELETE
	public Response deleteAllArtists() {
		am.deleteArtists();
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/{artistName}")
	public Response deleteSingleMusic(@PathParam("artistName") String name) {
		am.deleteSingleArtist(name);
		return Response.status(200).build();
	}
	
}
