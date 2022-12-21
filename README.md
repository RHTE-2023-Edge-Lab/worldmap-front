# worldmap-front Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```sh
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Configuration

Use the following environment variables to configure this application in production:

| Environment Variable                      | Description                                                                           | Example                             |
|-------------------------------------------|---------------------------------------------------------------------------------------|-------------------------------------|
| `KAFKA_BOOTSTRAP_SERVERS` | coma separated list of Kafka servers (hostname:port) | `localhost:9092` |
| `MP_MESSAGING_INCOMING_SHIPMENTS_TOPIC` | Kafka topic to receive shipment events | `shipment-records` |
| `MP_MESSAGING_CONNECTOR_SMALLRYE_KAFKA_SECURITY_PROTOCOL` | Security protocol used to communicate with the Kafka Broker | `SASL_PLAINTEXT` |
| `MP_MESSAGING_CONNECTOR_SMALLRYE_KAFKA_SASL_MECHANISM` | SASL mechanism used to authenticate to the Kafka Broker | `SCRAM-SHA-512` |
| `MP_MESSAGING_CONNECTOR_SMALLRYE_KAFKA_SASL_JAAS_CONFIG` | JAAS configuration | `org.apache.kafka.common.security.scram.ScramLoginModule required username='myuser' password='s3cr3t';` |

## Packaging and running the application

The application can be packaged using:

```sh
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```sh
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 

```sh
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 

```sh
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ship-map-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Container image

```sh
APP_VERSION="$(./mvnw -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)"
podman build -f src/main/docker/Dockerfile.jvm -t quay.io/rhte2023edgelab/worldmap-front:latest .
podman tag quay.io/rhte2023edgelab/worldmap-front:latest quay.io/rhte2023edgelab/worldmap-front:$APP_VERSION
podman push quay.io/rhte2023edgelab/worldmap-front:$APP_VERSION
podman push quay.io/rhte2023edgelab/worldmap-front:latest
```

## Deploy in Kubernetes

Create a project named "headquarter".

Install the **Red Hat Integration - AMQ Streams** operator

Create a Kafka resource.

```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: Kafka
metadata:
  name: headquarter
spec:
  kafka:
    config:
      offsets.topic.replication.factor: 3
      transaction.state.log.replication.factor: 3
      transaction.state.log.min.isr: 2
      default.replication.factor: 3
      min.insync.replicas: 2
      inter.broker.protocol.version: '3.2'
    storage:
      type: ephemeral
    listeners:
      - authentication:
          type: scram-sha-512
        name: plain
        port: 9092
        type: internal
        tls: false
      - authentication:
          type: scram-sha-512
        name: tls
        port: 9093
        type: route
        tls: true
    version: 3.2.3
    replicas: 3
  entityOperator:
    topicOperator: {}
    userOperator: {}
  zookeeper:
    storage:
      type: ephemeral
    replicas: 3
```

Deploy all Kubernetes manifests.

```sh
kubectl apply -f k8s
```

Create **kcat-hq.conf** as follow:

```ini
# Required connection configs for Kafka producer, consumer, and admin
bootstrap.servers=headquarter-kafka-tls-bootstrap-headquarter.apps.appdev.itix.xyz:443
ssl.ca.location=/home/nmasse/tmp/headquarter.pem
security.protocol=SASL_SSL
sasl.mechanisms=SCRAM-SHA-512
sasl.username=kafka-streams-shipments
sasl.password=s3cr3t

# Best practice for higher availability in librdkafka clients prior to 1.7
session.timeout.ms=45000
```

In a terminal, run:

```sh
while read line; do echo "$line" | kcat -b headquarter-kafka-tls-bootstrap-headquarter.apps.appdev.itix.xyz:443 -P -F ~/tmp/kcat-hq.conf -t headquarter-shipment-records -k 11:22:33:44; sleep 3; done <<EOF
{"parcelNumber":"11:22:33:44","from":"DUB","to":"PAR","timestamp":$(date +%s -d "now")}
{"parcelNumber":"11:22:33:44","from":"PAR","to":"BRN","timestamp":$(date +%s -d "1 second")}
{"parcelNumber":"11:22:33:44","from":"BRN","to":"ATH","timestamp":$(date +%s -d "2 second")}
{"parcelNumber":"11:22:33:44","from":"ATH","to":"VAR","timestamp":$(date +%s -d "3 second")}
EOF
```

## Related Guides

- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A JAX-RS implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
