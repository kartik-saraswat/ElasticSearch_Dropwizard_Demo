import org.elasticsearch.client.Client;

import io.dropwizard.Application;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.setup.Environment;
import resource.AppResource;

public class App extends Application<AppConfiguration> {

	@Override
	public void run(AppConfiguration config, Environment env) throws Exception {
		final ManagedEsClient managedClient = new ManagedEsClient(config.getEsConfiguration());
		Client client = managedClient.getClient();
		
		final AppResource appResource = new AppResource(client);
		
		env.healthChecks().register("ES cluster health", new EsClusterHealthCheck(managedClient.getClient()));
		env.lifecycle().manage(managedClient);
		env.jersey().register(appResource);
	}
	
	 @Override
	    public String getName() {
	        return "hello-world";
	    }
	 
	 public static void main(String[] args) throws Exception {
	        new App().run(args);
	    }
}
