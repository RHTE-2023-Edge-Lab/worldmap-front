package fr.itix;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

public class RandomShipmentGenerator implements Runnable {
    private BroadcastProcessor<ShipmentEntity> processor;

    public RandomShipmentGenerator(BroadcastProcessor<ShipmentEntity> processor) {
        this.processor = processor;
    }

    public void run() {
        Random rand = ThreadLocalRandom.current();
        while (true) {
            List<WarehouseEntity> warehouses = new WarehouseResource().getAllWarehouses();
            int index = rand.nextInt(warehouses.size());
            String origin = warehouses.get(index).code;
            warehouses.remove(index);
            String destination = warehouses.get(rand.nextInt(warehouses.size())).code;
            processor.onNext(new ShipmentEntity(origin, destination));
            try {
                Thread.sleep(rand.nextLong(2000));
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        // Uncomment the following line if the infinite loop above becomes interruptible
        //processor.onComplete();
    }
}
