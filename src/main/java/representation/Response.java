package representation;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

	@NotEmpty
	final int status;
	
	@NotEmpty
	final String message;
	
	Object object;
	
	public static final Response ERROR = new Response(500,"internal error");
	public static final Response NOT_FOUND = new Response(400,"not found");
	public static Response SUCCESS = new Response(200,"success");
	
	public Response(int status, String message){
		super();
		this.status = status;
		this.message = message;
	}

	@JsonProperty
	public int getStatus() {
		return status;
	}
	
	
	@JsonProperty
	public String getMessage() {
		return message;
	}
	
	@JsonProperty
	public Object getObject() {
		return object;
	}
	
	@JsonProperty
	public void setObject(Object object) {
		this.object = object;
	}
}
