package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Path("/hello")
public class GreetingResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @PUT
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public String b() {
        return "Hello from Quarkus REST";
    }
}
