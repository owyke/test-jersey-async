import jersey.repackaged.com.google.common.base.Optional;
import org.junit.Test;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Oskar on 07/07/2015.
 */
public class ThreadTest {


    @Test
    public void makeConcurrentRequests() throws ExecutionException, InterruptedException {
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target("http://localhost:8080");
        WebTarget endpoint = target.path("/gimme/tem");

        AsyncInvoker invoker = endpoint.request().async();

        List<Future<Response>> f = new ArrayList();

        for (int i = 0; i <100; i++) {
            f.add(invoker.get());
        }
        System.out.println("All requests are sent...");
        long start = System.currentTimeMillis();
        f.stream().map(future -> resolveFuture(future))
                  .forEach(optional -> System.out.println(optional.orNull().readEntity(String.class)));
        System.out.println(String.format("All responses are recevied in %d seconds.", (System.currentTimeMillis() - start)/1000));





    }

    private Optional<Response> resolveFuture(Future<Response> future) {
        try {
            return Optional.of(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.absent();
    }

}
