package org.dayanuyim.cloud;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationPath("/cloud")
public class CloudApp extends ResourceConfig{

    public CloudApp(){
        packages("org.dayanuyim.cloud");
        register(MultiPartFeature.class);
        register(LoggingFilter.class);

    	System.out.println("init cloud");
    }
}

