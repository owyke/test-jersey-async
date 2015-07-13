package se.wyko;

import org.glassfish.jersey.server.ManagedAsync;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by Oskar on 07/07/2015.
 */
@Path("/gimme")
public class AsyncController {


    @GET
    @Path("/five")
    public Response fiver( @Context HttpServletRequest httpRequest) throws InterruptedException {
        long received = System.currentTimeMillis();
        System.out.println("Start " + received);
        System.out.println(Thread.currentThread());

        Thread.sleep(5000);
        System.out.println("Stop  " + received);
        return Response.ok(String.format("Was received at: %d", received)).build();
    }

    @GET
    @Path("/tem")
    public void asyncGet(@Suspended final AsyncResponse asyncResponse) {
         String threadName = Thread.currentThread().toString();
        long received = System.currentTimeMillis();
        System.out.println("In    " + received + "###################");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread());
                System.out.println(("OSKAR " + threadName + " " + Thread.currentThread().toString()));
                asyncResponse.resume("OSKAR " + threadName + " " + Thread.currentThread().toString());

            }
        }).start();


        System.out.println("Out   " + received);
    }

}
