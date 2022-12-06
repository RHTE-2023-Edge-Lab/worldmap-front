package fr.itix;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.ArrayList;

@Path("/api/warehouses")
public class WarehouseResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WarehouseEntity> getAllWarehouses() {
        List<WarehouseEntity> warehouses = new ArrayList<WarehouseEntity>();
        warehouses.add(new WarehouseEntity("Paris", "PAR", 48.8317, 2.3558));
        warehouses.add(new WarehouseEntity("Brussels", "BRU", 50.8552, 4.3054));
        warehouses.add(new WarehouseEntity("London", "LON", 51.5343, -0.3004));
        warehouses.add(new WarehouseEntity("Lisboa", "LIS", 38.7436, -9.1602));
        warehouses.add(new WarehouseEntity("Athens", "ATH", 37.9909, 23.7033));
        warehouses.add(new WarehouseEntity("Stockholm", "STO", 59.326242,17.8419715));
        warehouses.add(new WarehouseEntity("Varsovia", "VAR", 52.2330653,20.9211121));
        warehouses.add(new WarehouseEntity("Dublin", "DUB", 53.3241807,-6.5327706));
        warehouses.add(new WarehouseEntity("Bucharest", "BUC", 44.4379269,26.0245979));
        warehouses.add(new WarehouseEntity("Brno", "BRN", 49.2021611,16.507921));
        return warehouses;
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public WarehouseEntity getWarehouse(@PathParam("code") String code) {
        List<WarehouseEntity> warehouses = this.getAllWarehouses();
        return warehouses.stream()
                         .filter(w -> w.code.equalsIgnoreCase(code))
                         .findFirst()
                         .orElseThrow();
    }
}