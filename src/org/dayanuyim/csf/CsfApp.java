package org.dayanuyim.csf;

import javax.ws.rs.ApplicationPath;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/csf")
public class CsfApp extends ResourceConfig{
	
	static{
		BasicConfigurator.configure();
	}

    public CsfApp(){
        packages("org.dayanuyim.csf");
        register(MultiPartFeature.class);
        register(LoggingFilter.class);

    	System.out.println("init csf");
    }
}
