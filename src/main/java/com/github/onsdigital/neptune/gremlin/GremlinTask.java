package com.github.onsdigital.neptune.gremlin;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;

import java.io.Closeable;
import java.io.IOException;

import static com.github.onsdigital.neptune.logging.LogBuilder.logBuilder;

public abstract class GremlinTask implements Closeable {

    protected Cluster cluster;
    protected String neptuneHost;
    protected int neptunePort;

    public GremlinTask(String neptuneHost, int neptunePort) {
        this.cluster = Cluster.build()
                .addContactPoint(neptuneHost)
                .port(neptunePort)
                .create();

        this.neptuneHost = neptuneHost;
        this.neptunePort = neptunePort;
    }

    public void connectAndExecute() {
        try (GraphTraversalSource g = EmptyGraph.instance()
                .traversal()
                .withRemote(DriverRemoteConnection
                        .using(cluster))) {

            executeTask(g);

        } catch (Exception e) {
            logBuilder().error(e, "something went wrong");
        }
    }

    public void close() throws IOException {
        logBuilder().info("closing cluster");
        cluster.close();
    }

    abstract void executeTask(GraphTraversalSource g);

    abstract String getName();
}
