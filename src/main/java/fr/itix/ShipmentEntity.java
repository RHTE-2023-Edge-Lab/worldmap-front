package fr.itix;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ShipmentEntity {
    public String parcelNumber;
    public String from;
    public String to;
    public long timestamp;

    public ShipmentEntity() {
    }

    public ShipmentEntity(String parcelNumber, String from, String to, long timestamp) {
        this.parcelNumber = parcelNumber;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }
}
