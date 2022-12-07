package fr.itix;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

@Path("/api/shipments")
public class ShipmentResource {
    private BroadcastProcessor<ShipmentEntity> processor = BroadcastProcessor.create();
    private Thread randomGenerator;

    public ShipmentResource() {
        randomGenerator = new Thread(new RandomShipmentGenerator(processor));
        randomGenerator.start();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<ShipmentEntity> streamShipments() {
        return processor.toHotStream();
    }


}
