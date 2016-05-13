# Scaffolding project for Kibana-based analytics

This template project provides the means to setup a locally running Elasticsearch-backed Kibana instance with zero configuration using Docker containers. The project also provides a minimalistic implementation of an indexer that is able to push data per-item or in-bulk into Elasticsearch (supports both synchronous and asynchronous programming models).

## Prerequisites

[Docker](https://docs.docker.com/engine/installation/) as well as [Docker Compose](https://docs.docker.com/compose/install/) need to be installed on your system.

## Versions

| Application   | Version |
| ------------- | ------- |
| Elasticsearch | 2.3.2   |
| Kibana        | 4.5.0   |

## Feeding data

### Implementing the feeder

Have a look at module `elasticsearch-feeder`. This module implements an example feeder in class `com.mgu.analytics.feeder.Feeder`. This class is derived from `AbstractBaseFeeder` and must implement the `run` method. The base class provides methods (cf. `withAdapter`) that create all contextual dependencies for you. So in your implementation of the `run` method, it suffices to do something like this:

```java
@Override
public void run() {
    withAdapter(search -> {
        search.index(generateDocuments());
    });
}
```

`withAdapter` injects an already configured instance of `ElasticAdapter` (cf. module `elasticsearch-adapter`) into the method. The actual feeding logic is encoded using a `ThrowingConsumer<T>` and executed asynchronously in the background. The application waits for the termination of the `ThrowingConsumer<T>` (w/o timeouts).

### Representing search documents

Module `elasticsearch-adapter` provides a `TypedDocument` which is the abstract base class for any search document that you want to feed into Elasticsearch.

### Configuring the feeder

The resource file `config.properties` contains the configuration of the feeder. Currently, this only indicates the name of the index to use.

### Launch the local infrastructure

Change to the root directory of this project and issue the following command.

```bash
$ docker-compose up
```

This will fire up Elasticsearch and Kibana. After both applications launched successfully, you can access Kibana via your favourite browser at this [URL](http://localhost:5601).

## License

This software is released under the terms of the MIT license.