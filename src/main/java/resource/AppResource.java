package resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.base.Optional;

import representation.Response;
import representation.Song;
import util.Parse;

@Path("/home")/* here we have created an index for the url*/
@Produces(MediaType.APPLICATION_JSON)
public class AppResource {
	private final Client client;

	public AppResource(Client client) {
		this.client = client;
	}

	@PUT
	@Path("/music/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSong(Song song){
		
		try {
			client.prepareIndex("music","songs",song.getName()).setSource(
					"name",song.getName(),
					"year",song.getYear(),
					"album",song.getAlbum(),
					"artist",song.getArtist(),
					"lyrics",song.getLyrics()
					).execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ERROR;
		}

		return Response.SUCCESS;
	}
	
	@PUT
	@Path("/music/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSong(Song song){

		System.out.println("Song: " +song);
		try {
			Map<String, Object> songMap = Parse.parseSong(song);
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index("music").type("songs").id(song.getName())
			.doc(songMap);
			client.update(updateRequest).get();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ERROR;
		}

		return Response.SUCCESS;
	}

	@GET
	@Path("/music/get/")
	public Response getSong(@QueryParam("key") Optional<String> key){
		
		Map<String, Song> songsMap;
		try {
			MultiMatchQueryBuilder builder = new MultiMatchQueryBuilder(key, "name","album","artist", "lyrics");
			SearchResponse searchResponse =	
					client.prepareSearch("music")
					.setTypes("songs")
					.setQuery(builder)
					.addFields("name", "year","album","artist","lyrics")
					.setFrom(0).setSize(10).execute().actionGet();
			SearchHit[] hits = searchResponse.getHits().getHits();
			if(hits.length == 0){
				return Response.NOT_FOUND;
			}
			songsMap = new HashMap<String, Song>();
			for(SearchHit hit : hits){
				songsMap.put(hit.getId(), Parse.parseSong(hit));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ERROR;
		}
		
		Response response = new Response(200,"succesful");
		response.setObject(songsMap);
		
		return response;
	}
	
	
	@GET
	@Path("/music/get/{name}")
	public Response getSongByName(@PathParam("name") Optional<String> name){
		Song song = null;
		try {
			if(name.isPresent()){
				GetResponse getResponse = client.prepareGet("music", "songs", name.get())
						.setFields("name","year","album","artist","lyrics")
						.get();
				if(getResponse.isExists()){
					song = Parse.parseSong( getResponse.getFields());
				} else{
					return Response.NOT_FOUND;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ERROR;
		}
		
		Response response = new Response(200,"succesful");
		response.setObject(song);
		
		return response;
	}
	
	
	/* This Api is work in progress*/
	@DELETE
	@Path("/music/remove/{name}")
	public Response removeSong(@PathParam("name") Optional<String> name){
		
		try {
			if(name.isPresent()){
				DeleteResponse deleteResponse = client.prepareDelete("music", "Songs", name.get())
				        .get();				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ERROR;
		}
		return Response.SUCCESS;
	}
}