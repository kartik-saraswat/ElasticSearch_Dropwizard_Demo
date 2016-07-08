package util;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.get.GetField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import com.google.common.base.Strings;

import representation.Song;

public class Parse {
	
	public static String parseNonEmpty(Object object, String defaultObject){
		if(object == null || !(object instanceof String) ){
			return defaultObject;
		} else{
			return String.class.cast(object);		}
	}
	
	public static Integer parseNonEmpty(Object object, Integer defaultObject){
		if(object == null || !(object instanceof Integer) ){
			return defaultObject;
		} else{
			return Integer.class.cast(object);		}
	}
	
	public static Long parseNonEmpty(Object object, Long defaultObject){
		if(object == null || !(object instanceof Long) ){
			return defaultObject;
		} else{
			return Long.class.cast(object);		}
	}
	
	public static Song parseSong(Map<String, GetField> getFieldMap){
		System.out.println("\nHit: ");
		Song song = new Song();
		System.out.println(getFieldMap);
		if(getFieldMap.containsKey("name")){
			System.out.print("name: " + getFieldMap.get("name").getValue().toString());
			song.setName( parseNonEmpty(getFieldMap.get("name").getValue(), "Unknown" ) );
		}
		
		if(getFieldMap.containsKey("year")){
			System.out.print("year: " + getFieldMap.get("year"));
			song.setYear( parseNonEmpty(getFieldMap.get("year").getValue(), -1 ) );
		}
		
		if(getFieldMap.containsKey("album")){
			song.setAlbum( parseNonEmpty(getFieldMap.get("album").getValue(), "Unknown" ) );
		}
		
		if(getFieldMap.containsKey("artist")){
			song.setArtist( parseNonEmpty(getFieldMap.get("artist").getValue(), "Unknown" ) );
		}
		
		if(getFieldMap.containsKey("lyrics")){
			song.setLyrics( parseNonEmpty(getFieldMap.get("lyrics").getValue(), "Unknown" ) );
		}
		
		return song;
	}
	
	public static Song parseSong(SearchHit searchHit){
		System.out.println("\nHit: ");
		Song song = new Song();
		Map<String, SearchHitField> hitFieldMap = searchHit.getFields();
		if(hitFieldMap.containsKey("name")){
			System.out.print("name: " + hitFieldMap.get("name").getValue());
			song.setName( parseNonEmpty(hitFieldMap.get("name").getValue(), "Unknown" ) );
		}
		
		if(hitFieldMap.containsKey("year")){
			System.out.print("year: " + hitFieldMap.get("year"));
			song.setYear( parseNonEmpty(hitFieldMap.get("year").getValue(), -1 ) );
		}
		
		if(hitFieldMap.containsKey("album")){
			song.setAlbum( parseNonEmpty(hitFieldMap.get("album").getValue(), "Unknown" ) );
		}
		
		if(hitFieldMap.containsKey("artist")){
			song.setArtist( parseNonEmpty(hitFieldMap.get("artist").getValue(), "Unknown" ) );
		}
		
		if(hitFieldMap.containsKey("lyrics")){
			song.setLyrics( parseNonEmpty(hitFieldMap.get("lyrics").getValue(), "Unknown" ) );
		}
		
		return song;
	}
	
	public static Map<String, Object> parseSong(Song song){
		Map<String, Object> songMap = new HashMap<String, Object>();
		
		if(!Strings.isNullOrEmpty(song.getName())){
			songMap.put("name", song.getName());
		}
		
		if(!(song.getYear() == null)){
			songMap.put("year", song.getYear());
		}
		
		if(!Strings.isNullOrEmpty(song.getAlbum())){
			songMap.put("album", song.getAlbum());
		}
		
		if(!Strings.isNullOrEmpty(song.getArtist())){
			songMap.put("artist", song.getArtist());
		}
		
		if(!Strings.isNullOrEmpty(song.getLyrics())){
			songMap.put("lyrics", song.getLyrics());
		}
		
		return songMap;
	}
}
