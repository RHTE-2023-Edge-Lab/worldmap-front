package fr.itix;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;

@Path("/api/shipments")
public class ShipmentResource {
    @Inject
    @Channel("shipments")
    Multi<ShipmentEntity> shipments;

    public ShipmentResource() {
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<ShipmentEntity> streamShipments() {
        return shipments;
    }
}
