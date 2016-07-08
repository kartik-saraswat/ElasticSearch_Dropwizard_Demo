package representation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {
	
	Integer year;
	String name;
	String album;
	String artist;
	String lyrics;
	
	@JsonProperty
	public Integer getYear() {
		return year;
	}
	
	@JsonProperty
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@JsonProperty
	public String getName() {
		return name;
	}
	
	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty
	public String getAlbum() {
		return album;
	}
	
	@JsonProperty
	public void setAlbum(String album) {
		this.album = album;
	}
	
	@JsonProperty
	public String getArtist() {
		return artist;
	}
	
	@JsonProperty
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	@JsonProperty
	public String getLyrics() {
		return lyrics;
	}
	
	@JsonProperty
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	@Override
	public String toString() {
		return "Song [year=" + year + ", name=" + name + ", album=" + album + ", artist=" + artist + ", lyrics="
				+ lyrics + "]";
	}
}
