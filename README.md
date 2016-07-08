# ElasticSearch_Dropwizard_Demo
This is an attempt to integrate latest versions of (0.9.3)Dropwizard and Elastic Search(2.3.3) and demonstrate CRUD operations.

# Maven Dependencies
  Dropwizard Core
  ---------------
     
     <dependency>
        <groupId>io.dropwizard</groupId>
        <artifactId>dropwizard-core</artifactId>
        <version>${dropwizard.version}</version>
    </dependency>
    
  
  ElasticSearch Dropwizard
  ------------------------
  
    <dependency>
      <groupId>io.dropwizard.modules</groupId>
      <artifactId>dropwizard-elasticsearch</artifactId>
      <version>1.0.0-rc2-1</version>
    </dependency>
    
  Maven Jar Plugin
  ----------------
  
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <version>2.4</version>
      <configuration>
          <archive>
              <manifest>
                  <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
              </manifest>
          </archive>
      </configuration>
    </plugin>
    
    
# Dropwizard Configuration Yaml

    esConfiguration: 
     nodeClient: false
     clusterName: elasticsearch
     servers: 
     - 127.0.0.1:9300

# Dropwizard Application run method
   
    @Override
  	public void run(AppConfiguration config, Environment env) throws Exception {
  		final ManagedEsClient managedClient = new ManagedEsClient(config.getEsConfiguration());
  		Client client = managedClient.getClient();
  		
  		final AppResource appResource = new AppResource(client);
  		
  		env.healthChecks().register("ES cluster health", new EsClusterHealthCheck(managedClient.getClient()));
  		env.lifecycle().manage(managedClient);
  		env.jersey().register(appResource);
  	}
# Resource Cclass
   
   Adding Document
   ---------------
  
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
  		
   Updating Document
   ---------------
  
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
  		
   GetSingle Document
   ---------------
  
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
  		
   MultiField Search
   -----------------
  
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
