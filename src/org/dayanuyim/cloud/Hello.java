package org.dayanuyim.cloud;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

 
@Path("/hello")
public class Hello {
 
	/*
    @GET
    public String sayHelloWorld() k
        return "Hello world";
    }   
    */
 
    @GET
    @Path("/{name}")
    public String sayHello(@PathParam("name") String name,
    						@Context HttpServletRequest request) throws Exception
    {
    	if(name.equals("fengbin"))
    		throw new Exception("I dont known you.");
    	
    	System.out.println("session id=" + request.getSession(true).getId());
        return "Hello, " + name;
    }
}
