import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.elasticsearch.config.EsConfiguration;

public class AppConfiguration extends Configuration {
	
	EsConfiguration esConfiguration;
	
	@NotEmpty
	private String template;
	
	@NotEmpty
	private String defaultName = "stranger";

	@JsonProperty
	public String getTemplate() {
		return template;
	}
	
	@JsonProperty
	public void setTemplate(String template) {
		this.template = template;
	}

	@JsonProperty
	public String getDefaultName() {
		return defaultName;
	}

	@JsonProperty
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	@JsonProperty
	public EsConfiguration getEsConfiguration() {
		return esConfiguration;
	}

	@JsonProperty
	public void setEsConfiguration(EsConfiguration esConfiguration) {
		this.esConfiguration = esConfiguration;
	}
}
