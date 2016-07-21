package org.dayanuyim.cloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/account")
public class Account {

	@Path("/login")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	//@Produces(MediaType.TEXT_PLAIN)
	public Response login(@FormDataParam("Username") String username,
	                       @FormDataParam("Password") String password)
	{
		//String username = parts.getField("Username").getValue();
		//String password = parts.getField("Password").getValue();

		String msg ="usr: " + username + ", pwd: " + password;
		return Response.status(201).entity(msg).build();
	}
	
	/*
	@Path("logout")
	@GET
	public void logout()
	{
		
	}
	*/
}
