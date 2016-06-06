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
import com.example.restservicedemo.domain.Music;
import com.example.restservicedemo.service.ArtistManager;
import com.example.restservicedemo.service.MusicManager;

@Path("music")
public class MusicRESTService {

	private MusicManager mm = new MusicManager();
	private ArtistManager am = new ArtistManager();
	
	@GET
	@Path("/{musicId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Music getMusic(@PathParam("musicId") Long id) {
		Artist a = new Artist();
		Music m = new Music();
		m = mm.getMusic(id);
		a = am.getArtist(m.getAuthorId());
		m.setAuthor(a.getFirstName() +  " " + a.getLastName());
		return m;
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Music> getAllMusic() {
		List<Music> music = new ArrayList<Music>();
		music = mm.getAllMusic();
		for(Music m : music) {
			Artist a = new Artist();
			a = am.getArtist(m.getAuthorId());
			m.setAuthor(a.getFirstName() + " " + a.getLastName());
		}
		return music;
	}
	
	@GET
	@Path("/find/{musicName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Music> findMusic(@PathParam("musicName") String name) {
		List<Music> music = new ArrayList<Music>();
		music = mm.findMusicByName(name);
		for(Music m : music) {
			Artist a = new Artist();
			a = am.getArtist(m.getAuthorId());
			m.setAuthor(a.getFirstName() + " " + a.getLastName());
		}
		return music;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMusic(Music music) {
		if(am.getArtist(music.getAuthorId()) != null) {
			mm.addMusic(music);
			return Response.status(201).entity("Music").build(); 
		}
		else return Response.status(406).build();
	}
	
	@PUT
	@Path("/{id}/{newName}")
	public Response updateMusicByName(@PathParam("id") Long id, @PathParam("newName") String new_name) {
		mm.updateMusicName(new_name, id);
		return Response.status(200).build();
	}
	
	@DELETE
	public Response deleteAllMusic() {
		mm.deleteMusic();
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/{musicName}")
	public Response deleteSingleMusic(@PathParam("musicName") String name) {
		mm.deleteSingleMusic(name);
		return Response.status(200).build();
	}
	
}
